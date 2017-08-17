/**
 */
package net.opengis.gml311.util;

import net.opengis.gml311.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see net.opengis.gml311.Gml311Package
 * @generated
 */
public class Gml311Switch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Gml311Package modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Gml311Switch() {
        if (modelPackage == null) {
            modelPackage = Gml311Package.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case Gml311Package.ABSOLUTE_EXTERNAL_POSITIONAL_ACCURACY_TYPE: {
                AbsoluteExternalPositionalAccuracyType absoluteExternalPositionalAccuracyType = (AbsoluteExternalPositionalAccuracyType)theEObject;
                T result = caseAbsoluteExternalPositionalAccuracyType(absoluteExternalPositionalAccuracyType);
                if (result == null) result = caseAbstractPositionalAccuracyType(absoluteExternalPositionalAccuracyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_CONTINUOUS_COVERAGE_TYPE: {
                AbstractContinuousCoverageType abstractContinuousCoverageType = (AbstractContinuousCoverageType)theEObject;
                T result = caseAbstractContinuousCoverageType(abstractContinuousCoverageType);
                if (result == null) result = caseAbstractCoverageType(abstractContinuousCoverageType);
                if (result == null) result = caseAbstractFeatureType(abstractContinuousCoverageType);
                if (result == null) result = caseAbstractGMLType(abstractContinuousCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_BASE_TYPE: {
                AbstractCoordinateOperationBaseType abstractCoordinateOperationBaseType = (AbstractCoordinateOperationBaseType)theEObject;
                T result = caseAbstractCoordinateOperationBaseType(abstractCoordinateOperationBaseType);
                if (result == null) result = caseDefinitionType(abstractCoordinateOperationBaseType);
                if (result == null) result = caseAbstractGMLType(abstractCoordinateOperationBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_COORDINATE_OPERATION_TYPE: {
                AbstractCoordinateOperationType abstractCoordinateOperationType = (AbstractCoordinateOperationType)theEObject;
                T result = caseAbstractCoordinateOperationType(abstractCoordinateOperationType);
                if (result == null) result = caseAbstractCoordinateOperationBaseType(abstractCoordinateOperationType);
                if (result == null) result = caseDefinitionType(abstractCoordinateOperationType);
                if (result == null) result = caseAbstractGMLType(abstractCoordinateOperationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_BASE_TYPE: {
                AbstractCoordinateSystemBaseType abstractCoordinateSystemBaseType = (AbstractCoordinateSystemBaseType)theEObject;
                T result = caseAbstractCoordinateSystemBaseType(abstractCoordinateSystemBaseType);
                if (result == null) result = caseDefinitionType(abstractCoordinateSystemBaseType);
                if (result == null) result = caseAbstractGMLType(abstractCoordinateSystemBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_COORDINATE_SYSTEM_TYPE: {
                AbstractCoordinateSystemType abstractCoordinateSystemType = (AbstractCoordinateSystemType)theEObject;
                T result = caseAbstractCoordinateSystemType(abstractCoordinateSystemType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(abstractCoordinateSystemType);
                if (result == null) result = caseDefinitionType(abstractCoordinateSystemType);
                if (result == null) result = caseAbstractGMLType(abstractCoordinateSystemType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_COVERAGE_TYPE: {
                AbstractCoverageType abstractCoverageType = (AbstractCoverageType)theEObject;
                T result = caseAbstractCoverageType(abstractCoverageType);
                if (result == null) result = caseAbstractFeatureType(abstractCoverageType);
                if (result == null) result = caseAbstractGMLType(abstractCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE: {
                AbstractCurveSegmentType abstractCurveSegmentType = (AbstractCurveSegmentType)theEObject;
                T result = caseAbstractCurveSegmentType(abstractCurveSegmentType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_CURVE_TYPE: {
                AbstractCurveType abstractCurveType = (AbstractCurveType)theEObject;
                T result = caseAbstractCurveType(abstractCurveType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(abstractCurveType);
                if (result == null) result = caseAbstractGeometryType(abstractCurveType);
                if (result == null) result = caseAbstractGMLType(abstractCurveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_DATUM_BASE_TYPE: {
                AbstractDatumBaseType abstractDatumBaseType = (AbstractDatumBaseType)theEObject;
                T result = caseAbstractDatumBaseType(abstractDatumBaseType);
                if (result == null) result = caseDefinitionType(abstractDatumBaseType);
                if (result == null) result = caseAbstractGMLType(abstractDatumBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_DATUM_TYPE: {
                AbstractDatumType abstractDatumType = (AbstractDatumType)theEObject;
                T result = caseAbstractDatumType(abstractDatumType);
                if (result == null) result = caseAbstractDatumBaseType(abstractDatumType);
                if (result == null) result = caseDefinitionType(abstractDatumType);
                if (result == null) result = caseAbstractGMLType(abstractDatumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_DISCRETE_COVERAGE_TYPE: {
                AbstractDiscreteCoverageType abstractDiscreteCoverageType = (AbstractDiscreteCoverageType)theEObject;
                T result = caseAbstractDiscreteCoverageType(abstractDiscreteCoverageType);
                if (result == null) result = caseAbstractCoverageType(abstractDiscreteCoverageType);
                if (result == null) result = caseAbstractFeatureType(abstractDiscreteCoverageType);
                if (result == null) result = caseAbstractGMLType(abstractDiscreteCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_FEATURE_COLLECTION_TYPE: {
                AbstractFeatureCollectionType abstractFeatureCollectionType = (AbstractFeatureCollectionType)theEObject;
                T result = caseAbstractFeatureCollectionType(abstractFeatureCollectionType);
                if (result == null) result = caseAbstractFeatureType(abstractFeatureCollectionType);
                if (result == null) result = caseAbstractGMLType(abstractFeatureCollectionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_FEATURE_TYPE: {
                AbstractFeatureType abstractFeatureType = (AbstractFeatureType)theEObject;
                T result = caseAbstractFeatureType(abstractFeatureType);
                if (result == null) result = caseAbstractGMLType(abstractFeatureType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GENERAL_CONVERSION_TYPE: {
                AbstractGeneralConversionType abstractGeneralConversionType = (AbstractGeneralConversionType)theEObject;
                T result = caseAbstractGeneralConversionType(abstractGeneralConversionType);
                if (result == null) result = caseAbstractCoordinateOperationType(abstractGeneralConversionType);
                if (result == null) result = caseAbstractCoordinateOperationBaseType(abstractGeneralConversionType);
                if (result == null) result = caseDefinitionType(abstractGeneralConversionType);
                if (result == null) result = caseAbstractGMLType(abstractGeneralConversionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GENERAL_DERIVED_CRS_TYPE: {
                AbstractGeneralDerivedCRSType abstractGeneralDerivedCRSType = (AbstractGeneralDerivedCRSType)theEObject;
                T result = caseAbstractGeneralDerivedCRSType(abstractGeneralDerivedCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(abstractGeneralDerivedCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(abstractGeneralDerivedCRSType);
                if (result == null) result = caseDefinitionType(abstractGeneralDerivedCRSType);
                if (result == null) result = caseAbstractGMLType(abstractGeneralDerivedCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_REF_TYPE: {
                AbstractGeneralOperationParameterRefType abstractGeneralOperationParameterRefType = (AbstractGeneralOperationParameterRefType)theEObject;
                T result = caseAbstractGeneralOperationParameterRefType(abstractGeneralOperationParameterRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GENERAL_OPERATION_PARAMETER_TYPE: {
                AbstractGeneralOperationParameterType abstractGeneralOperationParameterType = (AbstractGeneralOperationParameterType)theEObject;
                T result = caseAbstractGeneralOperationParameterType(abstractGeneralOperationParameterType);
                if (result == null) result = caseDefinitionType(abstractGeneralOperationParameterType);
                if (result == null) result = caseAbstractGMLType(abstractGeneralOperationParameterType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GENERAL_PARAMETER_VALUE_TYPE: {
                AbstractGeneralParameterValueType abstractGeneralParameterValueType = (AbstractGeneralParameterValueType)theEObject;
                T result = caseAbstractGeneralParameterValueType(abstractGeneralParameterValueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GENERAL_TRANSFORMATION_TYPE: {
                AbstractGeneralTransformationType abstractGeneralTransformationType = (AbstractGeneralTransformationType)theEObject;
                T result = caseAbstractGeneralTransformationType(abstractGeneralTransformationType);
                if (result == null) result = caseAbstractCoordinateOperationType(abstractGeneralTransformationType);
                if (result == null) result = caseAbstractCoordinateOperationBaseType(abstractGeneralTransformationType);
                if (result == null) result = caseDefinitionType(abstractGeneralTransformationType);
                if (result == null) result = caseAbstractGMLType(abstractGeneralTransformationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GEOMETRIC_AGGREGATE_TYPE: {
                AbstractGeometricAggregateType abstractGeometricAggregateType = (AbstractGeometricAggregateType)theEObject;
                T result = caseAbstractGeometricAggregateType(abstractGeometricAggregateType);
                if (result == null) result = caseAbstractGeometryType(abstractGeometricAggregateType);
                if (result == null) result = caseAbstractGMLType(abstractGeometricAggregateType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE: {
                AbstractGeometricPrimitiveType abstractGeometricPrimitiveType = (AbstractGeometricPrimitiveType)theEObject;
                T result = caseAbstractGeometricPrimitiveType(abstractGeometricPrimitiveType);
                if (result == null) result = caseAbstractGeometryType(abstractGeometricPrimitiveType);
                if (result == null) result = caseAbstractGMLType(abstractGeometricPrimitiveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GEOMETRY_TYPE: {
                AbstractGeometryType abstractGeometryType = (AbstractGeometryType)theEObject;
                T result = caseAbstractGeometryType(abstractGeometryType);
                if (result == null) result = caseAbstractGMLType(abstractGeometryType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GML_TYPE: {
                AbstractGMLType abstractGMLType = (AbstractGMLType)theEObject;
                T result = caseAbstractGMLType(abstractGMLType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE: {
                AbstractGriddedSurfaceType abstractGriddedSurfaceType = (AbstractGriddedSurfaceType)theEObject;
                T result = caseAbstractGriddedSurfaceType(abstractGriddedSurfaceType);
                if (result == null) result = caseAbstractParametricCurveSurfaceType(abstractGriddedSurfaceType);
                if (result == null) result = caseAbstractSurfacePatchType(abstractGriddedSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_META_DATA_TYPE: {
                AbstractMetaDataType abstractMetaDataType = (AbstractMetaDataType)theEObject;
                T result = caseAbstractMetaDataType(abstractMetaDataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_PARAMETRIC_CURVE_SURFACE_TYPE: {
                AbstractParametricCurveSurfaceType abstractParametricCurveSurfaceType = (AbstractParametricCurveSurfaceType)theEObject;
                T result = caseAbstractParametricCurveSurfaceType(abstractParametricCurveSurfaceType);
                if (result == null) result = caseAbstractSurfacePatchType(abstractParametricCurveSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_POSITIONAL_ACCURACY_TYPE: {
                AbstractPositionalAccuracyType abstractPositionalAccuracyType = (AbstractPositionalAccuracyType)theEObject;
                T result = caseAbstractPositionalAccuracyType(abstractPositionalAccuracyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_BASE_TYPE: {
                AbstractReferenceSystemBaseType abstractReferenceSystemBaseType = (AbstractReferenceSystemBaseType)theEObject;
                T result = caseAbstractReferenceSystemBaseType(abstractReferenceSystemBaseType);
                if (result == null) result = caseDefinitionType(abstractReferenceSystemBaseType);
                if (result == null) result = caseAbstractGMLType(abstractReferenceSystemBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_REFERENCE_SYSTEM_TYPE: {
                AbstractReferenceSystemType abstractReferenceSystemType = (AbstractReferenceSystemType)theEObject;
                T result = caseAbstractReferenceSystemType(abstractReferenceSystemType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(abstractReferenceSystemType);
                if (result == null) result = caseDefinitionType(abstractReferenceSystemType);
                if (result == null) result = caseAbstractGMLType(abstractReferenceSystemType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE: {
                AbstractRingPropertyType abstractRingPropertyType = (AbstractRingPropertyType)theEObject;
                T result = caseAbstractRingPropertyType(abstractRingPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_RING_TYPE: {
                AbstractRingType abstractRingType = (AbstractRingType)theEObject;
                T result = caseAbstractRingType(abstractRingType);
                if (result == null) result = caseAbstractGeometryType(abstractRingType);
                if (result == null) result = caseAbstractGMLType(abstractRingType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_SOLID_TYPE: {
                AbstractSolidType abstractSolidType = (AbstractSolidType)theEObject;
                T result = caseAbstractSolidType(abstractSolidType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(abstractSolidType);
                if (result == null) result = caseAbstractGeometryType(abstractSolidType);
                if (result == null) result = caseAbstractGMLType(abstractSolidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_STYLE_TYPE: {
                AbstractStyleType abstractStyleType = (AbstractStyleType)theEObject;
                T result = caseAbstractStyleType(abstractStyleType);
                if (result == null) result = caseAbstractGMLType(abstractStyleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_SURFACE_PATCH_TYPE: {
                AbstractSurfacePatchType abstractSurfacePatchType = (AbstractSurfacePatchType)theEObject;
                T result = caseAbstractSurfacePatchType(abstractSurfacePatchType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_SURFACE_TYPE: {
                AbstractSurfaceType abstractSurfaceType = (AbstractSurfaceType)theEObject;
                T result = caseAbstractSurfaceType(abstractSurfaceType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(abstractSurfaceType);
                if (result == null) result = caseAbstractGeometryType(abstractSurfaceType);
                if (result == null) result = caseAbstractGMLType(abstractSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TIME_COMPLEX_TYPE: {
                AbstractTimeComplexType abstractTimeComplexType = (AbstractTimeComplexType)theEObject;
                T result = caseAbstractTimeComplexType(abstractTimeComplexType);
                if (result == null) result = caseAbstractTimeObjectType(abstractTimeComplexType);
                if (result == null) result = caseAbstractGMLType(abstractTimeComplexType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TIME_GEOMETRIC_PRIMITIVE_TYPE: {
                AbstractTimeGeometricPrimitiveType abstractTimeGeometricPrimitiveType = (AbstractTimeGeometricPrimitiveType)theEObject;
                T result = caseAbstractTimeGeometricPrimitiveType(abstractTimeGeometricPrimitiveType);
                if (result == null) result = caseAbstractTimePrimitiveType(abstractTimeGeometricPrimitiveType);
                if (result == null) result = caseAbstractTimeObjectType(abstractTimeGeometricPrimitiveType);
                if (result == null) result = caseAbstractGMLType(abstractTimeGeometricPrimitiveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TIME_OBJECT_TYPE: {
                AbstractTimeObjectType abstractTimeObjectType = (AbstractTimeObjectType)theEObject;
                T result = caseAbstractTimeObjectType(abstractTimeObjectType);
                if (result == null) result = caseAbstractGMLType(abstractTimeObjectType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TIME_PRIMITIVE_TYPE: {
                AbstractTimePrimitiveType abstractTimePrimitiveType = (AbstractTimePrimitiveType)theEObject;
                T result = caseAbstractTimePrimitiveType(abstractTimePrimitiveType);
                if (result == null) result = caseAbstractTimeObjectType(abstractTimePrimitiveType);
                if (result == null) result = caseAbstractGMLType(abstractTimePrimitiveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TIME_REFERENCE_SYSTEM_TYPE: {
                AbstractTimeReferenceSystemType abstractTimeReferenceSystemType = (AbstractTimeReferenceSystemType)theEObject;
                T result = caseAbstractTimeReferenceSystemType(abstractTimeReferenceSystemType);
                if (result == null) result = caseDefinitionType(abstractTimeReferenceSystemType);
                if (result == null) result = caseAbstractGMLType(abstractTimeReferenceSystemType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TIME_SLICE_TYPE: {
                AbstractTimeSliceType abstractTimeSliceType = (AbstractTimeSliceType)theEObject;
                T result = caseAbstractTimeSliceType(abstractTimeSliceType);
                if (result == null) result = caseAbstractGMLType(abstractTimeSliceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE: {
                AbstractTimeTopologyPrimitiveType abstractTimeTopologyPrimitiveType = (AbstractTimeTopologyPrimitiveType)theEObject;
                T result = caseAbstractTimeTopologyPrimitiveType(abstractTimeTopologyPrimitiveType);
                if (result == null) result = caseAbstractTimePrimitiveType(abstractTimeTopologyPrimitiveType);
                if (result == null) result = caseAbstractTimeObjectType(abstractTimeTopologyPrimitiveType);
                if (result == null) result = caseAbstractGMLType(abstractTimeTopologyPrimitiveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TOPOLOGY_TYPE: {
                AbstractTopologyType abstractTopologyType = (AbstractTopologyType)theEObject;
                T result = caseAbstractTopologyType(abstractTopologyType);
                if (result == null) result = caseAbstractGMLType(abstractTopologyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE: {
                AbstractTopoPrimitiveType abstractTopoPrimitiveType = (AbstractTopoPrimitiveType)theEObject;
                T result = caseAbstractTopoPrimitiveType(abstractTopoPrimitiveType);
                if (result == null) result = caseAbstractTopologyType(abstractTopoPrimitiveType);
                if (result == null) result = caseAbstractGMLType(abstractTopoPrimitiveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.AFFINE_PLACEMENT_TYPE: {
                AffinePlacementType affinePlacementType = (AffinePlacementType)theEObject;
                T result = caseAffinePlacementType(affinePlacementType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ANGLE_CHOICE_TYPE: {
                AngleChoiceType angleChoiceType = (AngleChoiceType)theEObject;
                T result = caseAngleChoiceType(angleChoiceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ANGLE_TYPE: {
                AngleType angleType = (AngleType)theEObject;
                T result = caseAngleType(angleType);
                if (result == null) result = caseMeasureType(angleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ARC_BY_BULGE_TYPE: {
                ArcByBulgeType arcByBulgeType = (ArcByBulgeType)theEObject;
                T result = caseArcByBulgeType(arcByBulgeType);
                if (result == null) result = caseArcStringByBulgeType(arcByBulgeType);
                if (result == null) result = caseAbstractCurveSegmentType(arcByBulgeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ARC_BY_CENTER_POINT_TYPE: {
                ArcByCenterPointType arcByCenterPointType = (ArcByCenterPointType)theEObject;
                T result = caseArcByCenterPointType(arcByCenterPointType);
                if (result == null) result = caseAbstractCurveSegmentType(arcByCenterPointType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ARC_STRING_BY_BULGE_TYPE: {
                ArcStringByBulgeType arcStringByBulgeType = (ArcStringByBulgeType)theEObject;
                T result = caseArcStringByBulgeType(arcStringByBulgeType);
                if (result == null) result = caseAbstractCurveSegmentType(arcStringByBulgeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ARC_STRING_TYPE: {
                ArcStringType arcStringType = (ArcStringType)theEObject;
                T result = caseArcStringType(arcStringType);
                if (result == null) result = caseAbstractCurveSegmentType(arcStringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ARC_TYPE: {
                ArcType arcType = (ArcType)theEObject;
                T result = caseArcType(arcType);
                if (result == null) result = caseArcStringType(arcType);
                if (result == null) result = caseAbstractCurveSegmentType(arcType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.AREA_TYPE: {
                AreaType areaType = (AreaType)theEObject;
                T result = caseAreaType(areaType);
                if (result == null) result = caseMeasureType(areaType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ARRAY_ASSOCIATION_TYPE: {
                ArrayAssociationType arrayAssociationType = (ArrayAssociationType)theEObject;
                T result = caseArrayAssociationType(arrayAssociationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ARRAY_TYPE: {
                ArrayType arrayType = (ArrayType)theEObject;
                T result = caseArrayType(arrayType);
                if (result == null) result = caseAbstractGMLType(arrayType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ASSOCIATION_TYPE: {
                AssociationType associationType = (AssociationType)theEObject;
                T result = caseAssociationType(associationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BAG_TYPE: {
                BagType bagType = (BagType)theEObject;
                T result = caseBagType(bagType);
                if (result == null) result = caseAbstractGMLType(bagType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE: {
                BaseStyleDescriptorType baseStyleDescriptorType = (BaseStyleDescriptorType)theEObject;
                T result = caseBaseStyleDescriptorType(baseStyleDescriptorType);
                if (result == null) result = caseAbstractGMLType(baseStyleDescriptorType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BASE_UNIT_TYPE: {
                BaseUnitType baseUnitType = (BaseUnitType)theEObject;
                T result = caseBaseUnitType(baseUnitType);
                if (result == null) result = caseUnitDefinitionType(baseUnitType);
                if (result == null) result = caseDefinitionType(baseUnitType);
                if (result == null) result = caseAbstractGMLType(baseUnitType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BEZIER_TYPE: {
                BezierType bezierType = (BezierType)theEObject;
                T result = caseBezierType(bezierType);
                if (result == null) result = caseBSplineType(bezierType);
                if (result == null) result = caseAbstractCurveSegmentType(bezierType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BOOLEAN_PROPERTY_TYPE: {
                BooleanPropertyType booleanPropertyType = (BooleanPropertyType)theEObject;
                T result = caseBooleanPropertyType(booleanPropertyType);
                if (result == null) result = caseValuePropertyType(booleanPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BOUNDED_FEATURE_TYPE: {
                BoundedFeatureType boundedFeatureType = (BoundedFeatureType)theEObject;
                T result = caseBoundedFeatureType(boundedFeatureType);
                if (result == null) result = caseAbstractFeatureType(boundedFeatureType);
                if (result == null) result = caseAbstractGMLType(boundedFeatureType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BOUNDING_SHAPE_TYPE: {
                BoundingShapeType boundingShapeType = (BoundingShapeType)theEObject;
                T result = caseBoundingShapeType(boundingShapeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.BSPLINE_TYPE: {
                BSplineType bSplineType = (BSplineType)theEObject;
                T result = caseBSplineType(bSplineType);
                if (result == null) result = caseAbstractCurveSegmentType(bSplineType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CARTESIAN_CS_REF_TYPE: {
                CartesianCSRefType cartesianCSRefType = (CartesianCSRefType)theEObject;
                T result = caseCartesianCSRefType(cartesianCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CARTESIAN_CS_TYPE: {
                CartesianCSType cartesianCSType = (CartesianCSType)theEObject;
                T result = caseCartesianCSType(cartesianCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(cartesianCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(cartesianCSType);
                if (result == null) result = caseDefinitionType(cartesianCSType);
                if (result == null) result = caseAbstractGMLType(cartesianCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CATEGORY_EXTENT_TYPE: {
                CategoryExtentType categoryExtentType = (CategoryExtentType)theEObject;
                T result = caseCategoryExtentType(categoryExtentType);
                if (result == null) result = caseCodeOrNullListType(categoryExtentType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CATEGORY_PROPERTY_TYPE: {
                CategoryPropertyType categoryPropertyType = (CategoryPropertyType)theEObject;
                T result = caseCategoryPropertyType(categoryPropertyType);
                if (result == null) result = caseValuePropertyType(categoryPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CIRCLE_BY_CENTER_POINT_TYPE: {
                CircleByCenterPointType circleByCenterPointType = (CircleByCenterPointType)theEObject;
                T result = caseCircleByCenterPointType(circleByCenterPointType);
                if (result == null) result = caseArcByCenterPointType(circleByCenterPointType);
                if (result == null) result = caseAbstractCurveSegmentType(circleByCenterPointType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CIRCLE_TYPE: {
                CircleType circleType = (CircleType)theEObject;
                T result = caseCircleType(circleType);
                if (result == null) result = caseArcType(circleType);
                if (result == null) result = caseArcStringType(circleType);
                if (result == null) result = caseAbstractCurveSegmentType(circleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CLOTHOID_TYPE: {
                ClothoidType clothoidType = (ClothoidType)theEObject;
                T result = caseClothoidType(clothoidType);
                if (result == null) result = caseAbstractCurveSegmentType(clothoidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CODE_LIST_TYPE: {
                CodeListType codeListType = (CodeListType)theEObject;
                T result = caseCodeListType(codeListType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CODE_OR_NULL_LIST_TYPE: {
                CodeOrNullListType codeOrNullListType = (CodeOrNullListType)theEObject;
                T result = caseCodeOrNullListType(codeOrNullListType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CODE_TYPE: {
                CodeType codeType = (CodeType)theEObject;
                T result = caseCodeType(codeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOSITE_CURVE_PROPERTY_TYPE: {
                CompositeCurvePropertyType compositeCurvePropertyType = (CompositeCurvePropertyType)theEObject;
                T result = caseCompositeCurvePropertyType(compositeCurvePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOSITE_CURVE_TYPE: {
                CompositeCurveType compositeCurveType = (CompositeCurveType)theEObject;
                T result = caseCompositeCurveType(compositeCurveType);
                if (result == null) result = caseAbstractCurveType(compositeCurveType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(compositeCurveType);
                if (result == null) result = caseAbstractGeometryType(compositeCurveType);
                if (result == null) result = caseAbstractGMLType(compositeCurveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOSITE_SOLID_PROPERTY_TYPE: {
                CompositeSolidPropertyType compositeSolidPropertyType = (CompositeSolidPropertyType)theEObject;
                T result = caseCompositeSolidPropertyType(compositeSolidPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOSITE_SOLID_TYPE: {
                CompositeSolidType compositeSolidType = (CompositeSolidType)theEObject;
                T result = caseCompositeSolidType(compositeSolidType);
                if (result == null) result = caseAbstractSolidType(compositeSolidType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(compositeSolidType);
                if (result == null) result = caseAbstractGeometryType(compositeSolidType);
                if (result == null) result = caseAbstractGMLType(compositeSolidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOSITE_SURFACE_PROPERTY_TYPE: {
                CompositeSurfacePropertyType compositeSurfacePropertyType = (CompositeSurfacePropertyType)theEObject;
                T result = caseCompositeSurfacePropertyType(compositeSurfacePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOSITE_SURFACE_TYPE: {
                CompositeSurfaceType compositeSurfaceType = (CompositeSurfaceType)theEObject;
                T result = caseCompositeSurfaceType(compositeSurfaceType);
                if (result == null) result = caseAbstractSurfaceType(compositeSurfaceType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(compositeSurfaceType);
                if (result == null) result = caseAbstractGeometryType(compositeSurfaceType);
                if (result == null) result = caseAbstractGMLType(compositeSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOSITE_VALUE_TYPE: {
                CompositeValueType compositeValueType = (CompositeValueType)theEObject;
                T result = caseCompositeValueType(compositeValueType);
                if (result == null) result = caseAbstractGMLType(compositeValueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOUND_CRS_REF_TYPE: {
                CompoundCRSRefType compoundCRSRefType = (CompoundCRSRefType)theEObject;
                T result = caseCompoundCRSRefType(compoundCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COMPOUND_CRS_TYPE: {
                CompoundCRSType compoundCRSType = (CompoundCRSType)theEObject;
                T result = caseCompoundCRSType(compoundCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(compoundCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(compoundCRSType);
                if (result == null) result = caseDefinitionType(compoundCRSType);
                if (result == null) result = caseAbstractGMLType(compoundCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONCATENATED_OPERATION_REF_TYPE: {
                ConcatenatedOperationRefType concatenatedOperationRefType = (ConcatenatedOperationRefType)theEObject;
                T result = caseConcatenatedOperationRefType(concatenatedOperationRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONCATENATED_OPERATION_TYPE: {
                ConcatenatedOperationType concatenatedOperationType = (ConcatenatedOperationType)theEObject;
                T result = caseConcatenatedOperationType(concatenatedOperationType);
                if (result == null) result = caseAbstractCoordinateOperationType(concatenatedOperationType);
                if (result == null) result = caseAbstractCoordinateOperationBaseType(concatenatedOperationType);
                if (result == null) result = caseDefinitionType(concatenatedOperationType);
                if (result == null) result = caseAbstractGMLType(concatenatedOperationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONE_TYPE: {
                ConeType coneType = (ConeType)theEObject;
                T result = caseConeType(coneType);
                if (result == null) result = caseAbstractGriddedSurfaceType(coneType);
                if (result == null) result = caseAbstractParametricCurveSurfaceType(coneType);
                if (result == null) result = caseAbstractSurfacePatchType(coneType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONTAINER_PROPERTY_TYPE: {
                ContainerPropertyType containerPropertyType = (ContainerPropertyType)theEObject;
                T result = caseContainerPropertyType(containerPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONTROL_POINT_TYPE: {
                ControlPointType controlPointType = (ControlPointType)theEObject;
                T result = caseControlPointType(controlPointType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONVENTIONAL_UNIT_TYPE: {
                ConventionalUnitType conventionalUnitType = (ConventionalUnitType)theEObject;
                T result = caseConventionalUnitType(conventionalUnitType);
                if (result == null) result = caseUnitDefinitionType(conventionalUnitType);
                if (result == null) result = caseDefinitionType(conventionalUnitType);
                if (result == null) result = caseAbstractGMLType(conventionalUnitType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONVERSION_REF_TYPE: {
                ConversionRefType conversionRefType = (ConversionRefType)theEObject;
                T result = caseConversionRefType(conversionRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONVERSION_TO_PREFERRED_UNIT_TYPE: {
                ConversionToPreferredUnitType conversionToPreferredUnitType = (ConversionToPreferredUnitType)theEObject;
                T result = caseConversionToPreferredUnitType(conversionToPreferredUnitType);
                if (result == null) result = caseUnitOfMeasureType(conversionToPreferredUnitType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CONVERSION_TYPE: {
                ConversionType conversionType = (ConversionType)theEObject;
                T result = caseConversionType(conversionType);
                if (result == null) result = caseAbstractGeneralConversionType(conversionType);
                if (result == null) result = caseAbstractCoordinateOperationType(conversionType);
                if (result == null) result = caseAbstractCoordinateOperationBaseType(conversionType);
                if (result == null) result = caseDefinitionType(conversionType);
                if (result == null) result = caseAbstractGMLType(conversionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORDINATE_OPERATION_REF_TYPE: {
                CoordinateOperationRefType coordinateOperationRefType = (CoordinateOperationRefType)theEObject;
                T result = caseCoordinateOperationRefType(coordinateOperationRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORDINATE_REFERENCE_SYSTEM_REF_TYPE: {
                CoordinateReferenceSystemRefType coordinateReferenceSystemRefType = (CoordinateReferenceSystemRefType)theEObject;
                T result = caseCoordinateReferenceSystemRefType(coordinateReferenceSystemRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORDINATES_TYPE: {
                CoordinatesType coordinatesType = (CoordinatesType)theEObject;
                T result = caseCoordinatesType(coordinatesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORDINATE_SYSTEM_AXIS_BASE_TYPE: {
                CoordinateSystemAxisBaseType coordinateSystemAxisBaseType = (CoordinateSystemAxisBaseType)theEObject;
                T result = caseCoordinateSystemAxisBaseType(coordinateSystemAxisBaseType);
                if (result == null) result = caseDefinitionType(coordinateSystemAxisBaseType);
                if (result == null) result = caseAbstractGMLType(coordinateSystemAxisBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORDINATE_SYSTEM_AXIS_REF_TYPE: {
                CoordinateSystemAxisRefType coordinateSystemAxisRefType = (CoordinateSystemAxisRefType)theEObject;
                T result = caseCoordinateSystemAxisRefType(coordinateSystemAxisRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORDINATE_SYSTEM_AXIS_TYPE: {
                CoordinateSystemAxisType coordinateSystemAxisType = (CoordinateSystemAxisType)theEObject;
                T result = caseCoordinateSystemAxisType(coordinateSystemAxisType);
                if (result == null) result = caseCoordinateSystemAxisBaseType(coordinateSystemAxisType);
                if (result == null) result = caseDefinitionType(coordinateSystemAxisType);
                if (result == null) result = caseAbstractGMLType(coordinateSystemAxisType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORDINATE_SYSTEM_REF_TYPE: {
                CoordinateSystemRefType coordinateSystemRefType = (CoordinateSystemRefType)theEObject;
                T result = caseCoordinateSystemRefType(coordinateSystemRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COORD_TYPE: {
                CoordType coordType = (CoordType)theEObject;
                T result = caseCoordType(coordType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COUNT_PROPERTY_TYPE: {
                CountPropertyType countPropertyType = (CountPropertyType)theEObject;
                T result = caseCountPropertyType(countPropertyType);
                if (result == null) result = caseValuePropertyType(countPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COVARIANCE_ELEMENT_TYPE: {
                CovarianceElementType covarianceElementType = (CovarianceElementType)theEObject;
                T result = caseCovarianceElementType(covarianceElementType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COVARIANCE_MATRIX_TYPE: {
                CovarianceMatrixType covarianceMatrixType = (CovarianceMatrixType)theEObject;
                T result = caseCovarianceMatrixType(covarianceMatrixType);
                if (result == null) result = caseAbstractPositionalAccuracyType(covarianceMatrixType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.COVERAGE_FUNCTION_TYPE: {
                CoverageFunctionType coverageFunctionType = (CoverageFunctionType)theEObject;
                T result = caseCoverageFunctionType(coverageFunctionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CRS_REF_TYPE: {
                CRSRefType crsRefType = (CRSRefType)theEObject;
                T result = caseCRSRefType(crsRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CUBIC_SPLINE_TYPE: {
                CubicSplineType cubicSplineType = (CubicSplineType)theEObject;
                T result = caseCubicSplineType(cubicSplineType);
                if (result == null) result = caseAbstractCurveSegmentType(cubicSplineType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE: {
                CurveArrayPropertyType curveArrayPropertyType = (CurveArrayPropertyType)theEObject;
                T result = caseCurveArrayPropertyType(curveArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CURVE_PROPERTY_TYPE: {
                CurvePropertyType curvePropertyType = (CurvePropertyType)theEObject;
                T result = caseCurvePropertyType(curvePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CURVE_SEGMENT_ARRAY_PROPERTY_TYPE: {
                CurveSegmentArrayPropertyType curveSegmentArrayPropertyType = (CurveSegmentArrayPropertyType)theEObject;
                T result = caseCurveSegmentArrayPropertyType(curveSegmentArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CURVE_TYPE: {
                CurveType curveType = (CurveType)theEObject;
                T result = caseCurveType(curveType);
                if (result == null) result = caseAbstractCurveType(curveType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(curveType);
                if (result == null) result = caseAbstractGeometryType(curveType);
                if (result == null) result = caseAbstractGMLType(curveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CYLINDER_TYPE: {
                CylinderType cylinderType = (CylinderType)theEObject;
                T result = caseCylinderType(cylinderType);
                if (result == null) result = caseAbstractGriddedSurfaceType(cylinderType);
                if (result == null) result = caseAbstractParametricCurveSurfaceType(cylinderType);
                if (result == null) result = caseAbstractSurfacePatchType(cylinderType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CYLINDRICAL_CS_REF_TYPE: {
                CylindricalCSRefType cylindricalCSRefType = (CylindricalCSRefType)theEObject;
                T result = caseCylindricalCSRefType(cylindricalCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.CYLINDRICAL_CS_TYPE: {
                CylindricalCSType cylindricalCSType = (CylindricalCSType)theEObject;
                T result = caseCylindricalCSType(cylindricalCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(cylindricalCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(cylindricalCSType);
                if (result == null) result = caseDefinitionType(cylindricalCSType);
                if (result == null) result = caseAbstractGMLType(cylindricalCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DATA_BLOCK_TYPE: {
                DataBlockType dataBlockType = (DataBlockType)theEObject;
                T result = caseDataBlockType(dataBlockType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DATUM_REF_TYPE: {
                DatumRefType datumRefType = (DatumRefType)theEObject;
                T result = caseDatumRefType(datumRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DEFAULT_STYLE_PROPERTY_TYPE: {
                DefaultStylePropertyType defaultStylePropertyType = (DefaultStylePropertyType)theEObject;
                T result = caseDefaultStylePropertyType(defaultStylePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DEFINITION_PROXY_TYPE: {
                DefinitionProxyType definitionProxyType = (DefinitionProxyType)theEObject;
                T result = caseDefinitionProxyType(definitionProxyType);
                if (result == null) result = caseDefinitionType(definitionProxyType);
                if (result == null) result = caseAbstractGMLType(definitionProxyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DEFINITION_TYPE: {
                DefinitionType definitionType = (DefinitionType)theEObject;
                T result = caseDefinitionType(definitionType);
                if (result == null) result = caseAbstractGMLType(definitionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DEGREES_TYPE: {
                DegreesType degreesType = (DegreesType)theEObject;
                T result = caseDegreesType(degreesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DERIVATION_UNIT_TERM_TYPE: {
                DerivationUnitTermType derivationUnitTermType = (DerivationUnitTermType)theEObject;
                T result = caseDerivationUnitTermType(derivationUnitTermType);
                if (result == null) result = caseUnitOfMeasureType(derivationUnitTermType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DERIVED_CRS_REF_TYPE: {
                DerivedCRSRefType derivedCRSRefType = (DerivedCRSRefType)theEObject;
                T result = caseDerivedCRSRefType(derivedCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DERIVED_CRS_TYPE: {
                DerivedCRSType derivedCRSType = (DerivedCRSType)theEObject;
                T result = caseDerivedCRSType(derivedCRSType);
                if (result == null) result = caseAbstractGeneralDerivedCRSType(derivedCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(derivedCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(derivedCRSType);
                if (result == null) result = caseDefinitionType(derivedCRSType);
                if (result == null) result = caseAbstractGMLType(derivedCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DERIVED_CRS_TYPE_TYPE: {
                DerivedCRSTypeType derivedCRSTypeType = (DerivedCRSTypeType)theEObject;
                T result = caseDerivedCRSTypeType(derivedCRSTypeType);
                if (result == null) result = caseCodeType(derivedCRSTypeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DERIVED_UNIT_TYPE: {
                DerivedUnitType derivedUnitType = (DerivedUnitType)theEObject;
                T result = caseDerivedUnitType(derivedUnitType);
                if (result == null) result = caseUnitDefinitionType(derivedUnitType);
                if (result == null) result = caseDefinitionType(derivedUnitType);
                if (result == null) result = caseAbstractGMLType(derivedUnitType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DICTIONARY_ENTRY_TYPE: {
                DictionaryEntryType dictionaryEntryType = (DictionaryEntryType)theEObject;
                T result = caseDictionaryEntryType(dictionaryEntryType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DICTIONARY_TYPE: {
                DictionaryType dictionaryType = (DictionaryType)theEObject;
                T result = caseDictionaryType(dictionaryType);
                if (result == null) result = caseDefinitionType(dictionaryType);
                if (result == null) result = caseAbstractGMLType(dictionaryType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTED_EDGE_PROPERTY_TYPE: {
                DirectedEdgePropertyType directedEdgePropertyType = (DirectedEdgePropertyType)theEObject;
                T result = caseDirectedEdgePropertyType(directedEdgePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTED_FACE_PROPERTY_TYPE: {
                DirectedFacePropertyType directedFacePropertyType = (DirectedFacePropertyType)theEObject;
                T result = caseDirectedFacePropertyType(directedFacePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTED_NODE_PROPERTY_TYPE: {
                DirectedNodePropertyType directedNodePropertyType = (DirectedNodePropertyType)theEObject;
                T result = caseDirectedNodePropertyType(directedNodePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTED_OBSERVATION_AT_DISTANCE_TYPE: {
                DirectedObservationAtDistanceType directedObservationAtDistanceType = (DirectedObservationAtDistanceType)theEObject;
                T result = caseDirectedObservationAtDistanceType(directedObservationAtDistanceType);
                if (result == null) result = caseDirectedObservationType(directedObservationAtDistanceType);
                if (result == null) result = caseObservationType(directedObservationAtDistanceType);
                if (result == null) result = caseAbstractFeatureType(directedObservationAtDistanceType);
                if (result == null) result = caseAbstractGMLType(directedObservationAtDistanceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTED_OBSERVATION_TYPE: {
                DirectedObservationType directedObservationType = (DirectedObservationType)theEObject;
                T result = caseDirectedObservationType(directedObservationType);
                if (result == null) result = caseObservationType(directedObservationType);
                if (result == null) result = caseAbstractFeatureType(directedObservationType);
                if (result == null) result = caseAbstractGMLType(directedObservationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTED_TOPO_SOLID_PROPERTY_TYPE: {
                DirectedTopoSolidPropertyType directedTopoSolidPropertyType = (DirectedTopoSolidPropertyType)theEObject;
                T result = caseDirectedTopoSolidPropertyType(directedTopoSolidPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTION_PROPERTY_TYPE: {
                DirectionPropertyType directionPropertyType = (DirectionPropertyType)theEObject;
                T result = caseDirectionPropertyType(directionPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECTION_VECTOR_TYPE: {
                DirectionVectorType directionVectorType = (DirectionVectorType)theEObject;
                T result = caseDirectionVectorType(directionVectorType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECT_POSITION_LIST_TYPE: {
                DirectPositionListType directPositionListType = (DirectPositionListType)theEObject;
                T result = caseDirectPositionListType(directPositionListType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DIRECT_POSITION_TYPE: {
                DirectPositionType directPositionType = (DirectPositionType)theEObject;
                T result = caseDirectPositionType(directPositionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DMS_ANGLE_TYPE: {
                DMSAngleType dmsAngleType = (DMSAngleType)theEObject;
                T result = caseDMSAngleType(dmsAngleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                T result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DOMAIN_SET_TYPE: {
                DomainSetType domainSetType = (DomainSetType)theEObject;
                T result = caseDomainSetType(domainSetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DYNAMIC_FEATURE_COLLECTION_TYPE: {
                DynamicFeatureCollectionType dynamicFeatureCollectionType = (DynamicFeatureCollectionType)theEObject;
                T result = caseDynamicFeatureCollectionType(dynamicFeatureCollectionType);
                if (result == null) result = caseFeatureCollectionType(dynamicFeatureCollectionType);
                if (result == null) result = caseAbstractFeatureCollectionType(dynamicFeatureCollectionType);
                if (result == null) result = caseAbstractFeatureType(dynamicFeatureCollectionType);
                if (result == null) result = caseAbstractGMLType(dynamicFeatureCollectionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.DYNAMIC_FEATURE_TYPE: {
                DynamicFeatureType dynamicFeatureType = (DynamicFeatureType)theEObject;
                T result = caseDynamicFeatureType(dynamicFeatureType);
                if (result == null) result = caseAbstractFeatureType(dynamicFeatureType);
                if (result == null) result = caseAbstractGMLType(dynamicFeatureType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.EDGE_TYPE: {
                EdgeType edgeType = (EdgeType)theEObject;
                T result = caseEdgeType(edgeType);
                if (result == null) result = caseAbstractTopoPrimitiveType(edgeType);
                if (result == null) result = caseAbstractTopologyType(edgeType);
                if (result == null) result = caseAbstractGMLType(edgeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ELLIPSOIDAL_CS_REF_TYPE: {
                EllipsoidalCSRefType ellipsoidalCSRefType = (EllipsoidalCSRefType)theEObject;
                T result = caseEllipsoidalCSRefType(ellipsoidalCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ELLIPSOIDAL_CS_TYPE: {
                EllipsoidalCSType ellipsoidalCSType = (EllipsoidalCSType)theEObject;
                T result = caseEllipsoidalCSType(ellipsoidalCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(ellipsoidalCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(ellipsoidalCSType);
                if (result == null) result = caseDefinitionType(ellipsoidalCSType);
                if (result == null) result = caseAbstractGMLType(ellipsoidalCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ELLIPSOID_BASE_TYPE: {
                EllipsoidBaseType ellipsoidBaseType = (EllipsoidBaseType)theEObject;
                T result = caseEllipsoidBaseType(ellipsoidBaseType);
                if (result == null) result = caseDefinitionType(ellipsoidBaseType);
                if (result == null) result = caseAbstractGMLType(ellipsoidBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ELLIPSOID_REF_TYPE: {
                EllipsoidRefType ellipsoidRefType = (EllipsoidRefType)theEObject;
                T result = caseEllipsoidRefType(ellipsoidRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ELLIPSOID_TYPE: {
                EllipsoidType ellipsoidType = (EllipsoidType)theEObject;
                T result = caseEllipsoidType(ellipsoidType);
                if (result == null) result = caseEllipsoidBaseType(ellipsoidType);
                if (result == null) result = caseDefinitionType(ellipsoidType);
                if (result == null) result = caseAbstractGMLType(ellipsoidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ENGINEERING_CRS_REF_TYPE: {
                EngineeringCRSRefType engineeringCRSRefType = (EngineeringCRSRefType)theEObject;
                T result = caseEngineeringCRSRefType(engineeringCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ENGINEERING_CRS_TYPE: {
                EngineeringCRSType engineeringCRSType = (EngineeringCRSType)theEObject;
                T result = caseEngineeringCRSType(engineeringCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(engineeringCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(engineeringCRSType);
                if (result == null) result = caseDefinitionType(engineeringCRSType);
                if (result == null) result = caseAbstractGMLType(engineeringCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ENGINEERING_DATUM_REF_TYPE: {
                EngineeringDatumRefType engineeringDatumRefType = (EngineeringDatumRefType)theEObject;
                T result = caseEngineeringDatumRefType(engineeringDatumRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ENGINEERING_DATUM_TYPE: {
                EngineeringDatumType engineeringDatumType = (EngineeringDatumType)theEObject;
                T result = caseEngineeringDatumType(engineeringDatumType);
                if (result == null) result = caseAbstractDatumType(engineeringDatumType);
                if (result == null) result = caseAbstractDatumBaseType(engineeringDatumType);
                if (result == null) result = caseDefinitionType(engineeringDatumType);
                if (result == null) result = caseAbstractGMLType(engineeringDatumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ENVELOPE_TYPE: {
                EnvelopeType envelopeType = (EnvelopeType)theEObject;
                T result = caseEnvelopeType(envelopeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE: {
                EnvelopeWithTimePeriodType envelopeWithTimePeriodType = (EnvelopeWithTimePeriodType)theEObject;
                T result = caseEnvelopeWithTimePeriodType(envelopeWithTimePeriodType);
                if (result == null) result = caseEnvelopeType(envelopeWithTimePeriodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.EXTENT_TYPE: {
                ExtentType extentType = (ExtentType)theEObject;
                T result = caseExtentType(extentType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FACE_TYPE: {
                FaceType faceType = (FaceType)theEObject;
                T result = caseFaceType(faceType);
                if (result == null) result = caseAbstractTopoPrimitiveType(faceType);
                if (result == null) result = caseAbstractTopologyType(faceType);
                if (result == null) result = caseAbstractGMLType(faceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FEATURE_ARRAY_PROPERTY_TYPE: {
                FeatureArrayPropertyType featureArrayPropertyType = (FeatureArrayPropertyType)theEObject;
                T result = caseFeatureArrayPropertyType(featureArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FEATURE_COLLECTION_TYPE: {
                FeatureCollectionType featureCollectionType = (FeatureCollectionType)theEObject;
                T result = caseFeatureCollectionType(featureCollectionType);
                if (result == null) result = caseAbstractFeatureCollectionType(featureCollectionType);
                if (result == null) result = caseAbstractFeatureType(featureCollectionType);
                if (result == null) result = caseAbstractGMLType(featureCollectionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FEATURE_PROPERTY_TYPE: {
                FeaturePropertyType featurePropertyType = (FeaturePropertyType)theEObject;
                T result = caseFeaturePropertyType(featurePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FEATURE_STYLE_PROPERTY_TYPE: {
                FeatureStylePropertyType featureStylePropertyType = (FeatureStylePropertyType)theEObject;
                T result = caseFeatureStylePropertyType(featureStylePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FEATURE_STYLE_TYPE: {
                FeatureStyleType featureStyleType = (FeatureStyleType)theEObject;
                T result = caseFeatureStyleType(featureStyleType);
                if (result == null) result = caseAbstractGMLType(featureStyleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FILE_TYPE: {
                FileType fileType = (FileType)theEObject;
                T result = caseFileType(fileType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.FORMULA_TYPE: {
                FormulaType formulaType = (FormulaType)theEObject;
                T result = caseFormulaType(formulaType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GENERAL_CONVERSION_REF_TYPE: {
                GeneralConversionRefType generalConversionRefType = (GeneralConversionRefType)theEObject;
                T result = caseGeneralConversionRefType(generalConversionRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GENERAL_TRANSFORMATION_REF_TYPE: {
                GeneralTransformationRefType generalTransformationRefType = (GeneralTransformationRefType)theEObject;
                T result = caseGeneralTransformationRefType(generalTransformationRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GENERIC_META_DATA_TYPE: {
                GenericMetaDataType genericMetaDataType = (GenericMetaDataType)theEObject;
                T result = caseGenericMetaDataType(genericMetaDataType);
                if (result == null) result = caseAbstractMetaDataType(genericMetaDataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOCENTRIC_CRS_REF_TYPE: {
                GeocentricCRSRefType geocentricCRSRefType = (GeocentricCRSRefType)theEObject;
                T result = caseGeocentricCRSRefType(geocentricCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOCENTRIC_CRS_TYPE: {
                GeocentricCRSType geocentricCRSType = (GeocentricCRSType)theEObject;
                T result = caseGeocentricCRSType(geocentricCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(geocentricCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(geocentricCRSType);
                if (result == null) result = caseDefinitionType(geocentricCRSType);
                if (result == null) result = caseAbstractGMLType(geocentricCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEODESIC_STRING_TYPE: {
                GeodesicStringType geodesicStringType = (GeodesicStringType)theEObject;
                T result = caseGeodesicStringType(geodesicStringType);
                if (result == null) result = caseAbstractCurveSegmentType(geodesicStringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEODESIC_TYPE: {
                GeodesicType geodesicType = (GeodesicType)theEObject;
                T result = caseGeodesicType(geodesicType);
                if (result == null) result = caseGeodesicStringType(geodesicType);
                if (result == null) result = caseAbstractCurveSegmentType(geodesicType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEODETIC_DATUM_REF_TYPE: {
                GeodeticDatumRefType geodeticDatumRefType = (GeodeticDatumRefType)theEObject;
                T result = caseGeodeticDatumRefType(geodeticDatumRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEODETIC_DATUM_TYPE: {
                GeodeticDatumType geodeticDatumType = (GeodeticDatumType)theEObject;
                T result = caseGeodeticDatumType(geodeticDatumType);
                if (result == null) result = caseAbstractDatumType(geodeticDatumType);
                if (result == null) result = caseAbstractDatumBaseType(geodeticDatumType);
                if (result == null) result = caseDefinitionType(geodeticDatumType);
                if (result == null) result = caseAbstractGMLType(geodeticDatumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOGRAPHIC_CRS_REF_TYPE: {
                GeographicCRSRefType geographicCRSRefType = (GeographicCRSRefType)theEObject;
                T result = caseGeographicCRSRefType(geographicCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOGRAPHIC_CRS_TYPE: {
                GeographicCRSType geographicCRSType = (GeographicCRSType)theEObject;
                T result = caseGeographicCRSType(geographicCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(geographicCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(geographicCRSType);
                if (result == null) result = caseDefinitionType(geographicCRSType);
                if (result == null) result = caseAbstractGMLType(geographicCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOMETRIC_COMPLEX_PROPERTY_TYPE: {
                GeometricComplexPropertyType geometricComplexPropertyType = (GeometricComplexPropertyType)theEObject;
                T result = caseGeometricComplexPropertyType(geometricComplexPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE: {
                GeometricComplexType geometricComplexType = (GeometricComplexType)theEObject;
                T result = caseGeometricComplexType(geometricComplexType);
                if (result == null) result = caseAbstractGeometryType(geometricComplexType);
                if (result == null) result = caseAbstractGMLType(geometricComplexType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOMETRIC_PRIMITIVE_PROPERTY_TYPE: {
                GeometricPrimitivePropertyType geometricPrimitivePropertyType = (GeometricPrimitivePropertyType)theEObject;
                T result = caseGeometricPrimitivePropertyType(geometricPrimitivePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOMETRY_ARRAY_PROPERTY_TYPE: {
                GeometryArrayPropertyType geometryArrayPropertyType = (GeometryArrayPropertyType)theEObject;
                T result = caseGeometryArrayPropertyType(geometryArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOMETRY_PROPERTY_TYPE: {
                GeometryPropertyType geometryPropertyType = (GeometryPropertyType)theEObject;
                T result = caseGeometryPropertyType(geometryPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOMETRY_STYLE_PROPERTY_TYPE: {
                GeometryStylePropertyType geometryStylePropertyType = (GeometryStylePropertyType)theEObject;
                T result = caseGeometryStylePropertyType(geometryStylePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GEOMETRY_STYLE_TYPE: {
                GeometryStyleType geometryStyleType = (GeometryStyleType)theEObject;
                T result = caseGeometryStyleType(geometryStyleType);
                if (result == null) result = caseBaseStyleDescriptorType(geometryStyleType);
                if (result == null) result = caseAbstractGMLType(geometryStyleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRAPH_STYLE_PROPERTY_TYPE: {
                GraphStylePropertyType graphStylePropertyType = (GraphStylePropertyType)theEObject;
                T result = caseGraphStylePropertyType(graphStylePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRAPH_STYLE_TYPE: {
                GraphStyleType graphStyleType = (GraphStyleType)theEObject;
                T result = caseGraphStyleType(graphStyleType);
                if (result == null) result = caseBaseStyleDescriptorType(graphStyleType);
                if (result == null) result = caseAbstractGMLType(graphStyleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRID_COVERAGE_TYPE: {
                GridCoverageType gridCoverageType = (GridCoverageType)theEObject;
                T result = caseGridCoverageType(gridCoverageType);
                if (result == null) result = caseAbstractDiscreteCoverageType(gridCoverageType);
                if (result == null) result = caseAbstractCoverageType(gridCoverageType);
                if (result == null) result = caseAbstractFeatureType(gridCoverageType);
                if (result == null) result = caseAbstractGMLType(gridCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRID_DOMAIN_TYPE: {
                GridDomainType gridDomainType = (GridDomainType)theEObject;
                T result = caseGridDomainType(gridDomainType);
                if (result == null) result = caseDomainSetType(gridDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRID_ENVELOPE_TYPE: {
                GridEnvelopeType gridEnvelopeType = (GridEnvelopeType)theEObject;
                T result = caseGridEnvelopeType(gridEnvelopeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRID_FUNCTION_TYPE: {
                GridFunctionType gridFunctionType = (GridFunctionType)theEObject;
                T result = caseGridFunctionType(gridFunctionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRID_LENGTH_TYPE: {
                GridLengthType gridLengthType = (GridLengthType)theEObject;
                T result = caseGridLengthType(gridLengthType);
                if (result == null) result = caseMeasureType(gridLengthType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRID_LIMITS_TYPE: {
                GridLimitsType gridLimitsType = (GridLimitsType)theEObject;
                T result = caseGridLimitsType(gridLimitsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.GRID_TYPE: {
                GridType gridType = (GridType)theEObject;
                T result = caseGridType(gridType);
                if (result == null) result = caseAbstractGeometryType(gridType);
                if (result == null) result = caseAbstractGMLType(gridType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.HISTORY_PROPERTY_TYPE: {
                HistoryPropertyType historyPropertyType = (HistoryPropertyType)theEObject;
                T result = caseHistoryPropertyType(historyPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.IDENTIFIER_TYPE: {
                IdentifierType identifierType = (IdentifierType)theEObject;
                T result = caseIdentifierType(identifierType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.IMAGE_CRS_REF_TYPE: {
                ImageCRSRefType imageCRSRefType = (ImageCRSRefType)theEObject;
                T result = caseImageCRSRefType(imageCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.IMAGE_CRS_TYPE: {
                ImageCRSType imageCRSType = (ImageCRSType)theEObject;
                T result = caseImageCRSType(imageCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(imageCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(imageCRSType);
                if (result == null) result = caseDefinitionType(imageCRSType);
                if (result == null) result = caseAbstractGMLType(imageCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.IMAGE_DATUM_REF_TYPE: {
                ImageDatumRefType imageDatumRefType = (ImageDatumRefType)theEObject;
                T result = caseImageDatumRefType(imageDatumRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.IMAGE_DATUM_TYPE: {
                ImageDatumType imageDatumType = (ImageDatumType)theEObject;
                T result = caseImageDatumType(imageDatumType);
                if (result == null) result = caseAbstractDatumType(imageDatumType);
                if (result == null) result = caseAbstractDatumBaseType(imageDatumType);
                if (result == null) result = caseDefinitionType(imageDatumType);
                if (result == null) result = caseAbstractGMLType(imageDatumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.INDEX_MAP_TYPE: {
                IndexMapType indexMapType = (IndexMapType)theEObject;
                T result = caseIndexMapType(indexMapType);
                if (result == null) result = caseGridFunctionType(indexMapType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.INDIRECT_ENTRY_TYPE: {
                IndirectEntryType indirectEntryType = (IndirectEntryType)theEObject;
                T result = caseIndirectEntryType(indirectEntryType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ISOLATED_PROPERTY_TYPE: {
                IsolatedPropertyType isolatedPropertyType = (IsolatedPropertyType)theEObject;
                T result = caseIsolatedPropertyType(isolatedPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.KNOT_PROPERTY_TYPE: {
                KnotPropertyType knotPropertyType = (KnotPropertyType)theEObject;
                T result = caseKnotPropertyType(knotPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.KNOT_TYPE: {
                KnotType knotType = (KnotType)theEObject;
                T result = caseKnotType(knotType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LABEL_STYLE_PROPERTY_TYPE: {
                LabelStylePropertyType labelStylePropertyType = (LabelStylePropertyType)theEObject;
                T result = caseLabelStylePropertyType(labelStylePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LABEL_STYLE_TYPE: {
                LabelStyleType labelStyleType = (LabelStyleType)theEObject;
                T result = caseLabelStyleType(labelStyleType);
                if (result == null) result = caseBaseStyleDescriptorType(labelStyleType);
                if (result == null) result = caseAbstractGMLType(labelStyleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LABEL_TYPE: {
                LabelType labelType = (LabelType)theEObject;
                T result = caseLabelType(labelType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LENGTH_TYPE: {
                LengthType lengthType = (LengthType)theEObject;
                T result = caseLengthType(lengthType);
                if (result == null) result = caseMeasureType(lengthType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINEAR_CS_REF_TYPE: {
                LinearCSRefType linearCSRefType = (LinearCSRefType)theEObject;
                T result = caseLinearCSRefType(linearCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINEAR_CS_TYPE: {
                LinearCSType linearCSType = (LinearCSType)theEObject;
                T result = caseLinearCSType(linearCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(linearCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(linearCSType);
                if (result == null) result = caseDefinitionType(linearCSType);
                if (result == null) result = caseAbstractGMLType(linearCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINEAR_RING_PROPERTY_TYPE: {
                LinearRingPropertyType linearRingPropertyType = (LinearRingPropertyType)theEObject;
                T result = caseLinearRingPropertyType(linearRingPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINEAR_RING_TYPE: {
                LinearRingType linearRingType = (LinearRingType)theEObject;
                T result = caseLinearRingType(linearRingType);
                if (result == null) result = caseAbstractRingType(linearRingType);
                if (result == null) result = caseAbstractGeometryType(linearRingType);
                if (result == null) result = caseAbstractGMLType(linearRingType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINE_STRING_PROPERTY_TYPE: {
                LineStringPropertyType lineStringPropertyType = (LineStringPropertyType)theEObject;
                T result = caseLineStringPropertyType(lineStringPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINE_STRING_SEGMENT_ARRAY_PROPERTY_TYPE: {
                LineStringSegmentArrayPropertyType lineStringSegmentArrayPropertyType = (LineStringSegmentArrayPropertyType)theEObject;
                T result = caseLineStringSegmentArrayPropertyType(lineStringSegmentArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINE_STRING_SEGMENT_TYPE: {
                LineStringSegmentType lineStringSegmentType = (LineStringSegmentType)theEObject;
                T result = caseLineStringSegmentType(lineStringSegmentType);
                if (result == null) result = caseAbstractCurveSegmentType(lineStringSegmentType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LINE_STRING_TYPE: {
                LineStringType lineStringType = (LineStringType)theEObject;
                T result = caseLineStringType(lineStringType);
                if (result == null) result = caseAbstractCurveType(lineStringType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(lineStringType);
                if (result == null) result = caseAbstractGeometryType(lineStringType);
                if (result == null) result = caseAbstractGMLType(lineStringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.LOCATION_PROPERTY_TYPE: {
                LocationPropertyType locationPropertyType = (LocationPropertyType)theEObject;
                T result = caseLocationPropertyType(locationPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MEASURE_LIST_TYPE: {
                MeasureListType measureListType = (MeasureListType)theEObject;
                T result = caseMeasureListType(measureListType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MEASURE_OR_NULL_LIST_TYPE: {
                MeasureOrNullListType measureOrNullListType = (MeasureOrNullListType)theEObject;
                T result = caseMeasureOrNullListType(measureOrNullListType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MEASURE_TYPE: {
                MeasureType measureType = (MeasureType)theEObject;
                T result = caseMeasureType(measureType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.META_DATA_PROPERTY_TYPE: {
                MetaDataPropertyType metaDataPropertyType = (MetaDataPropertyType)theEObject;
                T result = caseMetaDataPropertyType(metaDataPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE: {
                MovingObjectStatusType movingObjectStatusType = (MovingObjectStatusType)theEObject;
                T result = caseMovingObjectStatusType(movingObjectStatusType);
                if (result == null) result = caseAbstractTimeSliceType(movingObjectStatusType);
                if (result == null) result = caseAbstractGMLType(movingObjectStatusType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_CURVE_COVERAGE_TYPE: {
                MultiCurveCoverageType multiCurveCoverageType = (MultiCurveCoverageType)theEObject;
                T result = caseMultiCurveCoverageType(multiCurveCoverageType);
                if (result == null) result = caseAbstractDiscreteCoverageType(multiCurveCoverageType);
                if (result == null) result = caseAbstractCoverageType(multiCurveCoverageType);
                if (result == null) result = caseAbstractFeatureType(multiCurveCoverageType);
                if (result == null) result = caseAbstractGMLType(multiCurveCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE: {
                MultiCurveDomainType multiCurveDomainType = (MultiCurveDomainType)theEObject;
                T result = caseMultiCurveDomainType(multiCurveDomainType);
                if (result == null) result = caseDomainSetType(multiCurveDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_CURVE_PROPERTY_TYPE: {
                MultiCurvePropertyType multiCurvePropertyType = (MultiCurvePropertyType)theEObject;
                T result = caseMultiCurvePropertyType(multiCurvePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_CURVE_TYPE: {
                MultiCurveType multiCurveType = (MultiCurveType)theEObject;
                T result = caseMultiCurveType(multiCurveType);
                if (result == null) result = caseAbstractGeometricAggregateType(multiCurveType);
                if (result == null) result = caseAbstractGeometryType(multiCurveType);
                if (result == null) result = caseAbstractGMLType(multiCurveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_GEOMETRY_PROPERTY_TYPE: {
                MultiGeometryPropertyType multiGeometryPropertyType = (MultiGeometryPropertyType)theEObject;
                T result = caseMultiGeometryPropertyType(multiGeometryPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_GEOMETRY_TYPE: {
                MultiGeometryType multiGeometryType = (MultiGeometryType)theEObject;
                T result = caseMultiGeometryType(multiGeometryType);
                if (result == null) result = caseAbstractGeometricAggregateType(multiGeometryType);
                if (result == null) result = caseAbstractGeometryType(multiGeometryType);
                if (result == null) result = caseAbstractGMLType(multiGeometryType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_LINE_STRING_PROPERTY_TYPE: {
                MultiLineStringPropertyType multiLineStringPropertyType = (MultiLineStringPropertyType)theEObject;
                T result = caseMultiLineStringPropertyType(multiLineStringPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_LINE_STRING_TYPE: {
                MultiLineStringType multiLineStringType = (MultiLineStringType)theEObject;
                T result = caseMultiLineStringType(multiLineStringType);
                if (result == null) result = caseAbstractGeometricAggregateType(multiLineStringType);
                if (result == null) result = caseAbstractGeometryType(multiLineStringType);
                if (result == null) result = caseAbstractGMLType(multiLineStringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_POINT_COVERAGE_TYPE: {
                MultiPointCoverageType multiPointCoverageType = (MultiPointCoverageType)theEObject;
                T result = caseMultiPointCoverageType(multiPointCoverageType);
                if (result == null) result = caseAbstractDiscreteCoverageType(multiPointCoverageType);
                if (result == null) result = caseAbstractCoverageType(multiPointCoverageType);
                if (result == null) result = caseAbstractFeatureType(multiPointCoverageType);
                if (result == null) result = caseAbstractGMLType(multiPointCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_POINT_DOMAIN_TYPE: {
                MultiPointDomainType multiPointDomainType = (MultiPointDomainType)theEObject;
                T result = caseMultiPointDomainType(multiPointDomainType);
                if (result == null) result = caseDomainSetType(multiPointDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_POINT_PROPERTY_TYPE: {
                MultiPointPropertyType multiPointPropertyType = (MultiPointPropertyType)theEObject;
                T result = caseMultiPointPropertyType(multiPointPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_POINT_TYPE: {
                MultiPointType multiPointType = (MultiPointType)theEObject;
                T result = caseMultiPointType(multiPointType);
                if (result == null) result = caseAbstractGeometricAggregateType(multiPointType);
                if (result == null) result = caseAbstractGeometryType(multiPointType);
                if (result == null) result = caseAbstractGMLType(multiPointType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_POLYGON_PROPERTY_TYPE: {
                MultiPolygonPropertyType multiPolygonPropertyType = (MultiPolygonPropertyType)theEObject;
                T result = caseMultiPolygonPropertyType(multiPolygonPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_POLYGON_TYPE: {
                MultiPolygonType multiPolygonType = (MultiPolygonType)theEObject;
                T result = caseMultiPolygonType(multiPolygonType);
                if (result == null) result = caseAbstractGeometricAggregateType(multiPolygonType);
                if (result == null) result = caseAbstractGeometryType(multiPolygonType);
                if (result == null) result = caseAbstractGMLType(multiPolygonType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SOLID_COVERAGE_TYPE: {
                MultiSolidCoverageType multiSolidCoverageType = (MultiSolidCoverageType)theEObject;
                T result = caseMultiSolidCoverageType(multiSolidCoverageType);
                if (result == null) result = caseAbstractDiscreteCoverageType(multiSolidCoverageType);
                if (result == null) result = caseAbstractCoverageType(multiSolidCoverageType);
                if (result == null) result = caseAbstractFeatureType(multiSolidCoverageType);
                if (result == null) result = caseAbstractGMLType(multiSolidCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SOLID_DOMAIN_TYPE: {
                MultiSolidDomainType multiSolidDomainType = (MultiSolidDomainType)theEObject;
                T result = caseMultiSolidDomainType(multiSolidDomainType);
                if (result == null) result = caseDomainSetType(multiSolidDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SOLID_PROPERTY_TYPE: {
                MultiSolidPropertyType multiSolidPropertyType = (MultiSolidPropertyType)theEObject;
                T result = caseMultiSolidPropertyType(multiSolidPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SOLID_TYPE: {
                MultiSolidType multiSolidType = (MultiSolidType)theEObject;
                T result = caseMultiSolidType(multiSolidType);
                if (result == null) result = caseAbstractGeometricAggregateType(multiSolidType);
                if (result == null) result = caseAbstractGeometryType(multiSolidType);
                if (result == null) result = caseAbstractGMLType(multiSolidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SURFACE_COVERAGE_TYPE: {
                MultiSurfaceCoverageType multiSurfaceCoverageType = (MultiSurfaceCoverageType)theEObject;
                T result = caseMultiSurfaceCoverageType(multiSurfaceCoverageType);
                if (result == null) result = caseAbstractDiscreteCoverageType(multiSurfaceCoverageType);
                if (result == null) result = caseAbstractCoverageType(multiSurfaceCoverageType);
                if (result == null) result = caseAbstractFeatureType(multiSurfaceCoverageType);
                if (result == null) result = caseAbstractGMLType(multiSurfaceCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SURFACE_DOMAIN_TYPE: {
                MultiSurfaceDomainType multiSurfaceDomainType = (MultiSurfaceDomainType)theEObject;
                T result = caseMultiSurfaceDomainType(multiSurfaceDomainType);
                if (result == null) result = caseDomainSetType(multiSurfaceDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SURFACE_PROPERTY_TYPE: {
                MultiSurfacePropertyType multiSurfacePropertyType = (MultiSurfacePropertyType)theEObject;
                T result = caseMultiSurfacePropertyType(multiSurfacePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.MULTI_SURFACE_TYPE: {
                MultiSurfaceType multiSurfaceType = (MultiSurfaceType)theEObject;
                T result = caseMultiSurfaceType(multiSurfaceType);
                if (result == null) result = caseAbstractGeometricAggregateType(multiSurfaceType);
                if (result == null) result = caseAbstractGeometryType(multiSurfaceType);
                if (result == null) result = caseAbstractGMLType(multiSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.NODE_TYPE: {
                NodeType nodeType = (NodeType)theEObject;
                T result = caseNodeType(nodeType);
                if (result == null) result = caseAbstractTopoPrimitiveType(nodeType);
                if (result == null) result = caseAbstractTopologyType(nodeType);
                if (result == null) result = caseAbstractGMLType(nodeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OBLIQUE_CARTESIAN_CS_REF_TYPE: {
                ObliqueCartesianCSRefType obliqueCartesianCSRefType = (ObliqueCartesianCSRefType)theEObject;
                T result = caseObliqueCartesianCSRefType(obliqueCartesianCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OBLIQUE_CARTESIAN_CS_TYPE: {
                ObliqueCartesianCSType obliqueCartesianCSType = (ObliqueCartesianCSType)theEObject;
                T result = caseObliqueCartesianCSType(obliqueCartesianCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(obliqueCartesianCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(obliqueCartesianCSType);
                if (result == null) result = caseDefinitionType(obliqueCartesianCSType);
                if (result == null) result = caseAbstractGMLType(obliqueCartesianCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OBSERVATION_TYPE: {
                ObservationType observationType = (ObservationType)theEObject;
                T result = caseObservationType(observationType);
                if (result == null) result = caseAbstractFeatureType(observationType);
                if (result == null) result = caseAbstractGMLType(observationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OFFSET_CURVE_TYPE: {
                OffsetCurveType offsetCurveType = (OffsetCurveType)theEObject;
                T result = caseOffsetCurveType(offsetCurveType);
                if (result == null) result = caseAbstractCurveSegmentType(offsetCurveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_METHOD_BASE_TYPE: {
                OperationMethodBaseType operationMethodBaseType = (OperationMethodBaseType)theEObject;
                T result = caseOperationMethodBaseType(operationMethodBaseType);
                if (result == null) result = caseDefinitionType(operationMethodBaseType);
                if (result == null) result = caseAbstractGMLType(operationMethodBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_METHOD_REF_TYPE: {
                OperationMethodRefType operationMethodRefType = (OperationMethodRefType)theEObject;
                T result = caseOperationMethodRefType(operationMethodRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_METHOD_TYPE: {
                OperationMethodType operationMethodType = (OperationMethodType)theEObject;
                T result = caseOperationMethodType(operationMethodType);
                if (result == null) result = caseOperationMethodBaseType(operationMethodType);
                if (result == null) result = caseDefinitionType(operationMethodType);
                if (result == null) result = caseAbstractGMLType(operationMethodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_PARAMETER_BASE_TYPE: {
                OperationParameterBaseType operationParameterBaseType = (OperationParameterBaseType)theEObject;
                T result = caseOperationParameterBaseType(operationParameterBaseType);
                if (result == null) result = caseAbstractGeneralOperationParameterType(operationParameterBaseType);
                if (result == null) result = caseDefinitionType(operationParameterBaseType);
                if (result == null) result = caseAbstractGMLType(operationParameterBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE: {
                OperationParameterGroupBaseType operationParameterGroupBaseType = (OperationParameterGroupBaseType)theEObject;
                T result = caseOperationParameterGroupBaseType(operationParameterGroupBaseType);
                if (result == null) result = caseAbstractGeneralOperationParameterType(operationParameterGroupBaseType);
                if (result == null) result = caseDefinitionType(operationParameterGroupBaseType);
                if (result == null) result = caseAbstractGMLType(operationParameterGroupBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_PARAMETER_GROUP_REF_TYPE: {
                OperationParameterGroupRefType operationParameterGroupRefType = (OperationParameterGroupRefType)theEObject;
                T result = caseOperationParameterGroupRefType(operationParameterGroupRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_PARAMETER_GROUP_TYPE: {
                OperationParameterGroupType operationParameterGroupType = (OperationParameterGroupType)theEObject;
                T result = caseOperationParameterGroupType(operationParameterGroupType);
                if (result == null) result = caseOperationParameterGroupBaseType(operationParameterGroupType);
                if (result == null) result = caseAbstractGeneralOperationParameterType(operationParameterGroupType);
                if (result == null) result = caseDefinitionType(operationParameterGroupType);
                if (result == null) result = caseAbstractGMLType(operationParameterGroupType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_PARAMETER_REF_TYPE: {
                OperationParameterRefType operationParameterRefType = (OperationParameterRefType)theEObject;
                T result = caseOperationParameterRefType(operationParameterRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_PARAMETER_TYPE: {
                OperationParameterType operationParameterType = (OperationParameterType)theEObject;
                T result = caseOperationParameterType(operationParameterType);
                if (result == null) result = caseOperationParameterBaseType(operationParameterType);
                if (result == null) result = caseAbstractGeneralOperationParameterType(operationParameterType);
                if (result == null) result = caseDefinitionType(operationParameterType);
                if (result == null) result = caseAbstractGMLType(operationParameterType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.OPERATION_REF_TYPE: {
                OperationRefType operationRefType = (OperationRefType)theEObject;
                T result = caseOperationRefType(operationRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ORIENTABLE_CURVE_TYPE: {
                OrientableCurveType orientableCurveType = (OrientableCurveType)theEObject;
                T result = caseOrientableCurveType(orientableCurveType);
                if (result == null) result = caseAbstractCurveType(orientableCurveType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(orientableCurveType);
                if (result == null) result = caseAbstractGeometryType(orientableCurveType);
                if (result == null) result = caseAbstractGMLType(orientableCurveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ORIENTABLE_SURFACE_TYPE: {
                OrientableSurfaceType orientableSurfaceType = (OrientableSurfaceType)theEObject;
                T result = caseOrientableSurfaceType(orientableSurfaceType);
                if (result == null) result = caseAbstractSurfaceType(orientableSurfaceType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(orientableSurfaceType);
                if (result == null) result = caseAbstractGeometryType(orientableSurfaceType);
                if (result == null) result = caseAbstractGMLType(orientableSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE: {
                ParameterValueGroupType parameterValueGroupType = (ParameterValueGroupType)theEObject;
                T result = caseParameterValueGroupType(parameterValueGroupType);
                if (result == null) result = caseAbstractGeneralParameterValueType(parameterValueGroupType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PARAMETER_VALUE_TYPE: {
                ParameterValueType parameterValueType = (ParameterValueType)theEObject;
                T result = caseParameterValueType(parameterValueType);
                if (result == null) result = caseAbstractGeneralParameterValueType(parameterValueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PASS_THROUGH_OPERATION_REF_TYPE: {
                PassThroughOperationRefType passThroughOperationRefType = (PassThroughOperationRefType)theEObject;
                T result = casePassThroughOperationRefType(passThroughOperationRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PASS_THROUGH_OPERATION_TYPE: {
                PassThroughOperationType passThroughOperationType = (PassThroughOperationType)theEObject;
                T result = casePassThroughOperationType(passThroughOperationType);
                if (result == null) result = caseAbstractCoordinateOperationType(passThroughOperationType);
                if (result == null) result = caseAbstractCoordinateOperationBaseType(passThroughOperationType);
                if (result == null) result = caseDefinitionType(passThroughOperationType);
                if (result == null) result = caseAbstractGMLType(passThroughOperationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PIXEL_IN_CELL_TYPE: {
                PixelInCellType pixelInCellType = (PixelInCellType)theEObject;
                T result = casePixelInCellType(pixelInCellType);
                if (result == null) result = caseCodeType(pixelInCellType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE: {
                PointArrayPropertyType pointArrayPropertyType = (PointArrayPropertyType)theEObject;
                T result = casePointArrayPropertyType(pointArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POINT_PROPERTY_TYPE: {
                PointPropertyType pointPropertyType = (PointPropertyType)theEObject;
                T result = casePointPropertyType(pointPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POINT_TYPE: {
                PointType pointType = (PointType)theEObject;
                T result = casePointType(pointType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(pointType);
                if (result == null) result = caseAbstractGeometryType(pointType);
                if (result == null) result = caseAbstractGMLType(pointType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POLAR_CS_REF_TYPE: {
                PolarCSRefType polarCSRefType = (PolarCSRefType)theEObject;
                T result = casePolarCSRefType(polarCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POLAR_CS_TYPE: {
                PolarCSType polarCSType = (PolarCSType)theEObject;
                T result = casePolarCSType(polarCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(polarCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(polarCSType);
                if (result == null) result = caseDefinitionType(polarCSType);
                if (result == null) result = caseAbstractGMLType(polarCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE: {
                PolygonPatchArrayPropertyType polygonPatchArrayPropertyType = (PolygonPatchArrayPropertyType)theEObject;
                T result = casePolygonPatchArrayPropertyType(polygonPatchArrayPropertyType);
                if (result == null) result = caseSurfacePatchArrayPropertyType(polygonPatchArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POLYGON_PATCH_TYPE: {
                PolygonPatchType polygonPatchType = (PolygonPatchType)theEObject;
                T result = casePolygonPatchType(polygonPatchType);
                if (result == null) result = caseAbstractSurfacePatchType(polygonPatchType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POLYGON_PROPERTY_TYPE: {
                PolygonPropertyType polygonPropertyType = (PolygonPropertyType)theEObject;
                T result = casePolygonPropertyType(polygonPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POLYGON_TYPE: {
                PolygonType polygonType = (PolygonType)theEObject;
                T result = casePolygonType(polygonType);
                if (result == null) result = caseAbstractSurfaceType(polygonType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(polygonType);
                if (result == null) result = caseAbstractGeometryType(polygonType);
                if (result == null) result = caseAbstractGMLType(polygonType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.POLYHEDRAL_SURFACE_TYPE: {
                PolyhedralSurfaceType polyhedralSurfaceType = (PolyhedralSurfaceType)theEObject;
                T result = casePolyhedralSurfaceType(polyhedralSurfaceType);
                if (result == null) result = caseSurfaceType(polyhedralSurfaceType);
                if (result == null) result = caseAbstractSurfaceType(polyhedralSurfaceType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(polyhedralSurfaceType);
                if (result == null) result = caseAbstractGeometryType(polyhedralSurfaceType);
                if (result == null) result = caseAbstractGMLType(polyhedralSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PRIME_MERIDIAN_BASE_TYPE: {
                PrimeMeridianBaseType primeMeridianBaseType = (PrimeMeridianBaseType)theEObject;
                T result = casePrimeMeridianBaseType(primeMeridianBaseType);
                if (result == null) result = caseDefinitionType(primeMeridianBaseType);
                if (result == null) result = caseAbstractGMLType(primeMeridianBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PRIME_MERIDIAN_REF_TYPE: {
                PrimeMeridianRefType primeMeridianRefType = (PrimeMeridianRefType)theEObject;
                T result = casePrimeMeridianRefType(primeMeridianRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PRIME_MERIDIAN_TYPE: {
                PrimeMeridianType primeMeridianType = (PrimeMeridianType)theEObject;
                T result = casePrimeMeridianType(primeMeridianType);
                if (result == null) result = casePrimeMeridianBaseType(primeMeridianType);
                if (result == null) result = caseDefinitionType(primeMeridianType);
                if (result == null) result = caseAbstractGMLType(primeMeridianType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PRIORITY_LOCATION_PROPERTY_TYPE: {
                PriorityLocationPropertyType priorityLocationPropertyType = (PriorityLocationPropertyType)theEObject;
                T result = casePriorityLocationPropertyType(priorityLocationPropertyType);
                if (result == null) result = caseLocationPropertyType(priorityLocationPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PROJECTED_CRS_REF_TYPE: {
                ProjectedCRSRefType projectedCRSRefType = (ProjectedCRSRefType)theEObject;
                T result = caseProjectedCRSRefType(projectedCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.PROJECTED_CRS_TYPE: {
                ProjectedCRSType projectedCRSType = (ProjectedCRSType)theEObject;
                T result = caseProjectedCRSType(projectedCRSType);
                if (result == null) result = caseAbstractGeneralDerivedCRSType(projectedCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(projectedCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(projectedCRSType);
                if (result == null) result = caseDefinitionType(projectedCRSType);
                if (result == null) result = caseAbstractGMLType(projectedCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.QUANTITY_EXTENT_TYPE: {
                QuantityExtentType quantityExtentType = (QuantityExtentType)theEObject;
                T result = caseQuantityExtentType(quantityExtentType);
                if (result == null) result = caseMeasureOrNullListType(quantityExtentType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.QUANTITY_PROPERTY_TYPE: {
                QuantityPropertyType quantityPropertyType = (QuantityPropertyType)theEObject;
                T result = caseQuantityPropertyType(quantityPropertyType);
                if (result == null) result = caseValuePropertyType(quantityPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RANGE_PARAMETERS_TYPE: {
                RangeParametersType rangeParametersType = (RangeParametersType)theEObject;
                T result = caseRangeParametersType(rangeParametersType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RANGE_SET_TYPE: {
                RangeSetType rangeSetType = (RangeSetType)theEObject;
                T result = caseRangeSetType(rangeSetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RECTANGLE_TYPE: {
                RectangleType rectangleType = (RectangleType)theEObject;
                T result = caseRectangleType(rectangleType);
                if (result == null) result = caseAbstractSurfacePatchType(rectangleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RECTIFIED_GRID_COVERAGE_TYPE: {
                RectifiedGridCoverageType rectifiedGridCoverageType = (RectifiedGridCoverageType)theEObject;
                T result = caseRectifiedGridCoverageType(rectifiedGridCoverageType);
                if (result == null) result = caseAbstractDiscreteCoverageType(rectifiedGridCoverageType);
                if (result == null) result = caseAbstractCoverageType(rectifiedGridCoverageType);
                if (result == null) result = caseAbstractFeatureType(rectifiedGridCoverageType);
                if (result == null) result = caseAbstractGMLType(rectifiedGridCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RECTIFIED_GRID_DOMAIN_TYPE: {
                RectifiedGridDomainType rectifiedGridDomainType = (RectifiedGridDomainType)theEObject;
                T result = caseRectifiedGridDomainType(rectifiedGridDomainType);
                if (result == null) result = caseDomainSetType(rectifiedGridDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RECTIFIED_GRID_TYPE: {
                RectifiedGridType rectifiedGridType = (RectifiedGridType)theEObject;
                T result = caseRectifiedGridType(rectifiedGridType);
                if (result == null) result = caseGridType(rectifiedGridType);
                if (result == null) result = caseAbstractGeometryType(rectifiedGridType);
                if (result == null) result = caseAbstractGMLType(rectifiedGridType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.REFERENCE_SYSTEM_REF_TYPE: {
                ReferenceSystemRefType referenceSystemRefType = (ReferenceSystemRefType)theEObject;
                T result = caseReferenceSystemRefType(referenceSystemRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.REFERENCE_TYPE: {
                ReferenceType referenceType = (ReferenceType)theEObject;
                T result = caseReferenceType(referenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.REF_LOCATION_TYPE: {
                RefLocationType refLocationType = (RefLocationType)theEObject;
                T result = caseRefLocationType(refLocationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RELATED_TIME_TYPE: {
                RelatedTimeType relatedTimeType = (RelatedTimeType)theEObject;
                T result = caseRelatedTimeType(relatedTimeType);
                if (result == null) result = caseTimePrimitivePropertyType(relatedTimeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RELATIVE_INTERNAL_POSITIONAL_ACCURACY_TYPE: {
                RelativeInternalPositionalAccuracyType relativeInternalPositionalAccuracyType = (RelativeInternalPositionalAccuracyType)theEObject;
                T result = caseRelativeInternalPositionalAccuracyType(relativeInternalPositionalAccuracyType);
                if (result == null) result = caseAbstractPositionalAccuracyType(relativeInternalPositionalAccuracyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RING_PROPERTY_TYPE: {
                RingPropertyType ringPropertyType = (RingPropertyType)theEObject;
                T result = caseRingPropertyType(ringPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.RING_TYPE: {
                RingType ringType = (RingType)theEObject;
                T result = caseRingType(ringType);
                if (result == null) result = caseAbstractRingType(ringType);
                if (result == null) result = caseAbstractGeometryType(ringType);
                if (result == null) result = caseAbstractGMLType(ringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.ROW_TYPE: {
                RowType rowType = (RowType)theEObject;
                T result = caseRowType(rowType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SCALAR_VALUE_PROPERTY_TYPE: {
                ScalarValuePropertyType scalarValuePropertyType = (ScalarValuePropertyType)theEObject;
                T result = caseScalarValuePropertyType(scalarValuePropertyType);
                if (result == null) result = caseValuePropertyType(scalarValuePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SCALE_TYPE: {
                ScaleType scaleType = (ScaleType)theEObject;
                T result = caseScaleType(scaleType);
                if (result == null) result = caseMeasureType(scaleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE: {
                SecondDefiningParameterType secondDefiningParameterType = (SecondDefiningParameterType)theEObject;
                T result = caseSecondDefiningParameterType(secondDefiningParameterType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SEQUENCE_RULE_TYPE: {
                SequenceRuleType sequenceRuleType = (SequenceRuleType)theEObject;
                T result = caseSequenceRuleType(sequenceRuleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SINGLE_OPERATION_REF_TYPE: {
                SingleOperationRefType singleOperationRefType = (SingleOperationRefType)theEObject;
                T result = caseSingleOperationRefType(singleOperationRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE: {
                SolidArrayPropertyType solidArrayPropertyType = (SolidArrayPropertyType)theEObject;
                T result = caseSolidArrayPropertyType(solidArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SOLID_PROPERTY_TYPE: {
                SolidPropertyType solidPropertyType = (SolidPropertyType)theEObject;
                T result = caseSolidPropertyType(solidPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SOLID_TYPE: {
                SolidType solidType = (SolidType)theEObject;
                T result = caseSolidType(solidType);
                if (result == null) result = caseAbstractSolidType(solidType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(solidType);
                if (result == null) result = caseAbstractGeometryType(solidType);
                if (result == null) result = caseAbstractGMLType(solidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SPEED_TYPE: {
                SpeedType speedType = (SpeedType)theEObject;
                T result = caseSpeedType(speedType);
                if (result == null) result = caseMeasureType(speedType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SPHERE_TYPE: {
                SphereType sphereType = (SphereType)theEObject;
                T result = caseSphereType(sphereType);
                if (result == null) result = caseAbstractGriddedSurfaceType(sphereType);
                if (result == null) result = caseAbstractParametricCurveSurfaceType(sphereType);
                if (result == null) result = caseAbstractSurfacePatchType(sphereType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SPHERICAL_CS_REF_TYPE: {
                SphericalCSRefType sphericalCSRefType = (SphericalCSRefType)theEObject;
                T result = caseSphericalCSRefType(sphericalCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SPHERICAL_CS_TYPE: {
                SphericalCSType sphericalCSType = (SphericalCSType)theEObject;
                T result = caseSphericalCSType(sphericalCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(sphericalCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(sphericalCSType);
                if (result == null) result = caseDefinitionType(sphericalCSType);
                if (result == null) result = caseAbstractGMLType(sphericalCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.STRING_OR_REF_TYPE: {
                StringOrRefType stringOrRefType = (StringOrRefType)theEObject;
                T result = caseStringOrRefType(stringOrRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.STYLE_TYPE: {
                StyleType styleType = (StyleType)theEObject;
                T result = caseStyleType(styleType);
                if (result == null) result = caseAbstractStyleType(styleType);
                if (result == null) result = caseAbstractGMLType(styleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.STYLE_VARIATION_TYPE: {
                StyleVariationType styleVariationType = (StyleVariationType)theEObject;
                T result = caseStyleVariationType(styleVariationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SURFACE_ARRAY_PROPERTY_TYPE: {
                SurfaceArrayPropertyType surfaceArrayPropertyType = (SurfaceArrayPropertyType)theEObject;
                T result = caseSurfaceArrayPropertyType(surfaceArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE: {
                SurfacePatchArrayPropertyType surfacePatchArrayPropertyType = (SurfacePatchArrayPropertyType)theEObject;
                T result = caseSurfacePatchArrayPropertyType(surfacePatchArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SURFACE_PROPERTY_TYPE: {
                SurfacePropertyType surfacePropertyType = (SurfacePropertyType)theEObject;
                T result = caseSurfacePropertyType(surfacePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SURFACE_TYPE: {
                SurfaceType surfaceType = (SurfaceType)theEObject;
                T result = caseSurfaceType(surfaceType);
                if (result == null) result = caseAbstractSurfaceType(surfaceType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(surfaceType);
                if (result == null) result = caseAbstractGeometryType(surfaceType);
                if (result == null) result = caseAbstractGMLType(surfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.SYMBOL_TYPE: {
                SymbolType symbolType = (SymbolType)theEObject;
                T result = caseSymbolType(symbolType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TARGET_PROPERTY_TYPE: {
                TargetPropertyType targetPropertyType = (TargetPropertyType)theEObject;
                T result = caseTargetPropertyType(targetPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TEMPORAL_CRS_REF_TYPE: {
                TemporalCRSRefType temporalCRSRefType = (TemporalCRSRefType)theEObject;
                T result = caseTemporalCRSRefType(temporalCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TEMPORAL_CRS_TYPE: {
                TemporalCRSType temporalCRSType = (TemporalCRSType)theEObject;
                T result = caseTemporalCRSType(temporalCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(temporalCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(temporalCRSType);
                if (result == null) result = caseDefinitionType(temporalCRSType);
                if (result == null) result = caseAbstractGMLType(temporalCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TEMPORAL_CS_REF_TYPE: {
                TemporalCSRefType temporalCSRefType = (TemporalCSRefType)theEObject;
                T result = caseTemporalCSRefType(temporalCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TEMPORAL_CS_TYPE: {
                TemporalCSType temporalCSType = (TemporalCSType)theEObject;
                T result = caseTemporalCSType(temporalCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(temporalCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(temporalCSType);
                if (result == null) result = caseDefinitionType(temporalCSType);
                if (result == null) result = caseAbstractGMLType(temporalCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TEMPORAL_DATUM_BASE_TYPE: {
                TemporalDatumBaseType temporalDatumBaseType = (TemporalDatumBaseType)theEObject;
                T result = caseTemporalDatumBaseType(temporalDatumBaseType);
                if (result == null) result = caseAbstractDatumType(temporalDatumBaseType);
                if (result == null) result = caseAbstractDatumBaseType(temporalDatumBaseType);
                if (result == null) result = caseDefinitionType(temporalDatumBaseType);
                if (result == null) result = caseAbstractGMLType(temporalDatumBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TEMPORAL_DATUM_REF_TYPE: {
                TemporalDatumRefType temporalDatumRefType = (TemporalDatumRefType)theEObject;
                T result = caseTemporalDatumRefType(temporalDatumRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TEMPORAL_DATUM_TYPE: {
                TemporalDatumType temporalDatumType = (TemporalDatumType)theEObject;
                T result = caseTemporalDatumType(temporalDatumType);
                if (result == null) result = caseTemporalDatumBaseType(temporalDatumType);
                if (result == null) result = caseAbstractDatumType(temporalDatumType);
                if (result == null) result = caseAbstractDatumBaseType(temporalDatumType);
                if (result == null) result = caseDefinitionType(temporalDatumType);
                if (result == null) result = caseAbstractGMLType(temporalDatumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_CALENDAR_ERA_PROPERTY_TYPE: {
                TimeCalendarEraPropertyType timeCalendarEraPropertyType = (TimeCalendarEraPropertyType)theEObject;
                T result = caseTimeCalendarEraPropertyType(timeCalendarEraPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_CALENDAR_ERA_TYPE: {
                TimeCalendarEraType timeCalendarEraType = (TimeCalendarEraType)theEObject;
                T result = caseTimeCalendarEraType(timeCalendarEraType);
                if (result == null) result = caseDefinitionType(timeCalendarEraType);
                if (result == null) result = caseAbstractGMLType(timeCalendarEraType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_CALENDAR_PROPERTY_TYPE: {
                TimeCalendarPropertyType timeCalendarPropertyType = (TimeCalendarPropertyType)theEObject;
                T result = caseTimeCalendarPropertyType(timeCalendarPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_CALENDAR_TYPE: {
                TimeCalendarType timeCalendarType = (TimeCalendarType)theEObject;
                T result = caseTimeCalendarType(timeCalendarType);
                if (result == null) result = caseAbstractTimeReferenceSystemType(timeCalendarType);
                if (result == null) result = caseDefinitionType(timeCalendarType);
                if (result == null) result = caseAbstractGMLType(timeCalendarType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_CLOCK_PROPERTY_TYPE: {
                TimeClockPropertyType timeClockPropertyType = (TimeClockPropertyType)theEObject;
                T result = caseTimeClockPropertyType(timeClockPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_CLOCK_TYPE: {
                TimeClockType timeClockType = (TimeClockType)theEObject;
                T result = caseTimeClockType(timeClockType);
                if (result == null) result = caseAbstractTimeReferenceSystemType(timeClockType);
                if (result == null) result = caseDefinitionType(timeClockType);
                if (result == null) result = caseAbstractGMLType(timeClockType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE: {
                TimeCoordinateSystemType timeCoordinateSystemType = (TimeCoordinateSystemType)theEObject;
                T result = caseTimeCoordinateSystemType(timeCoordinateSystemType);
                if (result == null) result = caseAbstractTimeReferenceSystemType(timeCoordinateSystemType);
                if (result == null) result = caseDefinitionType(timeCoordinateSystemType);
                if (result == null) result = caseAbstractGMLType(timeCoordinateSystemType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_EDGE_PROPERTY_TYPE: {
                TimeEdgePropertyType timeEdgePropertyType = (TimeEdgePropertyType)theEObject;
                T result = caseTimeEdgePropertyType(timeEdgePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_EDGE_TYPE: {
                TimeEdgeType timeEdgeType = (TimeEdgeType)theEObject;
                T result = caseTimeEdgeType(timeEdgeType);
                if (result == null) result = caseAbstractTimeTopologyPrimitiveType(timeEdgeType);
                if (result == null) result = caseAbstractTimePrimitiveType(timeEdgeType);
                if (result == null) result = caseAbstractTimeObjectType(timeEdgeType);
                if (result == null) result = caseAbstractGMLType(timeEdgeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_GEOMETRIC_PRIMITIVE_PROPERTY_TYPE: {
                TimeGeometricPrimitivePropertyType timeGeometricPrimitivePropertyType = (TimeGeometricPrimitivePropertyType)theEObject;
                T result = caseTimeGeometricPrimitivePropertyType(timeGeometricPrimitivePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_INSTANT_PROPERTY_TYPE: {
                TimeInstantPropertyType timeInstantPropertyType = (TimeInstantPropertyType)theEObject;
                T result = caseTimeInstantPropertyType(timeInstantPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_INSTANT_TYPE: {
                TimeInstantType timeInstantType = (TimeInstantType)theEObject;
                T result = caseTimeInstantType(timeInstantType);
                if (result == null) result = caseAbstractTimeGeometricPrimitiveType(timeInstantType);
                if (result == null) result = caseAbstractTimePrimitiveType(timeInstantType);
                if (result == null) result = caseAbstractTimeObjectType(timeInstantType);
                if (result == null) result = caseAbstractGMLType(timeInstantType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_INTERVAL_LENGTH_TYPE: {
                TimeIntervalLengthType timeIntervalLengthType = (TimeIntervalLengthType)theEObject;
                T result = caseTimeIntervalLengthType(timeIntervalLengthType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_NODE_PROPERTY_TYPE: {
                TimeNodePropertyType timeNodePropertyType = (TimeNodePropertyType)theEObject;
                T result = caseTimeNodePropertyType(timeNodePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_NODE_TYPE: {
                TimeNodeType timeNodeType = (TimeNodeType)theEObject;
                T result = caseTimeNodeType(timeNodeType);
                if (result == null) result = caseAbstractTimeTopologyPrimitiveType(timeNodeType);
                if (result == null) result = caseAbstractTimePrimitiveType(timeNodeType);
                if (result == null) result = caseAbstractTimeObjectType(timeNodeType);
                if (result == null) result = caseAbstractGMLType(timeNodeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_ORDINAL_ERA_PROPERTY_TYPE: {
                TimeOrdinalEraPropertyType timeOrdinalEraPropertyType = (TimeOrdinalEraPropertyType)theEObject;
                T result = caseTimeOrdinalEraPropertyType(timeOrdinalEraPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_ORDINAL_ERA_TYPE: {
                TimeOrdinalEraType timeOrdinalEraType = (TimeOrdinalEraType)theEObject;
                T result = caseTimeOrdinalEraType(timeOrdinalEraType);
                if (result == null) result = caseDefinitionType(timeOrdinalEraType);
                if (result == null) result = caseAbstractGMLType(timeOrdinalEraType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE: {
                TimeOrdinalReferenceSystemType timeOrdinalReferenceSystemType = (TimeOrdinalReferenceSystemType)theEObject;
                T result = caseTimeOrdinalReferenceSystemType(timeOrdinalReferenceSystemType);
                if (result == null) result = caseAbstractTimeReferenceSystemType(timeOrdinalReferenceSystemType);
                if (result == null) result = caseDefinitionType(timeOrdinalReferenceSystemType);
                if (result == null) result = caseAbstractGMLType(timeOrdinalReferenceSystemType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_PERIOD_PROPERTY_TYPE: {
                TimePeriodPropertyType timePeriodPropertyType = (TimePeriodPropertyType)theEObject;
                T result = caseTimePeriodPropertyType(timePeriodPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_PERIOD_TYPE: {
                TimePeriodType timePeriodType = (TimePeriodType)theEObject;
                T result = caseTimePeriodType(timePeriodType);
                if (result == null) result = caseAbstractTimeGeometricPrimitiveType(timePeriodType);
                if (result == null) result = caseAbstractTimePrimitiveType(timePeriodType);
                if (result == null) result = caseAbstractTimeObjectType(timePeriodType);
                if (result == null) result = caseAbstractGMLType(timePeriodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_POSITION_TYPE: {
                TimePositionType timePositionType = (TimePositionType)theEObject;
                T result = caseTimePositionType(timePositionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_PRIMITIVE_PROPERTY_TYPE: {
                TimePrimitivePropertyType timePrimitivePropertyType = (TimePrimitivePropertyType)theEObject;
                T result = caseTimePrimitivePropertyType(timePrimitivePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_PROPERTY_TYPE: {
                TimeTopologyComplexPropertyType timeTopologyComplexPropertyType = (TimeTopologyComplexPropertyType)theEObject;
                T result = caseTimeTopologyComplexPropertyType(timeTopologyComplexPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE: {
                TimeTopologyComplexType timeTopologyComplexType = (TimeTopologyComplexType)theEObject;
                T result = caseTimeTopologyComplexType(timeTopologyComplexType);
                if (result == null) result = caseAbstractTimeComplexType(timeTopologyComplexType);
                if (result == null) result = caseAbstractTimeObjectType(timeTopologyComplexType);
                if (result == null) result = caseAbstractGMLType(timeTopologyComplexType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_TOPOLOGY_PRIMITIVE_PROPERTY_TYPE: {
                TimeTopologyPrimitivePropertyType timeTopologyPrimitivePropertyType = (TimeTopologyPrimitivePropertyType)theEObject;
                T result = caseTimeTopologyPrimitivePropertyType(timeTopologyPrimitivePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIME_TYPE: {
                TimeType timeType = (TimeType)theEObject;
                T result = caseTimeType(timeType);
                if (result == null) result = caseMeasureType(timeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TIN_TYPE: {
                TinType tinType = (TinType)theEObject;
                T result = caseTinType(tinType);
                if (result == null) result = caseTriangulatedSurfaceType(tinType);
                if (result == null) result = caseSurfaceType(tinType);
                if (result == null) result = caseAbstractSurfaceType(tinType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(tinType);
                if (result == null) result = caseAbstractGeometryType(tinType);
                if (result == null) result = caseAbstractGMLType(tinType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_COMPLEX_MEMBER_TYPE: {
                TopoComplexMemberType topoComplexMemberType = (TopoComplexMemberType)theEObject;
                T result = caseTopoComplexMemberType(topoComplexMemberType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_COMPLEX_TYPE: {
                TopoComplexType topoComplexType = (TopoComplexType)theEObject;
                T result = caseTopoComplexType(topoComplexType);
                if (result == null) result = caseAbstractTopologyType(topoComplexType);
                if (result == null) result = caseAbstractGMLType(topoComplexType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_CURVE_PROPERTY_TYPE: {
                TopoCurvePropertyType topoCurvePropertyType = (TopoCurvePropertyType)theEObject;
                T result = caseTopoCurvePropertyType(topoCurvePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_CURVE_TYPE: {
                TopoCurveType topoCurveType = (TopoCurveType)theEObject;
                T result = caseTopoCurveType(topoCurveType);
                if (result == null) result = caseAbstractTopologyType(topoCurveType);
                if (result == null) result = caseAbstractGMLType(topoCurveType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPOLOGY_STYLE_PROPERTY_TYPE: {
                TopologyStylePropertyType topologyStylePropertyType = (TopologyStylePropertyType)theEObject;
                T result = caseTopologyStylePropertyType(topologyStylePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPOLOGY_STYLE_TYPE: {
                TopologyStyleType topologyStyleType = (TopologyStyleType)theEObject;
                T result = caseTopologyStyleType(topologyStyleType);
                if (result == null) result = caseBaseStyleDescriptorType(topologyStyleType);
                if (result == null) result = caseAbstractGMLType(topologyStyleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_POINT_PROPERTY_TYPE: {
                TopoPointPropertyType topoPointPropertyType = (TopoPointPropertyType)theEObject;
                T result = caseTopoPointPropertyType(topoPointPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_POINT_TYPE: {
                TopoPointType topoPointType = (TopoPointType)theEObject;
                T result = caseTopoPointType(topoPointType);
                if (result == null) result = caseAbstractTopologyType(topoPointType);
                if (result == null) result = caseAbstractGMLType(topoPointType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE: {
                TopoPrimitiveArrayAssociationType topoPrimitiveArrayAssociationType = (TopoPrimitiveArrayAssociationType)theEObject;
                T result = caseTopoPrimitiveArrayAssociationType(topoPrimitiveArrayAssociationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_PRIMITIVE_MEMBER_TYPE: {
                TopoPrimitiveMemberType topoPrimitiveMemberType = (TopoPrimitiveMemberType)theEObject;
                T result = caseTopoPrimitiveMemberType(topoPrimitiveMemberType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_SOLID_TYPE: {
                TopoSolidType topoSolidType = (TopoSolidType)theEObject;
                T result = caseTopoSolidType(topoSolidType);
                if (result == null) result = caseAbstractTopoPrimitiveType(topoSolidType);
                if (result == null) result = caseAbstractTopologyType(topoSolidType);
                if (result == null) result = caseAbstractGMLType(topoSolidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_SURFACE_PROPERTY_TYPE: {
                TopoSurfacePropertyType topoSurfacePropertyType = (TopoSurfacePropertyType)theEObject;
                T result = caseTopoSurfacePropertyType(topoSurfacePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_SURFACE_TYPE: {
                TopoSurfaceType topoSurfaceType = (TopoSurfaceType)theEObject;
                T result = caseTopoSurfaceType(topoSurfaceType);
                if (result == null) result = caseAbstractTopologyType(topoSurfaceType);
                if (result == null) result = caseAbstractGMLType(topoSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_VOLUME_PROPERTY_TYPE: {
                TopoVolumePropertyType topoVolumePropertyType = (TopoVolumePropertyType)theEObject;
                T result = caseTopoVolumePropertyType(topoVolumePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TOPO_VOLUME_TYPE: {
                TopoVolumeType topoVolumeType = (TopoVolumeType)theEObject;
                T result = caseTopoVolumeType(topoVolumeType);
                if (result == null) result = caseAbstractTopologyType(topoVolumeType);
                if (result == null) result = caseAbstractGMLType(topoVolumeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TRACK_TYPE: {
                TrackType trackType = (TrackType)theEObject;
                T result = caseTrackType(trackType);
                if (result == null) result = caseHistoryPropertyType(trackType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TRANSFORMATION_REF_TYPE: {
                TransformationRefType transformationRefType = (TransformationRefType)theEObject;
                T result = caseTransformationRefType(transformationRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TRANSFORMATION_TYPE: {
                TransformationType transformationType = (TransformationType)theEObject;
                T result = caseTransformationType(transformationType);
                if (result == null) result = caseAbstractGeneralTransformationType(transformationType);
                if (result == null) result = caseAbstractCoordinateOperationType(transformationType);
                if (result == null) result = caseAbstractCoordinateOperationBaseType(transformationType);
                if (result == null) result = caseDefinitionType(transformationType);
                if (result == null) result = caseAbstractGMLType(transformationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TRIANGLE_PATCH_ARRAY_PROPERTY_TYPE: {
                TrianglePatchArrayPropertyType trianglePatchArrayPropertyType = (TrianglePatchArrayPropertyType)theEObject;
                T result = caseTrianglePatchArrayPropertyType(trianglePatchArrayPropertyType);
                if (result == null) result = caseSurfacePatchArrayPropertyType(trianglePatchArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TRIANGLE_TYPE: {
                TriangleType triangleType = (TriangleType)theEObject;
                T result = caseTriangleType(triangleType);
                if (result == null) result = caseAbstractSurfacePatchType(triangleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.TRIANGULATED_SURFACE_TYPE: {
                TriangulatedSurfaceType triangulatedSurfaceType = (TriangulatedSurfaceType)theEObject;
                T result = caseTriangulatedSurfaceType(triangulatedSurfaceType);
                if (result == null) result = caseSurfaceType(triangulatedSurfaceType);
                if (result == null) result = caseAbstractSurfaceType(triangulatedSurfaceType);
                if (result == null) result = caseAbstractGeometricPrimitiveType(triangulatedSurfaceType);
                if (result == null) result = caseAbstractGeometryType(triangulatedSurfaceType);
                if (result == null) result = caseAbstractGMLType(triangulatedSurfaceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.UNIT_DEFINITION_TYPE: {
                UnitDefinitionType unitDefinitionType = (UnitDefinitionType)theEObject;
                T result = caseUnitDefinitionType(unitDefinitionType);
                if (result == null) result = caseDefinitionType(unitDefinitionType);
                if (result == null) result = caseAbstractGMLType(unitDefinitionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.UNIT_OF_MEASURE_TYPE: {
                UnitOfMeasureType unitOfMeasureType = (UnitOfMeasureType)theEObject;
                T result = caseUnitOfMeasureType(unitOfMeasureType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.USER_DEFINED_CS_REF_TYPE: {
                UserDefinedCSRefType userDefinedCSRefType = (UserDefinedCSRefType)theEObject;
                T result = caseUserDefinedCSRefType(userDefinedCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.USER_DEFINED_CS_TYPE: {
                UserDefinedCSType userDefinedCSType = (UserDefinedCSType)theEObject;
                T result = caseUserDefinedCSType(userDefinedCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(userDefinedCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(userDefinedCSType);
                if (result == null) result = caseDefinitionType(userDefinedCSType);
                if (result == null) result = caseAbstractGMLType(userDefinedCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE: {
                ValueArrayPropertyType valueArrayPropertyType = (ValueArrayPropertyType)theEObject;
                T result = caseValueArrayPropertyType(valueArrayPropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VALUE_ARRAY_TYPE: {
                ValueArrayType valueArrayType = (ValueArrayType)theEObject;
                T result = caseValueArrayType(valueArrayType);
                if (result == null) result = caseCompositeValueType(valueArrayType);
                if (result == null) result = caseAbstractGMLType(valueArrayType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VALUE_PROPERTY_TYPE: {
                ValuePropertyType valuePropertyType = (ValuePropertyType)theEObject;
                T result = caseValuePropertyType(valuePropertyType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VECTOR_TYPE: {
                VectorType vectorType = (VectorType)theEObject;
                T result = caseVectorType(vectorType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VERTICAL_CRS_REF_TYPE: {
                VerticalCRSRefType verticalCRSRefType = (VerticalCRSRefType)theEObject;
                T result = caseVerticalCRSRefType(verticalCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VERTICAL_CRS_TYPE: {
                VerticalCRSType verticalCRSType = (VerticalCRSType)theEObject;
                T result = caseVerticalCRSType(verticalCRSType);
                if (result == null) result = caseAbstractReferenceSystemType(verticalCRSType);
                if (result == null) result = caseAbstractReferenceSystemBaseType(verticalCRSType);
                if (result == null) result = caseDefinitionType(verticalCRSType);
                if (result == null) result = caseAbstractGMLType(verticalCRSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VERTICAL_CS_REF_TYPE: {
                VerticalCSRefType verticalCSRefType = (VerticalCSRefType)theEObject;
                T result = caseVerticalCSRefType(verticalCSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VERTICAL_CS_TYPE: {
                VerticalCSType verticalCSType = (VerticalCSType)theEObject;
                T result = caseVerticalCSType(verticalCSType);
                if (result == null) result = caseAbstractCoordinateSystemType(verticalCSType);
                if (result == null) result = caseAbstractCoordinateSystemBaseType(verticalCSType);
                if (result == null) result = caseDefinitionType(verticalCSType);
                if (result == null) result = caseAbstractGMLType(verticalCSType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VERTICAL_DATUM_REF_TYPE: {
                VerticalDatumRefType verticalDatumRefType = (VerticalDatumRefType)theEObject;
                T result = caseVerticalDatumRefType(verticalDatumRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VERTICAL_DATUM_TYPE: {
                VerticalDatumType verticalDatumType = (VerticalDatumType)theEObject;
                T result = caseVerticalDatumType(verticalDatumType);
                if (result == null) result = caseAbstractDatumType(verticalDatumType);
                if (result == null) result = caseAbstractDatumBaseType(verticalDatumType);
                if (result == null) result = caseDefinitionType(verticalDatumType);
                if (result == null) result = caseAbstractGMLType(verticalDatumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VERTICAL_DATUM_TYPE_TYPE: {
                VerticalDatumTypeType verticalDatumTypeType = (VerticalDatumTypeType)theEObject;
                T result = caseVerticalDatumTypeType(verticalDatumTypeType);
                if (result == null) result = caseCodeType(verticalDatumTypeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Gml311Package.VOLUME_TYPE: {
                VolumeType volumeType = (VolumeType)theEObject;
                T result = caseVolumeType(volumeType);
                if (result == null) result = caseMeasureType(volumeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Absolute External Positional Accuracy Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Absolute External Positional Accuracy Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbsoluteExternalPositionalAccuracyType(AbsoluteExternalPositionalAccuracyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Continuous Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Continuous Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractContinuousCoverageType(AbstractContinuousCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Coordinate Operation Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Coordinate Operation Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractCoordinateOperationBaseType(AbstractCoordinateOperationBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Coordinate Operation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Coordinate Operation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractCoordinateOperationType(AbstractCoordinateOperationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Coordinate System Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Coordinate System Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractCoordinateSystemBaseType(AbstractCoordinateSystemBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Coordinate System Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Coordinate System Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractCoordinateSystemType(AbstractCoordinateSystemType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractCoverageType(AbstractCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Curve Segment Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Curve Segment Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractCurveSegmentType(AbstractCurveSegmentType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Curve Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Curve Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractCurveType(AbstractCurveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Datum Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Datum Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractDatumBaseType(AbstractDatumBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Datum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Datum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractDatumType(AbstractDatumType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Discrete Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Discrete Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractDiscreteCoverageType(AbstractDiscreteCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Feature Collection Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Feature Collection Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractFeatureCollectionType(AbstractFeatureCollectionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Feature Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Feature Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractFeatureType(AbstractFeatureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract General Conversion Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract General Conversion Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeneralConversionType(AbstractGeneralConversionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract General Derived CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract General Derived CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeneralDerivedCRSType(AbstractGeneralDerivedCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract General Operation Parameter Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract General Operation Parameter Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeneralOperationParameterRefType(AbstractGeneralOperationParameterRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract General Operation Parameter Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract General Operation Parameter Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeneralOperationParameterType(AbstractGeneralOperationParameterType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract General Parameter Value Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract General Parameter Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeneralParameterValueType(AbstractGeneralParameterValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract General Transformation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract General Transformation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeneralTransformationType(AbstractGeneralTransformationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Geometric Aggregate Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Geometric Aggregate Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeometricAggregateType(AbstractGeometricAggregateType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Geometric Primitive Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Geometric Primitive Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeometricPrimitiveType(AbstractGeometricPrimitiveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Geometry Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Geometry Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGeometryType(AbstractGeometryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract GML Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract GML Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGMLType(AbstractGMLType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Gridded Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Gridded Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractGriddedSurfaceType(AbstractGriddedSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Meta Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Meta Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractMetaDataType(AbstractMetaDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Parametric Curve Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Parametric Curve Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractParametricCurveSurfaceType(AbstractParametricCurveSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Positional Accuracy Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Positional Accuracy Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractPositionalAccuracyType(AbstractPositionalAccuracyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Reference System Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Reference System Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractReferenceSystemBaseType(AbstractReferenceSystemBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Reference System Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Reference System Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractReferenceSystemType(AbstractReferenceSystemType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Ring Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Ring Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractRingPropertyType(AbstractRingPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Ring Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Ring Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractRingType(AbstractRingType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Solid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Solid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractSolidType(AbstractSolidType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractStyleType(AbstractStyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Surface Patch Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Surface Patch Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractSurfacePatchType(AbstractSurfacePatchType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractSurfaceType(AbstractSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Time Complex Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Time Complex Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTimeComplexType(AbstractTimeComplexType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Time Geometric Primitive Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Time Geometric Primitive Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTimeGeometricPrimitiveType(AbstractTimeGeometricPrimitiveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Time Object Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Time Object Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTimeObjectType(AbstractTimeObjectType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Time Primitive Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Time Primitive Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTimePrimitiveType(AbstractTimePrimitiveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Time Reference System Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Time Reference System Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTimeReferenceSystemType(AbstractTimeReferenceSystemType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Time Slice Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Time Slice Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTimeSliceType(AbstractTimeSliceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Time Topology Primitive Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Time Topology Primitive Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTimeTopologyPrimitiveType(AbstractTimeTopologyPrimitiveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Topology Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Topology Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTopologyType(AbstractTopologyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Topo Primitive Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Topo Primitive Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractTopoPrimitiveType(AbstractTopoPrimitiveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Affine Placement Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Affine Placement Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAffinePlacementType(AffinePlacementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Angle Choice Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Angle Choice Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAngleChoiceType(AngleChoiceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Angle Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Angle Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAngleType(AngleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arc By Bulge Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arc By Bulge Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArcByBulgeType(ArcByBulgeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arc By Center Point Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arc By Center Point Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArcByCenterPointType(ArcByCenterPointType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arc String By Bulge Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arc String By Bulge Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArcStringByBulgeType(ArcStringByBulgeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arc String Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arc String Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArcStringType(ArcStringType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arc Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arc Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArcType(ArcType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Area Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Area Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAreaType(AreaType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Array Association Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Array Association Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArrayAssociationType(ArrayAssociationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Array Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Array Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArrayType(ArrayType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Association Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Association Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssociationType(AssociationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bag Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bag Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBagType(BagType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Base Style Descriptor Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Base Style Descriptor Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBaseStyleDescriptorType(BaseStyleDescriptorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Base Unit Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Base Unit Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBaseUnitType(BaseUnitType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bezier Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bezier Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBezierType(BezierType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Boolean Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Boolean Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBooleanPropertyType(BooleanPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bounded Feature Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bounded Feature Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBoundedFeatureType(BoundedFeatureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bounding Shape Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bounding Shape Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBoundingShapeType(BoundingShapeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>BSpline Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>BSpline Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBSplineType(BSplineType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cartesian CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cartesian CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCartesianCSRefType(CartesianCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cartesian CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cartesian CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCartesianCSType(CartesianCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Category Extent Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Category Extent Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCategoryExtentType(CategoryExtentType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Category Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Category Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCategoryPropertyType(CategoryPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Circle By Center Point Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Circle By Center Point Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCircleByCenterPointType(CircleByCenterPointType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Circle Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Circle Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCircleType(CircleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Clothoid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Clothoid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClothoidType(ClothoidType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Code List Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Code List Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCodeListType(CodeListType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Code Or Null List Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Code Or Null List Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCodeOrNullListType(CodeOrNullListType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Code Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Code Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCodeType(CodeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Curve Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Curve Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeCurvePropertyType(CompositeCurvePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Curve Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Curve Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeCurveType(CompositeCurveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Solid Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Solid Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeSolidPropertyType(CompositeSolidPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Solid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Solid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeSolidType(CompositeSolidType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Surface Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Surface Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeSurfacePropertyType(CompositeSurfacePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeSurfaceType(CompositeSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Composite Value Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Composite Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompositeValueType(CompositeValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Compound CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Compound CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompoundCRSRefType(CompoundCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Compound CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Compound CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompoundCRSType(CompoundCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concatenated Operation Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concatenated Operation Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConcatenatedOperationRefType(ConcatenatedOperationRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concatenated Operation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concatenated Operation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConcatenatedOperationType(ConcatenatedOperationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cone Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cone Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConeType(ConeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Container Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Container Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContainerPropertyType(ContainerPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Control Point Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Control Point Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseControlPointType(ControlPointType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conventional Unit Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conventional Unit Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConventionalUnitType(ConventionalUnitType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conversion Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conversion Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConversionRefType(ConversionRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conversion To Preferred Unit Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conversion To Preferred Unit Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConversionToPreferredUnitType(ConversionToPreferredUnitType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conversion Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conversion Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConversionType(ConversionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coordinate Operation Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coordinate Operation Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordinateOperationRefType(CoordinateOperationRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coordinate Reference System Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coordinate Reference System Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordinateReferenceSystemRefType(CoordinateReferenceSystemRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coordinates Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coordinates Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordinatesType(CoordinatesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coordinate System Axis Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coordinate System Axis Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordinateSystemAxisBaseType(CoordinateSystemAxisBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coordinate System Axis Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coordinate System Axis Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordinateSystemAxisRefType(CoordinateSystemAxisRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coordinate System Axis Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coordinate System Axis Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordinateSystemAxisType(CoordinateSystemAxisType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coordinate System Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coordinate System Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordinateSystemRefType(CoordinateSystemRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coord Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coord Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoordType(CoordType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Count Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Count Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCountPropertyType(CountPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Covariance Element Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Covariance Element Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCovarianceElementType(CovarianceElementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Covariance Matrix Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Covariance Matrix Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCovarianceMatrixType(CovarianceMatrixType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Function Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Function Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCoverageFunctionType(CoverageFunctionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCRSRefType(CRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cubic Spline Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cubic Spline Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCubicSplineType(CubicSplineType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Curve Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Curve Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCurveArrayPropertyType(CurveArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Curve Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Curve Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCurvePropertyType(CurvePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Curve Segment Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Curve Segment Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCurveSegmentArrayPropertyType(CurveSegmentArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Curve Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Curve Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCurveType(CurveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cylinder Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cylinder Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCylinderType(CylinderType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cylindrical CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cylindrical CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCylindricalCSRefType(CylindricalCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cylindrical CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cylindrical CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCylindricalCSType(CylindricalCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Block Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Block Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataBlockType(DataBlockType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Datum Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Datum Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDatumRefType(DatumRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Default Style Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Default Style Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefaultStylePropertyType(DefaultStylePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Definition Proxy Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Definition Proxy Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefinitionProxyType(DefinitionProxyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Definition Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Definition Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefinitionType(DefinitionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Degrees Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Degrees Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDegreesType(DegreesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Derivation Unit Term Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Derivation Unit Term Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDerivationUnitTermType(DerivationUnitTermType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Derived CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Derived CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDerivedCRSRefType(DerivedCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Derived CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Derived CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDerivedCRSType(DerivedCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Derived CRS Type Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Derived CRS Type Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDerivedCRSTypeType(DerivedCRSTypeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Derived Unit Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Derived Unit Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDerivedUnitType(DerivedUnitType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dictionary Entry Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dictionary Entry Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictionaryEntryType(DictionaryEntryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dictionary Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dictionary Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictionaryType(DictionaryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Directed Edge Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Directed Edge Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectedEdgePropertyType(DirectedEdgePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Directed Face Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Directed Face Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectedFacePropertyType(DirectedFacePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Directed Node Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Directed Node Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectedNodePropertyType(DirectedNodePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Directed Observation At Distance Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Directed Observation At Distance Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectedObservationAtDistanceType(DirectedObservationAtDistanceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Directed Observation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Directed Observation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectedObservationType(DirectedObservationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Directed Topo Solid Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Directed Topo Solid Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectedTopoSolidPropertyType(DirectedTopoSolidPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Direction Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Direction Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectionPropertyType(DirectionPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Direction Vector Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Direction Vector Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectionVectorType(DirectionVectorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Direct Position List Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Direct Position List Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectPositionListType(DirectPositionListType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Direct Position Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Direct Position Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDirectPositionType(DirectPositionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>DMS Angle Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>DMS Angle Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDMSAngleType(DMSAngleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Domain Set Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Set Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDomainSetType(DomainSetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dynamic Feature Collection Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dynamic Feature Collection Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDynamicFeatureCollectionType(DynamicFeatureCollectionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dynamic Feature Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dynamic Feature Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDynamicFeatureType(DynamicFeatureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Edge Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Edge Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEdgeType(EdgeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ellipsoidal CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ellipsoidal CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEllipsoidalCSRefType(EllipsoidalCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ellipsoidal CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ellipsoidal CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEllipsoidalCSType(EllipsoidalCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ellipsoid Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ellipsoid Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEllipsoidBaseType(EllipsoidBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ellipsoid Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ellipsoid Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEllipsoidRefType(EllipsoidRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ellipsoid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ellipsoid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEllipsoidType(EllipsoidType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Engineering CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Engineering CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEngineeringCRSRefType(EngineeringCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Engineering CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Engineering CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEngineeringCRSType(EngineeringCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Engineering Datum Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Engineering Datum Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEngineeringDatumRefType(EngineeringDatumRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Engineering Datum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Engineering Datum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEngineeringDatumType(EngineeringDatumType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Envelope Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Envelope Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnvelopeType(EnvelopeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Envelope With Time Period Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Envelope With Time Period Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnvelopeWithTimePeriodType(EnvelopeWithTimePeriodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Extent Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Extent Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExtentType(ExtentType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Face Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Face Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFaceType(FaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureArrayPropertyType(FeatureArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Collection Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Collection Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureCollectionType(FeatureCollectionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeaturePropertyType(FeaturePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Style Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Style Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureStylePropertyType(FeatureStylePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureStyleType(FeatureStyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>File Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>File Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFileType(FileType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Formula Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Formula Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFormulaType(FormulaType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>General Conversion Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>General Conversion Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeneralConversionRefType(GeneralConversionRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>General Transformation Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>General Transformation Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeneralTransformationRefType(GeneralTransformationRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Generic Meta Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Generic Meta Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGenericMetaDataType(GenericMetaDataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geocentric CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geocentric CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeocentricCRSRefType(GeocentricCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geocentric CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geocentric CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeocentricCRSType(GeocentricCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geodesic String Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geodesic String Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeodesicStringType(GeodesicStringType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geodesic Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geodesic Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeodesicType(GeodesicType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geodetic Datum Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geodetic Datum Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeodeticDatumRefType(GeodeticDatumRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geodetic Datum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geodetic Datum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeodeticDatumType(GeodeticDatumType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geographic CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geographic CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeographicCRSRefType(GeographicCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geographic CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geographic CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeographicCRSType(GeographicCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geometric Complex Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geometric Complex Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeometricComplexPropertyType(GeometricComplexPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geometric Complex Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geometric Complex Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeometricComplexType(GeometricComplexType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geometric Primitive Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geometric Primitive Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeometricPrimitivePropertyType(GeometricPrimitivePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geometry Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geometry Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeometryArrayPropertyType(GeometryArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geometry Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geometry Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeometryPropertyType(GeometryPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geometry Style Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geometry Style Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeometryStylePropertyType(GeometryStylePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geometry Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geometry Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeometryStyleType(GeometryStyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Graph Style Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Graph Style Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGraphStylePropertyType(GraphStylePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Graph Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Graph Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGraphStyleType(GraphStyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGridCoverageType(GridCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGridDomainType(GridDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Envelope Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Envelope Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGridEnvelopeType(GridEnvelopeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Function Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Function Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGridFunctionType(GridFunctionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Length Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Length Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGridLengthType(GridLengthType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Limits Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Limits Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGridLimitsType(GridLimitsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGridType(GridType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>History Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>History Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseHistoryPropertyType(HistoryPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Identifier Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Identifier Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIdentifierType(IdentifierType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Image CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Image CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImageCRSRefType(ImageCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Image CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Image CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImageCRSType(ImageCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Image Datum Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Image Datum Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImageDatumRefType(ImageDatumRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Image Datum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Image Datum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImageDatumType(ImageDatumType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Index Map Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Index Map Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIndexMapType(IndexMapType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Indirect Entry Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Indirect Entry Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIndirectEntryType(IndirectEntryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Isolated Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Isolated Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIsolatedPropertyType(IsolatedPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Knot Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Knot Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseKnotPropertyType(KnotPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Knot Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Knot Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseKnotType(KnotType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Label Style Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Label Style Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLabelStylePropertyType(LabelStylePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Label Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Label Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLabelStyleType(LabelStyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Label Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Label Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLabelType(LabelType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Length Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Length Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLengthType(LengthType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Linear CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Linear CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLinearCSRefType(LinearCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Linear CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Linear CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLinearCSType(LinearCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Linear Ring Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Linear Ring Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLinearRingPropertyType(LinearRingPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Linear Ring Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Linear Ring Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLinearRingType(LinearRingType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Line String Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Line String Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLineStringPropertyType(LineStringPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Line String Segment Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Line String Segment Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLineStringSegmentArrayPropertyType(LineStringSegmentArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Line String Segment Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Line String Segment Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLineStringSegmentType(LineStringSegmentType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Line String Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Line String Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLineStringType(LineStringType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Location Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Location Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLocationPropertyType(LocationPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Measure List Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Measure List Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMeasureListType(MeasureListType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Measure Or Null List Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Measure Or Null List Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMeasureOrNullListType(MeasureOrNullListType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Measure Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Measure Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMeasureType(MeasureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Meta Data Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Meta Data Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetaDataPropertyType(MetaDataPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Moving Object Status Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Moving Object Status Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMovingObjectStatusType(MovingObjectStatusType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Curve Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Curve Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiCurveCoverageType(MultiCurveCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Curve Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Curve Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiCurveDomainType(MultiCurveDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Curve Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Curve Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiCurvePropertyType(MultiCurvePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Curve Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Curve Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiCurveType(MultiCurveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Geometry Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Geometry Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiGeometryPropertyType(MultiGeometryPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Geometry Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Geometry Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiGeometryType(MultiGeometryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Line String Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Line String Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiLineStringPropertyType(MultiLineStringPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Line String Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Line String Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiLineStringType(MultiLineStringType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Point Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Point Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiPointCoverageType(MultiPointCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Point Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Point Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiPointDomainType(MultiPointDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Point Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Point Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiPointPropertyType(MultiPointPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Point Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Point Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiPointType(MultiPointType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Polygon Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Polygon Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiPolygonPropertyType(MultiPolygonPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Polygon Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Polygon Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiPolygonType(MultiPolygonType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Solid Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Solid Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSolidCoverageType(MultiSolidCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Solid Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Solid Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSolidDomainType(MultiSolidDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Solid Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Solid Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSolidPropertyType(MultiSolidPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Solid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Solid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSolidType(MultiSolidType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Surface Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Surface Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSurfaceCoverageType(MultiSurfaceCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Surface Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Surface Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSurfaceDomainType(MultiSurfaceDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Surface Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Surface Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSurfacePropertyType(MultiSurfacePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multi Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multi Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiSurfaceType(MultiSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Node Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Node Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNodeType(NodeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Oblique Cartesian CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Oblique Cartesian CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObliqueCartesianCSRefType(ObliqueCartesianCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Oblique Cartesian CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Oblique Cartesian CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObliqueCartesianCSType(ObliqueCartesianCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Observation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Observation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObservationType(ObservationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Offset Curve Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Offset Curve Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOffsetCurveType(OffsetCurveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Method Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Method Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationMethodBaseType(OperationMethodBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Method Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Method Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationMethodRefType(OperationMethodRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Method Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Method Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationMethodType(OperationMethodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Parameter Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Parameter Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationParameterBaseType(OperationParameterBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Parameter Group Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Parameter Group Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationParameterGroupBaseType(OperationParameterGroupBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Parameter Group Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Parameter Group Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationParameterGroupRefType(OperationParameterGroupRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Parameter Group Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Parameter Group Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationParameterGroupType(OperationParameterGroupType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Parameter Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Parameter Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationParameterRefType(OperationParameterRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Parameter Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Parameter Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationParameterType(OperationParameterType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationRefType(OperationRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Orientable Curve Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Orientable Curve Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOrientableCurveType(OrientableCurveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Orientable Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Orientable Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOrientableSurfaceType(OrientableSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter Value Group Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter Value Group Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameterValueGroupType(ParameterValueGroupType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter Value Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameterValueType(ParameterValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pass Through Operation Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pass Through Operation Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePassThroughOperationRefType(PassThroughOperationRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pass Through Operation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pass Through Operation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePassThroughOperationType(PassThroughOperationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pixel In Cell Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pixel In Cell Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePixelInCellType(PixelInCellType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Point Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Point Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePointArrayPropertyType(PointArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Point Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Point Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePointPropertyType(PointPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Point Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Point Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePointType(PointType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Polar CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Polar CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePolarCSRefType(PolarCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Polar CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Polar CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePolarCSType(PolarCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Polygon Patch Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Polygon Patch Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePolygonPatchArrayPropertyType(PolygonPatchArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Polygon Patch Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Polygon Patch Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePolygonPatchType(PolygonPatchType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Polygon Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Polygon Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePolygonPropertyType(PolygonPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Polygon Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Polygon Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePolygonType(PolygonType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Polyhedral Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Polyhedral Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePolyhedralSurfaceType(PolyhedralSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Prime Meridian Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Prime Meridian Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePrimeMeridianBaseType(PrimeMeridianBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Prime Meridian Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Prime Meridian Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePrimeMeridianRefType(PrimeMeridianRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Prime Meridian Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Prime Meridian Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePrimeMeridianType(PrimeMeridianType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Priority Location Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Priority Location Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePriorityLocationPropertyType(PriorityLocationPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Projected CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Projected CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProjectedCRSRefType(ProjectedCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Projected CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Projected CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProjectedCRSType(ProjectedCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Quantity Extent Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Quantity Extent Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQuantityExtentType(QuantityExtentType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Quantity Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Quantity Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQuantityPropertyType(QuantityPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Parameters Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Parameters Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRangeParametersType(RangeParametersType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Set Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Set Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRangeSetType(RangeSetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rectangle Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rectangle Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRectangleType(RectangleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rectified Grid Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rectified Grid Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRectifiedGridCoverageType(RectifiedGridCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rectified Grid Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rectified Grid Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRectifiedGridDomainType(RectifiedGridDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rectified Grid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rectified Grid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRectifiedGridType(RectifiedGridType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference System Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference System Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReferenceSystemRefType(ReferenceSystemRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReferenceType(ReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ref Location Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ref Location Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRefLocationType(RefLocationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Related Time Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Related Time Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRelatedTimeType(RelatedTimeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Relative Internal Positional Accuracy Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Relative Internal Positional Accuracy Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRelativeInternalPositionalAccuracyType(RelativeInternalPositionalAccuracyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ring Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ring Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRingPropertyType(RingPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ring Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ring Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRingType(RingType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Row Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Row Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRowType(RowType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Scalar Value Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Scalar Value Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseScalarValuePropertyType(ScalarValuePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Scale Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Scale Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseScaleType(ScaleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Second Defining Parameter Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Second Defining Parameter Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSecondDefiningParameterType(SecondDefiningParameterType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sequence Rule Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sequence Rule Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSequenceRuleType(SequenceRuleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Single Operation Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Single Operation Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSingleOperationRefType(SingleOperationRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Solid Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Solid Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSolidArrayPropertyType(SolidArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Solid Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Solid Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSolidPropertyType(SolidPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Solid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Solid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSolidType(SolidType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Speed Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Speed Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpeedType(SpeedType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sphere Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sphere Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSphereType(SphereType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Spherical CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Spherical CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSphericalCSRefType(SphericalCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Spherical CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Spherical CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSphericalCSType(SphericalCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>String Or Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>String Or Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStringOrRefType(StringOrRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStyleType(StyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Style Variation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Style Variation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStyleVariationType(StyleVariationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Surface Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Surface Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSurfaceArrayPropertyType(SurfaceArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Surface Patch Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Surface Patch Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSurfacePatchArrayPropertyType(SurfacePatchArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Surface Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Surface Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSurfacePropertyType(SurfacePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSurfaceType(SurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Symbol Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Symbol Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSymbolType(SymbolType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Target Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Target Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTargetPropertyType(TargetPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Temporal CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Temporal CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTemporalCRSRefType(TemporalCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Temporal CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Temporal CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTemporalCRSType(TemporalCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Temporal CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Temporal CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTemporalCSRefType(TemporalCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Temporal CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Temporal CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTemporalCSType(TemporalCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Temporal Datum Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Temporal Datum Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTemporalDatumBaseType(TemporalDatumBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Temporal Datum Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Temporal Datum Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTemporalDatumRefType(TemporalDatumRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Temporal Datum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Temporal Datum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTemporalDatumType(TemporalDatumType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Calendar Era Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Calendar Era Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeCalendarEraPropertyType(TimeCalendarEraPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Calendar Era Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Calendar Era Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeCalendarEraType(TimeCalendarEraType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Calendar Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Calendar Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeCalendarPropertyType(TimeCalendarPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Calendar Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Calendar Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeCalendarType(TimeCalendarType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Clock Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Clock Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeClockPropertyType(TimeClockPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Clock Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Clock Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeClockType(TimeClockType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Coordinate System Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Coordinate System Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeCoordinateSystemType(TimeCoordinateSystemType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Edge Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Edge Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeEdgePropertyType(TimeEdgePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Edge Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Edge Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeEdgeType(TimeEdgeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Geometric Primitive Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Geometric Primitive Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeGeometricPrimitivePropertyType(TimeGeometricPrimitivePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Instant Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Instant Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeInstantPropertyType(TimeInstantPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Instant Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Instant Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeInstantType(TimeInstantType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Interval Length Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Interval Length Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeIntervalLengthType(TimeIntervalLengthType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Node Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Node Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeNodePropertyType(TimeNodePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Node Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Node Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeNodeType(TimeNodeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Ordinal Era Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Ordinal Era Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeOrdinalEraPropertyType(TimeOrdinalEraPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Ordinal Era Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Ordinal Era Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeOrdinalEraType(TimeOrdinalEraType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Ordinal Reference System Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Ordinal Reference System Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeOrdinalReferenceSystemType(TimeOrdinalReferenceSystemType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Period Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Period Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimePeriodPropertyType(TimePeriodPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Period Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Period Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimePeriodType(TimePeriodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Position Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Position Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimePositionType(TimePositionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Primitive Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Primitive Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimePrimitivePropertyType(TimePrimitivePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Topology Complex Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Topology Complex Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeTopologyComplexPropertyType(TimeTopologyComplexPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Topology Complex Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Topology Complex Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeTopologyComplexType(TimeTopologyComplexType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Topology Primitive Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Topology Primitive Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeTopologyPrimitivePropertyType(TimeTopologyPrimitivePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeType(TimeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tin Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tin Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTinType(TinType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Complex Member Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Complex Member Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoComplexMemberType(TopoComplexMemberType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Complex Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Complex Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoComplexType(TopoComplexType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Curve Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Curve Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoCurvePropertyType(TopoCurvePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Curve Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Curve Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoCurveType(TopoCurveType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topology Style Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topology Style Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopologyStylePropertyType(TopologyStylePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topology Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topology Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopologyStyleType(TopologyStyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Point Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Point Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoPointPropertyType(TopoPointPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Point Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Point Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoPointType(TopoPointType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Primitive Array Association Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Primitive Array Association Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoPrimitiveArrayAssociationType(TopoPrimitiveArrayAssociationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Primitive Member Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Primitive Member Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoPrimitiveMemberType(TopoPrimitiveMemberType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Solid Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Solid Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoSolidType(TopoSolidType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Surface Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Surface Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoSurfacePropertyType(TopoSurfacePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoSurfaceType(TopoSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Volume Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Volume Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoVolumePropertyType(TopoVolumePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Topo Volume Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Topo Volume Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTopoVolumeType(TopoVolumeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Track Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Track Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTrackType(TrackType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Transformation Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Transformation Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTransformationRefType(TransformationRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Transformation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Transformation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTransformationType(TransformationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Triangle Patch Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Triangle Patch Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTrianglePatchArrayPropertyType(TrianglePatchArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Triangle Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Triangle Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTriangleType(TriangleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Triangulated Surface Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Triangulated Surface Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTriangulatedSurfaceType(TriangulatedSurfaceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unit Definition Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unit Definition Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnitDefinitionType(UnitDefinitionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unit Of Measure Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unit Of Measure Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnitOfMeasureType(UnitOfMeasureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>User Defined CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>User Defined CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUserDefinedCSRefType(UserDefinedCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>User Defined CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>User Defined CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUserDefinedCSType(UserDefinedCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Array Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Array Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValueArrayPropertyType(ValueArrayPropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Array Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Array Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValueArrayType(ValueArrayType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseValuePropertyType(ValuePropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vector Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vector Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVectorType(VectorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vertical CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vertical CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerticalCRSRefType(VerticalCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vertical CRS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vertical CRS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerticalCRSType(VerticalCRSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vertical CS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vertical CS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerticalCSRefType(VerticalCSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vertical CS Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vertical CS Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerticalCSType(VerticalCSType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vertical Datum Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vertical Datum Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerticalDatumRefType(VerticalDatumRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vertical Datum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vertical Datum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerticalDatumType(VerticalDatumType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Vertical Datum Type Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Vertical Datum Type Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerticalDatumTypeType(VerticalDatumTypeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Volume Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Volume Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVolumeType(VolumeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //Gml311Switch
