/**
 */
package net.opengis.gml311.util;

import net.opengis.gml311.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.gml311.Gml311Package
 * @generated
 */
public class Gml311AdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Gml311Package modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Gml311AdapterFactory() {
        if (modelPackage == null) {
            modelPackage = Gml311Package.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Gml311Switch<Adapter> modelSwitch =
        new Gml311Switch<Adapter>() {
            @Override
            public Adapter caseAbsoluteExternalPositionalAccuracyType(AbsoluteExternalPositionalAccuracyType object) {
                return createAbsoluteExternalPositionalAccuracyTypeAdapter();
            }
            @Override
            public Adapter caseAbstractContinuousCoverageType(AbstractContinuousCoverageType object) {
                return createAbstractContinuousCoverageTypeAdapter();
            }
            @Override
            public Adapter caseAbstractCoordinateOperationBaseType(AbstractCoordinateOperationBaseType object) {
                return createAbstractCoordinateOperationBaseTypeAdapter();
            }
            @Override
            public Adapter caseAbstractCoordinateOperationType(AbstractCoordinateOperationType object) {
                return createAbstractCoordinateOperationTypeAdapter();
            }
            @Override
            public Adapter caseAbstractCoordinateSystemBaseType(AbstractCoordinateSystemBaseType object) {
                return createAbstractCoordinateSystemBaseTypeAdapter();
            }
            @Override
            public Adapter caseAbstractCoordinateSystemType(AbstractCoordinateSystemType object) {
                return createAbstractCoordinateSystemTypeAdapter();
            }
            @Override
            public Adapter caseAbstractCoverageType(AbstractCoverageType object) {
                return createAbstractCoverageTypeAdapter();
            }
            @Override
            public Adapter caseAbstractCurveSegmentType(AbstractCurveSegmentType object) {
                return createAbstractCurveSegmentTypeAdapter();
            }
            @Override
            public Adapter caseAbstractCurveType(AbstractCurveType object) {
                return createAbstractCurveTypeAdapter();
            }
            @Override
            public Adapter caseAbstractDatumBaseType(AbstractDatumBaseType object) {
                return createAbstractDatumBaseTypeAdapter();
            }
            @Override
            public Adapter caseAbstractDatumType(AbstractDatumType object) {
                return createAbstractDatumTypeAdapter();
            }
            @Override
            public Adapter caseAbstractDiscreteCoverageType(AbstractDiscreteCoverageType object) {
                return createAbstractDiscreteCoverageTypeAdapter();
            }
            @Override
            public Adapter caseAbstractFeatureCollectionType(AbstractFeatureCollectionType object) {
                return createAbstractFeatureCollectionTypeAdapter();
            }
            @Override
            public Adapter caseAbstractFeatureType(AbstractFeatureType object) {
                return createAbstractFeatureTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeneralConversionType(AbstractGeneralConversionType object) {
                return createAbstractGeneralConversionTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeneralDerivedCRSType(AbstractGeneralDerivedCRSType object) {
                return createAbstractGeneralDerivedCRSTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeneralOperationParameterRefType(AbstractGeneralOperationParameterRefType object) {
                return createAbstractGeneralOperationParameterRefTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeneralOperationParameterType(AbstractGeneralOperationParameterType object) {
                return createAbstractGeneralOperationParameterTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeneralParameterValueType(AbstractGeneralParameterValueType object) {
                return createAbstractGeneralParameterValueTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeneralTransformationType(AbstractGeneralTransformationType object) {
                return createAbstractGeneralTransformationTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeometricAggregateType(AbstractGeometricAggregateType object) {
                return createAbstractGeometricAggregateTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeometricPrimitiveType(AbstractGeometricPrimitiveType object) {
                return createAbstractGeometricPrimitiveTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGeometryType(AbstractGeometryType object) {
                return createAbstractGeometryTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGMLType(AbstractGMLType object) {
                return createAbstractGMLTypeAdapter();
            }
            @Override
            public Adapter caseAbstractGriddedSurfaceType(AbstractGriddedSurfaceType object) {
                return createAbstractGriddedSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseAbstractMetaDataType(AbstractMetaDataType object) {
                return createAbstractMetaDataTypeAdapter();
            }
            @Override
            public Adapter caseAbstractParametricCurveSurfaceType(AbstractParametricCurveSurfaceType object) {
                return createAbstractParametricCurveSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseAbstractPositionalAccuracyType(AbstractPositionalAccuracyType object) {
                return createAbstractPositionalAccuracyTypeAdapter();
            }
            @Override
            public Adapter caseAbstractReferenceSystemBaseType(AbstractReferenceSystemBaseType object) {
                return createAbstractReferenceSystemBaseTypeAdapter();
            }
            @Override
            public Adapter caseAbstractReferenceSystemType(AbstractReferenceSystemType object) {
                return createAbstractReferenceSystemTypeAdapter();
            }
            @Override
            public Adapter caseAbstractRingPropertyType(AbstractRingPropertyType object) {
                return createAbstractRingPropertyTypeAdapter();
            }
            @Override
            public Adapter caseAbstractRingType(AbstractRingType object) {
                return createAbstractRingTypeAdapter();
            }
            @Override
            public Adapter caseAbstractSolidType(AbstractSolidType object) {
                return createAbstractSolidTypeAdapter();
            }
            @Override
            public Adapter caseAbstractStyleType(AbstractStyleType object) {
                return createAbstractStyleTypeAdapter();
            }
            @Override
            public Adapter caseAbstractSurfacePatchType(AbstractSurfacePatchType object) {
                return createAbstractSurfacePatchTypeAdapter();
            }
            @Override
            public Adapter caseAbstractSurfaceType(AbstractSurfaceType object) {
                return createAbstractSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTimeComplexType(AbstractTimeComplexType object) {
                return createAbstractTimeComplexTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTimeGeometricPrimitiveType(AbstractTimeGeometricPrimitiveType object) {
                return createAbstractTimeGeometricPrimitiveTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTimeObjectType(AbstractTimeObjectType object) {
                return createAbstractTimeObjectTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTimePrimitiveType(AbstractTimePrimitiveType object) {
                return createAbstractTimePrimitiveTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTimeReferenceSystemType(AbstractTimeReferenceSystemType object) {
                return createAbstractTimeReferenceSystemTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTimeSliceType(AbstractTimeSliceType object) {
                return createAbstractTimeSliceTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTimeTopologyPrimitiveType(AbstractTimeTopologyPrimitiveType object) {
                return createAbstractTimeTopologyPrimitiveTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTopologyType(AbstractTopologyType object) {
                return createAbstractTopologyTypeAdapter();
            }
            @Override
            public Adapter caseAbstractTopoPrimitiveType(AbstractTopoPrimitiveType object) {
                return createAbstractTopoPrimitiveTypeAdapter();
            }
            @Override
            public Adapter caseAffinePlacementType(AffinePlacementType object) {
                return createAffinePlacementTypeAdapter();
            }
            @Override
            public Adapter caseAngleChoiceType(AngleChoiceType object) {
                return createAngleChoiceTypeAdapter();
            }
            @Override
            public Adapter caseAngleType(AngleType object) {
                return createAngleTypeAdapter();
            }
            @Override
            public Adapter caseArcByBulgeType(ArcByBulgeType object) {
                return createArcByBulgeTypeAdapter();
            }
            @Override
            public Adapter caseArcByCenterPointType(ArcByCenterPointType object) {
                return createArcByCenterPointTypeAdapter();
            }
            @Override
            public Adapter caseArcStringByBulgeType(ArcStringByBulgeType object) {
                return createArcStringByBulgeTypeAdapter();
            }
            @Override
            public Adapter caseArcStringType(ArcStringType object) {
                return createArcStringTypeAdapter();
            }
            @Override
            public Adapter caseArcType(ArcType object) {
                return createArcTypeAdapter();
            }
            @Override
            public Adapter caseAreaType(AreaType object) {
                return createAreaTypeAdapter();
            }
            @Override
            public Adapter caseArrayAssociationType(ArrayAssociationType object) {
                return createArrayAssociationTypeAdapter();
            }
            @Override
            public Adapter caseArrayType(ArrayType object) {
                return createArrayTypeAdapter();
            }
            @Override
            public Adapter caseAssociationType(AssociationType object) {
                return createAssociationTypeAdapter();
            }
            @Override
            public Adapter caseBagType(BagType object) {
                return createBagTypeAdapter();
            }
            @Override
            public Adapter caseBaseStyleDescriptorType(BaseStyleDescriptorType object) {
                return createBaseStyleDescriptorTypeAdapter();
            }
            @Override
            public Adapter caseBaseUnitType(BaseUnitType object) {
                return createBaseUnitTypeAdapter();
            }
            @Override
            public Adapter caseBezierType(BezierType object) {
                return createBezierTypeAdapter();
            }
            @Override
            public Adapter caseBooleanPropertyType(BooleanPropertyType object) {
                return createBooleanPropertyTypeAdapter();
            }
            @Override
            public Adapter caseBoundedFeatureType(BoundedFeatureType object) {
                return createBoundedFeatureTypeAdapter();
            }
            @Override
            public Adapter caseBoundingShapeType(BoundingShapeType object) {
                return createBoundingShapeTypeAdapter();
            }
            @Override
            public Adapter caseBSplineType(BSplineType object) {
                return createBSplineTypeAdapter();
            }
            @Override
            public Adapter caseCartesianCSRefType(CartesianCSRefType object) {
                return createCartesianCSRefTypeAdapter();
            }
            @Override
            public Adapter caseCartesianCSType(CartesianCSType object) {
                return createCartesianCSTypeAdapter();
            }
            @Override
            public Adapter caseCategoryExtentType(CategoryExtentType object) {
                return createCategoryExtentTypeAdapter();
            }
            @Override
            public Adapter caseCategoryPropertyType(CategoryPropertyType object) {
                return createCategoryPropertyTypeAdapter();
            }
            @Override
            public Adapter caseCircleByCenterPointType(CircleByCenterPointType object) {
                return createCircleByCenterPointTypeAdapter();
            }
            @Override
            public Adapter caseCircleType(CircleType object) {
                return createCircleTypeAdapter();
            }
            @Override
            public Adapter caseClothoidType(ClothoidType object) {
                return createClothoidTypeAdapter();
            }
            @Override
            public Adapter caseCodeListType(CodeListType object) {
                return createCodeListTypeAdapter();
            }
            @Override
            public Adapter caseCodeOrNullListType(CodeOrNullListType object) {
                return createCodeOrNullListTypeAdapter();
            }
            @Override
            public Adapter caseCodeType(CodeType object) {
                return createCodeTypeAdapter();
            }
            @Override
            public Adapter caseCompositeCurvePropertyType(CompositeCurvePropertyType object) {
                return createCompositeCurvePropertyTypeAdapter();
            }
            @Override
            public Adapter caseCompositeCurveType(CompositeCurveType object) {
                return createCompositeCurveTypeAdapter();
            }
            @Override
            public Adapter caseCompositeSolidPropertyType(CompositeSolidPropertyType object) {
                return createCompositeSolidPropertyTypeAdapter();
            }
            @Override
            public Adapter caseCompositeSolidType(CompositeSolidType object) {
                return createCompositeSolidTypeAdapter();
            }
            @Override
            public Adapter caseCompositeSurfacePropertyType(CompositeSurfacePropertyType object) {
                return createCompositeSurfacePropertyTypeAdapter();
            }
            @Override
            public Adapter caseCompositeSurfaceType(CompositeSurfaceType object) {
                return createCompositeSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseCompositeValueType(CompositeValueType object) {
                return createCompositeValueTypeAdapter();
            }
            @Override
            public Adapter caseCompoundCRSRefType(CompoundCRSRefType object) {
                return createCompoundCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseCompoundCRSType(CompoundCRSType object) {
                return createCompoundCRSTypeAdapter();
            }
            @Override
            public Adapter caseConcatenatedOperationRefType(ConcatenatedOperationRefType object) {
                return createConcatenatedOperationRefTypeAdapter();
            }
            @Override
            public Adapter caseConcatenatedOperationType(ConcatenatedOperationType object) {
                return createConcatenatedOperationTypeAdapter();
            }
            @Override
            public Adapter caseConeType(ConeType object) {
                return createConeTypeAdapter();
            }
            @Override
            public Adapter caseContainerPropertyType(ContainerPropertyType object) {
                return createContainerPropertyTypeAdapter();
            }
            @Override
            public Adapter caseControlPointType(ControlPointType object) {
                return createControlPointTypeAdapter();
            }
            @Override
            public Adapter caseConventionalUnitType(ConventionalUnitType object) {
                return createConventionalUnitTypeAdapter();
            }
            @Override
            public Adapter caseConversionRefType(ConversionRefType object) {
                return createConversionRefTypeAdapter();
            }
            @Override
            public Adapter caseConversionToPreferredUnitType(ConversionToPreferredUnitType object) {
                return createConversionToPreferredUnitTypeAdapter();
            }
            @Override
            public Adapter caseConversionType(ConversionType object) {
                return createConversionTypeAdapter();
            }
            @Override
            public Adapter caseCoordinateOperationRefType(CoordinateOperationRefType object) {
                return createCoordinateOperationRefTypeAdapter();
            }
            @Override
            public Adapter caseCoordinateReferenceSystemRefType(CoordinateReferenceSystemRefType object) {
                return createCoordinateReferenceSystemRefTypeAdapter();
            }
            @Override
            public Adapter caseCoordinatesType(CoordinatesType object) {
                return createCoordinatesTypeAdapter();
            }
            @Override
            public Adapter caseCoordinateSystemAxisBaseType(CoordinateSystemAxisBaseType object) {
                return createCoordinateSystemAxisBaseTypeAdapter();
            }
            @Override
            public Adapter caseCoordinateSystemAxisRefType(CoordinateSystemAxisRefType object) {
                return createCoordinateSystemAxisRefTypeAdapter();
            }
            @Override
            public Adapter caseCoordinateSystemAxisType(CoordinateSystemAxisType object) {
                return createCoordinateSystemAxisTypeAdapter();
            }
            @Override
            public Adapter caseCoordinateSystemRefType(CoordinateSystemRefType object) {
                return createCoordinateSystemRefTypeAdapter();
            }
            @Override
            public Adapter caseCoordType(CoordType object) {
                return createCoordTypeAdapter();
            }
            @Override
            public Adapter caseCountPropertyType(CountPropertyType object) {
                return createCountPropertyTypeAdapter();
            }
            @Override
            public Adapter caseCovarianceElementType(CovarianceElementType object) {
                return createCovarianceElementTypeAdapter();
            }
            @Override
            public Adapter caseCovarianceMatrixType(CovarianceMatrixType object) {
                return createCovarianceMatrixTypeAdapter();
            }
            @Override
            public Adapter caseCoverageFunctionType(CoverageFunctionType object) {
                return createCoverageFunctionTypeAdapter();
            }
            @Override
            public Adapter caseCRSRefType(CRSRefType object) {
                return createCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseCubicSplineType(CubicSplineType object) {
                return createCubicSplineTypeAdapter();
            }
            @Override
            public Adapter caseCurveArrayPropertyType(CurveArrayPropertyType object) {
                return createCurveArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseCurvePropertyType(CurvePropertyType object) {
                return createCurvePropertyTypeAdapter();
            }
            @Override
            public Adapter caseCurveSegmentArrayPropertyType(CurveSegmentArrayPropertyType object) {
                return createCurveSegmentArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseCurveType(CurveType object) {
                return createCurveTypeAdapter();
            }
            @Override
            public Adapter caseCylinderType(CylinderType object) {
                return createCylinderTypeAdapter();
            }
            @Override
            public Adapter caseCylindricalCSRefType(CylindricalCSRefType object) {
                return createCylindricalCSRefTypeAdapter();
            }
            @Override
            public Adapter caseCylindricalCSType(CylindricalCSType object) {
                return createCylindricalCSTypeAdapter();
            }
            @Override
            public Adapter caseDataBlockType(DataBlockType object) {
                return createDataBlockTypeAdapter();
            }
            @Override
            public Adapter caseDatumRefType(DatumRefType object) {
                return createDatumRefTypeAdapter();
            }
            @Override
            public Adapter caseDefaultStylePropertyType(DefaultStylePropertyType object) {
                return createDefaultStylePropertyTypeAdapter();
            }
            @Override
            public Adapter caseDefinitionProxyType(DefinitionProxyType object) {
                return createDefinitionProxyTypeAdapter();
            }
            @Override
            public Adapter caseDefinitionType(DefinitionType object) {
                return createDefinitionTypeAdapter();
            }
            @Override
            public Adapter caseDegreesType(DegreesType object) {
                return createDegreesTypeAdapter();
            }
            @Override
            public Adapter caseDerivationUnitTermType(DerivationUnitTermType object) {
                return createDerivationUnitTermTypeAdapter();
            }
            @Override
            public Adapter caseDerivedCRSRefType(DerivedCRSRefType object) {
                return createDerivedCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseDerivedCRSType(DerivedCRSType object) {
                return createDerivedCRSTypeAdapter();
            }
            @Override
            public Adapter caseDerivedCRSTypeType(DerivedCRSTypeType object) {
                return createDerivedCRSTypeTypeAdapter();
            }
            @Override
            public Adapter caseDerivedUnitType(DerivedUnitType object) {
                return createDerivedUnitTypeAdapter();
            }
            @Override
            public Adapter caseDictionaryEntryType(DictionaryEntryType object) {
                return createDictionaryEntryTypeAdapter();
            }
            @Override
            public Adapter caseDictionaryType(DictionaryType object) {
                return createDictionaryTypeAdapter();
            }
            @Override
            public Adapter caseDirectedEdgePropertyType(DirectedEdgePropertyType object) {
                return createDirectedEdgePropertyTypeAdapter();
            }
            @Override
            public Adapter caseDirectedFacePropertyType(DirectedFacePropertyType object) {
                return createDirectedFacePropertyTypeAdapter();
            }
            @Override
            public Adapter caseDirectedNodePropertyType(DirectedNodePropertyType object) {
                return createDirectedNodePropertyTypeAdapter();
            }
            @Override
            public Adapter caseDirectedObservationAtDistanceType(DirectedObservationAtDistanceType object) {
                return createDirectedObservationAtDistanceTypeAdapter();
            }
            @Override
            public Adapter caseDirectedObservationType(DirectedObservationType object) {
                return createDirectedObservationTypeAdapter();
            }
            @Override
            public Adapter caseDirectedTopoSolidPropertyType(DirectedTopoSolidPropertyType object) {
                return createDirectedTopoSolidPropertyTypeAdapter();
            }
            @Override
            public Adapter caseDirectionPropertyType(DirectionPropertyType object) {
                return createDirectionPropertyTypeAdapter();
            }
            @Override
            public Adapter caseDirectionVectorType(DirectionVectorType object) {
                return createDirectionVectorTypeAdapter();
            }
            @Override
            public Adapter caseDirectPositionListType(DirectPositionListType object) {
                return createDirectPositionListTypeAdapter();
            }
            @Override
            public Adapter caseDirectPositionType(DirectPositionType object) {
                return createDirectPositionTypeAdapter();
            }
            @Override
            public Adapter caseDMSAngleType(DMSAngleType object) {
                return createDMSAngleTypeAdapter();
            }
            @Override
            public Adapter caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            @Override
            public Adapter caseDomainSetType(DomainSetType object) {
                return createDomainSetTypeAdapter();
            }
            @Override
            public Adapter caseDynamicFeatureCollectionType(DynamicFeatureCollectionType object) {
                return createDynamicFeatureCollectionTypeAdapter();
            }
            @Override
            public Adapter caseDynamicFeatureType(DynamicFeatureType object) {
                return createDynamicFeatureTypeAdapter();
            }
            @Override
            public Adapter caseEdgeType(EdgeType object) {
                return createEdgeTypeAdapter();
            }
            @Override
            public Adapter caseEllipsoidalCSRefType(EllipsoidalCSRefType object) {
                return createEllipsoidalCSRefTypeAdapter();
            }
            @Override
            public Adapter caseEllipsoidalCSType(EllipsoidalCSType object) {
                return createEllipsoidalCSTypeAdapter();
            }
            @Override
            public Adapter caseEllipsoidBaseType(EllipsoidBaseType object) {
                return createEllipsoidBaseTypeAdapter();
            }
            @Override
            public Adapter caseEllipsoidRefType(EllipsoidRefType object) {
                return createEllipsoidRefTypeAdapter();
            }
            @Override
            public Adapter caseEllipsoidType(EllipsoidType object) {
                return createEllipsoidTypeAdapter();
            }
            @Override
            public Adapter caseEngineeringCRSRefType(EngineeringCRSRefType object) {
                return createEngineeringCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseEngineeringCRSType(EngineeringCRSType object) {
                return createEngineeringCRSTypeAdapter();
            }
            @Override
            public Adapter caseEngineeringDatumRefType(EngineeringDatumRefType object) {
                return createEngineeringDatumRefTypeAdapter();
            }
            @Override
            public Adapter caseEngineeringDatumType(EngineeringDatumType object) {
                return createEngineeringDatumTypeAdapter();
            }
            @Override
            public Adapter caseEnvelopeType(EnvelopeType object) {
                return createEnvelopeTypeAdapter();
            }
            @Override
            public Adapter caseEnvelopeWithTimePeriodType(EnvelopeWithTimePeriodType object) {
                return createEnvelopeWithTimePeriodTypeAdapter();
            }
            @Override
            public Adapter caseExtentType(ExtentType object) {
                return createExtentTypeAdapter();
            }
            @Override
            public Adapter caseFaceType(FaceType object) {
                return createFaceTypeAdapter();
            }
            @Override
            public Adapter caseFeatureArrayPropertyType(FeatureArrayPropertyType object) {
                return createFeatureArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseFeatureCollectionType(FeatureCollectionType object) {
                return createFeatureCollectionTypeAdapter();
            }
            @Override
            public Adapter caseFeaturePropertyType(FeaturePropertyType object) {
                return createFeaturePropertyTypeAdapter();
            }
            @Override
            public Adapter caseFeatureStylePropertyType(FeatureStylePropertyType object) {
                return createFeatureStylePropertyTypeAdapter();
            }
            @Override
            public Adapter caseFeatureStyleType(FeatureStyleType object) {
                return createFeatureStyleTypeAdapter();
            }
            @Override
            public Adapter caseFileType(FileType object) {
                return createFileTypeAdapter();
            }
            @Override
            public Adapter caseFormulaType(FormulaType object) {
                return createFormulaTypeAdapter();
            }
            @Override
            public Adapter caseGeneralConversionRefType(GeneralConversionRefType object) {
                return createGeneralConversionRefTypeAdapter();
            }
            @Override
            public Adapter caseGeneralTransformationRefType(GeneralTransformationRefType object) {
                return createGeneralTransformationRefTypeAdapter();
            }
            @Override
            public Adapter caseGenericMetaDataType(GenericMetaDataType object) {
                return createGenericMetaDataTypeAdapter();
            }
            @Override
            public Adapter caseGeocentricCRSRefType(GeocentricCRSRefType object) {
                return createGeocentricCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseGeocentricCRSType(GeocentricCRSType object) {
                return createGeocentricCRSTypeAdapter();
            }
            @Override
            public Adapter caseGeodesicStringType(GeodesicStringType object) {
                return createGeodesicStringTypeAdapter();
            }
            @Override
            public Adapter caseGeodesicType(GeodesicType object) {
                return createGeodesicTypeAdapter();
            }
            @Override
            public Adapter caseGeodeticDatumRefType(GeodeticDatumRefType object) {
                return createGeodeticDatumRefTypeAdapter();
            }
            @Override
            public Adapter caseGeodeticDatumType(GeodeticDatumType object) {
                return createGeodeticDatumTypeAdapter();
            }
            @Override
            public Adapter caseGeographicCRSRefType(GeographicCRSRefType object) {
                return createGeographicCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseGeographicCRSType(GeographicCRSType object) {
                return createGeographicCRSTypeAdapter();
            }
            @Override
            public Adapter caseGeometricComplexPropertyType(GeometricComplexPropertyType object) {
                return createGeometricComplexPropertyTypeAdapter();
            }
            @Override
            public Adapter caseGeometricComplexType(GeometricComplexType object) {
                return createGeometricComplexTypeAdapter();
            }
            @Override
            public Adapter caseGeometricPrimitivePropertyType(GeometricPrimitivePropertyType object) {
                return createGeometricPrimitivePropertyTypeAdapter();
            }
            @Override
            public Adapter caseGeometryArrayPropertyType(GeometryArrayPropertyType object) {
                return createGeometryArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseGeometryPropertyType(GeometryPropertyType object) {
                return createGeometryPropertyTypeAdapter();
            }
            @Override
            public Adapter caseGeometryStylePropertyType(GeometryStylePropertyType object) {
                return createGeometryStylePropertyTypeAdapter();
            }
            @Override
            public Adapter caseGeometryStyleType(GeometryStyleType object) {
                return createGeometryStyleTypeAdapter();
            }
            @Override
            public Adapter caseGraphStylePropertyType(GraphStylePropertyType object) {
                return createGraphStylePropertyTypeAdapter();
            }
            @Override
            public Adapter caseGraphStyleType(GraphStyleType object) {
                return createGraphStyleTypeAdapter();
            }
            @Override
            public Adapter caseGridCoverageType(GridCoverageType object) {
                return createGridCoverageTypeAdapter();
            }
            @Override
            public Adapter caseGridDomainType(GridDomainType object) {
                return createGridDomainTypeAdapter();
            }
            @Override
            public Adapter caseGridEnvelopeType(GridEnvelopeType object) {
                return createGridEnvelopeTypeAdapter();
            }
            @Override
            public Adapter caseGridFunctionType(GridFunctionType object) {
                return createGridFunctionTypeAdapter();
            }
            @Override
            public Adapter caseGridLengthType(GridLengthType object) {
                return createGridLengthTypeAdapter();
            }
            @Override
            public Adapter caseGridLimitsType(GridLimitsType object) {
                return createGridLimitsTypeAdapter();
            }
            @Override
            public Adapter caseGridType(GridType object) {
                return createGridTypeAdapter();
            }
            @Override
            public Adapter caseHistoryPropertyType(HistoryPropertyType object) {
                return createHistoryPropertyTypeAdapter();
            }
            @Override
            public Adapter caseIdentifierType(IdentifierType object) {
                return createIdentifierTypeAdapter();
            }
            @Override
            public Adapter caseImageCRSRefType(ImageCRSRefType object) {
                return createImageCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseImageCRSType(ImageCRSType object) {
                return createImageCRSTypeAdapter();
            }
            @Override
            public Adapter caseImageDatumRefType(ImageDatumRefType object) {
                return createImageDatumRefTypeAdapter();
            }
            @Override
            public Adapter caseImageDatumType(ImageDatumType object) {
                return createImageDatumTypeAdapter();
            }
            @Override
            public Adapter caseIndexMapType(IndexMapType object) {
                return createIndexMapTypeAdapter();
            }
            @Override
            public Adapter caseIndirectEntryType(IndirectEntryType object) {
                return createIndirectEntryTypeAdapter();
            }
            @Override
            public Adapter caseIsolatedPropertyType(IsolatedPropertyType object) {
                return createIsolatedPropertyTypeAdapter();
            }
            @Override
            public Adapter caseKnotPropertyType(KnotPropertyType object) {
                return createKnotPropertyTypeAdapter();
            }
            @Override
            public Adapter caseKnotType(KnotType object) {
                return createKnotTypeAdapter();
            }
            @Override
            public Adapter caseLabelStylePropertyType(LabelStylePropertyType object) {
                return createLabelStylePropertyTypeAdapter();
            }
            @Override
            public Adapter caseLabelStyleType(LabelStyleType object) {
                return createLabelStyleTypeAdapter();
            }
            @Override
            public Adapter caseLabelType(LabelType object) {
                return createLabelTypeAdapter();
            }
            @Override
            public Adapter caseLengthType(LengthType object) {
                return createLengthTypeAdapter();
            }
            @Override
            public Adapter caseLinearCSRefType(LinearCSRefType object) {
                return createLinearCSRefTypeAdapter();
            }
            @Override
            public Adapter caseLinearCSType(LinearCSType object) {
                return createLinearCSTypeAdapter();
            }
            @Override
            public Adapter caseLinearRingPropertyType(LinearRingPropertyType object) {
                return createLinearRingPropertyTypeAdapter();
            }
            @Override
            public Adapter caseLinearRingType(LinearRingType object) {
                return createLinearRingTypeAdapter();
            }
            @Override
            public Adapter caseLineStringPropertyType(LineStringPropertyType object) {
                return createLineStringPropertyTypeAdapter();
            }
            @Override
            public Adapter caseLineStringSegmentArrayPropertyType(LineStringSegmentArrayPropertyType object) {
                return createLineStringSegmentArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseLineStringSegmentType(LineStringSegmentType object) {
                return createLineStringSegmentTypeAdapter();
            }
            @Override
            public Adapter caseLineStringType(LineStringType object) {
                return createLineStringTypeAdapter();
            }
            @Override
            public Adapter caseLocationPropertyType(LocationPropertyType object) {
                return createLocationPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMeasureListType(MeasureListType object) {
                return createMeasureListTypeAdapter();
            }
            @Override
            public Adapter caseMeasureOrNullListType(MeasureOrNullListType object) {
                return createMeasureOrNullListTypeAdapter();
            }
            @Override
            public Adapter caseMeasureType(MeasureType object) {
                return createMeasureTypeAdapter();
            }
            @Override
            public Adapter caseMetaDataPropertyType(MetaDataPropertyType object) {
                return createMetaDataPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMovingObjectStatusType(MovingObjectStatusType object) {
                return createMovingObjectStatusTypeAdapter();
            }
            @Override
            public Adapter caseMultiCurveCoverageType(MultiCurveCoverageType object) {
                return createMultiCurveCoverageTypeAdapter();
            }
            @Override
            public Adapter caseMultiCurveDomainType(MultiCurveDomainType object) {
                return createMultiCurveDomainTypeAdapter();
            }
            @Override
            public Adapter caseMultiCurvePropertyType(MultiCurvePropertyType object) {
                return createMultiCurvePropertyTypeAdapter();
            }
            @Override
            public Adapter caseMultiCurveType(MultiCurveType object) {
                return createMultiCurveTypeAdapter();
            }
            @Override
            public Adapter caseMultiGeometryPropertyType(MultiGeometryPropertyType object) {
                return createMultiGeometryPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMultiGeometryType(MultiGeometryType object) {
                return createMultiGeometryTypeAdapter();
            }
            @Override
            public Adapter caseMultiLineStringPropertyType(MultiLineStringPropertyType object) {
                return createMultiLineStringPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMultiLineStringType(MultiLineStringType object) {
                return createMultiLineStringTypeAdapter();
            }
            @Override
            public Adapter caseMultiPointCoverageType(MultiPointCoverageType object) {
                return createMultiPointCoverageTypeAdapter();
            }
            @Override
            public Adapter caseMultiPointDomainType(MultiPointDomainType object) {
                return createMultiPointDomainTypeAdapter();
            }
            @Override
            public Adapter caseMultiPointPropertyType(MultiPointPropertyType object) {
                return createMultiPointPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMultiPointType(MultiPointType object) {
                return createMultiPointTypeAdapter();
            }
            @Override
            public Adapter caseMultiPolygonPropertyType(MultiPolygonPropertyType object) {
                return createMultiPolygonPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMultiPolygonType(MultiPolygonType object) {
                return createMultiPolygonTypeAdapter();
            }
            @Override
            public Adapter caseMultiSolidCoverageType(MultiSolidCoverageType object) {
                return createMultiSolidCoverageTypeAdapter();
            }
            @Override
            public Adapter caseMultiSolidDomainType(MultiSolidDomainType object) {
                return createMultiSolidDomainTypeAdapter();
            }
            @Override
            public Adapter caseMultiSolidPropertyType(MultiSolidPropertyType object) {
                return createMultiSolidPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMultiSolidType(MultiSolidType object) {
                return createMultiSolidTypeAdapter();
            }
            @Override
            public Adapter caseMultiSurfaceCoverageType(MultiSurfaceCoverageType object) {
                return createMultiSurfaceCoverageTypeAdapter();
            }
            @Override
            public Adapter caseMultiSurfaceDomainType(MultiSurfaceDomainType object) {
                return createMultiSurfaceDomainTypeAdapter();
            }
            @Override
            public Adapter caseMultiSurfacePropertyType(MultiSurfacePropertyType object) {
                return createMultiSurfacePropertyTypeAdapter();
            }
            @Override
            public Adapter caseMultiSurfaceType(MultiSurfaceType object) {
                return createMultiSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseNodeType(NodeType object) {
                return createNodeTypeAdapter();
            }
            @Override
            public Adapter caseObliqueCartesianCSRefType(ObliqueCartesianCSRefType object) {
                return createObliqueCartesianCSRefTypeAdapter();
            }
            @Override
            public Adapter caseObliqueCartesianCSType(ObliqueCartesianCSType object) {
                return createObliqueCartesianCSTypeAdapter();
            }
            @Override
            public Adapter caseObservationType(ObservationType object) {
                return createObservationTypeAdapter();
            }
            @Override
            public Adapter caseOffsetCurveType(OffsetCurveType object) {
                return createOffsetCurveTypeAdapter();
            }
            @Override
            public Adapter caseOperationMethodBaseType(OperationMethodBaseType object) {
                return createOperationMethodBaseTypeAdapter();
            }
            @Override
            public Adapter caseOperationMethodRefType(OperationMethodRefType object) {
                return createOperationMethodRefTypeAdapter();
            }
            @Override
            public Adapter caseOperationMethodType(OperationMethodType object) {
                return createOperationMethodTypeAdapter();
            }
            @Override
            public Adapter caseOperationParameterBaseType(OperationParameterBaseType object) {
                return createOperationParameterBaseTypeAdapter();
            }
            @Override
            public Adapter caseOperationParameterGroupBaseType(OperationParameterGroupBaseType object) {
                return createOperationParameterGroupBaseTypeAdapter();
            }
            @Override
            public Adapter caseOperationParameterGroupRefType(OperationParameterGroupRefType object) {
                return createOperationParameterGroupRefTypeAdapter();
            }
            @Override
            public Adapter caseOperationParameterGroupType(OperationParameterGroupType object) {
                return createOperationParameterGroupTypeAdapter();
            }
            @Override
            public Adapter caseOperationParameterRefType(OperationParameterRefType object) {
                return createOperationParameterRefTypeAdapter();
            }
            @Override
            public Adapter caseOperationParameterType(OperationParameterType object) {
                return createOperationParameterTypeAdapter();
            }
            @Override
            public Adapter caseOperationRefType(OperationRefType object) {
                return createOperationRefTypeAdapter();
            }
            @Override
            public Adapter caseOrientableCurveType(OrientableCurveType object) {
                return createOrientableCurveTypeAdapter();
            }
            @Override
            public Adapter caseOrientableSurfaceType(OrientableSurfaceType object) {
                return createOrientableSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseParameterValueGroupType(ParameterValueGroupType object) {
                return createParameterValueGroupTypeAdapter();
            }
            @Override
            public Adapter caseParameterValueType(ParameterValueType object) {
                return createParameterValueTypeAdapter();
            }
            @Override
            public Adapter casePassThroughOperationRefType(PassThroughOperationRefType object) {
                return createPassThroughOperationRefTypeAdapter();
            }
            @Override
            public Adapter casePassThroughOperationType(PassThroughOperationType object) {
                return createPassThroughOperationTypeAdapter();
            }
            @Override
            public Adapter casePixelInCellType(PixelInCellType object) {
                return createPixelInCellTypeAdapter();
            }
            @Override
            public Adapter casePointArrayPropertyType(PointArrayPropertyType object) {
                return createPointArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter casePointPropertyType(PointPropertyType object) {
                return createPointPropertyTypeAdapter();
            }
            @Override
            public Adapter casePointType(PointType object) {
                return createPointTypeAdapter();
            }
            @Override
            public Adapter casePolarCSRefType(PolarCSRefType object) {
                return createPolarCSRefTypeAdapter();
            }
            @Override
            public Adapter casePolarCSType(PolarCSType object) {
                return createPolarCSTypeAdapter();
            }
            @Override
            public Adapter casePolygonPatchArrayPropertyType(PolygonPatchArrayPropertyType object) {
                return createPolygonPatchArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter casePolygonPatchType(PolygonPatchType object) {
                return createPolygonPatchTypeAdapter();
            }
            @Override
            public Adapter casePolygonPropertyType(PolygonPropertyType object) {
                return createPolygonPropertyTypeAdapter();
            }
            @Override
            public Adapter casePolygonType(PolygonType object) {
                return createPolygonTypeAdapter();
            }
            @Override
            public Adapter casePolyhedralSurfaceType(PolyhedralSurfaceType object) {
                return createPolyhedralSurfaceTypeAdapter();
            }
            @Override
            public Adapter casePrimeMeridianBaseType(PrimeMeridianBaseType object) {
                return createPrimeMeridianBaseTypeAdapter();
            }
            @Override
            public Adapter casePrimeMeridianRefType(PrimeMeridianRefType object) {
                return createPrimeMeridianRefTypeAdapter();
            }
            @Override
            public Adapter casePrimeMeridianType(PrimeMeridianType object) {
                return createPrimeMeridianTypeAdapter();
            }
            @Override
            public Adapter casePriorityLocationPropertyType(PriorityLocationPropertyType object) {
                return createPriorityLocationPropertyTypeAdapter();
            }
            @Override
            public Adapter caseProjectedCRSRefType(ProjectedCRSRefType object) {
                return createProjectedCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseProjectedCRSType(ProjectedCRSType object) {
                return createProjectedCRSTypeAdapter();
            }
            @Override
            public Adapter caseQuantityExtentType(QuantityExtentType object) {
                return createQuantityExtentTypeAdapter();
            }
            @Override
            public Adapter caseQuantityPropertyType(QuantityPropertyType object) {
                return createQuantityPropertyTypeAdapter();
            }
            @Override
            public Adapter caseRangeParametersType(RangeParametersType object) {
                return createRangeParametersTypeAdapter();
            }
            @Override
            public Adapter caseRangeSetType(RangeSetType object) {
                return createRangeSetTypeAdapter();
            }
            @Override
            public Adapter caseRectangleType(RectangleType object) {
                return createRectangleTypeAdapter();
            }
            @Override
            public Adapter caseRectifiedGridCoverageType(RectifiedGridCoverageType object) {
                return createRectifiedGridCoverageTypeAdapter();
            }
            @Override
            public Adapter caseRectifiedGridDomainType(RectifiedGridDomainType object) {
                return createRectifiedGridDomainTypeAdapter();
            }
            @Override
            public Adapter caseRectifiedGridType(RectifiedGridType object) {
                return createRectifiedGridTypeAdapter();
            }
            @Override
            public Adapter caseReferenceSystemRefType(ReferenceSystemRefType object) {
                return createReferenceSystemRefTypeAdapter();
            }
            @Override
            public Adapter caseReferenceType(ReferenceType object) {
                return createReferenceTypeAdapter();
            }
            @Override
            public Adapter caseRefLocationType(RefLocationType object) {
                return createRefLocationTypeAdapter();
            }
            @Override
            public Adapter caseRelatedTimeType(RelatedTimeType object) {
                return createRelatedTimeTypeAdapter();
            }
            @Override
            public Adapter caseRelativeInternalPositionalAccuracyType(RelativeInternalPositionalAccuracyType object) {
                return createRelativeInternalPositionalAccuracyTypeAdapter();
            }
            @Override
            public Adapter caseRingPropertyType(RingPropertyType object) {
                return createRingPropertyTypeAdapter();
            }
            @Override
            public Adapter caseRingType(RingType object) {
                return createRingTypeAdapter();
            }
            @Override
            public Adapter caseRowType(RowType object) {
                return createRowTypeAdapter();
            }
            @Override
            public Adapter caseScalarValuePropertyType(ScalarValuePropertyType object) {
                return createScalarValuePropertyTypeAdapter();
            }
            @Override
            public Adapter caseScaleType(ScaleType object) {
                return createScaleTypeAdapter();
            }
            @Override
            public Adapter caseSecondDefiningParameterType(SecondDefiningParameterType object) {
                return createSecondDefiningParameterTypeAdapter();
            }
            @Override
            public Adapter caseSequenceRuleType(SequenceRuleType object) {
                return createSequenceRuleTypeAdapter();
            }
            @Override
            public Adapter caseSingleOperationRefType(SingleOperationRefType object) {
                return createSingleOperationRefTypeAdapter();
            }
            @Override
            public Adapter caseSolidArrayPropertyType(SolidArrayPropertyType object) {
                return createSolidArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseSolidPropertyType(SolidPropertyType object) {
                return createSolidPropertyTypeAdapter();
            }
            @Override
            public Adapter caseSolidType(SolidType object) {
                return createSolidTypeAdapter();
            }
            @Override
            public Adapter caseSpeedType(SpeedType object) {
                return createSpeedTypeAdapter();
            }
            @Override
            public Adapter caseSphereType(SphereType object) {
                return createSphereTypeAdapter();
            }
            @Override
            public Adapter caseSphericalCSRefType(SphericalCSRefType object) {
                return createSphericalCSRefTypeAdapter();
            }
            @Override
            public Adapter caseSphericalCSType(SphericalCSType object) {
                return createSphericalCSTypeAdapter();
            }
            @Override
            public Adapter caseStringOrRefType(StringOrRefType object) {
                return createStringOrRefTypeAdapter();
            }
            @Override
            public Adapter caseStyleType(StyleType object) {
                return createStyleTypeAdapter();
            }
            @Override
            public Adapter caseStyleVariationType(StyleVariationType object) {
                return createStyleVariationTypeAdapter();
            }
            @Override
            public Adapter caseSurfaceArrayPropertyType(SurfaceArrayPropertyType object) {
                return createSurfaceArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseSurfacePatchArrayPropertyType(SurfacePatchArrayPropertyType object) {
                return createSurfacePatchArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseSurfacePropertyType(SurfacePropertyType object) {
                return createSurfacePropertyTypeAdapter();
            }
            @Override
            public Adapter caseSurfaceType(SurfaceType object) {
                return createSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseSymbolType(SymbolType object) {
                return createSymbolTypeAdapter();
            }
            @Override
            public Adapter caseTargetPropertyType(TargetPropertyType object) {
                return createTargetPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTemporalCRSRefType(TemporalCRSRefType object) {
                return createTemporalCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseTemporalCRSType(TemporalCRSType object) {
                return createTemporalCRSTypeAdapter();
            }
            @Override
            public Adapter caseTemporalCSRefType(TemporalCSRefType object) {
                return createTemporalCSRefTypeAdapter();
            }
            @Override
            public Adapter caseTemporalCSType(TemporalCSType object) {
                return createTemporalCSTypeAdapter();
            }
            @Override
            public Adapter caseTemporalDatumBaseType(TemporalDatumBaseType object) {
                return createTemporalDatumBaseTypeAdapter();
            }
            @Override
            public Adapter caseTemporalDatumRefType(TemporalDatumRefType object) {
                return createTemporalDatumRefTypeAdapter();
            }
            @Override
            public Adapter caseTemporalDatumType(TemporalDatumType object) {
                return createTemporalDatumTypeAdapter();
            }
            @Override
            public Adapter caseTimeCalendarEraPropertyType(TimeCalendarEraPropertyType object) {
                return createTimeCalendarEraPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeCalendarEraType(TimeCalendarEraType object) {
                return createTimeCalendarEraTypeAdapter();
            }
            @Override
            public Adapter caseTimeCalendarPropertyType(TimeCalendarPropertyType object) {
                return createTimeCalendarPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeCalendarType(TimeCalendarType object) {
                return createTimeCalendarTypeAdapter();
            }
            @Override
            public Adapter caseTimeClockPropertyType(TimeClockPropertyType object) {
                return createTimeClockPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeClockType(TimeClockType object) {
                return createTimeClockTypeAdapter();
            }
            @Override
            public Adapter caseTimeCoordinateSystemType(TimeCoordinateSystemType object) {
                return createTimeCoordinateSystemTypeAdapter();
            }
            @Override
            public Adapter caseTimeEdgePropertyType(TimeEdgePropertyType object) {
                return createTimeEdgePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeEdgeType(TimeEdgeType object) {
                return createTimeEdgeTypeAdapter();
            }
            @Override
            public Adapter caseTimeGeometricPrimitivePropertyType(TimeGeometricPrimitivePropertyType object) {
                return createTimeGeometricPrimitivePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeInstantPropertyType(TimeInstantPropertyType object) {
                return createTimeInstantPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeInstantType(TimeInstantType object) {
                return createTimeInstantTypeAdapter();
            }
            @Override
            public Adapter caseTimeIntervalLengthType(TimeIntervalLengthType object) {
                return createTimeIntervalLengthTypeAdapter();
            }
            @Override
            public Adapter caseTimeNodePropertyType(TimeNodePropertyType object) {
                return createTimeNodePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeNodeType(TimeNodeType object) {
                return createTimeNodeTypeAdapter();
            }
            @Override
            public Adapter caseTimeOrdinalEraPropertyType(TimeOrdinalEraPropertyType object) {
                return createTimeOrdinalEraPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeOrdinalEraType(TimeOrdinalEraType object) {
                return createTimeOrdinalEraTypeAdapter();
            }
            @Override
            public Adapter caseTimeOrdinalReferenceSystemType(TimeOrdinalReferenceSystemType object) {
                return createTimeOrdinalReferenceSystemTypeAdapter();
            }
            @Override
            public Adapter caseTimePeriodPropertyType(TimePeriodPropertyType object) {
                return createTimePeriodPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimePeriodType(TimePeriodType object) {
                return createTimePeriodTypeAdapter();
            }
            @Override
            public Adapter caseTimePositionType(TimePositionType object) {
                return createTimePositionTypeAdapter();
            }
            @Override
            public Adapter caseTimePrimitivePropertyType(TimePrimitivePropertyType object) {
                return createTimePrimitivePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeTopologyComplexPropertyType(TimeTopologyComplexPropertyType object) {
                return createTimeTopologyComplexPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeTopologyComplexType(TimeTopologyComplexType object) {
                return createTimeTopologyComplexTypeAdapter();
            }
            @Override
            public Adapter caseTimeTopologyPrimitivePropertyType(TimeTopologyPrimitivePropertyType object) {
                return createTimeTopologyPrimitivePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTimeType(TimeType object) {
                return createTimeTypeAdapter();
            }
            @Override
            public Adapter caseTinType(TinType object) {
                return createTinTypeAdapter();
            }
            @Override
            public Adapter caseTopoComplexMemberType(TopoComplexMemberType object) {
                return createTopoComplexMemberTypeAdapter();
            }
            @Override
            public Adapter caseTopoComplexType(TopoComplexType object) {
                return createTopoComplexTypeAdapter();
            }
            @Override
            public Adapter caseTopoCurvePropertyType(TopoCurvePropertyType object) {
                return createTopoCurvePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTopoCurveType(TopoCurveType object) {
                return createTopoCurveTypeAdapter();
            }
            @Override
            public Adapter caseTopologyStylePropertyType(TopologyStylePropertyType object) {
                return createTopologyStylePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTopologyStyleType(TopologyStyleType object) {
                return createTopologyStyleTypeAdapter();
            }
            @Override
            public Adapter caseTopoPointPropertyType(TopoPointPropertyType object) {
                return createTopoPointPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTopoPointType(TopoPointType object) {
                return createTopoPointTypeAdapter();
            }
            @Override
            public Adapter caseTopoPrimitiveArrayAssociationType(TopoPrimitiveArrayAssociationType object) {
                return createTopoPrimitiveArrayAssociationTypeAdapter();
            }
            @Override
            public Adapter caseTopoPrimitiveMemberType(TopoPrimitiveMemberType object) {
                return createTopoPrimitiveMemberTypeAdapter();
            }
            @Override
            public Adapter caseTopoSolidType(TopoSolidType object) {
                return createTopoSolidTypeAdapter();
            }
            @Override
            public Adapter caseTopoSurfacePropertyType(TopoSurfacePropertyType object) {
                return createTopoSurfacePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTopoSurfaceType(TopoSurfaceType object) {
                return createTopoSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseTopoVolumePropertyType(TopoVolumePropertyType object) {
                return createTopoVolumePropertyTypeAdapter();
            }
            @Override
            public Adapter caseTopoVolumeType(TopoVolumeType object) {
                return createTopoVolumeTypeAdapter();
            }
            @Override
            public Adapter caseTrackType(TrackType object) {
                return createTrackTypeAdapter();
            }
            @Override
            public Adapter caseTransformationRefType(TransformationRefType object) {
                return createTransformationRefTypeAdapter();
            }
            @Override
            public Adapter caseTransformationType(TransformationType object) {
                return createTransformationTypeAdapter();
            }
            @Override
            public Adapter caseTrianglePatchArrayPropertyType(TrianglePatchArrayPropertyType object) {
                return createTrianglePatchArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseTriangleType(TriangleType object) {
                return createTriangleTypeAdapter();
            }
            @Override
            public Adapter caseTriangulatedSurfaceType(TriangulatedSurfaceType object) {
                return createTriangulatedSurfaceTypeAdapter();
            }
            @Override
            public Adapter caseUnitDefinitionType(UnitDefinitionType object) {
                return createUnitDefinitionTypeAdapter();
            }
            @Override
            public Adapter caseUnitOfMeasureType(UnitOfMeasureType object) {
                return createUnitOfMeasureTypeAdapter();
            }
            @Override
            public Adapter caseUserDefinedCSRefType(UserDefinedCSRefType object) {
                return createUserDefinedCSRefTypeAdapter();
            }
            @Override
            public Adapter caseUserDefinedCSType(UserDefinedCSType object) {
                return createUserDefinedCSTypeAdapter();
            }
            @Override
            public Adapter caseValueArrayPropertyType(ValueArrayPropertyType object) {
                return createValueArrayPropertyTypeAdapter();
            }
            @Override
            public Adapter caseValueArrayType(ValueArrayType object) {
                return createValueArrayTypeAdapter();
            }
            @Override
            public Adapter caseValuePropertyType(ValuePropertyType object) {
                return createValuePropertyTypeAdapter();
            }
            @Override
            public Adapter caseVectorType(VectorType object) {
                return createVectorTypeAdapter();
            }
            @Override
            public Adapter caseVerticalCRSRefType(VerticalCRSRefType object) {
                return createVerticalCRSRefTypeAdapter();
            }
            @Override
            public Adapter caseVerticalCRSType(VerticalCRSType object) {
                return createVerticalCRSTypeAdapter();
            }
            @Override
            public Adapter caseVerticalCSRefType(VerticalCSRefType object) {
                return createVerticalCSRefTypeAdapter();
            }
            @Override
            public Adapter caseVerticalCSType(VerticalCSType object) {
                return createVerticalCSTypeAdapter();
            }
            @Override
            public Adapter caseVerticalDatumRefType(VerticalDatumRefType object) {
                return createVerticalDatumRefTypeAdapter();
            }
            @Override
            public Adapter caseVerticalDatumType(VerticalDatumType object) {
                return createVerticalDatumTypeAdapter();
            }
            @Override
            public Adapter caseVerticalDatumTypeType(VerticalDatumTypeType object) {
                return createVerticalDatumTypeTypeAdapter();
            }
            @Override
            public Adapter caseVolumeType(VolumeType object) {
                return createVolumeTypeAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbsoluteExternalPositionalAccuracyType <em>Absolute External Positional Accuracy Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbsoluteExternalPositionalAccuracyType
     * @generated
     */
    public Adapter createAbsoluteExternalPositionalAccuracyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractContinuousCoverageType <em>Abstract Continuous Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractContinuousCoverageType
     * @generated
     */
    public Adapter createAbstractContinuousCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractCoordinateOperationBaseType <em>Abstract Coordinate Operation Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractCoordinateOperationBaseType
     * @generated
     */
    public Adapter createAbstractCoordinateOperationBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractCoordinateOperationType <em>Abstract Coordinate Operation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractCoordinateOperationType
     * @generated
     */
    public Adapter createAbstractCoordinateOperationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractCoordinateSystemBaseType <em>Abstract Coordinate System Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractCoordinateSystemBaseType
     * @generated
     */
    public Adapter createAbstractCoordinateSystemBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractCoordinateSystemType <em>Abstract Coordinate System Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractCoordinateSystemType
     * @generated
     */
    public Adapter createAbstractCoordinateSystemTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractCoverageType <em>Abstract Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractCoverageType
     * @generated
     */
    public Adapter createAbstractCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractCurveSegmentType <em>Abstract Curve Segment Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractCurveSegmentType
     * @generated
     */
    public Adapter createAbstractCurveSegmentTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractCurveType <em>Abstract Curve Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractCurveType
     * @generated
     */
    public Adapter createAbstractCurveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractDatumBaseType <em>Abstract Datum Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractDatumBaseType
     * @generated
     */
    public Adapter createAbstractDatumBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractDatumType <em>Abstract Datum Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractDatumType
     * @generated
     */
    public Adapter createAbstractDatumTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractDiscreteCoverageType <em>Abstract Discrete Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractDiscreteCoverageType
     * @generated
     */
    public Adapter createAbstractDiscreteCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractFeatureCollectionType <em>Abstract Feature Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractFeatureCollectionType
     * @generated
     */
    public Adapter createAbstractFeatureCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractFeatureType <em>Abstract Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractFeatureType
     * @generated
     */
    public Adapter createAbstractFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeneralConversionType <em>Abstract General Conversion Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeneralConversionType
     * @generated
     */
    public Adapter createAbstractGeneralConversionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeneralDerivedCRSType <em>Abstract General Derived CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeneralDerivedCRSType
     * @generated
     */
    public Adapter createAbstractGeneralDerivedCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeneralOperationParameterRefType <em>Abstract General Operation Parameter Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeneralOperationParameterRefType
     * @generated
     */
    public Adapter createAbstractGeneralOperationParameterRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeneralOperationParameterType <em>Abstract General Operation Parameter Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeneralOperationParameterType
     * @generated
     */
    public Adapter createAbstractGeneralOperationParameterTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeneralParameterValueType <em>Abstract General Parameter Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeneralParameterValueType
     * @generated
     */
    public Adapter createAbstractGeneralParameterValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeneralTransformationType <em>Abstract General Transformation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeneralTransformationType
     * @generated
     */
    public Adapter createAbstractGeneralTransformationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeometricAggregateType <em>Abstract Geometric Aggregate Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeometricAggregateType
     * @generated
     */
    public Adapter createAbstractGeometricAggregateTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeometricPrimitiveType <em>Abstract Geometric Primitive Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeometricPrimitiveType
     * @generated
     */
    public Adapter createAbstractGeometricPrimitiveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGeometryType <em>Abstract Geometry Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGeometryType
     * @generated
     */
    public Adapter createAbstractGeometryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGMLType <em>Abstract GML Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGMLType
     * @generated
     */
    public Adapter createAbstractGMLTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractGriddedSurfaceType <em>Abstract Gridded Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractGriddedSurfaceType
     * @generated
     */
    public Adapter createAbstractGriddedSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractMetaDataType <em>Abstract Meta Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractMetaDataType
     * @generated
     */
    public Adapter createAbstractMetaDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractParametricCurveSurfaceType <em>Abstract Parametric Curve Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractParametricCurveSurfaceType
     * @generated
     */
    public Adapter createAbstractParametricCurveSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractPositionalAccuracyType <em>Abstract Positional Accuracy Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractPositionalAccuracyType
     * @generated
     */
    public Adapter createAbstractPositionalAccuracyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractReferenceSystemBaseType <em>Abstract Reference System Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractReferenceSystemBaseType
     * @generated
     */
    public Adapter createAbstractReferenceSystemBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractReferenceSystemType <em>Abstract Reference System Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractReferenceSystemType
     * @generated
     */
    public Adapter createAbstractReferenceSystemTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractRingPropertyType <em>Abstract Ring Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractRingPropertyType
     * @generated
     */
    public Adapter createAbstractRingPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractRingType <em>Abstract Ring Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractRingType
     * @generated
     */
    public Adapter createAbstractRingTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractSolidType <em>Abstract Solid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractSolidType
     * @generated
     */
    public Adapter createAbstractSolidTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractStyleType <em>Abstract Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractStyleType
     * @generated
     */
    public Adapter createAbstractStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractSurfacePatchType <em>Abstract Surface Patch Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractSurfacePatchType
     * @generated
     */
    public Adapter createAbstractSurfacePatchTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractSurfaceType <em>Abstract Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractSurfaceType
     * @generated
     */
    public Adapter createAbstractSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTimeComplexType <em>Abstract Time Complex Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTimeComplexType
     * @generated
     */
    public Adapter createAbstractTimeComplexTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTimeGeometricPrimitiveType <em>Abstract Time Geometric Primitive Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTimeGeometricPrimitiveType
     * @generated
     */
    public Adapter createAbstractTimeGeometricPrimitiveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTimeObjectType <em>Abstract Time Object Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTimeObjectType
     * @generated
     */
    public Adapter createAbstractTimeObjectTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTimePrimitiveType <em>Abstract Time Primitive Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTimePrimitiveType
     * @generated
     */
    public Adapter createAbstractTimePrimitiveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTimeReferenceSystemType <em>Abstract Time Reference System Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTimeReferenceSystemType
     * @generated
     */
    public Adapter createAbstractTimeReferenceSystemTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTimeSliceType <em>Abstract Time Slice Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTimeSliceType
     * @generated
     */
    public Adapter createAbstractTimeSliceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTimeTopologyPrimitiveType <em>Abstract Time Topology Primitive Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTimeTopologyPrimitiveType
     * @generated
     */
    public Adapter createAbstractTimeTopologyPrimitiveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTopologyType <em>Abstract Topology Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTopologyType
     * @generated
     */
    public Adapter createAbstractTopologyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AbstractTopoPrimitiveType <em>Abstract Topo Primitive Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AbstractTopoPrimitiveType
     * @generated
     */
    public Adapter createAbstractTopoPrimitiveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AffinePlacementType <em>Affine Placement Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AffinePlacementType
     * @generated
     */
    public Adapter createAffinePlacementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AngleChoiceType <em>Angle Choice Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AngleChoiceType
     * @generated
     */
    public Adapter createAngleChoiceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AngleType <em>Angle Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AngleType
     * @generated
     */
    public Adapter createAngleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ArcByBulgeType <em>Arc By Bulge Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ArcByBulgeType
     * @generated
     */
    public Adapter createArcByBulgeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ArcByCenterPointType <em>Arc By Center Point Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ArcByCenterPointType
     * @generated
     */
    public Adapter createArcByCenterPointTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ArcStringByBulgeType <em>Arc String By Bulge Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ArcStringByBulgeType
     * @generated
     */
    public Adapter createArcStringByBulgeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ArcStringType <em>Arc String Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ArcStringType
     * @generated
     */
    public Adapter createArcStringTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ArcType <em>Arc Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ArcType
     * @generated
     */
    public Adapter createArcTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AreaType <em>Area Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AreaType
     * @generated
     */
    public Adapter createAreaTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ArrayAssociationType <em>Array Association Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ArrayAssociationType
     * @generated
     */
    public Adapter createArrayAssociationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ArrayType <em>Array Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ArrayType
     * @generated
     */
    public Adapter createArrayTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.AssociationType <em>Association Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.AssociationType
     * @generated
     */
    public Adapter createAssociationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BagType <em>Bag Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BagType
     * @generated
     */
    public Adapter createBagTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BaseStyleDescriptorType <em>Base Style Descriptor Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BaseStyleDescriptorType
     * @generated
     */
    public Adapter createBaseStyleDescriptorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BaseUnitType <em>Base Unit Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BaseUnitType
     * @generated
     */
    public Adapter createBaseUnitTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BezierType <em>Bezier Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BezierType
     * @generated
     */
    public Adapter createBezierTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BooleanPropertyType <em>Boolean Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BooleanPropertyType
     * @generated
     */
    public Adapter createBooleanPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BoundedFeatureType <em>Bounded Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BoundedFeatureType
     * @generated
     */
    public Adapter createBoundedFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BoundingShapeType <em>Bounding Shape Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BoundingShapeType
     * @generated
     */
    public Adapter createBoundingShapeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.BSplineType <em>BSpline Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.BSplineType
     * @generated
     */
    public Adapter createBSplineTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CartesianCSRefType <em>Cartesian CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CartesianCSRefType
     * @generated
     */
    public Adapter createCartesianCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CartesianCSType <em>Cartesian CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CartesianCSType
     * @generated
     */
    public Adapter createCartesianCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CategoryExtentType <em>Category Extent Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CategoryExtentType
     * @generated
     */
    public Adapter createCategoryExtentTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CategoryPropertyType <em>Category Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CategoryPropertyType
     * @generated
     */
    public Adapter createCategoryPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CircleByCenterPointType <em>Circle By Center Point Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CircleByCenterPointType
     * @generated
     */
    public Adapter createCircleByCenterPointTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CircleType <em>Circle Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CircleType
     * @generated
     */
    public Adapter createCircleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ClothoidType <em>Clothoid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ClothoidType
     * @generated
     */
    public Adapter createClothoidTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CodeListType <em>Code List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CodeListType
     * @generated
     */
    public Adapter createCodeListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CodeOrNullListType <em>Code Or Null List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CodeOrNullListType
     * @generated
     */
    public Adapter createCodeOrNullListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CodeType <em>Code Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CodeType
     * @generated
     */
    public Adapter createCodeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompositeCurvePropertyType <em>Composite Curve Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompositeCurvePropertyType
     * @generated
     */
    public Adapter createCompositeCurvePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompositeCurveType <em>Composite Curve Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompositeCurveType
     * @generated
     */
    public Adapter createCompositeCurveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompositeSolidPropertyType <em>Composite Solid Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompositeSolidPropertyType
     * @generated
     */
    public Adapter createCompositeSolidPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompositeSolidType <em>Composite Solid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompositeSolidType
     * @generated
     */
    public Adapter createCompositeSolidTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompositeSurfacePropertyType <em>Composite Surface Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompositeSurfacePropertyType
     * @generated
     */
    public Adapter createCompositeSurfacePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompositeSurfaceType <em>Composite Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompositeSurfaceType
     * @generated
     */
    public Adapter createCompositeSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompositeValueType <em>Composite Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompositeValueType
     * @generated
     */
    public Adapter createCompositeValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompoundCRSRefType <em>Compound CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompoundCRSRefType
     * @generated
     */
    public Adapter createCompoundCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CompoundCRSType <em>Compound CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CompoundCRSType
     * @generated
     */
    public Adapter createCompoundCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ConcatenatedOperationRefType <em>Concatenated Operation Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ConcatenatedOperationRefType
     * @generated
     */
    public Adapter createConcatenatedOperationRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ConcatenatedOperationType <em>Concatenated Operation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ConcatenatedOperationType
     * @generated
     */
    public Adapter createConcatenatedOperationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ConeType <em>Cone Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ConeType
     * @generated
     */
    public Adapter createConeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ContainerPropertyType <em>Container Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ContainerPropertyType
     * @generated
     */
    public Adapter createContainerPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ControlPointType <em>Control Point Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ControlPointType
     * @generated
     */
    public Adapter createControlPointTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ConventionalUnitType <em>Conventional Unit Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ConventionalUnitType
     * @generated
     */
    public Adapter createConventionalUnitTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ConversionRefType <em>Conversion Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ConversionRefType
     * @generated
     */
    public Adapter createConversionRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ConversionToPreferredUnitType <em>Conversion To Preferred Unit Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ConversionToPreferredUnitType
     * @generated
     */
    public Adapter createConversionToPreferredUnitTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ConversionType <em>Conversion Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ConversionType
     * @generated
     */
    public Adapter createConversionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordinateOperationRefType <em>Coordinate Operation Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordinateOperationRefType
     * @generated
     */
    public Adapter createCoordinateOperationRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordinateReferenceSystemRefType <em>Coordinate Reference System Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordinateReferenceSystemRefType
     * @generated
     */
    public Adapter createCoordinateReferenceSystemRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordinatesType <em>Coordinates Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordinatesType
     * @generated
     */
    public Adapter createCoordinatesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordinateSystemAxisBaseType <em>Coordinate System Axis Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordinateSystemAxisBaseType
     * @generated
     */
    public Adapter createCoordinateSystemAxisBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordinateSystemAxisRefType <em>Coordinate System Axis Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordinateSystemAxisRefType
     * @generated
     */
    public Adapter createCoordinateSystemAxisRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordinateSystemAxisType <em>Coordinate System Axis Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordinateSystemAxisType
     * @generated
     */
    public Adapter createCoordinateSystemAxisTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordinateSystemRefType <em>Coordinate System Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordinateSystemRefType
     * @generated
     */
    public Adapter createCoordinateSystemRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoordType <em>Coord Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoordType
     * @generated
     */
    public Adapter createCoordTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CountPropertyType <em>Count Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CountPropertyType
     * @generated
     */
    public Adapter createCountPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CovarianceElementType <em>Covariance Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CovarianceElementType
     * @generated
     */
    public Adapter createCovarianceElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CovarianceMatrixType <em>Covariance Matrix Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CovarianceMatrixType
     * @generated
     */
    public Adapter createCovarianceMatrixTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CoverageFunctionType <em>Coverage Function Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CoverageFunctionType
     * @generated
     */
    public Adapter createCoverageFunctionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CRSRefType <em>CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CRSRefType
     * @generated
     */
    public Adapter createCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CubicSplineType <em>Cubic Spline Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CubicSplineType
     * @generated
     */
    public Adapter createCubicSplineTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CurveArrayPropertyType <em>Curve Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CurveArrayPropertyType
     * @generated
     */
    public Adapter createCurveArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CurvePropertyType <em>Curve Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CurvePropertyType
     * @generated
     */
    public Adapter createCurvePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CurveSegmentArrayPropertyType <em>Curve Segment Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CurveSegmentArrayPropertyType
     * @generated
     */
    public Adapter createCurveSegmentArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CurveType <em>Curve Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CurveType
     * @generated
     */
    public Adapter createCurveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CylinderType <em>Cylinder Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CylinderType
     * @generated
     */
    public Adapter createCylinderTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CylindricalCSRefType <em>Cylindrical CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CylindricalCSRefType
     * @generated
     */
    public Adapter createCylindricalCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.CylindricalCSType <em>Cylindrical CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.CylindricalCSType
     * @generated
     */
    public Adapter createCylindricalCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DataBlockType <em>Data Block Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DataBlockType
     * @generated
     */
    public Adapter createDataBlockTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DatumRefType <em>Datum Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DatumRefType
     * @generated
     */
    public Adapter createDatumRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DefaultStylePropertyType <em>Default Style Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DefaultStylePropertyType
     * @generated
     */
    public Adapter createDefaultStylePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DefinitionProxyType <em>Definition Proxy Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DefinitionProxyType
     * @generated
     */
    public Adapter createDefinitionProxyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DefinitionType <em>Definition Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DefinitionType
     * @generated
     */
    public Adapter createDefinitionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DegreesType <em>Degrees Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DegreesType
     * @generated
     */
    public Adapter createDegreesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DerivationUnitTermType <em>Derivation Unit Term Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DerivationUnitTermType
     * @generated
     */
    public Adapter createDerivationUnitTermTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DerivedCRSRefType <em>Derived CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DerivedCRSRefType
     * @generated
     */
    public Adapter createDerivedCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DerivedCRSType <em>Derived CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DerivedCRSType
     * @generated
     */
    public Adapter createDerivedCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DerivedCRSTypeType <em>Derived CRS Type Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DerivedCRSTypeType
     * @generated
     */
    public Adapter createDerivedCRSTypeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DerivedUnitType <em>Derived Unit Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DerivedUnitType
     * @generated
     */
    public Adapter createDerivedUnitTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DictionaryEntryType <em>Dictionary Entry Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DictionaryEntryType
     * @generated
     */
    public Adapter createDictionaryEntryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DictionaryType <em>Dictionary Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DictionaryType
     * @generated
     */
    public Adapter createDictionaryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectedEdgePropertyType <em>Directed Edge Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectedEdgePropertyType
     * @generated
     */
    public Adapter createDirectedEdgePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectedFacePropertyType <em>Directed Face Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectedFacePropertyType
     * @generated
     */
    public Adapter createDirectedFacePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectedNodePropertyType <em>Directed Node Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectedNodePropertyType
     * @generated
     */
    public Adapter createDirectedNodePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectedObservationAtDistanceType <em>Directed Observation At Distance Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectedObservationAtDistanceType
     * @generated
     */
    public Adapter createDirectedObservationAtDistanceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectedObservationType <em>Directed Observation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectedObservationType
     * @generated
     */
    public Adapter createDirectedObservationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectedTopoSolidPropertyType <em>Directed Topo Solid Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectedTopoSolidPropertyType
     * @generated
     */
    public Adapter createDirectedTopoSolidPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectionPropertyType <em>Direction Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectionPropertyType
     * @generated
     */
    public Adapter createDirectionPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectionVectorType <em>Direction Vector Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectionVectorType
     * @generated
     */
    public Adapter createDirectionVectorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectPositionListType <em>Direct Position List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectPositionListType
     * @generated
     */
    public Adapter createDirectPositionListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DirectPositionType <em>Direct Position Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DirectPositionType
     * @generated
     */
    public Adapter createDirectPositionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DMSAngleType <em>DMS Angle Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DMSAngleType
     * @generated
     */
    public Adapter createDMSAngleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DomainSetType <em>Domain Set Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DomainSetType
     * @generated
     */
    public Adapter createDomainSetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DynamicFeatureCollectionType <em>Dynamic Feature Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DynamicFeatureCollectionType
     * @generated
     */
    public Adapter createDynamicFeatureCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.DynamicFeatureType <em>Dynamic Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.DynamicFeatureType
     * @generated
     */
    public Adapter createDynamicFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EdgeType <em>Edge Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EdgeType
     * @generated
     */
    public Adapter createEdgeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EllipsoidalCSRefType <em>Ellipsoidal CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EllipsoidalCSRefType
     * @generated
     */
    public Adapter createEllipsoidalCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EllipsoidalCSType <em>Ellipsoidal CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EllipsoidalCSType
     * @generated
     */
    public Adapter createEllipsoidalCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EllipsoidBaseType <em>Ellipsoid Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EllipsoidBaseType
     * @generated
     */
    public Adapter createEllipsoidBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EllipsoidRefType <em>Ellipsoid Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EllipsoidRefType
     * @generated
     */
    public Adapter createEllipsoidRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EllipsoidType <em>Ellipsoid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EllipsoidType
     * @generated
     */
    public Adapter createEllipsoidTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EngineeringCRSRefType <em>Engineering CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EngineeringCRSRefType
     * @generated
     */
    public Adapter createEngineeringCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EngineeringCRSType <em>Engineering CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EngineeringCRSType
     * @generated
     */
    public Adapter createEngineeringCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EngineeringDatumRefType <em>Engineering Datum Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EngineeringDatumRefType
     * @generated
     */
    public Adapter createEngineeringDatumRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EngineeringDatumType <em>Engineering Datum Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EngineeringDatumType
     * @generated
     */
    public Adapter createEngineeringDatumTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EnvelopeType <em>Envelope Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EnvelopeType
     * @generated
     */
    public Adapter createEnvelopeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.EnvelopeWithTimePeriodType <em>Envelope With Time Period Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.EnvelopeWithTimePeriodType
     * @generated
     */
    public Adapter createEnvelopeWithTimePeriodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ExtentType <em>Extent Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ExtentType
     * @generated
     */
    public Adapter createExtentTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FaceType <em>Face Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FaceType
     * @generated
     */
    public Adapter createFaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FeatureArrayPropertyType <em>Feature Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FeatureArrayPropertyType
     * @generated
     */
    public Adapter createFeatureArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FeatureCollectionType <em>Feature Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FeatureCollectionType
     * @generated
     */
    public Adapter createFeatureCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FeaturePropertyType <em>Feature Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FeaturePropertyType
     * @generated
     */
    public Adapter createFeaturePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FeatureStylePropertyType <em>Feature Style Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FeatureStylePropertyType
     * @generated
     */
    public Adapter createFeatureStylePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FeatureStyleType <em>Feature Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FeatureStyleType
     * @generated
     */
    public Adapter createFeatureStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FileType <em>File Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FileType
     * @generated
     */
    public Adapter createFileTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.FormulaType <em>Formula Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.FormulaType
     * @generated
     */
    public Adapter createFormulaTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeneralConversionRefType <em>General Conversion Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeneralConversionRefType
     * @generated
     */
    public Adapter createGeneralConversionRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeneralTransformationRefType <em>General Transformation Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeneralTransformationRefType
     * @generated
     */
    public Adapter createGeneralTransformationRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GenericMetaDataType <em>Generic Meta Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GenericMetaDataType
     * @generated
     */
    public Adapter createGenericMetaDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeocentricCRSRefType <em>Geocentric CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeocentricCRSRefType
     * @generated
     */
    public Adapter createGeocentricCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeocentricCRSType <em>Geocentric CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeocentricCRSType
     * @generated
     */
    public Adapter createGeocentricCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeodesicStringType <em>Geodesic String Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeodesicStringType
     * @generated
     */
    public Adapter createGeodesicStringTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeodesicType <em>Geodesic Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeodesicType
     * @generated
     */
    public Adapter createGeodesicTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeodeticDatumRefType <em>Geodetic Datum Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeodeticDatumRefType
     * @generated
     */
    public Adapter createGeodeticDatumRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeodeticDatumType <em>Geodetic Datum Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeodeticDatumType
     * @generated
     */
    public Adapter createGeodeticDatumTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeographicCRSRefType <em>Geographic CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeographicCRSRefType
     * @generated
     */
    public Adapter createGeographicCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeographicCRSType <em>Geographic CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeographicCRSType
     * @generated
     */
    public Adapter createGeographicCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeometricComplexPropertyType <em>Geometric Complex Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeometricComplexPropertyType
     * @generated
     */
    public Adapter createGeometricComplexPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeometricComplexType <em>Geometric Complex Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeometricComplexType
     * @generated
     */
    public Adapter createGeometricComplexTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeometricPrimitivePropertyType <em>Geometric Primitive Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeometricPrimitivePropertyType
     * @generated
     */
    public Adapter createGeometricPrimitivePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeometryArrayPropertyType <em>Geometry Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeometryArrayPropertyType
     * @generated
     */
    public Adapter createGeometryArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeometryPropertyType <em>Geometry Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeometryPropertyType
     * @generated
     */
    public Adapter createGeometryPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeometryStylePropertyType <em>Geometry Style Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeometryStylePropertyType
     * @generated
     */
    public Adapter createGeometryStylePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GeometryStyleType <em>Geometry Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GeometryStyleType
     * @generated
     */
    public Adapter createGeometryStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GraphStylePropertyType <em>Graph Style Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GraphStylePropertyType
     * @generated
     */
    public Adapter createGraphStylePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GraphStyleType <em>Graph Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GraphStyleType
     * @generated
     */
    public Adapter createGraphStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GridCoverageType <em>Grid Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GridCoverageType
     * @generated
     */
    public Adapter createGridCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GridDomainType <em>Grid Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GridDomainType
     * @generated
     */
    public Adapter createGridDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GridEnvelopeType <em>Grid Envelope Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GridEnvelopeType
     * @generated
     */
    public Adapter createGridEnvelopeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GridFunctionType <em>Grid Function Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GridFunctionType
     * @generated
     */
    public Adapter createGridFunctionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GridLengthType <em>Grid Length Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GridLengthType
     * @generated
     */
    public Adapter createGridLengthTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GridLimitsType <em>Grid Limits Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GridLimitsType
     * @generated
     */
    public Adapter createGridLimitsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.GridType <em>Grid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.GridType
     * @generated
     */
    public Adapter createGridTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.HistoryPropertyType <em>History Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.HistoryPropertyType
     * @generated
     */
    public Adapter createHistoryPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.IdentifierType <em>Identifier Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.IdentifierType
     * @generated
     */
    public Adapter createIdentifierTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ImageCRSRefType <em>Image CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ImageCRSRefType
     * @generated
     */
    public Adapter createImageCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ImageCRSType <em>Image CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ImageCRSType
     * @generated
     */
    public Adapter createImageCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ImageDatumRefType <em>Image Datum Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ImageDatumRefType
     * @generated
     */
    public Adapter createImageDatumRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ImageDatumType <em>Image Datum Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ImageDatumType
     * @generated
     */
    public Adapter createImageDatumTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.IndexMapType <em>Index Map Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.IndexMapType
     * @generated
     */
    public Adapter createIndexMapTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.IndirectEntryType <em>Indirect Entry Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.IndirectEntryType
     * @generated
     */
    public Adapter createIndirectEntryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.IsolatedPropertyType <em>Isolated Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.IsolatedPropertyType
     * @generated
     */
    public Adapter createIsolatedPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.KnotPropertyType <em>Knot Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.KnotPropertyType
     * @generated
     */
    public Adapter createKnotPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.KnotType <em>Knot Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.KnotType
     * @generated
     */
    public Adapter createKnotTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LabelStylePropertyType <em>Label Style Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LabelStylePropertyType
     * @generated
     */
    public Adapter createLabelStylePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LabelStyleType <em>Label Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LabelStyleType
     * @generated
     */
    public Adapter createLabelStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LabelType <em>Label Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LabelType
     * @generated
     */
    public Adapter createLabelTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LengthType <em>Length Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LengthType
     * @generated
     */
    public Adapter createLengthTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LinearCSRefType <em>Linear CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LinearCSRefType
     * @generated
     */
    public Adapter createLinearCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LinearCSType <em>Linear CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LinearCSType
     * @generated
     */
    public Adapter createLinearCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LinearRingPropertyType <em>Linear Ring Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LinearRingPropertyType
     * @generated
     */
    public Adapter createLinearRingPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LinearRingType <em>Linear Ring Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LinearRingType
     * @generated
     */
    public Adapter createLinearRingTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LineStringPropertyType <em>Line String Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LineStringPropertyType
     * @generated
     */
    public Adapter createLineStringPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LineStringSegmentArrayPropertyType <em>Line String Segment Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LineStringSegmentArrayPropertyType
     * @generated
     */
    public Adapter createLineStringSegmentArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LineStringSegmentType <em>Line String Segment Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LineStringSegmentType
     * @generated
     */
    public Adapter createLineStringSegmentTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LineStringType <em>Line String Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LineStringType
     * @generated
     */
    public Adapter createLineStringTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.LocationPropertyType <em>Location Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.LocationPropertyType
     * @generated
     */
    public Adapter createLocationPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MeasureListType <em>Measure List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MeasureListType
     * @generated
     */
    public Adapter createMeasureListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MeasureOrNullListType <em>Measure Or Null List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MeasureOrNullListType
     * @generated
     */
    public Adapter createMeasureOrNullListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MeasureType <em>Measure Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MeasureType
     * @generated
     */
    public Adapter createMeasureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MetaDataPropertyType <em>Meta Data Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MetaDataPropertyType
     * @generated
     */
    public Adapter createMetaDataPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MovingObjectStatusType <em>Moving Object Status Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MovingObjectStatusType
     * @generated
     */
    public Adapter createMovingObjectStatusTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiCurveCoverageType <em>Multi Curve Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiCurveCoverageType
     * @generated
     */
    public Adapter createMultiCurveCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiCurveDomainType <em>Multi Curve Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiCurveDomainType
     * @generated
     */
    public Adapter createMultiCurveDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiCurvePropertyType <em>Multi Curve Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiCurvePropertyType
     * @generated
     */
    public Adapter createMultiCurvePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiCurveType <em>Multi Curve Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiCurveType
     * @generated
     */
    public Adapter createMultiCurveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiGeometryPropertyType <em>Multi Geometry Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiGeometryPropertyType
     * @generated
     */
    public Adapter createMultiGeometryPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiGeometryType <em>Multi Geometry Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiGeometryType
     * @generated
     */
    public Adapter createMultiGeometryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiLineStringPropertyType <em>Multi Line String Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiLineStringPropertyType
     * @generated
     */
    public Adapter createMultiLineStringPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiLineStringType <em>Multi Line String Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiLineStringType
     * @generated
     */
    public Adapter createMultiLineStringTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiPointCoverageType <em>Multi Point Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiPointCoverageType
     * @generated
     */
    public Adapter createMultiPointCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiPointDomainType <em>Multi Point Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiPointDomainType
     * @generated
     */
    public Adapter createMultiPointDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiPointPropertyType <em>Multi Point Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiPointPropertyType
     * @generated
     */
    public Adapter createMultiPointPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiPointType <em>Multi Point Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiPointType
     * @generated
     */
    public Adapter createMultiPointTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiPolygonPropertyType <em>Multi Polygon Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiPolygonPropertyType
     * @generated
     */
    public Adapter createMultiPolygonPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiPolygonType <em>Multi Polygon Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiPolygonType
     * @generated
     */
    public Adapter createMultiPolygonTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSolidCoverageType <em>Multi Solid Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSolidCoverageType
     * @generated
     */
    public Adapter createMultiSolidCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSolidDomainType <em>Multi Solid Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSolidDomainType
     * @generated
     */
    public Adapter createMultiSolidDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSolidPropertyType <em>Multi Solid Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSolidPropertyType
     * @generated
     */
    public Adapter createMultiSolidPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSolidType <em>Multi Solid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSolidType
     * @generated
     */
    public Adapter createMultiSolidTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSurfaceCoverageType <em>Multi Surface Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSurfaceCoverageType
     * @generated
     */
    public Adapter createMultiSurfaceCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSurfaceDomainType <em>Multi Surface Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSurfaceDomainType
     * @generated
     */
    public Adapter createMultiSurfaceDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSurfacePropertyType <em>Multi Surface Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSurfacePropertyType
     * @generated
     */
    public Adapter createMultiSurfacePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.MultiSurfaceType <em>Multi Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.MultiSurfaceType
     * @generated
     */
    public Adapter createMultiSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.NodeType <em>Node Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.NodeType
     * @generated
     */
    public Adapter createNodeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ObliqueCartesianCSRefType <em>Oblique Cartesian CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ObliqueCartesianCSRefType
     * @generated
     */
    public Adapter createObliqueCartesianCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ObliqueCartesianCSType <em>Oblique Cartesian CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ObliqueCartesianCSType
     * @generated
     */
    public Adapter createObliqueCartesianCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ObservationType <em>Observation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ObservationType
     * @generated
     */
    public Adapter createObservationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OffsetCurveType <em>Offset Curve Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OffsetCurveType
     * @generated
     */
    public Adapter createOffsetCurveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationMethodBaseType <em>Operation Method Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationMethodBaseType
     * @generated
     */
    public Adapter createOperationMethodBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationMethodRefType <em>Operation Method Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationMethodRefType
     * @generated
     */
    public Adapter createOperationMethodRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationMethodType <em>Operation Method Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationMethodType
     * @generated
     */
    public Adapter createOperationMethodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationParameterBaseType <em>Operation Parameter Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationParameterBaseType
     * @generated
     */
    public Adapter createOperationParameterBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationParameterGroupBaseType <em>Operation Parameter Group Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationParameterGroupBaseType
     * @generated
     */
    public Adapter createOperationParameterGroupBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationParameterGroupRefType <em>Operation Parameter Group Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationParameterGroupRefType
     * @generated
     */
    public Adapter createOperationParameterGroupRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationParameterGroupType <em>Operation Parameter Group Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationParameterGroupType
     * @generated
     */
    public Adapter createOperationParameterGroupTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationParameterRefType <em>Operation Parameter Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationParameterRefType
     * @generated
     */
    public Adapter createOperationParameterRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationParameterType <em>Operation Parameter Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationParameterType
     * @generated
     */
    public Adapter createOperationParameterTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OperationRefType <em>Operation Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OperationRefType
     * @generated
     */
    public Adapter createOperationRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OrientableCurveType <em>Orientable Curve Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OrientableCurveType
     * @generated
     */
    public Adapter createOrientableCurveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.OrientableSurfaceType <em>Orientable Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.OrientableSurfaceType
     * @generated
     */
    public Adapter createOrientableSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ParameterValueGroupType <em>Parameter Value Group Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ParameterValueGroupType
     * @generated
     */
    public Adapter createParameterValueGroupTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ParameterValueType <em>Parameter Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ParameterValueType
     * @generated
     */
    public Adapter createParameterValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PassThroughOperationRefType <em>Pass Through Operation Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PassThroughOperationRefType
     * @generated
     */
    public Adapter createPassThroughOperationRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PassThroughOperationType <em>Pass Through Operation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PassThroughOperationType
     * @generated
     */
    public Adapter createPassThroughOperationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PixelInCellType <em>Pixel In Cell Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PixelInCellType
     * @generated
     */
    public Adapter createPixelInCellTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PointArrayPropertyType <em>Point Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PointArrayPropertyType
     * @generated
     */
    public Adapter createPointArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PointPropertyType <em>Point Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PointPropertyType
     * @generated
     */
    public Adapter createPointPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PointType <em>Point Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PointType
     * @generated
     */
    public Adapter createPointTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PolarCSRefType <em>Polar CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PolarCSRefType
     * @generated
     */
    public Adapter createPolarCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PolarCSType <em>Polar CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PolarCSType
     * @generated
     */
    public Adapter createPolarCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PolygonPatchArrayPropertyType <em>Polygon Patch Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PolygonPatchArrayPropertyType
     * @generated
     */
    public Adapter createPolygonPatchArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PolygonPatchType <em>Polygon Patch Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PolygonPatchType
     * @generated
     */
    public Adapter createPolygonPatchTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PolygonPropertyType <em>Polygon Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PolygonPropertyType
     * @generated
     */
    public Adapter createPolygonPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PolygonType <em>Polygon Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PolygonType
     * @generated
     */
    public Adapter createPolygonTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PolyhedralSurfaceType <em>Polyhedral Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PolyhedralSurfaceType
     * @generated
     */
    public Adapter createPolyhedralSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PrimeMeridianBaseType <em>Prime Meridian Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PrimeMeridianBaseType
     * @generated
     */
    public Adapter createPrimeMeridianBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PrimeMeridianRefType <em>Prime Meridian Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PrimeMeridianRefType
     * @generated
     */
    public Adapter createPrimeMeridianRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PrimeMeridianType <em>Prime Meridian Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PrimeMeridianType
     * @generated
     */
    public Adapter createPrimeMeridianTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.PriorityLocationPropertyType <em>Priority Location Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.PriorityLocationPropertyType
     * @generated
     */
    public Adapter createPriorityLocationPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ProjectedCRSRefType <em>Projected CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ProjectedCRSRefType
     * @generated
     */
    public Adapter createProjectedCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ProjectedCRSType <em>Projected CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ProjectedCRSType
     * @generated
     */
    public Adapter createProjectedCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.QuantityExtentType <em>Quantity Extent Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.QuantityExtentType
     * @generated
     */
    public Adapter createQuantityExtentTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.QuantityPropertyType <em>Quantity Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.QuantityPropertyType
     * @generated
     */
    public Adapter createQuantityPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RangeParametersType <em>Range Parameters Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RangeParametersType
     * @generated
     */
    public Adapter createRangeParametersTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RangeSetType <em>Range Set Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RangeSetType
     * @generated
     */
    public Adapter createRangeSetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RectangleType <em>Rectangle Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RectangleType
     * @generated
     */
    public Adapter createRectangleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RectifiedGridCoverageType <em>Rectified Grid Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RectifiedGridCoverageType
     * @generated
     */
    public Adapter createRectifiedGridCoverageTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RectifiedGridDomainType <em>Rectified Grid Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RectifiedGridDomainType
     * @generated
     */
    public Adapter createRectifiedGridDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RectifiedGridType <em>Rectified Grid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RectifiedGridType
     * @generated
     */
    public Adapter createRectifiedGridTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ReferenceSystemRefType <em>Reference System Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ReferenceSystemRefType
     * @generated
     */
    public Adapter createReferenceSystemRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ReferenceType <em>Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ReferenceType
     * @generated
     */
    public Adapter createReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RefLocationType <em>Ref Location Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RefLocationType
     * @generated
     */
    public Adapter createRefLocationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RelatedTimeType <em>Related Time Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RelatedTimeType
     * @generated
     */
    public Adapter createRelatedTimeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RelativeInternalPositionalAccuracyType <em>Relative Internal Positional Accuracy Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RelativeInternalPositionalAccuracyType
     * @generated
     */
    public Adapter createRelativeInternalPositionalAccuracyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RingPropertyType <em>Ring Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RingPropertyType
     * @generated
     */
    public Adapter createRingPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RingType <em>Ring Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RingType
     * @generated
     */
    public Adapter createRingTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.RowType <em>Row Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.RowType
     * @generated
     */
    public Adapter createRowTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ScalarValuePropertyType <em>Scalar Value Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ScalarValuePropertyType
     * @generated
     */
    public Adapter createScalarValuePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ScaleType <em>Scale Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ScaleType
     * @generated
     */
    public Adapter createScaleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SecondDefiningParameterType <em>Second Defining Parameter Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SecondDefiningParameterType
     * @generated
     */
    public Adapter createSecondDefiningParameterTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SequenceRuleType <em>Sequence Rule Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SequenceRuleType
     * @generated
     */
    public Adapter createSequenceRuleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SingleOperationRefType <em>Single Operation Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SingleOperationRefType
     * @generated
     */
    public Adapter createSingleOperationRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SolidArrayPropertyType <em>Solid Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SolidArrayPropertyType
     * @generated
     */
    public Adapter createSolidArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SolidPropertyType <em>Solid Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SolidPropertyType
     * @generated
     */
    public Adapter createSolidPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SolidType <em>Solid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SolidType
     * @generated
     */
    public Adapter createSolidTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SpeedType <em>Speed Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SpeedType
     * @generated
     */
    public Adapter createSpeedTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SphereType <em>Sphere Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SphereType
     * @generated
     */
    public Adapter createSphereTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SphericalCSRefType <em>Spherical CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SphericalCSRefType
     * @generated
     */
    public Adapter createSphericalCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SphericalCSType <em>Spherical CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SphericalCSType
     * @generated
     */
    public Adapter createSphericalCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.StringOrRefType <em>String Or Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.StringOrRefType
     * @generated
     */
    public Adapter createStringOrRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.StyleType <em>Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.StyleType
     * @generated
     */
    public Adapter createStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.StyleVariationType <em>Style Variation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.StyleVariationType
     * @generated
     */
    public Adapter createStyleVariationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SurfaceArrayPropertyType <em>Surface Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SurfaceArrayPropertyType
     * @generated
     */
    public Adapter createSurfaceArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SurfacePatchArrayPropertyType <em>Surface Patch Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SurfacePatchArrayPropertyType
     * @generated
     */
    public Adapter createSurfacePatchArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SurfacePropertyType <em>Surface Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SurfacePropertyType
     * @generated
     */
    public Adapter createSurfacePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SurfaceType <em>Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SurfaceType
     * @generated
     */
    public Adapter createSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.SymbolType <em>Symbol Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.SymbolType
     * @generated
     */
    public Adapter createSymbolTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TargetPropertyType <em>Target Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TargetPropertyType
     * @generated
     */
    public Adapter createTargetPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TemporalCRSRefType <em>Temporal CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TemporalCRSRefType
     * @generated
     */
    public Adapter createTemporalCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TemporalCRSType <em>Temporal CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TemporalCRSType
     * @generated
     */
    public Adapter createTemporalCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TemporalCSRefType <em>Temporal CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TemporalCSRefType
     * @generated
     */
    public Adapter createTemporalCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TemporalCSType <em>Temporal CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TemporalCSType
     * @generated
     */
    public Adapter createTemporalCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TemporalDatumBaseType <em>Temporal Datum Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TemporalDatumBaseType
     * @generated
     */
    public Adapter createTemporalDatumBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TemporalDatumRefType <em>Temporal Datum Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TemporalDatumRefType
     * @generated
     */
    public Adapter createTemporalDatumRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TemporalDatumType <em>Temporal Datum Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TemporalDatumType
     * @generated
     */
    public Adapter createTemporalDatumTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeCalendarEraPropertyType <em>Time Calendar Era Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeCalendarEraPropertyType
     * @generated
     */
    public Adapter createTimeCalendarEraPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeCalendarEraType <em>Time Calendar Era Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeCalendarEraType
     * @generated
     */
    public Adapter createTimeCalendarEraTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeCalendarPropertyType <em>Time Calendar Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeCalendarPropertyType
     * @generated
     */
    public Adapter createTimeCalendarPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeCalendarType <em>Time Calendar Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeCalendarType
     * @generated
     */
    public Adapter createTimeCalendarTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeClockPropertyType <em>Time Clock Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeClockPropertyType
     * @generated
     */
    public Adapter createTimeClockPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeClockType <em>Time Clock Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeClockType
     * @generated
     */
    public Adapter createTimeClockTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeCoordinateSystemType <em>Time Coordinate System Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeCoordinateSystemType
     * @generated
     */
    public Adapter createTimeCoordinateSystemTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeEdgePropertyType <em>Time Edge Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeEdgePropertyType
     * @generated
     */
    public Adapter createTimeEdgePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeEdgeType <em>Time Edge Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeEdgeType
     * @generated
     */
    public Adapter createTimeEdgeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeGeometricPrimitivePropertyType <em>Time Geometric Primitive Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeGeometricPrimitivePropertyType
     * @generated
     */
    public Adapter createTimeGeometricPrimitivePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeInstantPropertyType <em>Time Instant Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeInstantPropertyType
     * @generated
     */
    public Adapter createTimeInstantPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeInstantType <em>Time Instant Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeInstantType
     * @generated
     */
    public Adapter createTimeInstantTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeIntervalLengthType <em>Time Interval Length Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeIntervalLengthType
     * @generated
     */
    public Adapter createTimeIntervalLengthTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeNodePropertyType <em>Time Node Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeNodePropertyType
     * @generated
     */
    public Adapter createTimeNodePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeNodeType <em>Time Node Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeNodeType
     * @generated
     */
    public Adapter createTimeNodeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeOrdinalEraPropertyType <em>Time Ordinal Era Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeOrdinalEraPropertyType
     * @generated
     */
    public Adapter createTimeOrdinalEraPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeOrdinalEraType <em>Time Ordinal Era Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeOrdinalEraType
     * @generated
     */
    public Adapter createTimeOrdinalEraTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeOrdinalReferenceSystemType <em>Time Ordinal Reference System Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeOrdinalReferenceSystemType
     * @generated
     */
    public Adapter createTimeOrdinalReferenceSystemTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimePeriodPropertyType <em>Time Period Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimePeriodPropertyType
     * @generated
     */
    public Adapter createTimePeriodPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimePeriodType <em>Time Period Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimePeriodType
     * @generated
     */
    public Adapter createTimePeriodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimePositionType <em>Time Position Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimePositionType
     * @generated
     */
    public Adapter createTimePositionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimePrimitivePropertyType <em>Time Primitive Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimePrimitivePropertyType
     * @generated
     */
    public Adapter createTimePrimitivePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeTopologyComplexPropertyType <em>Time Topology Complex Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeTopologyComplexPropertyType
     * @generated
     */
    public Adapter createTimeTopologyComplexPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeTopologyComplexType <em>Time Topology Complex Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeTopologyComplexType
     * @generated
     */
    public Adapter createTimeTopologyComplexTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeTopologyPrimitivePropertyType <em>Time Topology Primitive Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeTopologyPrimitivePropertyType
     * @generated
     */
    public Adapter createTimeTopologyPrimitivePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TimeType <em>Time Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TimeType
     * @generated
     */
    public Adapter createTimeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TinType <em>Tin Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TinType
     * @generated
     */
    public Adapter createTinTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoComplexMemberType <em>Topo Complex Member Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoComplexMemberType
     * @generated
     */
    public Adapter createTopoComplexMemberTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoComplexType <em>Topo Complex Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoComplexType
     * @generated
     */
    public Adapter createTopoComplexTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoCurvePropertyType <em>Topo Curve Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoCurvePropertyType
     * @generated
     */
    public Adapter createTopoCurvePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoCurveType <em>Topo Curve Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoCurveType
     * @generated
     */
    public Adapter createTopoCurveTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopologyStylePropertyType <em>Topology Style Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopologyStylePropertyType
     * @generated
     */
    public Adapter createTopologyStylePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopologyStyleType <em>Topology Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopologyStyleType
     * @generated
     */
    public Adapter createTopologyStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoPointPropertyType <em>Topo Point Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoPointPropertyType
     * @generated
     */
    public Adapter createTopoPointPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoPointType <em>Topo Point Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoPointType
     * @generated
     */
    public Adapter createTopoPointTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoPrimitiveArrayAssociationType <em>Topo Primitive Array Association Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoPrimitiveArrayAssociationType
     * @generated
     */
    public Adapter createTopoPrimitiveArrayAssociationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoPrimitiveMemberType <em>Topo Primitive Member Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoPrimitiveMemberType
     * @generated
     */
    public Adapter createTopoPrimitiveMemberTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoSolidType <em>Topo Solid Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoSolidType
     * @generated
     */
    public Adapter createTopoSolidTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoSurfacePropertyType <em>Topo Surface Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoSurfacePropertyType
     * @generated
     */
    public Adapter createTopoSurfacePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoSurfaceType <em>Topo Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoSurfaceType
     * @generated
     */
    public Adapter createTopoSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoVolumePropertyType <em>Topo Volume Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoVolumePropertyType
     * @generated
     */
    public Adapter createTopoVolumePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TopoVolumeType <em>Topo Volume Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TopoVolumeType
     * @generated
     */
    public Adapter createTopoVolumeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TrackType <em>Track Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TrackType
     * @generated
     */
    public Adapter createTrackTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TransformationRefType <em>Transformation Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TransformationRefType
     * @generated
     */
    public Adapter createTransformationRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TransformationType <em>Transformation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TransformationType
     * @generated
     */
    public Adapter createTransformationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TrianglePatchArrayPropertyType <em>Triangle Patch Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TrianglePatchArrayPropertyType
     * @generated
     */
    public Adapter createTrianglePatchArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TriangleType <em>Triangle Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TriangleType
     * @generated
     */
    public Adapter createTriangleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.TriangulatedSurfaceType <em>Triangulated Surface Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.TriangulatedSurfaceType
     * @generated
     */
    public Adapter createTriangulatedSurfaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.UnitDefinitionType <em>Unit Definition Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.UnitDefinitionType
     * @generated
     */
    public Adapter createUnitDefinitionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.UnitOfMeasureType <em>Unit Of Measure Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.UnitOfMeasureType
     * @generated
     */
    public Adapter createUnitOfMeasureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.UserDefinedCSRefType <em>User Defined CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.UserDefinedCSRefType
     * @generated
     */
    public Adapter createUserDefinedCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.UserDefinedCSType <em>User Defined CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.UserDefinedCSType
     * @generated
     */
    public Adapter createUserDefinedCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ValueArrayPropertyType <em>Value Array Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ValueArrayPropertyType
     * @generated
     */
    public Adapter createValueArrayPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ValueArrayType <em>Value Array Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ValueArrayType
     * @generated
     */
    public Adapter createValueArrayTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.ValuePropertyType <em>Value Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.ValuePropertyType
     * @generated
     */
    public Adapter createValuePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VectorType <em>Vector Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VectorType
     * @generated
     */
    public Adapter createVectorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VerticalCRSRefType <em>Vertical CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VerticalCRSRefType
     * @generated
     */
    public Adapter createVerticalCRSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VerticalCRSType <em>Vertical CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VerticalCRSType
     * @generated
     */
    public Adapter createVerticalCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VerticalCSRefType <em>Vertical CS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VerticalCSRefType
     * @generated
     */
    public Adapter createVerticalCSRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VerticalCSType <em>Vertical CS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VerticalCSType
     * @generated
     */
    public Adapter createVerticalCSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VerticalDatumRefType <em>Vertical Datum Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VerticalDatumRefType
     * @generated
     */
    public Adapter createVerticalDatumRefTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VerticalDatumType <em>Vertical Datum Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VerticalDatumType
     * @generated
     */
    public Adapter createVerticalDatumTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VerticalDatumTypeType <em>Vertical Datum Type Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VerticalDatumTypeType
     * @generated
     */
    public Adapter createVerticalDatumTypeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.gml311.VolumeType <em>Volume Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.gml311.VolumeType
     * @generated
     */
    public Adapter createVolumeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //Gml311AdapterFactory
