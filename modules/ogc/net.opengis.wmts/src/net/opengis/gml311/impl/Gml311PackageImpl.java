/**
 */
package net.opengis.gml311.impl;

import java.io.IOException;
import java.net.URL;
import net.opengis.gml311.Gml311Factory;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.util.Gml311Validator;

import net.opengis.ows11.Ows11Package;

import net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl;

import net.opengis.wmts.v_1.wmtsv_1Package;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.w3._2001.smil20.Smil20Package;

import org.w3._2001.smil20.impl.Smil20PackageImpl;

import org.w3._2001.smil20.language.LanguagePackage;

import org.w3._2001.smil20.language.impl.LanguagePackageImpl;

import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Gml311PackageImpl extends EPackageImpl implements Gml311Package {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected String packageFilename = "gml311.ecore";

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass absoluteExternalPositionalAccuracyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractContinuousCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractCoordinateOperationBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractCoordinateOperationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractCoordinateSystemBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractCoordinateSystemTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractCurveSegmentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractCurveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractDatumBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractDatumTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractDiscreteCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractFeatureCollectionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeneralConversionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeneralDerivedCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeneralOperationParameterRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeneralOperationParameterTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeneralParameterValueTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeneralTransformationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeometricAggregateTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeometricPrimitiveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGeometryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGMLTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractGriddedSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractMetaDataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractParametricCurveSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractPositionalAccuracyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractReferenceSystemBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractReferenceSystemTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractRingPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractRingTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractSolidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractStyleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractSurfacePatchTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTimeComplexTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTimeGeometricPrimitiveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTimeObjectTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTimePrimitiveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTimeReferenceSystemTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTimeSliceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTimeTopologyPrimitiveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTopologyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTopoPrimitiveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass affinePlacementTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass angleChoiceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass angleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arcByBulgeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arcByCenterPointTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arcStringByBulgeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arcStringTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arcTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass areaTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arrayAssociationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arrayTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass associationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass bagTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass baseStyleDescriptorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass baseUnitTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass bezierTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass booleanPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass boundedFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass boundingShapeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass bSplineTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cartesianCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cartesianCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass categoryExtentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass categoryPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass circleByCenterPointTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass circleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass clothoidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass codeListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass codeOrNullListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass codeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositeCurvePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositeCurveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositeSolidPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositeSolidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositeSurfacePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositeSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compositeValueTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compoundCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass compoundCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass concatenatedOperationRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass concatenatedOperationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coneTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass containerPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass controlPointTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conventionalUnitTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conversionRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conversionToPreferredUnitTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conversionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordinateOperationRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordinateReferenceSystemRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordinatesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordinateSystemAxisBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordinateSystemAxisRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordinateSystemAxisTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordinateSystemRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coordTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass countPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass covarianceElementTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass covarianceMatrixTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageFunctionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass crsRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cubicSplineTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass curveArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass curvePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass curveSegmentArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass curveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cylinderTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cylindricalCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cylindricalCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dataBlockTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass datumRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass defaultStylePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass definitionProxyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass definitionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass degreesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass derivationUnitTermTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass derivedCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass derivedCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass derivedCRSTypeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass derivedUnitTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dictionaryEntryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dictionaryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directedEdgePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directedFacePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directedNodePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directedObservationAtDistanceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directedObservationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directedTopoSolidPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directionPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directionVectorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directPositionListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass directPositionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dmsAngleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass documentRootEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass domainSetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dynamicFeatureCollectionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dynamicFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass edgeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ellipsoidalCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ellipsoidalCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ellipsoidBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ellipsoidRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ellipsoidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass engineeringCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass engineeringCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass engineeringDatumRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass engineeringDatumTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass envelopeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass envelopeWithTimePeriodTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass extentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass faceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureCollectionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featurePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureStylePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureStyleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass fileTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass formulaTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass generalConversionRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass generalTransformationRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass genericMetaDataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geocentricCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geocentricCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geodesicStringTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geodesicTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geodeticDatumRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geodeticDatumTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geographicCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geographicCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometricComplexPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometricComplexTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometricPrimitivePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometryArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometryPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometryStylePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometryStyleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass graphStylePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass graphStyleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridEnvelopeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridFunctionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridLengthTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridLimitsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass historyPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass identifierTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass imageCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass imageCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass imageDatumRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass imageDatumTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass indexMapTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass indirectEntryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass isolatedPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass knotPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass knotTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass labelStylePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass labelStyleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass labelTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lengthTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass linearCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass linearCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass linearRingPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass linearRingTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lineStringPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lineStringSegmentArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lineStringSegmentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lineStringTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass locationPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass measureListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass measureOrNullListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass measureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metaDataPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass movingObjectStatusTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiCurveCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiCurveDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiCurvePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiCurveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiGeometryPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiGeometryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiLineStringPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiLineStringTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiPointCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiPointDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiPointPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiPointTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiPolygonPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiPolygonTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSolidCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSolidDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSolidPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSolidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSurfaceCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSurfaceDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSurfacePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nodeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass obliqueCartesianCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass obliqueCartesianCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass observationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass offsetCurveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationMethodBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationMethodRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationMethodTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationParameterBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationParameterGroupBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationParameterGroupRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationParameterGroupTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationParameterRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationParameterTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass orientableCurveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass orientableSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parameterValueGroupTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parameterValueTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass passThroughOperationRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass passThroughOperationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass pixelInCellTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass pointArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass pointPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass pointTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass polarCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass polarCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass polygonPatchArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass polygonPatchTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass polygonPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass polygonTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass polyhedralSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass primeMeridianBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass primeMeridianRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass primeMeridianTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass priorityLocationPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass projectedCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass projectedCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass quantityExtentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass quantityPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rangeParametersTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rangeSetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rectangleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rectifiedGridCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rectifiedGridDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rectifiedGridTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass referenceSystemRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass referenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass refLocationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass relatedTimeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass relativeInternalPositionalAccuracyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ringPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ringTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rowTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass scalarValuePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass scaleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass secondDefiningParameterTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sequenceRuleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass singleOperationRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass solidArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass solidPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass solidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass speedTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sphereTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sphericalCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sphericalCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stringOrRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass styleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass styleVariationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass surfaceArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass surfacePatchArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass surfacePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass surfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass symbolTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass targetPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalDatumBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalDatumRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalDatumTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeCalendarEraPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeCalendarEraTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeCalendarPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeCalendarTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeClockPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeClockTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeCoordinateSystemTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeEdgePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeEdgeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeGeometricPrimitivePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeInstantPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeInstantTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeIntervalLengthTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeNodePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeNodeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeOrdinalEraPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeOrdinalEraTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeOrdinalReferenceSystemTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timePeriodPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timePeriodTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timePositionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timePrimitivePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeTopologyComplexPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeTopologyComplexTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeTopologyPrimitivePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tinTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoComplexMemberTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoComplexTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoCurvePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoCurveTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topologyStylePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topologyStyleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoPointPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoPointTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoPrimitiveArrayAssociationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoPrimitiveMemberTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoSolidTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoSurfacePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoVolumePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass topoVolumeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass trackTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass transformationRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass transformationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass trianglePatchArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass triangleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass triangulatedSurfaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unitDefinitionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unitOfMeasureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass userDefinedCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass userDefinedCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valueArrayPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valueArrayTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valuePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass vectorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verticalCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verticalCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verticalCSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verticalCSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verticalDatumRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verticalDatumTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verticalDatumTypeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass volumeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum aesheticCriteriaTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum compassPointEnumerationEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum curveInterpolationTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum directionTypeMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum drawingTypeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum fileValueModelTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum graphTypeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum incrementOrderEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum isSphereTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum knotTypesTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum lineTypeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum nullEnumerationMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum queryGrammarEnumerationEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum relativePositionTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum sequenceRuleNamesEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum signTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum successionTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum surfaceInterpolationTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum symbolTypeEnumerationEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum timeIndeterminateValueTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum timeUnitTypeMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType aesheticCriteriaTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType arcMinutesTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType arcSecondsTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType booleanListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType booleanOrNullEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType booleanOrNullListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType calDateEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType compassPointEnumerationObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType countExtentTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType curveInterpolationTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType decimalMinutesTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType degreeValueTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType directionTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType directionTypeMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType directionTypeMember1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType doubleListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType doubleOrNullEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType doubleOrNullListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType drawingTypeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType fileValueModelTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType graphTypeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType incrementOrderObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType integerListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType integerOrNullEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType integerOrNullListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType isSphereTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType knotTypesTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType lineTypeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nameListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nameOrNullEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nameOrNullListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType ncNameListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nullEnumerationEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nullEnumerationMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nullEnumerationMember1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nullTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType qNameListEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType queryGrammarEnumerationObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType relativePositionTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType sequenceRuleNamesObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType signTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType stringOrNullEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType successionTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType surfaceInterpolationTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType symbolTypeEnumerationObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType timeIndeterminateValueTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType timePositionUnionEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType timeUnitTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType timeUnitTypeMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType timeUnitTypeMember1EDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see net.opengis.gml311.Gml311Package#eNS_URI
     * @see #init()
     * @generated
     */
    private Gml311PackageImpl() {
        super(eNS_URI, Gml311Factory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link Gml311Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @generated
     */
    public static Gml311Package init() {
        if (isInited) return (Gml311Package)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI);

        // Obtain or create and register package
        Gml311PackageImpl theGml311Package = (Gml311PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Gml311PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Gml311PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows11Package.eINSTANCE.eClass();
        XlinkPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Smil20PackageImpl theSmil20Package = (Smil20PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI) instanceof Smil20PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI) : Smil20Package.eINSTANCE);
        LanguagePackageImpl theLanguagePackage = (LanguagePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI) instanceof LanguagePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI) : LanguagePackage.eINSTANCE);
        wmtsv_1PackageImpl thewmtsv_1Package = (wmtsv_1PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(wmtsv_1Package.eNS_URI) instanceof wmtsv_1PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(wmtsv_1Package.eNS_URI) : wmtsv_1Package.eINSTANCE);

        // Load packages
        theGml311Package.loadPackage();

        // Create package meta-data objects
        theSmil20Package.createPackageContents();
        theLanguagePackage.createPackageContents();
        thewmtsv_1Package.createPackageContents();

        // Initialize created meta-data
        theSmil20Package.initializePackageContents();
        theLanguagePackage.initializePackageContents();
        thewmtsv_1Package.initializePackageContents();

        // Fix loaded packages
        theGml311Package.fixPackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theGml311Package, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return Gml311Validator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theGml311Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Gml311Package.eNS_URI, theGml311Package);
        return theGml311Package;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbsoluteExternalPositionalAccuracyType() {
        if (absoluteExternalPositionalAccuracyTypeEClass == null) {
            absoluteExternalPositionalAccuracyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(0);
        }
        return absoluteExternalPositionalAccuracyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbsoluteExternalPositionalAccuracyType_Result() {
        return (EReference)getAbsoluteExternalPositionalAccuracyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractContinuousCoverageType() {
        if (abstractContinuousCoverageTypeEClass == null) {
            abstractContinuousCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(1);
        }
        return abstractContinuousCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractContinuousCoverageType_CoverageFunction() {
        return (EReference)getAbstractContinuousCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractCoordinateOperationBaseType() {
        if (abstractCoordinateOperationBaseTypeEClass == null) {
            abstractCoordinateOperationBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(2);
        }
        return abstractCoordinateOperationBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateOperationBaseType_CoordinateOperationName() {
        return (EReference)getAbstractCoordinateOperationBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractCoordinateOperationType() {
        if (abstractCoordinateOperationTypeEClass == null) {
            abstractCoordinateOperationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(3);
        }
        return abstractCoordinateOperationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateOperationType_CoordinateOperationID() {
        return (EReference)getAbstractCoordinateOperationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateOperationType_Remarks() {
        return (EReference)getAbstractCoordinateOperationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCoordinateOperationType_OperationVersion() {
        return (EAttribute)getAbstractCoordinateOperationType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateOperationType_ValidArea() {
        return (EReference)getAbstractCoordinateOperationType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCoordinateOperationType_Scope() {
        return (EAttribute)getAbstractCoordinateOperationType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCoordinateOperationType_PositionalAccuracyGroup() {
        return (EAttribute)getAbstractCoordinateOperationType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateOperationType_PositionalAccuracy() {
        return (EReference)getAbstractCoordinateOperationType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateOperationType_SourceCRS() {
        return (EReference)getAbstractCoordinateOperationType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateOperationType_TargetCRS() {
        return (EReference)getAbstractCoordinateOperationType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractCoordinateSystemBaseType() {
        if (abstractCoordinateSystemBaseTypeEClass == null) {
            abstractCoordinateSystemBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(4);
        }
        return abstractCoordinateSystemBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateSystemBaseType_CsName() {
        return (EReference)getAbstractCoordinateSystemBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractCoordinateSystemType() {
        if (abstractCoordinateSystemTypeEClass == null) {
            abstractCoordinateSystemTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(5);
        }
        return abstractCoordinateSystemTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateSystemType_CsID() {
        return (EReference)getAbstractCoordinateSystemType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateSystemType_Remarks() {
        return (EReference)getAbstractCoordinateSystemType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoordinateSystemType_UsesAxis() {
        return (EReference)getAbstractCoordinateSystemType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractCoverageType() {
        if (abstractCoverageTypeEClass == null) {
            abstractCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(6);
        }
        return abstractCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCoverageType_DomainSetGroup() {
        return (EAttribute)getAbstractCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoverageType_DomainSet() {
        return (EReference)getAbstractCoverageType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractCoverageType_RangeSet() {
        return (EReference)getAbstractCoverageType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCoverageType_Dimension() {
        return (EAttribute)getAbstractCoverageType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractCurveSegmentType() {
        if (abstractCurveSegmentTypeEClass == null) {
            abstractCurveSegmentTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(7);
        }
        return abstractCurveSegmentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCurveSegmentType_NumDerivativeInterior() {
        return (EAttribute)getAbstractCurveSegmentType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCurveSegmentType_NumDerivativesAtEnd() {
        return (EAttribute)getAbstractCurveSegmentType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractCurveSegmentType_NumDerivativesAtStart() {
        return (EAttribute)getAbstractCurveSegmentType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractCurveType() {
        if (abstractCurveTypeEClass == null) {
            abstractCurveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(8);
        }
        return abstractCurveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractDatumBaseType() {
        if (abstractDatumBaseTypeEClass == null) {
            abstractDatumBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(9);
        }
        return abstractDatumBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractDatumBaseType_DatumName() {
        return (EReference)getAbstractDatumBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractDatumType() {
        if (abstractDatumTypeEClass == null) {
            abstractDatumTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(10);
        }
        return abstractDatumTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractDatumType_DatumID() {
        return (EReference)getAbstractDatumType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractDatumType_Remarks() {
        return (EReference)getAbstractDatumType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractDatumType_AnchorPoint() {
        return (EReference)getAbstractDatumType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractDatumType_RealizationEpoch() {
        return (EAttribute)getAbstractDatumType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractDatumType_ValidArea() {
        return (EReference)getAbstractDatumType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractDatumType_Scope() {
        return (EAttribute)getAbstractDatumType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractDiscreteCoverageType() {
        if (abstractDiscreteCoverageTypeEClass == null) {
            abstractDiscreteCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(11);
        }
        return abstractDiscreteCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractDiscreteCoverageType_CoverageFunction() {
        return (EReference)getAbstractDiscreteCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractFeatureCollectionType() {
        if (abstractFeatureCollectionTypeEClass == null) {
            abstractFeatureCollectionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(12);
        }
        return abstractFeatureCollectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractFeatureCollectionType_FeatureMember() {
        return (EReference)getAbstractFeatureCollectionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractFeatureCollectionType_FeatureMembers() {
        return (EReference)getAbstractFeatureCollectionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractFeatureType() {
        if (abstractFeatureTypeEClass == null) {
            abstractFeatureTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(13);
        }
        return abstractFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractFeatureType_BoundedBy() {
        return (EReference)getAbstractFeatureType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractFeatureType_LocationGroup() {
        return (EAttribute)getAbstractFeatureType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractFeatureType_Location() {
        return (EReference)getAbstractFeatureType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeneralConversionType() {
        if (abstractGeneralConversionTypeEClass == null) {
            abstractGeneralConversionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(14);
        }
        return abstractGeneralConversionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeneralDerivedCRSType() {
        if (abstractGeneralDerivedCRSTypeEClass == null) {
            abstractGeneralDerivedCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(15);
        }
        return abstractGeneralDerivedCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractGeneralDerivedCRSType_BaseCRS() {
        return (EReference)getAbstractGeneralDerivedCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractGeneralDerivedCRSType_DefinedByConversion() {
        return (EReference)getAbstractGeneralDerivedCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeneralOperationParameterRefType() {
        if (abstractGeneralOperationParameterRefTypeEClass == null) {
            abstractGeneralOperationParameterRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(16);
        }
        return abstractGeneralOperationParameterRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_GeneralOperationParameterGroup() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractGeneralOperationParameterRefType_GeneralOperationParameter() {
        return (EReference)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_Actuate() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_Arcrole() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_Href() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_RemoteSchema() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_Role() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_Show() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_Title() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterRefType_Type() {
        return (EAttribute)getAbstractGeneralOperationParameterRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeneralOperationParameterType() {
        if (abstractGeneralOperationParameterTypeEClass == null) {
            abstractGeneralOperationParameterTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(17);
        }
        return abstractGeneralOperationParameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeneralOperationParameterType_MinimumOccurs() {
        return (EAttribute)getAbstractGeneralOperationParameterType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeneralParameterValueType() {
        if (abstractGeneralParameterValueTypeEClass == null) {
            abstractGeneralParameterValueTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(18);
        }
        return abstractGeneralParameterValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeneralTransformationType() {
        if (abstractGeneralTransformationTypeEClass == null) {
            abstractGeneralTransformationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(19);
        }
        return abstractGeneralTransformationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeometricAggregateType() {
        if (abstractGeometricAggregateTypeEClass == null) {
            abstractGeometricAggregateTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(20);
        }
        return abstractGeometricAggregateTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeometricPrimitiveType() {
        if (abstractGeometricPrimitiveTypeEClass == null) {
            abstractGeometricPrimitiveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(21);
        }
        return abstractGeometricPrimitiveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGeometryType() {
        if (abstractGeometryTypeEClass == null) {
            abstractGeometryTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(22);
        }
        return abstractGeometryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeometryType_AxisLabels() {
        return (EAttribute)getAbstractGeometryType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeometryType_Gid() {
        return (EAttribute)getAbstractGeometryType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeometryType_SrsDimension() {
        return (EAttribute)getAbstractGeometryType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeometryType_SrsName() {
        return (EAttribute)getAbstractGeometryType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGeometryType_UomLabels() {
        return (EAttribute)getAbstractGeometryType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGMLType() {
        if (abstractGMLTypeEClass == null) {
            abstractGMLTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(23);
        }
        return abstractGMLTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractGMLType_MetaDataProperty() {
        return (EReference)getAbstractGMLType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractGMLType_Description() {
        return (EReference)getAbstractGMLType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGMLType_NameGroup() {
        return (EAttribute)getAbstractGMLType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractGMLType_Name() {
        return (EReference)getAbstractGMLType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGMLType_Id() {
        return (EAttribute)getAbstractGMLType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractGriddedSurfaceType() {
        if (abstractGriddedSurfaceTypeEClass == null) {
            abstractGriddedSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(24);
        }
        return abstractGriddedSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractGriddedSurfaceType_Row() {
        return (EReference)getAbstractGriddedSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGriddedSurfaceType_Rows() {
        return (EAttribute)getAbstractGriddedSurfaceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractGriddedSurfaceType_Columns() {
        return (EAttribute)getAbstractGriddedSurfaceType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractMetaDataType() {
        if (abstractMetaDataTypeEClass == null) {
            abstractMetaDataTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(25);
        }
        return abstractMetaDataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetaDataType_Mixed() {
        return (EAttribute)getAbstractMetaDataType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractMetaDataType_Id() {
        return (EAttribute)getAbstractMetaDataType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractParametricCurveSurfaceType() {
        if (abstractParametricCurveSurfaceTypeEClass == null) {
            abstractParametricCurveSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(26);
        }
        return abstractParametricCurveSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractPositionalAccuracyType() {
        if (abstractPositionalAccuracyTypeEClass == null) {
            abstractPositionalAccuracyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(27);
        }
        return abstractPositionalAccuracyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractPositionalAccuracyType_MeasureDescription() {
        return (EReference)getAbstractPositionalAccuracyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractReferenceSystemBaseType() {
        if (abstractReferenceSystemBaseTypeEClass == null) {
            abstractReferenceSystemBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(28);
        }
        return abstractReferenceSystemBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractReferenceSystemBaseType_SrsName() {
        return (EReference)getAbstractReferenceSystemBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractReferenceSystemType() {
        if (abstractReferenceSystemTypeEClass == null) {
            abstractReferenceSystemTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(29);
        }
        return abstractReferenceSystemTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractReferenceSystemType_SrsID() {
        return (EReference)getAbstractReferenceSystemType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractReferenceSystemType_Remarks() {
        return (EReference)getAbstractReferenceSystemType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractReferenceSystemType_ValidArea() {
        return (EReference)getAbstractReferenceSystemType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceSystemType_Scope() {
        return (EAttribute)getAbstractReferenceSystemType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractRingPropertyType() {
        if (abstractRingPropertyTypeEClass == null) {
            abstractRingPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(30);
        }
        return abstractRingPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractRingPropertyType_RingGroup() {
        return (EAttribute)getAbstractRingPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractRingPropertyType_Ring() {
        return (EReference)getAbstractRingPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractRingType() {
        if (abstractRingTypeEClass == null) {
            abstractRingTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(31);
        }
        return abstractRingTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractSolidType() {
        if (abstractSolidTypeEClass == null) {
            abstractSolidTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(32);
        }
        return abstractSolidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractStyleType() {
        if (abstractStyleTypeEClass == null) {
            abstractStyleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(33);
        }
        return abstractStyleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractSurfacePatchType() {
        if (abstractSurfacePatchTypeEClass == null) {
            abstractSurfacePatchTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(34);
        }
        return abstractSurfacePatchTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractSurfaceType() {
        if (abstractSurfaceTypeEClass == null) {
            abstractSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(35);
        }
        return abstractSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTimeComplexType() {
        if (abstractTimeComplexTypeEClass == null) {
            abstractTimeComplexTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(36);
        }
        return abstractTimeComplexTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTimeGeometricPrimitiveType() {
        if (abstractTimeGeometricPrimitiveTypeEClass == null) {
            abstractTimeGeometricPrimitiveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(37);
        }
        return abstractTimeGeometricPrimitiveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractTimeGeometricPrimitiveType_Frame() {
        return (EAttribute)getAbstractTimeGeometricPrimitiveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTimeObjectType() {
        if (abstractTimeObjectTypeEClass == null) {
            abstractTimeObjectTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(38);
        }
        return abstractTimeObjectTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTimePrimitiveType() {
        if (abstractTimePrimitiveTypeEClass == null) {
            abstractTimePrimitiveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(39);
        }
        return abstractTimePrimitiveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractTimePrimitiveType_RelatedTime() {
        return (EReference)getAbstractTimePrimitiveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTimeReferenceSystemType() {
        if (abstractTimeReferenceSystemTypeEClass == null) {
            abstractTimeReferenceSystemTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(40);
        }
        return abstractTimeReferenceSystemTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractTimeReferenceSystemType_DomainOfValidity() {
        return (EAttribute)getAbstractTimeReferenceSystemType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTimeSliceType() {
        if (abstractTimeSliceTypeEClass == null) {
            abstractTimeSliceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(41);
        }
        return abstractTimeSliceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractTimeSliceType_ValidTime() {
        return (EReference)getAbstractTimeSliceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractTimeSliceType_DataSource() {
        return (EReference)getAbstractTimeSliceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTimeTopologyPrimitiveType() {
        if (abstractTimeTopologyPrimitiveTypeEClass == null) {
            abstractTimeTopologyPrimitiveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(42);
        }
        return abstractTimeTopologyPrimitiveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractTimeTopologyPrimitiveType_Complex() {
        return (EReference)getAbstractTimeTopologyPrimitiveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTopologyType() {
        if (abstractTopologyTypeEClass == null) {
            abstractTopologyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(43);
        }
        return abstractTopologyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTopoPrimitiveType() {
        if (abstractTopoPrimitiveTypeEClass == null) {
            abstractTopoPrimitiveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(44);
        }
        return abstractTopoPrimitiveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractTopoPrimitiveType_Isolated() {
        return (EReference)getAbstractTopoPrimitiveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAbstractTopoPrimitiveType_Container() {
        return (EReference)getAbstractTopoPrimitiveType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAffinePlacementType() {
        if (affinePlacementTypeEClass == null) {
            affinePlacementTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(47);
        }
        return affinePlacementTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAffinePlacementType_Location() {
        return (EReference)getAffinePlacementType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAffinePlacementType_RefDirection() {
        return (EReference)getAffinePlacementType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAffinePlacementType_InDimension() {
        return (EAttribute)getAffinePlacementType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAffinePlacementType_OutDimension() {
        return (EAttribute)getAffinePlacementType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAngleChoiceType() {
        if (angleChoiceTypeEClass == null) {
            angleChoiceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(48);
        }
        return angleChoiceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAngleChoiceType_Angle() {
        return (EReference)getAngleChoiceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAngleChoiceType_DmsAngle() {
        return (EReference)getAngleChoiceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAngleType() {
        if (angleTypeEClass == null) {
            angleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(49);
        }
        return angleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArcByBulgeType() {
        if (arcByBulgeTypeEClass == null) {
            arcByBulgeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(50);
        }
        return arcByBulgeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArcByCenterPointType() {
        if (arcByCenterPointTypeEClass == null) {
            arcByCenterPointTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(51);
        }
        return arcByCenterPointTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_Pos() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_PointProperty() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_PointRep() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_PosList() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_Coordinates() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_Radius() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_StartAngle() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcByCenterPointType_EndAngle() {
        return (EReference)getArcByCenterPointType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcByCenterPointType_Interpolation() {
        return (EAttribute)getArcByCenterPointType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcByCenterPointType_NumArc() {
        return (EAttribute)getArcByCenterPointType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArcStringByBulgeType() {
        if (arcStringByBulgeTypeEClass == null) {
            arcStringByBulgeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(54);
        }
        return arcStringByBulgeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcStringByBulgeType_Group() {
        return (EAttribute)getArcStringByBulgeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringByBulgeType_Pos() {
        return (EReference)getArcStringByBulgeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringByBulgeType_PointProperty() {
        return (EReference)getArcStringByBulgeType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringByBulgeType_PointRep() {
        return (EReference)getArcStringByBulgeType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringByBulgeType_PosList() {
        return (EReference)getArcStringByBulgeType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringByBulgeType_Coordinates() {
        return (EReference)getArcStringByBulgeType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcStringByBulgeType_Bulge() {
        return (EAttribute)getArcStringByBulgeType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringByBulgeType_Normal() {
        return (EReference)getArcStringByBulgeType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcStringByBulgeType_Interpolation() {
        return (EAttribute)getArcStringByBulgeType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcStringByBulgeType_NumArc() {
        return (EAttribute)getArcStringByBulgeType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArcStringType() {
        if (arcStringTypeEClass == null) {
            arcStringTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(55);
        }
        return arcStringTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcStringType_Group() {
        return (EAttribute)getArcStringType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringType_Pos() {
        return (EReference)getArcStringType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringType_PointProperty() {
        return (EReference)getArcStringType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringType_PointRep() {
        return (EReference)getArcStringType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringType_PosList() {
        return (EReference)getArcStringType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcStringType_Coordinates() {
        return (EReference)getArcStringType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcStringType_Interpolation() {
        return (EAttribute)getArcStringType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcStringType_NumArc() {
        return (EAttribute)getArcStringType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArcType() {
        if (arcTypeEClass == null) {
            arcTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(56);
        }
        return arcTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAreaType() {
        if (areaTypeEClass == null) {
            areaTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(57);
        }
        return areaTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArrayAssociationType() {
        if (arrayAssociationTypeEClass == null) {
            arrayAssociationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(58);
        }
        return arrayAssociationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArrayAssociationType_ObjectGroup() {
        return (EAttribute)getArrayAssociationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArrayAssociationType_Object() {
        return (EReference)getArrayAssociationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArrayType() {
        if (arrayTypeEClass == null) {
            arrayTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(59);
        }
        return arrayTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArrayType_Members() {
        return (EReference)getArrayType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAssociationType() {
        if (associationTypeEClass == null) {
            associationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(60);
        }
        return associationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_ObjectGroup() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAssociationType_Object() {
        return (EReference)getAssociationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_Actuate() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_Arcrole() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_Href() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_RemoteSchema() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_Role() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_Show() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_Title() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAssociationType_Type() {
        return (EAttribute)getAssociationType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBagType() {
        if (bagTypeEClass == null) {
            bagTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(61);
        }
        return bagTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBagType_Member() {
        return (EReference)getBagType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBagType_Members() {
        return (EReference)getBagType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBaseStyleDescriptorType() {
        if (baseStyleDescriptorTypeEClass == null) {
            baseStyleDescriptorTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(62);
        }
        return baseStyleDescriptorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBaseStyleDescriptorType_SpatialResolution() {
        return (EReference)getBaseStyleDescriptorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBaseStyleDescriptorType_StyleVariation() {
        return (EReference)getBaseStyleDescriptorType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBaseStyleDescriptorType_Animate() {
        return (EReference)getBaseStyleDescriptorType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBaseStyleDescriptorType_AnimateMotion() {
        return (EReference)getBaseStyleDescriptorType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBaseStyleDescriptorType_AnimateColor() {
        return (EReference)getBaseStyleDescriptorType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBaseStyleDescriptorType_Set() {
        return (EReference)getBaseStyleDescriptorType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBaseUnitType() {
        if (baseUnitTypeEClass == null) {
            baseUnitTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(63);
        }
        return baseUnitTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBaseUnitType_UnitsSystem() {
        return (EReference)getBaseUnitType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBezierType() {
        if (bezierTypeEClass == null) {
            bezierTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(64);
        }
        return bezierTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBooleanPropertyType() {
        if (booleanPropertyTypeEClass == null) {
            booleanPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(68);
        }
        return booleanPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBoundedFeatureType() {
        if (boundedFeatureTypeEClass == null) {
            boundedFeatureTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(69);
        }
        return boundedFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBoundingShapeType() {
        if (boundingShapeTypeEClass == null) {
            boundingShapeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(70);
        }
        return boundingShapeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBoundingShapeType_EnvelopeGroup() {
        return (EAttribute)getBoundingShapeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBoundingShapeType_Envelope() {
        return (EReference)getBoundingShapeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBoundingShapeType_Null() {
        return (EAttribute)getBoundingShapeType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBSplineType() {
        if (bSplineTypeEClass == null) {
            bSplineTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(71);
        }
        return bSplineTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBSplineType_Group() {
        return (EAttribute)getBSplineType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBSplineType_Pos() {
        return (EReference)getBSplineType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBSplineType_PointProperty() {
        return (EReference)getBSplineType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBSplineType_PointRep() {
        return (EReference)getBSplineType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBSplineType_PosList() {
        return (EReference)getBSplineType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBSplineType_Coordinates() {
        return (EReference)getBSplineType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBSplineType_Degree() {
        return (EAttribute)getBSplineType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBSplineType_Knot() {
        return (EReference)getBSplineType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBSplineType_Interpolation() {
        return (EAttribute)getBSplineType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBSplineType_IsPolynomial() {
        return (EAttribute)getBSplineType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBSplineType_KnotType() {
        return (EAttribute)getBSplineType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCartesianCSRefType() {
        if (cartesianCSRefTypeEClass == null) {
            cartesianCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(73);
        }
        return cartesianCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCartesianCSRefType_CartesianCS() {
        return (EReference)getCartesianCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_Actuate() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_Arcrole() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_Href() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_RemoteSchema() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_Role() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_Show() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_Title() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCartesianCSRefType_Type() {
        return (EAttribute)getCartesianCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCartesianCSType() {
        if (cartesianCSTypeEClass == null) {
            cartesianCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(74);
        }
        return cartesianCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCategoryExtentType() {
        if (categoryExtentTypeEClass == null) {
            categoryExtentTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(75);
        }
        return categoryExtentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCategoryPropertyType() {
        if (categoryPropertyTypeEClass == null) {
            categoryPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(76);
        }
        return categoryPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCircleByCenterPointType() {
        if (circleByCenterPointTypeEClass == null) {
            circleByCenterPointTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(77);
        }
        return circleByCenterPointTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCircleType() {
        if (circleTypeEClass == null) {
            circleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(78);
        }
        return circleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getClothoidType() {
        if (clothoidTypeEClass == null) {
            clothoidTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(79);
        }
        return clothoidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getClothoidType_RefLocation() {
        return (EReference)getClothoidType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getClothoidType_ScaleFactor() {
        return (EAttribute)getClothoidType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getClothoidType_StartParameter() {
        return (EAttribute)getClothoidType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getClothoidType_EndParameter() {
        return (EAttribute)getClothoidType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCodeListType() {
        if (codeListTypeEClass == null) {
            codeListTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(80);
        }
        return codeListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCodeListType_Value() {
        return (EAttribute)getCodeListType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCodeListType_CodeSpace() {
        return (EAttribute)getCodeListType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCodeOrNullListType() {
        if (codeOrNullListTypeEClass == null) {
            codeOrNullListTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(81);
        }
        return codeOrNullListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCodeOrNullListType_Value() {
        return (EAttribute)getCodeOrNullListType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCodeOrNullListType_CodeSpace() {
        return (EAttribute)getCodeOrNullListType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCodeType() {
        if (codeTypeEClass == null) {
            codeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(82);
        }
        return codeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCodeType_Value() {
        return (EAttribute)getCodeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCodeType_CodeSpace() {
        return (EAttribute)getCodeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositeCurvePropertyType() {
        if (compositeCurvePropertyTypeEClass == null) {
            compositeCurvePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(85);
        }
        return compositeCurvePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeCurvePropertyType_CompositeCurve() {
        return (EReference)getCompositeCurvePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_Actuate() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_Arcrole() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_Href() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_RemoteSchema() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_Role() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_Show() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_Title() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeCurvePropertyType_Type() {
        return (EAttribute)getCompositeCurvePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositeCurveType() {
        if (compositeCurveTypeEClass == null) {
            compositeCurveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(86);
        }
        return compositeCurveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeCurveType_CurveMember() {
        return (EReference)getCompositeCurveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositeSolidPropertyType() {
        if (compositeSolidPropertyTypeEClass == null) {
            compositeSolidPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(87);
        }
        return compositeSolidPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeSolidPropertyType_CompositeSolid() {
        return (EReference)getCompositeSolidPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_Actuate() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_Arcrole() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_Href() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_RemoteSchema() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_Role() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_Show() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_Title() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSolidPropertyType_Type() {
        return (EAttribute)getCompositeSolidPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositeSolidType() {
        if (compositeSolidTypeEClass == null) {
            compositeSolidTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(88);
        }
        return compositeSolidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeSolidType_SolidMember() {
        return (EReference)getCompositeSolidType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositeSurfacePropertyType() {
        if (compositeSurfacePropertyTypeEClass == null) {
            compositeSurfacePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(89);
        }
        return compositeSurfacePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeSurfacePropertyType_CompositeSurface() {
        return (EReference)getCompositeSurfacePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_Actuate() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_Arcrole() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_Href() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_RemoteSchema() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_Role() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_Show() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_Title() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompositeSurfacePropertyType_Type() {
        return (EAttribute)getCompositeSurfacePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositeSurfaceType() {
        if (compositeSurfaceTypeEClass == null) {
            compositeSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(90);
        }
        return compositeSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeSurfaceType_SurfaceMember() {
        return (EReference)getCompositeSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompositeValueType() {
        if (compositeValueTypeEClass == null) {
            compositeValueTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(91);
        }
        return compositeValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeValueType_ValueComponent() {
        return (EReference)getCompositeValueType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompositeValueType_ValueComponents() {
        return (EReference)getCompositeValueType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompoundCRSRefType() {
        if (compoundCRSRefTypeEClass == null) {
            compoundCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(92);
        }
        return compoundCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompoundCRSRefType_CompoundCRS() {
        return (EReference)getCompoundCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_Actuate() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_Arcrole() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_Href() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_RemoteSchema() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_Role() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_Show() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_Title() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCompoundCRSRefType_Type() {
        return (EAttribute)getCompoundCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCompoundCRSType() {
        if (compoundCRSTypeEClass == null) {
            compoundCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(93);
        }
        return compoundCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCompoundCRSType_IncludesCRS() {
        return (EReference)getCompoundCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConcatenatedOperationRefType() {
        if (concatenatedOperationRefTypeEClass == null) {
            concatenatedOperationRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(94);
        }
        return concatenatedOperationRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConcatenatedOperationRefType_ConcatenatedOperation() {
        return (EReference)getConcatenatedOperationRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_Actuate() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_Arcrole() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_Href() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_RemoteSchema() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_Role() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_Show() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_Title() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConcatenatedOperationRefType_Type() {
        return (EAttribute)getConcatenatedOperationRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConcatenatedOperationType() {
        if (concatenatedOperationTypeEClass == null) {
            concatenatedOperationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(95);
        }
        return concatenatedOperationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConcatenatedOperationType_UsesSingleOperation() {
        return (EReference)getConcatenatedOperationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConeType() {
        if (coneTypeEClass == null) {
            coneTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(96);
        }
        return coneTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConeType_HorizontalCurveType() {
        return (EAttribute)getConeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConeType_VerticalCurveType() {
        return (EAttribute)getConeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getContainerPropertyType() {
        if (containerPropertyTypeEClass == null) {
            containerPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(97);
        }
        return containerPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getContainerPropertyType_Face() {
        return (EReference)getContainerPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getContainerPropertyType_TopoSolid() {
        return (EReference)getContainerPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_Actuate() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_Arcrole() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_Href() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_RemoteSchema() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_Role() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_Show() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_Title() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContainerPropertyType_Type() {
        return (EAttribute)getContainerPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getControlPointType() {
        if (controlPointTypeEClass == null) {
            controlPointTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(98);
        }
        return controlPointTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getControlPointType_PosList() {
        return (EReference)getControlPointType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getControlPointType_GeometricPositionGroup() {
        return (EAttribute)getControlPointType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getControlPointType_Pos() {
        return (EReference)getControlPointType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getControlPointType_PointProperty() {
        return (EReference)getControlPointType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConventionalUnitType() {
        if (conventionalUnitTypeEClass == null) {
            conventionalUnitTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(99);
        }
        return conventionalUnitTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConventionalUnitType_ConversionToPreferredUnit() {
        return (EReference)getConventionalUnitType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConventionalUnitType_RoughConversionToPreferredUnit() {
        return (EReference)getConventionalUnitType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConventionalUnitType_DerivationUnitTerm() {
        return (EReference)getConventionalUnitType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConversionRefType() {
        if (conversionRefTypeEClass == null) {
            conversionRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(100);
        }
        return conversionRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConversionRefType_Conversion() {
        return (EReference)getConversionRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_Actuate() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_Arcrole() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_Href() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_RemoteSchema() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_Role() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_Show() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_Title() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionRefType_Type() {
        return (EAttribute)getConversionRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConversionToPreferredUnitType() {
        if (conversionToPreferredUnitTypeEClass == null) {
            conversionToPreferredUnitTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(101);
        }
        return conversionToPreferredUnitTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConversionToPreferredUnitType_Factor() {
        return (EAttribute)getConversionToPreferredUnitType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConversionToPreferredUnitType_Formula() {
        return (EReference)getConversionToPreferredUnitType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConversionType() {
        if (conversionTypeEClass == null) {
            conversionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(102);
        }
        return conversionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConversionType_UsesMethod() {
        return (EReference)getConversionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConversionType_UsesValue() {
        return (EReference)getConversionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordinateOperationRefType() {
        if (coordinateOperationRefTypeEClass == null) {
            coordinateOperationRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(103);
        }
        return coordinateOperationRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_CoordinateOperationGroup() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateOperationRefType_CoordinateOperation() {
        return (EReference)getCoordinateOperationRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_Actuate() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_Arcrole() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_Href() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_RemoteSchema() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_Role() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_Show() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_Title() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateOperationRefType_Type() {
        return (EAttribute)getCoordinateOperationRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordinateReferenceSystemRefType() {
        if (coordinateReferenceSystemRefTypeEClass == null) {
            coordinateReferenceSystemRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(104);
        }
        return coordinateReferenceSystemRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_CoordinateReferenceSystemGroup() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateReferenceSystemRefType_CoordinateReferenceSystem() {
        return (EReference)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_Actuate() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_Arcrole() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_Href() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_RemoteSchema() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_Role() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_Show() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_Title() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateReferenceSystemRefType_Type() {
        return (EAttribute)getCoordinateReferenceSystemRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordinatesType() {
        if (coordinatesTypeEClass == null) {
            coordinatesTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(105);
        }
        return coordinatesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinatesType_Value() {
        return (EAttribute)getCoordinatesType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinatesType_Cs() {
        return (EAttribute)getCoordinatesType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinatesType_Decimal() {
        return (EAttribute)getCoordinatesType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinatesType_Ts() {
        return (EAttribute)getCoordinatesType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordinateSystemAxisBaseType() {
        if (coordinateSystemAxisBaseTypeEClass == null) {
            coordinateSystemAxisBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(106);
        }
        return coordinateSystemAxisBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordinateSystemAxisRefType() {
        if (coordinateSystemAxisRefTypeEClass == null) {
            coordinateSystemAxisRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(107);
        }
        return coordinateSystemAxisRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateSystemAxisRefType_CoordinateSystemAxis() {
        return (EReference)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_Actuate() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_Arcrole() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_Href() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_RemoteSchema() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_Role() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_Show() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_Title() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisRefType_Type() {
        return (EAttribute)getCoordinateSystemAxisRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordinateSystemAxisType() {
        if (coordinateSystemAxisTypeEClass == null) {
            coordinateSystemAxisTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(108);
        }
        return coordinateSystemAxisTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateSystemAxisType_AxisID() {
        return (EReference)getCoordinateSystemAxisType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateSystemAxisType_Remarks() {
        return (EReference)getCoordinateSystemAxisType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateSystemAxisType_AxisAbbrev() {
        return (EReference)getCoordinateSystemAxisType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateSystemAxisType_AxisDirection() {
        return (EReference)getCoordinateSystemAxisType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemAxisType_Uom() {
        return (EAttribute)getCoordinateSystemAxisType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordinateSystemRefType() {
        if (coordinateSystemRefTypeEClass == null) {
            coordinateSystemRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(109);
        }
        return coordinateSystemRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_CoordinateSystemGroup() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoordinateSystemRefType_CoordinateSystem() {
        return (EReference)getCoordinateSystemRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_Actuate() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_Arcrole() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_Href() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_RemoteSchema() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_Role() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_Show() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_Title() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordinateSystemRefType_Type() {
        return (EAttribute)getCoordinateSystemRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoordType() {
        if (coordTypeEClass == null) {
            coordTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(110);
        }
        return coordTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordType_X() {
        return (EAttribute)getCoordType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordType_Y() {
        return (EAttribute)getCoordType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoordType_Z() {
        return (EAttribute)getCoordType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCountPropertyType() {
        if (countPropertyTypeEClass == null) {
            countPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(112);
        }
        return countPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCovarianceElementType() {
        if (covarianceElementTypeEClass == null) {
            covarianceElementTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(113);
        }
        return covarianceElementTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCovarianceElementType_RowIndex() {
        return (EAttribute)getCovarianceElementType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCovarianceElementType_ColumnIndex() {
        return (EAttribute)getCovarianceElementType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCovarianceElementType_Covariance() {
        return (EAttribute)getCovarianceElementType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCovarianceMatrixType() {
        if (covarianceMatrixTypeEClass == null) {
            covarianceMatrixTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(114);
        }
        return covarianceMatrixTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCovarianceMatrixType_UnitOfMeasure() {
        return (EReference)getCovarianceMatrixType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCovarianceMatrixType_IncludesElement() {
        return (EReference)getCovarianceMatrixType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageFunctionType() {
        if (coverageFunctionTypeEClass == null) {
            coverageFunctionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(115);
        }
        return coverageFunctionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageFunctionType_MappingRule() {
        return (EReference)getCoverageFunctionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageFunctionType_GridFunctionGroup() {
        return (EAttribute)getCoverageFunctionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageFunctionType_GridFunction() {
        return (EReference)getCoverageFunctionType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCRSRefType() {
        if (crsRefTypeEClass == null) {
            crsRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(116);
        }
        return crsRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_CRSGroup() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCRSRefType_CRS() {
        return (EReference)getCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_Actuate() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_Arcrole() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_Href() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_RemoteSchema() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_Role() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_Show() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_Title() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCRSRefType_Type() {
        return (EAttribute)getCRSRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCubicSplineType() {
        if (cubicSplineTypeEClass == null) {
            cubicSplineTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(117);
        }
        return cubicSplineTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCubicSplineType_Group() {
        return (EAttribute)getCubicSplineType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCubicSplineType_Pos() {
        return (EReference)getCubicSplineType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCubicSplineType_PointProperty() {
        return (EReference)getCubicSplineType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCubicSplineType_PointRep() {
        return (EReference)getCubicSplineType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCubicSplineType_PosList() {
        return (EReference)getCubicSplineType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCubicSplineType_Coordinates() {
        return (EReference)getCubicSplineType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCubicSplineType_VectorAtStart() {
        return (EReference)getCubicSplineType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCubicSplineType_VectorAtEnd() {
        return (EReference)getCubicSplineType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCubicSplineType_Degree() {
        return (EAttribute)getCubicSplineType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCubicSplineType_Interpolation() {
        return (EAttribute)getCubicSplineType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCurveArrayPropertyType() {
        if (curveArrayPropertyTypeEClass == null) {
            curveArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(118);
        }
        return curveArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurveArrayPropertyType_CurveGroup() {
        return (EAttribute)getCurveArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCurveArrayPropertyType_Curve() {
        return (EReference)getCurveArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCurvePropertyType() {
        if (curvePropertyTypeEClass == null) {
            curvePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(121);
        }
        return curvePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_CurveGroup() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCurvePropertyType_Curve() {
        return (EReference)getCurvePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_Actuate() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_Arcrole() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_Href() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_RemoteSchema() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_Role() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_Show() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_Title() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurvePropertyType_Type() {
        return (EAttribute)getCurvePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCurveSegmentArrayPropertyType() {
        if (curveSegmentArrayPropertyTypeEClass == null) {
            curveSegmentArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(122);
        }
        return curveSegmentArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCurveSegmentArrayPropertyType_CurveSegmentGroup() {
        return (EAttribute)getCurveSegmentArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCurveSegmentArrayPropertyType_CurveSegment() {
        return (EReference)getCurveSegmentArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCurveType() {
        if (curveTypeEClass == null) {
            curveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(123);
        }
        return curveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCurveType_Segments() {
        return (EReference)getCurveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCylinderType() {
        if (cylinderTypeEClass == null) {
            cylinderTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(124);
        }
        return cylinderTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylinderType_HorizontalCurveType() {
        return (EAttribute)getCylinderType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylinderType_VerticalCurveType() {
        return (EAttribute)getCylinderType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCylindricalCSRefType() {
        if (cylindricalCSRefTypeEClass == null) {
            cylindricalCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(125);
        }
        return cylindricalCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCylindricalCSRefType_CylindricalCS() {
        return (EReference)getCylindricalCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_Actuate() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_Arcrole() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_Href() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_RemoteSchema() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_Role() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_Show() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_Title() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCylindricalCSRefType_Type() {
        return (EAttribute)getCylindricalCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCylindricalCSType() {
        if (cylindricalCSTypeEClass == null) {
            cylindricalCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(126);
        }
        return cylindricalCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDataBlockType() {
        if (dataBlockTypeEClass == null) {
            dataBlockTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(127);
        }
        return dataBlockTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataBlockType_RangeParameters() {
        return (EReference)getDataBlockType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataBlockType_TupleList() {
        return (EReference)getDataBlockType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDataBlockType_DoubleOrNullTupleList() {
        return (EAttribute)getDataBlockType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDatumRefType() {
        if (datumRefTypeEClass == null) {
            datumRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(128);
        }
        return datumRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_DatumGroup() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDatumRefType_Datum() {
        return (EReference)getDatumRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_Actuate() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_Arcrole() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_Href() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_RemoteSchema() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_Role() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_Show() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_Title() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDatumRefType_Type() {
        return (EAttribute)getDatumRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefaultStylePropertyType() {
        if (defaultStylePropertyTypeEClass == null) {
            defaultStylePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(130);
        }
        return defaultStylePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_StyleGroup() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDefaultStylePropertyType_Style() {
        return (EReference)getDefaultStylePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_About() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_Actuate() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_Arcrole() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_Href() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_RemoteSchema() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_Role() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_Show() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_Title() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultStylePropertyType_Type() {
        return (EAttribute)getDefaultStylePropertyType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefinitionProxyType() {
        if (definitionProxyTypeEClass == null) {
            definitionProxyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(131);
        }
        return definitionProxyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDefinitionProxyType_DefinitionRef() {
        return (EReference)getDefinitionProxyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefinitionType() {
        if (definitionTypeEClass == null) {
            definitionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(132);
        }
        return definitionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDegreesType() {
        if (degreesTypeEClass == null) {
            degreesTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(133);
        }
        return degreesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDegreesType_Value() {
        return (EAttribute)getDegreesType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDegreesType_Direction() {
        return (EAttribute)getDegreesType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDerivationUnitTermType() {
        if (derivationUnitTermTypeEClass == null) {
            derivationUnitTermTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(135);
        }
        return derivationUnitTermTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivationUnitTermType_Exponent() {
        return (EAttribute)getDerivationUnitTermType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDerivedCRSRefType() {
        if (derivedCRSRefTypeEClass == null) {
            derivedCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(136);
        }
        return derivedCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDerivedCRSRefType_DerivedCRS() {
        return (EReference)getDerivedCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_Actuate() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_Arcrole() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_Href() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_RemoteSchema() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_Role() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_Show() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_Title() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDerivedCRSRefType_Type() {
        return (EAttribute)getDerivedCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDerivedCRSType() {
        if (derivedCRSTypeEClass == null) {
            derivedCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(137);
        }
        return derivedCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDerivedCRSType_DerivedCRSType() {
        return (EReference)getDerivedCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDerivedCRSType_UsesCS() {
        return (EReference)getDerivedCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDerivedCRSTypeType() {
        if (derivedCRSTypeTypeEClass == null) {
            derivedCRSTypeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(138);
        }
        return derivedCRSTypeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDerivedUnitType() {
        if (derivedUnitTypeEClass == null) {
            derivedUnitTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(139);
        }
        return derivedUnitTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDerivedUnitType_DerivationUnitTerm() {
        return (EReference)getDerivedUnitType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDictionaryEntryType() {
        if (dictionaryEntryTypeEClass == null) {
            dictionaryEntryTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(140);
        }
        return dictionaryEntryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_DefinitionGroup() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDictionaryEntryType_Definition() {
        return (EReference)getDictionaryEntryType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_Actuate() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_Arcrole() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_Href() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_RemoteSchema() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_Role() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_Show() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_Title() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryEntryType_Type() {
        return (EAttribute)getDictionaryEntryType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDictionaryType() {
        if (dictionaryTypeEClass == null) {
            dictionaryTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(141);
        }
        return dictionaryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryType_Group() {
        return (EAttribute)getDictionaryType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDictionaryType_DictionaryEntryGroup() {
        return (EAttribute)getDictionaryType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDictionaryType_DictionaryEntry() {
        return (EReference)getDictionaryType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDictionaryType_IndirectEntry() {
        return (EReference)getDictionaryType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectedEdgePropertyType() {
        if (directedEdgePropertyTypeEClass == null) {
            directedEdgePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(142);
        }
        return directedEdgePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectedEdgePropertyType_Edge() {
        return (EReference)getDirectedEdgePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Actuate() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Arcrole() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Href() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Orientation() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_RemoteSchema() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Role() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Show() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Title() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedEdgePropertyType_Type() {
        return (EAttribute)getDirectedEdgePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectedFacePropertyType() {
        if (directedFacePropertyTypeEClass == null) {
            directedFacePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(143);
        }
        return directedFacePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectedFacePropertyType_Face() {
        return (EReference)getDirectedFacePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Actuate() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Arcrole() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Href() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Orientation() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_RemoteSchema() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Role() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Show() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Title() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedFacePropertyType_Type() {
        return (EAttribute)getDirectedFacePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectedNodePropertyType() {
        if (directedNodePropertyTypeEClass == null) {
            directedNodePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(144);
        }
        return directedNodePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectedNodePropertyType_Node() {
        return (EReference)getDirectedNodePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Actuate() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Arcrole() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Href() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Orientation() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_RemoteSchema() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Role() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Show() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Title() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedNodePropertyType_Type() {
        return (EAttribute)getDirectedNodePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectedObservationAtDistanceType() {
        if (directedObservationAtDistanceTypeEClass == null) {
            directedObservationAtDistanceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(145);
        }
        return directedObservationAtDistanceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectedObservationAtDistanceType_Distance() {
        return (EReference)getDirectedObservationAtDistanceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectedObservationType() {
        if (directedObservationTypeEClass == null) {
            directedObservationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(146);
        }
        return directedObservationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectedObservationType_Direction() {
        return (EReference)getDirectedObservationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectedTopoSolidPropertyType() {
        if (directedTopoSolidPropertyTypeEClass == null) {
            directedTopoSolidPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(147);
        }
        return directedTopoSolidPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectedTopoSolidPropertyType_TopoSolid() {
        return (EReference)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Actuate() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Arcrole() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Href() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Orientation() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_RemoteSchema() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Role() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Show() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Title() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectedTopoSolidPropertyType_Type() {
        return (EAttribute)getDirectedTopoSolidPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectionPropertyType() {
        if (directionPropertyTypeEClass == null) {
            directionPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(148);
        }
        return directionPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectionPropertyType_DirectionVector() {
        return (EReference)getDirectionPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_CompassPoint() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectionPropertyType_DirectionKeyword() {
        return (EReference)getDirectionPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectionPropertyType_DirectionString() {
        return (EReference)getDirectionPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_Actuate() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_Arcrole() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_Href() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_RemoteSchema() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_Role() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_Show() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_Title() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectionPropertyType_Type() {
        return (EAttribute)getDirectionPropertyType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectionVectorType() {
        if (directionVectorTypeEClass == null) {
            directionVectorTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(153);
        }
        return directionVectorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectionVectorType_Vector() {
        return (EReference)getDirectionVectorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectionVectorType_HorizontalAngle() {
        return (EReference)getDirectionVectorType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDirectionVectorType_VerticalAngle() {
        return (EReference)getDirectionVectorType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectPositionListType() {
        if (directPositionListTypeEClass == null) {
            directPositionListTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(154);
        }
        return directPositionListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionListType_Value() {
        return (EAttribute)getDirectPositionListType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionListType_AxisLabels() {
        return (EAttribute)getDirectPositionListType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionListType_Count() {
        return (EAttribute)getDirectPositionListType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionListType_SrsDimension() {
        return (EAttribute)getDirectPositionListType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionListType_SrsName() {
        return (EAttribute)getDirectPositionListType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionListType_UomLabels() {
        return (EAttribute)getDirectPositionListType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDirectPositionType() {
        if (directPositionTypeEClass == null) {
            directPositionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(155);
        }
        return directPositionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionType_Value() {
        return (EAttribute)getDirectPositionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionType_AxisLabels() {
        return (EAttribute)getDirectPositionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionType_SrsDimension() {
        return (EAttribute)getDirectPositionType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionType_SrsName() {
        return (EAttribute)getDirectPositionType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDirectPositionType_UomLabels() {
        return (EAttribute)getDirectPositionType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDMSAngleType() {
        if (dmsAngleTypeEClass == null) {
            dmsAngleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(156);
        }
        return dmsAngleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDMSAngleType_Degrees() {
        return (EReference)getDMSAngleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDMSAngleType_DecimalMinutes() {
        return (EAttribute)getDMSAngleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDMSAngleType_Minutes() {
        return (EAttribute)getDMSAngleType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDMSAngleType_Seconds() {
        return (EAttribute)getDMSAngleType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDocumentRoot() {
        if (documentRootEClass == null) {
            documentRootEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(157);
        }
        return documentRootEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Mixed() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XMLNSPrefixMap() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XSISchemaLocation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Association() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ContinuousCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Coverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Feature() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GML() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Object() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateOperation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Definition() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateReferenceSystem() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ReferenceSystem() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateSystem() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Curve() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeometricPrimitive() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Geometry() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CurveSegment() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Datum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DiscreteCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureCollection() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeneralConversion() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Operation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SingleOperation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeneralDerivedCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeneralOperationParameter() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeneralParameterValue() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeneralTransformation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeometricAggregate() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GriddedSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ParametricCurveSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SurfacePatch() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ImplicitGeometry() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MetaData() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PositionalAccuracy() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Reference() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(36);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Ring() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(37);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Solid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(38);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_StrictAssociation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(39);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Style() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(40);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Surface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(41);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeComplex() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(42);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeObject() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(43);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeGeometricPrimitive() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(44);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimePrimitive() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(45);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeReferenceSystem() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(46);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeSlice() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(47);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeTopologyPrimitive() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(48);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Topology() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(49);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoPrimitive() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(50);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbsoluteExternalPositionalAccuracy() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(51);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbstractGeneralOperationParameterRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(52);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AffinePlacement() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(53);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AnchorPoint() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(54);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Angle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(55);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Arc() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(56);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ArcString() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(57);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ArcByBulge() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(58);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ArcStringByBulge() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(59);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ArcByCenterPoint() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(60);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Array() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(61);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AxisAbbrev() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(62);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AxisDirection() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(63);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AxisID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(64);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Bag() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(65);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BaseCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(66);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BaseCurve() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(67);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BaseSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(68);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BaseUnit() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(69);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UnitDefinition() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(70);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Bezier() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(71);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BSpline() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(72);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Boolean() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(73);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_BooleanList() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(74);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_BooleanValue() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(75);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BoundedBy() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(76);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BoundingBox() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(77);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BoundingPolygon() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(78);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CartesianCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(79);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CartesianCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(80);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CatalogSymbol() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(81);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Category() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(82);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CategoryExtent() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(83);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CategoryList() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(84);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CenterLineOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(85);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CenterOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(86);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Circle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(87);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CircleByCenterPoint() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(88);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Clothoid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(89);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_ColumnIndex() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(90);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_CompassPoint() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(91);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CompositeCurve() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(92);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CompositeSolid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(93);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CompositeSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(94);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CompositeValue() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(95);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CompoundCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(96);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CompoundCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(97);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ConcatenatedOperation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(98);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ConcatenatedOperationRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(99);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Cone() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(100);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Container() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(101);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ConventionalUnit() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(102);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Conversion() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(103);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ConversionRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(104);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ConversionToPreferredUnit() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(105);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Coord() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(106);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateOperationID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(107);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateOperationName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(108);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Name() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(109);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateOperationRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(110);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateReferenceSystemRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(111);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Coordinates() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(112);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateSystemAxis() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(113);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateSystemAxisRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(114);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoordinateSystemRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(115);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Count() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(116);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_CountExtent() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(117);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_CountList() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(118);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Covariance() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(119);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CovarianceMatrix() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(120);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageFunction() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(121);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CrsRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(122);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CsID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(123);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CsName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(124);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CubicSpline() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(125);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Curve1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(126);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CurveArrayProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(127);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CurveMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(128);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CurveMembers() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(129);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CurveProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(130);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Cylinder() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(131);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CylindricalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(132);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CylindricalCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(133);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DataBlock() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(134);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DataSource() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(135);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DatumID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(136);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DatumName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(137);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DatumRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(138);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_DecimalMinutes() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(139);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DefaultStyle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(140);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DefinedByConversion() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(141);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DefinitionCollection() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(142);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DefinitionMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(143);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DictionaryEntry() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(144);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DefinitionProxy() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(145);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DefinitionRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(146);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Degrees() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(147);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DerivationUnitTerm() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(148);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DerivedCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(149);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DerivedCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(150);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DerivedCRSType() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(151);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DerivedUnit() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(152);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Description() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(153);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Dictionary() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(154);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DirectedEdge() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(155);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DirectedFace() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(156);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DirectedNode() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(157);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DirectedObservation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(158);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Observation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(159);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DirectedObservationAtDistance() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(160);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DirectedTopoSolid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(161);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Direction() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(162);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DirectionVector() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(163);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DmsAngle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(164);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DmsAngleValue() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(165);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DomainSet() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(166);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_DoubleOrNullTupleList() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(167);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Duration() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(168);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Edge() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(169);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EdgeOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(170);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Ellipsoid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(171);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EllipsoidalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(172);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EllipsoidalCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(173);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EllipsoidID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(174);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EllipsoidName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(175);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EllipsoidRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(176);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EngineeringCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(177);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EngineeringCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(178);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EngineeringDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(179);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EngineeringDatumRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(180);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Envelope() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(181);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EnvelopeWithTimePeriod() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(182);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ExtentOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(183);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Exterior() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(184);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Face() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(185);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureCollection1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(186);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(187);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureMembers() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(188);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(189);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureStyle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(190);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureStyle1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(191);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_File() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(192);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeneralConversionRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(193);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeneralTransformationRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(194);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GenericMetaData() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(195);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeocentricCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(196);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeocentricCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(197);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Geodesic() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(198);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeodesicString() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(199);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeodeticDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(200);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeodeticDatumRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(201);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeographicCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(202);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeographicCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(203);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeometricComplex() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(204);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeometryMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(205);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeometryMembers() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(206);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeometryStyle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(207);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GeometryStyle1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(208);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GraphStyle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(209);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GraphStyle1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(210);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GreenwichLongitude() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(211);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Grid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(212);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GridCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(213);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GridDomain() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(214);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GridFunction() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(215);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GroupID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(216);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GroupName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(217);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_History() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(218);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ImageCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(219);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ImageCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(220);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ImageDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(221);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ImageDatumRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(222);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_IncludesCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(223);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_IncludesElement() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(224);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_IncludesParameter() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(225);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_IncludesValue() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(226);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_IndexMap() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(227);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_IndirectEntry() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(228);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_InnerBoundaryIs() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(229);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Interior() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(230);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_IntegerValue() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(231);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_IntegerValueList() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(232);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_InverseFlattening() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(233);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Isolated() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(234);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_IsSphere() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(235);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LabelStyle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(236);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LabelStyle1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(237);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LinearCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(238);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LinearCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(239);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LinearRing() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(240);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LineString() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(241);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LineStringMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(242);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LineStringProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(243);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LineStringSegment() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(244);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Location() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(245);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LocationKeyWord() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(246);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LocationString() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(247);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MappingRule() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(248);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MaximalComplex() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(249);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_MaximumOccurs() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(250);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Measure() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(251);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MeasureDescription() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(252);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Member() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(253);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Members() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(254);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MeridianID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(255);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MeridianName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(256);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MetaDataProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(257);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MethodFormula() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(258);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MethodID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(259);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MethodName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(260);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_MinimumOccurs() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(261);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Minutes() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(262);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_ModifiedCoordinate() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(263);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MovingObjectStatus() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(264);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiCenterLineOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(265);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiCenterOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(266);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(267);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiCurve() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(268);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiCurveCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(269);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiCurveDomain() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(270);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiCurveProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(271);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiEdgeOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(272);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiExtentOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(273);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiGeometry() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(274);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiGeometryProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(275);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiLineString() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(276);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiLocation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(277);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiPoint() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(278);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiPointCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(279);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiPointDomain() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(280);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiPointProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(281);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiPolygon() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(282);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiPosition() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(283);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSolid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(284);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSolidCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(285);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSolidDomain() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(286);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSolidProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(287);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(288);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSurfaceCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(289);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSurfaceDomain() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(290);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MultiSurfaceProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(291);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Node() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(292);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Null() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(293);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ObliqueCartesianCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(294);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ObliqueCartesianCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(295);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OffsetCurve() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(296);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationMethod() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(297);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationMethodRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(298);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationParameter() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(299);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationParameterGroup() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(300);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationParameterGroupRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(301);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationParameterRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(302);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(303);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_OperationVersion() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(304);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OrientableCurve() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(305);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OrientableSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(306);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Origin() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(307);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OuterBoundaryIs() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(308);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ParameterID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(309);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ParameterName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(310);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ParameterValue() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(311);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ParameterValueGroup() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(312);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PassThroughOperation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(313);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PassThroughOperationRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(314);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Patches() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(315);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PixelInCell() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(316);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Point() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(317);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PointArrayProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(318);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PointMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(319);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PointMembers() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(320);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PointProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(321);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PointRep() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(322);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PolarCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(323);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PolarCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(324);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Polygon() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(325);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PolygonMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(326);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PolygonPatch() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(327);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PolygonPatches() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(328);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PolygonProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(329);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PolyhedralSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(330);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Surface1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(331);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Pos() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(332);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Position() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(333);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PosList() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(334);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PrimeMeridian() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(335);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PrimeMeridianRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(336);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PriorityLocation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(337);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ProjectedCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(338);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ProjectedCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(339);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Quantity() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(340);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_QuantityExtent() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(341);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_QuantityList() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(342);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_QuantityType() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(343);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RangeParameters() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(344);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RangeSet() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(345);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_RealizationEpoch() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(346);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Rectangle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(347);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RectifiedGrid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(348);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RectifiedGridCoverage() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(349);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RectifiedGridDomain() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(350);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ReferenceSystemRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(351);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RelativeInternalPositionalAccuracy() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(352);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Remarks() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(353);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Result() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(354);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ResultOf() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(355);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Ring1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(356);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_RoughConversionToPreferredUnit() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(357);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_RowIndex() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(358);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Scope() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(359);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SecondDefiningParameter() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(360);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Seconds() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(361);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Segments() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(362);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SemiMajorAxis() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(363);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SemiMinorAxis() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(364);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SingleOperationRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(365);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Solid1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(366);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SolidArrayProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(367);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SolidMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(368);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SolidMembers() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(369);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SolidProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(370);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SourceCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(371);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_SourceDimensions() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(372);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Sphere() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(373);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SphericalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(374);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SphericalCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(375);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SrsID() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(376);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SrsName() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(377);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Status() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(378);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_StringValue() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(379);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Style1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(380);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SubComplex() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(381);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Subject() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(382);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Target() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(383);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SuperComplex() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(384);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SurfaceArrayProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(385);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SurfaceMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(386);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SurfaceMembers() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(387);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SurfaceProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(388);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Symbol() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(389);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TargetCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(390);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_TargetDimensions() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(391);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(392);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(393);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(394);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(395);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(396);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalDatumRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(397);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalExtent() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(398);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeCalendar() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(399);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeCalendarEra() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(400);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeClock() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(401);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeCoordinateSystem() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(402);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeEdge() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(403);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeInstant() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(404);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeInterval() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(405);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeNode() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(406);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeOrdinalEra() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(407);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeOrdinalReferenceSystem() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(408);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimePeriod() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(409);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimePosition() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(410);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TimeTopologyComplex() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(411);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Tin() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(412);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TriangulatedSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(413);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoComplex() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(414);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoComplexProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(415);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoCurve() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(416);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoCurveProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(417);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopologyStyle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(418);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopologyStyle1() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(419);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoPoint() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(420);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoPointProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(421);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoPrimitiveMember() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(422);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoPrimitiveMembers() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(423);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoSolid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(424);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoSurface() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(425);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoSurfaceProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(426);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoVolume() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(427);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TopoVolumeProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(428);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Track() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(429);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Transformation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(430);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TransformationRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(431);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Triangle() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(432);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TrianglePatches() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(433);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TupleList() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(434);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UnitOfMeasure() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(435);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UserDefinedCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(436);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UserDefinedCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(437);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesAxis() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(438);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesCartesianCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(439);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(440);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesEllipsoid() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(441);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesEllipsoidalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(442);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesEngineeringDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(443);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesGeodeticDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(444);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesImageDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(445);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesMethod() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(446);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesObliqueCartesianCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(447);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesOperation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(448);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesParameter() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(449);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesPrimeMeridian() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(450);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesSingleOperation() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(451);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesSphericalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(452);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesTemporalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(453);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesTemporalDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(454);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesValue() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(455);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesVerticalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(456);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UsesVerticalDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(457);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Using() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(458);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValidArea() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(459);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValidTime() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(460);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Value() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(461);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueArray() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(462);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueComponent() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(463);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueComponents() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(464);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_ValueFile() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(465);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueList() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(466);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueOfParameter() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(467);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueProperty() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(468);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValuesOfGroup() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(469);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Vector() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(470);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Version() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(471);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalCRS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(472);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalCRSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(473);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalCS() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(474);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalCSRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(475);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalDatum() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(476);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalDatumRef() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(477);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalDatumType() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(478);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VerticalExtent() {
        return (EReference)getDocumentRoot().getEStructuralFeatures().get(479);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Id() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(480);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_RemoteSchema() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(481);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Transform() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(482);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Uom() {
        return (EAttribute)getDocumentRoot().getEStructuralFeatures().get(483);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDomainSetType() {
        if (domainSetTypeEClass == null) {
            domainSetTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(158);
        }
        return domainSetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_GeometryGroup() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDomainSetType_Geometry() {
        return (EReference)getDomainSetType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_TimeObjectGroup() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDomainSetType_TimeObject() {
        return (EReference)getDomainSetType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_Actuate() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_Arcrole() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_Href() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_RemoteSchema() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_Role() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_Show() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_Title() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSetType_Type() {
        return (EAttribute)getDomainSetType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDynamicFeatureCollectionType() {
        if (dynamicFeatureCollectionTypeEClass == null) {
            dynamicFeatureCollectionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(164);
        }
        return dynamicFeatureCollectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDynamicFeatureCollectionType_ValidTime() {
        return (EReference)getDynamicFeatureCollectionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDynamicFeatureCollectionType_HistoryGroup() {
        return (EAttribute)getDynamicFeatureCollectionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDynamicFeatureCollectionType_History() {
        return (EReference)getDynamicFeatureCollectionType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDynamicFeatureCollectionType_DataSource() {
        return (EReference)getDynamicFeatureCollectionType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDynamicFeatureType() {
        if (dynamicFeatureTypeEClass == null) {
            dynamicFeatureTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(165);
        }
        return dynamicFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDynamicFeatureType_ValidTime() {
        return (EReference)getDynamicFeatureType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDynamicFeatureType_HistoryGroup() {
        return (EAttribute)getDynamicFeatureType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDynamicFeatureType_History() {
        return (EReference)getDynamicFeatureType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDynamicFeatureType_DataSource() {
        return (EReference)getDynamicFeatureType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEdgeType() {
        if (edgeTypeEClass == null) {
            edgeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(166);
        }
        return edgeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEdgeType_DirectedNode() {
        return (EReference)getEdgeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEdgeType_DirectedFace() {
        return (EReference)getEdgeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEdgeType_CurveProperty() {
        return (EReference)getEdgeType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEllipsoidalCSRefType() {
        if (ellipsoidalCSRefTypeEClass == null) {
            ellipsoidalCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(167);
        }
        return ellipsoidalCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEllipsoidalCSRefType_EllipsoidalCS() {
        return (EReference)getEllipsoidalCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_Actuate() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_Arcrole() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_Href() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_RemoteSchema() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_Role() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_Show() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_Title() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidalCSRefType_Type() {
        return (EAttribute)getEllipsoidalCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEllipsoidalCSType() {
        if (ellipsoidalCSTypeEClass == null) {
            ellipsoidalCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(168);
        }
        return ellipsoidalCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEllipsoidBaseType() {
        if (ellipsoidBaseTypeEClass == null) {
            ellipsoidBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(169);
        }
        return ellipsoidBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEllipsoidBaseType_EllipsoidName() {
        return (EReference)getEllipsoidBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEllipsoidRefType() {
        if (ellipsoidRefTypeEClass == null) {
            ellipsoidRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(170);
        }
        return ellipsoidRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEllipsoidRefType_Ellipsoid() {
        return (EReference)getEllipsoidRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_Actuate() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_Arcrole() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_Href() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_RemoteSchema() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_Role() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_Show() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_Title() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEllipsoidRefType_Type() {
        return (EAttribute)getEllipsoidRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEllipsoidType() {
        if (ellipsoidTypeEClass == null) {
            ellipsoidTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(171);
        }
        return ellipsoidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEllipsoidType_EllipsoidID() {
        return (EReference)getEllipsoidType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEllipsoidType_Remarks() {
        return (EReference)getEllipsoidType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEllipsoidType_SemiMajorAxis() {
        return (EReference)getEllipsoidType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEllipsoidType_SecondDefiningParameter() {
        return (EReference)getEllipsoidType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEngineeringCRSRefType() {
        if (engineeringCRSRefTypeEClass == null) {
            engineeringCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(172);
        }
        return engineeringCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEngineeringCRSRefType_EngineeringCRS() {
        return (EReference)getEngineeringCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_Actuate() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_Arcrole() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_Href() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_RemoteSchema() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_Role() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_Show() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_Title() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringCRSRefType_Type() {
        return (EAttribute)getEngineeringCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEngineeringCRSType() {
        if (engineeringCRSTypeEClass == null) {
            engineeringCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(173);
        }
        return engineeringCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEngineeringCRSType_UsesCS() {
        return (EReference)getEngineeringCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEngineeringCRSType_UsesEngineeringDatum() {
        return (EReference)getEngineeringCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEngineeringDatumRefType() {
        if (engineeringDatumRefTypeEClass == null) {
            engineeringDatumRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(174);
        }
        return engineeringDatumRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEngineeringDatumRefType_EngineeringDatum() {
        return (EReference)getEngineeringDatumRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_Actuate() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_Arcrole() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_Href() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_RemoteSchema() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_Role() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_Show() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_Title() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEngineeringDatumRefType_Type() {
        return (EAttribute)getEngineeringDatumRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEngineeringDatumType() {
        if (engineeringDatumTypeEClass == null) {
            engineeringDatumTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(175);
        }
        return engineeringDatumTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEnvelopeType() {
        if (envelopeTypeEClass == null) {
            envelopeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(176);
        }
        return envelopeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEnvelopeType_LowerCorner() {
        return (EReference)getEnvelopeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEnvelopeType_UpperCorner() {
        return (EReference)getEnvelopeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEnvelopeType_Coord() {
        return (EReference)getEnvelopeType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEnvelopeType_Pos() {
        return (EReference)getEnvelopeType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEnvelopeType_Coordinates() {
        return (EReference)getEnvelopeType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEnvelopeType_AxisLabels() {
        return (EAttribute)getEnvelopeType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEnvelopeType_SrsDimension() {
        return (EAttribute)getEnvelopeType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEnvelopeType_SrsName() {
        return (EAttribute)getEnvelopeType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEnvelopeType_UomLabels() {
        return (EAttribute)getEnvelopeType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEnvelopeWithTimePeriodType() {
        if (envelopeWithTimePeriodTypeEClass == null) {
            envelopeWithTimePeriodTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(177);
        }
        return envelopeWithTimePeriodTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEnvelopeWithTimePeriodType_TimePosition() {
        return (EReference)getEnvelopeWithTimePeriodType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEnvelopeWithTimePeriodType_Frame() {
        return (EAttribute)getEnvelopeWithTimePeriodType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExtentType() {
        if (extentTypeEClass == null) {
            extentTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(178);
        }
        return extentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtentType_Description() {
        return (EReference)getExtentType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtentType_BoundingBox() {
        return (EReference)getExtentType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtentType_BoundingPolygon() {
        return (EReference)getExtentType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtentType_VerticalExtent() {
        return (EReference)getExtentType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtentType_TemporalExtent() {
        return (EReference)getExtentType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFaceType() {
        if (faceTypeEClass == null) {
            faceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(179);
        }
        return faceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFaceType_DirectedEdge() {
        return (EReference)getFaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFaceType_DirectedTopoSolid() {
        return (EReference)getFaceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFaceType_SurfaceProperty() {
        return (EReference)getFaceType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureArrayPropertyType() {
        if (featureArrayPropertyTypeEClass == null) {
            featureArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(180);
        }
        return featureArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureArrayPropertyType_FeatureGroup() {
        return (EAttribute)getFeatureArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureArrayPropertyType_Feature() {
        return (EReference)getFeatureArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureCollectionType() {
        if (featureCollectionTypeEClass == null) {
            featureCollectionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(181);
        }
        return featureCollectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeaturePropertyType() {
        if (featurePropertyTypeEClass == null) {
            featurePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(182);
        }
        return featurePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_FeatureGroup() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeaturePropertyType_Feature() {
        return (EReference)getFeaturePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_Actuate() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_Arcrole() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_Href() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_RemoteSchema() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_Role() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_Show() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_Title() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturePropertyType_Type() {
        return (EAttribute)getFeaturePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureStylePropertyType() {
        if (featureStylePropertyTypeEClass == null) {
            featureStylePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(183);
        }
        return featureStylePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureStylePropertyType_FeatureStyle() {
        return (EReference)getFeatureStylePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_About() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_Actuate() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_Arcrole() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_Href() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_RemoteSchema() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_Role() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_Show() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_Title() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStylePropertyType_Type() {
        return (EAttribute)getFeatureStylePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureStyleType() {
        if (featureStyleTypeEClass == null) {
            featureStyleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(184);
        }
        return featureStyleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStyleType_FeatureConstraint() {
        return (EAttribute)getFeatureStyleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureStyleType_GeometryStyle() {
        return (EReference)getFeatureStyleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureStyleType_TopologyStyle() {
        return (EReference)getFeatureStyleType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureStyleType_LabelStyle() {
        return (EReference)getFeatureStyleType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStyleType_BaseType() {
        return (EAttribute)getFeatureStyleType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStyleType_FeatureType() {
        return (EAttribute)getFeatureStyleType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureStyleType_QueryGrammar() {
        return (EAttribute)getFeatureStyleType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFileType() {
        if (fileTypeEClass == null) {
            fileTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(185);
        }
        return fileTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFileType_RangeParameters() {
        return (EReference)getFileType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileType_FileName() {
        return (EAttribute)getFileType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileType_FileStructure() {
        return (EAttribute)getFileType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileType_MimeType() {
        return (EAttribute)getFileType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFileType_Compression() {
        return (EAttribute)getFileType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFormulaType() {
        if (formulaTypeEClass == null) {
            formulaTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(188);
        }
        return formulaTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFormulaType_A() {
        return (EAttribute)getFormulaType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFormulaType_B() {
        return (EAttribute)getFormulaType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFormulaType_C() {
        return (EAttribute)getFormulaType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFormulaType_D() {
        return (EAttribute)getFormulaType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeneralConversionRefType() {
        if (generalConversionRefTypeEClass == null) {
            generalConversionRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(189);
        }
        return generalConversionRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_GeneralConversionGroup() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeneralConversionRefType_GeneralConversion() {
        return (EReference)getGeneralConversionRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_Actuate() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_Arcrole() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_Href() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_RemoteSchema() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_Role() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_Show() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_Title() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralConversionRefType_Type() {
        return (EAttribute)getGeneralConversionRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeneralTransformationRefType() {
        if (generalTransformationRefTypeEClass == null) {
            generalTransformationRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(190);
        }
        return generalTransformationRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_GeneralTransformationGroup() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeneralTransformationRefType_GeneralTransformation() {
        return (EReference)getGeneralTransformationRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_Actuate() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_Arcrole() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_Href() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_RemoteSchema() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_Role() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_Show() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_Title() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeneralTransformationRefType_Type() {
        return (EAttribute)getGeneralTransformationRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGenericMetaDataType() {
        if (genericMetaDataTypeEClass == null) {
            genericMetaDataTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(191);
        }
        return genericMetaDataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGenericMetaDataType_Any() {
        return (EAttribute)getGenericMetaDataType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeocentricCRSRefType() {
        if (geocentricCRSRefTypeEClass == null) {
            geocentricCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(192);
        }
        return geocentricCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeocentricCRSRefType_GeocentricCRS() {
        return (EReference)getGeocentricCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_Actuate() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_Arcrole() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_Href() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_RemoteSchema() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_Role() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_Show() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_Title() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeocentricCRSRefType_Type() {
        return (EAttribute)getGeocentricCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeocentricCRSType() {
        if (geocentricCRSTypeEClass == null) {
            geocentricCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(193);
        }
        return geocentricCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeocentricCRSType_UsesCartesianCS() {
        return (EReference)getGeocentricCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeocentricCRSType_UsesSphericalCS() {
        return (EReference)getGeocentricCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeocentricCRSType_UsesGeodeticDatum() {
        return (EReference)getGeocentricCRSType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeodesicStringType() {
        if (geodesicStringTypeEClass == null) {
            geodesicStringTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(194);
        }
        return geodesicStringTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeodesicStringType_PosList() {
        return (EReference)getGeodesicStringType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodesicStringType_GeometricPositionGroup() {
        return (EAttribute)getGeodesicStringType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeodesicStringType_Pos() {
        return (EReference)getGeodesicStringType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeodesicStringType_PointProperty() {
        return (EReference)getGeodesicStringType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodesicStringType_Interpolation() {
        return (EAttribute)getGeodesicStringType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeodesicType() {
        if (geodesicTypeEClass == null) {
            geodesicTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(195);
        }
        return geodesicTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeodeticDatumRefType() {
        if (geodeticDatumRefTypeEClass == null) {
            geodeticDatumRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(196);
        }
        return geodeticDatumRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeodeticDatumRefType_GeodeticDatum() {
        return (EReference)getGeodeticDatumRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_Actuate() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_Arcrole() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_Href() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_RemoteSchema() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_Role() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_Show() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_Title() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeodeticDatumRefType_Type() {
        return (EAttribute)getGeodeticDatumRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeodeticDatumType() {
        if (geodeticDatumTypeEClass == null) {
            geodeticDatumTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(197);
        }
        return geodeticDatumTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeodeticDatumType_UsesPrimeMeridian() {
        return (EReference)getGeodeticDatumType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeodeticDatumType_UsesEllipsoid() {
        return (EReference)getGeodeticDatumType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeographicCRSRefType() {
        if (geographicCRSRefTypeEClass == null) {
            geographicCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(198);
        }
        return geographicCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeographicCRSRefType_GeographicCRS() {
        return (EReference)getGeographicCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_Actuate() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_Arcrole() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_Href() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_RemoteSchema() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_Role() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_Show() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_Title() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeographicCRSRefType_Type() {
        return (EAttribute)getGeographicCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeographicCRSType() {
        if (geographicCRSTypeEClass == null) {
            geographicCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(199);
        }
        return geographicCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeographicCRSType_UsesEllipsoidalCS() {
        return (EReference)getGeographicCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeographicCRSType_UsesGeodeticDatum() {
        return (EReference)getGeographicCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometricComplexPropertyType() {
        if (geometricComplexPropertyTypeEClass == null) {
            geometricComplexPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(200);
        }
        return geometricComplexPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometricComplexPropertyType_GeometricComplex() {
        return (EReference)getGeometricComplexPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometricComplexPropertyType_CompositeCurve() {
        return (EReference)getGeometricComplexPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometricComplexPropertyType_CompositeSurface() {
        return (EReference)getGeometricComplexPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometricComplexPropertyType_CompositeSolid() {
        return (EReference)getGeometricComplexPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_Actuate() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_Arcrole() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_Href() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_RemoteSchema() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_Role() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_Show() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_Title() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricComplexPropertyType_Type() {
        return (EAttribute)getGeometricComplexPropertyType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometricComplexType() {
        if (geometricComplexTypeEClass == null) {
            geometricComplexTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(201);
        }
        return geometricComplexTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometricComplexType_Element() {
        return (EReference)getGeometricComplexType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometricPrimitivePropertyType() {
        if (geometricPrimitivePropertyTypeEClass == null) {
            geometricPrimitivePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(202);
        }
        return geometricPrimitivePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_GeometricPrimitiveGroup() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometricPrimitivePropertyType_GeometricPrimitive() {
        return (EReference)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_Actuate() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_Arcrole() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_Href() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_RemoteSchema() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_Role() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_Show() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_Title() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometricPrimitivePropertyType_Type() {
        return (EAttribute)getGeometricPrimitivePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometryArrayPropertyType() {
        if (geometryArrayPropertyTypeEClass == null) {
            geometryArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(203);
        }
        return geometryArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryArrayPropertyType_GeometryGroup() {
        return (EAttribute)getGeometryArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometryArrayPropertyType_Geometry() {
        return (EReference)getGeometryArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometryPropertyType() {
        if (geometryPropertyTypeEClass == null) {
            geometryPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(204);
        }
        return geometryPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_GeometryGroup() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometryPropertyType_Geometry() {
        return (EReference)getGeometryPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_Actuate() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_Arcrole() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_Href() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_RemoteSchema() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_Role() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_Show() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_Title() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryPropertyType_Type() {
        return (EAttribute)getGeometryPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometryStylePropertyType() {
        if (geometryStylePropertyTypeEClass == null) {
            geometryStylePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(205);
        }
        return geometryStylePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometryStylePropertyType_GeometryStyle() {
        return (EReference)getGeometryStylePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_About() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_Actuate() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_Arcrole() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_Href() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_RemoteSchema() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_Role() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_Show() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_Title() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStylePropertyType_Type() {
        return (EAttribute)getGeometryStylePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometryStyleType() {
        if (geometryStyleTypeEClass == null) {
            geometryStyleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(206);
        }
        return geometryStyleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometryStyleType_Symbol() {
        return (EReference)getGeometryStyleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStyleType_Style() {
        return (EAttribute)getGeometryStyleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometryStyleType_LabelStyle() {
        return (EReference)getGeometryStyleType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStyleType_GeometryProperty() {
        return (EAttribute)getGeometryStyleType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryStyleType_GeometryType() {
        return (EAttribute)getGeometryStyleType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGraphStylePropertyType() {
        if (graphStylePropertyTypeEClass == null) {
            graphStylePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(207);
        }
        return graphStylePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGraphStylePropertyType_GraphStyle() {
        return (EReference)getGraphStylePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_About() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_Actuate() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_Arcrole() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_Href() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_RemoteSchema() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_Role() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_Show() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_Title() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStylePropertyType_Type() {
        return (EAttribute)getGraphStylePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGraphStyleType() {
        if (graphStyleTypeEClass == null) {
            graphStyleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(208);
        }
        return graphStyleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_Planar() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_Directed() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_Grid() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_MinDistance() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_MinAngle() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_GraphType() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_DrawingType() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_LineType() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGraphStyleType_AestheticCriteria() {
        return (EAttribute)getGraphStyleType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridCoverageType() {
        if (gridCoverageTypeEClass == null) {
            gridCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(211);
        }
        return gridCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGridCoverageType_GridDomain() {
        return (EReference)getGridCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridDomainType() {
        if (gridDomainTypeEClass == null) {
            gridDomainTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(212);
        }
        return gridDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGridDomainType_Grid() {
        return (EReference)getGridDomainType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridEnvelopeType() {
        if (gridEnvelopeTypeEClass == null) {
            gridEnvelopeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(213);
        }
        return gridEnvelopeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridEnvelopeType_Low() {
        return (EAttribute)getGridEnvelopeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridEnvelopeType_High() {
        return (EAttribute)getGridEnvelopeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridFunctionType() {
        if (gridFunctionTypeEClass == null) {
            gridFunctionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(214);
        }
        return gridFunctionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGridFunctionType_SequenceRule() {
        return (EReference)getGridFunctionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridFunctionType_StartPoint() {
        return (EAttribute)getGridFunctionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridLengthType() {
        if (gridLengthTypeEClass == null) {
            gridLengthTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(215);
        }
        return gridLengthTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridLimitsType() {
        if (gridLimitsTypeEClass == null) {
            gridLimitsTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(216);
        }
        return gridLimitsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGridLimitsType_GridEnvelope() {
        return (EReference)getGridLimitsType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridType() {
        if (gridTypeEClass == null) {
            gridTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(217);
        }
        return gridTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGridType_Limits() {
        return (EReference)getGridType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridType_AxisName() {
        return (EAttribute)getGridType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridType_Dimension() {
        return (EAttribute)getGridType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getHistoryPropertyType() {
        if (historyPropertyTypeEClass == null) {
            historyPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(218);
        }
        return historyPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHistoryPropertyType_Group() {
        return (EAttribute)getHistoryPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHistoryPropertyType_TimeSliceGroup() {
        return (EAttribute)getHistoryPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHistoryPropertyType_TimeSlice() {
        return (EReference)getHistoryPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIdentifierType() {
        if (identifierTypeEClass == null) {
            identifierTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(219);
        }
        return identifierTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentifierType_NameGroup() {
        return (EAttribute)getIdentifierType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIdentifierType_Name() {
        return (EReference)getIdentifierType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentifierType_Version() {
        return (EAttribute)getIdentifierType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIdentifierType_Remarks() {
        return (EReference)getIdentifierType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getImageCRSRefType() {
        if (imageCRSRefTypeEClass == null) {
            imageCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(220);
        }
        return imageCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getImageCRSRefType_ImageCRS() {
        return (EReference)getImageCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_Actuate() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_Arcrole() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_Href() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_RemoteSchema() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_Role() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_Show() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_Title() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_Type() {
        return (EAttribute)getImageCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getImageCRSType() {
        if (imageCRSTypeEClass == null) {
            imageCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(221);
        }
        return imageCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getImageCRSType_UsesCartesianCS() {
        return (EReference)getImageCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getImageCRSType_UsesObliqueCartesianCS() {
        return (EReference)getImageCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getImageCRSType_UsesImageDatum() {
        return (EReference)getImageCRSType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getImageDatumRefType() {
        if (imageDatumRefTypeEClass == null) {
            imageDatumRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(222);
        }
        return imageDatumRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getImageDatumRefType_ImageDatum() {
        return (EReference)getImageDatumRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_Actuate() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_Arcrole() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_Href() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_RemoteSchema() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_Role() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_Show() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_Title() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageDatumRefType_Type() {
        return (EAttribute)getImageDatumRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getImageDatumType() {
        if (imageDatumTypeEClass == null) {
            imageDatumTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(223);
        }
        return imageDatumTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getImageDatumType_PixelInCell() {
        return (EReference)getImageDatumType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIndexMapType() {
        if (indexMapTypeEClass == null) {
            indexMapTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(226);
        }
        return indexMapTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIndexMapType_LookUpTable() {
        return (EAttribute)getIndexMapType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIndirectEntryType() {
        if (indirectEntryTypeEClass == null) {
            indirectEntryTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(227);
        }
        return indirectEntryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIndirectEntryType_DefinitionProxy() {
        return (EReference)getIndirectEntryType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIsolatedPropertyType() {
        if (isolatedPropertyTypeEClass == null) {
            isolatedPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(231);
        }
        return isolatedPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIsolatedPropertyType_Node() {
        return (EReference)getIsolatedPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIsolatedPropertyType_Edge() {
        return (EReference)getIsolatedPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_Actuate() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_Arcrole() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_Href() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_RemoteSchema() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_Role() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_Show() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_Title() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIsolatedPropertyType_Type() {
        return (EAttribute)getIsolatedPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getKnotPropertyType() {
        if (knotPropertyTypeEClass == null) {
            knotPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(234);
        }
        return knotPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getKnotPropertyType_Knot() {
        return (EReference)getKnotPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getKnotType() {
        if (knotTypeEClass == null) {
            knotTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(235);
        }
        return knotTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getKnotType_Value() {
        return (EAttribute)getKnotType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getKnotType_Multiplicity() {
        return (EAttribute)getKnotType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getKnotType_Weight() {
        return (EAttribute)getKnotType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLabelStylePropertyType() {
        if (labelStylePropertyTypeEClass == null) {
            labelStylePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(238);
        }
        return labelStylePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLabelStylePropertyType_LabelStyle() {
        return (EReference)getLabelStylePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_About() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_Actuate() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_Arcrole() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_Href() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_RemoteSchema() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_Role() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_Show() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_Title() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStylePropertyType_Type() {
        return (EAttribute)getLabelStylePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLabelStyleType() {
        if (labelStyleTypeEClass == null) {
            labelStyleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(239);
        }
        return labelStyleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelStyleType_Style() {
        return (EAttribute)getLabelStyleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLabelStyleType_Label() {
        return (EReference)getLabelStyleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLabelType() {
        if (labelTypeEClass == null) {
            labelTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(240);
        }
        return labelTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelType_Mixed() {
        return (EAttribute)getLabelType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelType_LabelExpression() {
        return (EAttribute)getLabelType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLabelType_Transform() {
        return (EAttribute)getLabelType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLengthType() {
        if (lengthTypeEClass == null) {
            lengthTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(241);
        }
        return lengthTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLinearCSRefType() {
        if (linearCSRefTypeEClass == null) {
            linearCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(242);
        }
        return linearCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearCSRefType_LinearCS() {
        return (EReference)getLinearCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_Actuate() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_Arcrole() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_Href() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_RemoteSchema() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_Role() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_Show() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_Title() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearCSRefType_Type() {
        return (EAttribute)getLinearCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLinearCSType() {
        if (linearCSTypeEClass == null) {
            linearCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(243);
        }
        return linearCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLinearRingPropertyType() {
        if (linearRingPropertyTypeEClass == null) {
            linearRingPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(244);
        }
        return linearRingPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearRingPropertyType_LinearRing() {
        return (EReference)getLinearRingPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLinearRingType() {
        if (linearRingTypeEClass == null) {
            linearRingTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(245);
        }
        return linearRingTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLinearRingType_Group() {
        return (EAttribute)getLinearRingType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearRingType_Pos() {
        return (EReference)getLinearRingType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearRingType_PointProperty() {
        return (EReference)getLinearRingType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearRingType_PointRep() {
        return (EReference)getLinearRingType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearRingType_PosList() {
        return (EReference)getLinearRingType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearRingType_Coordinates() {
        return (EReference)getLinearRingType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLinearRingType_Coord() {
        return (EReference)getLinearRingType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLineStringPropertyType() {
        if (lineStringPropertyTypeEClass == null) {
            lineStringPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(246);
        }
        return lineStringPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringPropertyType_LineString() {
        return (EReference)getLineStringPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_Actuate() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_Arcrole() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_Href() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_RemoteSchema() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_Role() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_Show() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_Title() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringPropertyType_Type() {
        return (EAttribute)getLineStringPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLineStringSegmentArrayPropertyType() {
        if (lineStringSegmentArrayPropertyTypeEClass == null) {
            lineStringSegmentArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(247);
        }
        return lineStringSegmentArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringSegmentArrayPropertyType_LineStringSegment() {
        return (EReference)getLineStringSegmentArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLineStringSegmentType() {
        if (lineStringSegmentTypeEClass == null) {
            lineStringSegmentTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(248);
        }
        return lineStringSegmentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringSegmentType_Group() {
        return (EAttribute)getLineStringSegmentType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringSegmentType_Pos() {
        return (EReference)getLineStringSegmentType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringSegmentType_PointProperty() {
        return (EReference)getLineStringSegmentType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringSegmentType_PointRep() {
        return (EReference)getLineStringSegmentType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringSegmentType_PosList() {
        return (EReference)getLineStringSegmentType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringSegmentType_Coordinates() {
        return (EReference)getLineStringSegmentType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringSegmentType_Interpolation() {
        return (EAttribute)getLineStringSegmentType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLineStringType() {
        if (lineStringTypeEClass == null) {
            lineStringTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(249);
        }
        return lineStringTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLineStringType_Group() {
        return (EAttribute)getLineStringType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringType_Pos() {
        return (EReference)getLineStringType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringType_PointProperty() {
        return (EReference)getLineStringType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringType_PointRep() {
        return (EReference)getLineStringType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringType_Coord() {
        return (EReference)getLineStringType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringType_PosList() {
        return (EReference)getLineStringType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLineStringType_Coordinates() {
        return (EReference)getLineStringType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLocationPropertyType() {
        if (locationPropertyTypeEClass == null) {
            locationPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(252);
        }
        return locationPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_GeometryGroup() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLocationPropertyType_Geometry() {
        return (EReference)getLocationPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLocationPropertyType_LocationKeyWord() {
        return (EReference)getLocationPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLocationPropertyType_LocationString() {
        return (EReference)getLocationPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Null() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Actuate() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Arcrole() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Href() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_RemoteSchema() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Role() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Show() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Title() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocationPropertyType_Type() {
        return (EAttribute)getLocationPropertyType().getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMeasureListType() {
        if (measureListTypeEClass == null) {
            measureListTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(253);
        }
        return measureListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureListType_Value() {
        return (EAttribute)getMeasureListType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureListType_Uom() {
        return (EAttribute)getMeasureListType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMeasureOrNullListType() {
        if (measureOrNullListTypeEClass == null) {
            measureOrNullListTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(254);
        }
        return measureOrNullListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureOrNullListType_Value() {
        return (EAttribute)getMeasureOrNullListType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureOrNullListType_Uom() {
        return (EAttribute)getMeasureOrNullListType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMeasureType() {
        if (measureTypeEClass == null) {
            measureTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(255);
        }
        return measureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureType_Value() {
        return (EAttribute)getMeasureType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureType_Uom() {
        return (EAttribute)getMeasureType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMetaDataPropertyType() {
        if (metaDataPropertyTypeEClass == null) {
            metaDataPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(256);
        }
        return metaDataPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Any() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_About() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Actuate() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Arcrole() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Href() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_RemoteSchema() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Role() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Show() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Title() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetaDataPropertyType_Type() {
        return (EAttribute)getMetaDataPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMovingObjectStatusType() {
        if (movingObjectStatusTypeEClass == null) {
            movingObjectStatusTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(257);
        }
        return movingObjectStatusTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMovingObjectStatusType_LocationGroup() {
        return (EAttribute)getMovingObjectStatusType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMovingObjectStatusType_Location() {
        return (EReference)getMovingObjectStatusType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMovingObjectStatusType_Speed() {
        return (EReference)getMovingObjectStatusType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMovingObjectStatusType_Bearing() {
        return (EReference)getMovingObjectStatusType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMovingObjectStatusType_Acceleration() {
        return (EReference)getMovingObjectStatusType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMovingObjectStatusType_Elevation() {
        return (EReference)getMovingObjectStatusType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMovingObjectStatusType_Status() {
        return (EReference)getMovingObjectStatusType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiCurveCoverageType() {
        if (multiCurveCoverageTypeEClass == null) {
            multiCurveCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(258);
        }
        return multiCurveCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiCurveCoverageType_MultiCurveDomain() {
        return (EReference)getMultiCurveCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiCurveDomainType() {
        if (multiCurveDomainTypeEClass == null) {
            multiCurveDomainTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(259);
        }
        return multiCurveDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiCurveDomainType_MultiCurve() {
        return (EReference)getMultiCurveDomainType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiCurvePropertyType() {
        if (multiCurvePropertyTypeEClass == null) {
            multiCurvePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(260);
        }
        return multiCurvePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiCurvePropertyType_MultiCurve() {
        return (EReference)getMultiCurvePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_Actuate() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_Arcrole() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_Href() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_RemoteSchema() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_Role() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_Show() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_Title() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiCurvePropertyType_Type() {
        return (EAttribute)getMultiCurvePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiCurveType() {
        if (multiCurveTypeEClass == null) {
            multiCurveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(261);
        }
        return multiCurveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiCurveType_CurveMember() {
        return (EReference)getMultiCurveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiCurveType_CurveMembers() {
        return (EReference)getMultiCurveType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiGeometryPropertyType() {
        if (multiGeometryPropertyTypeEClass == null) {
            multiGeometryPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(262);
        }
        return multiGeometryPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_GeometricAggregateGroup() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiGeometryPropertyType_GeometricAggregate() {
        return (EReference)getMultiGeometryPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_Actuate() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_Arcrole() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_Href() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_RemoteSchema() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_Role() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_Show() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_Title() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiGeometryPropertyType_Type() {
        return (EAttribute)getMultiGeometryPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiGeometryType() {
        if (multiGeometryTypeEClass == null) {
            multiGeometryTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(263);
        }
        return multiGeometryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiGeometryType_GeometryMember() {
        return (EReference)getMultiGeometryType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiGeometryType_GeometryMembers() {
        return (EReference)getMultiGeometryType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiLineStringPropertyType() {
        if (multiLineStringPropertyTypeEClass == null) {
            multiLineStringPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(264);
        }
        return multiLineStringPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiLineStringPropertyType_MultiLineString() {
        return (EReference)getMultiLineStringPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_Actuate() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_Arcrole() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_Href() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_RemoteSchema() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_Role() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_Show() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_Title() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiLineStringPropertyType_Type() {
        return (EAttribute)getMultiLineStringPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiLineStringType() {
        if (multiLineStringTypeEClass == null) {
            multiLineStringTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(265);
        }
        return multiLineStringTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiLineStringType_LineStringMember() {
        return (EReference)getMultiLineStringType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiPointCoverageType() {
        if (multiPointCoverageTypeEClass == null) {
            multiPointCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(266);
        }
        return multiPointCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiPointCoverageType_MultiPointDomain() {
        return (EReference)getMultiPointCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiPointDomainType() {
        if (multiPointDomainTypeEClass == null) {
            multiPointDomainTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(267);
        }
        return multiPointDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiPointDomainType_MultiPoint() {
        return (EReference)getMultiPointDomainType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiPointPropertyType() {
        if (multiPointPropertyTypeEClass == null) {
            multiPointPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(268);
        }
        return multiPointPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiPointPropertyType_MultiPoint() {
        return (EReference)getMultiPointPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_Actuate() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_Arcrole() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_Href() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_RemoteSchema() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_Role() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_Show() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_Title() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPointPropertyType_Type() {
        return (EAttribute)getMultiPointPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiPointType() {
        if (multiPointTypeEClass == null) {
            multiPointTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(269);
        }
        return multiPointTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiPointType_PointMember() {
        return (EReference)getMultiPointType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiPointType_PointMembers() {
        return (EReference)getMultiPointType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiPolygonPropertyType() {
        if (multiPolygonPropertyTypeEClass == null) {
            multiPolygonPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(270);
        }
        return multiPolygonPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiPolygonPropertyType_MultiPolygon() {
        return (EReference)getMultiPolygonPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_Actuate() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_Arcrole() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_Href() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_RemoteSchema() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_Role() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_Show() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_Title() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiPolygonPropertyType_Type() {
        return (EAttribute)getMultiPolygonPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiPolygonType() {
        if (multiPolygonTypeEClass == null) {
            multiPolygonTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(271);
        }
        return multiPolygonTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiPolygonType_PolygonMember() {
        return (EReference)getMultiPolygonType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSolidCoverageType() {
        if (multiSolidCoverageTypeEClass == null) {
            multiSolidCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(272);
        }
        return multiSolidCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSolidCoverageType_MultiSolidDomain() {
        return (EReference)getMultiSolidCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSolidDomainType() {
        if (multiSolidDomainTypeEClass == null) {
            multiSolidDomainTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(273);
        }
        return multiSolidDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSolidDomainType_MultiSolid() {
        return (EReference)getMultiSolidDomainType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSolidPropertyType() {
        if (multiSolidPropertyTypeEClass == null) {
            multiSolidPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(274);
        }
        return multiSolidPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSolidPropertyType_MultiSolid() {
        return (EReference)getMultiSolidPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_Actuate() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_Arcrole() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_Href() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_RemoteSchema() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_Role() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_Show() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_Title() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSolidPropertyType_Type() {
        return (EAttribute)getMultiSolidPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSolidType() {
        if (multiSolidTypeEClass == null) {
            multiSolidTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(275);
        }
        return multiSolidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSolidType_SolidMember() {
        return (EReference)getMultiSolidType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSolidType_SolidMembers() {
        return (EReference)getMultiSolidType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSurfaceCoverageType() {
        if (multiSurfaceCoverageTypeEClass == null) {
            multiSurfaceCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(276);
        }
        return multiSurfaceCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSurfaceCoverageType_MultiSurfaceDomain() {
        return (EReference)getMultiSurfaceCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSurfaceDomainType() {
        if (multiSurfaceDomainTypeEClass == null) {
            multiSurfaceDomainTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(277);
        }
        return multiSurfaceDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSurfaceDomainType_MultiSurface() {
        return (EReference)getMultiSurfaceDomainType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSurfacePropertyType() {
        if (multiSurfacePropertyTypeEClass == null) {
            multiSurfacePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(278);
        }
        return multiSurfacePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSurfacePropertyType_MultiSurface() {
        return (EReference)getMultiSurfacePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_Actuate() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_Arcrole() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_Href() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_RemoteSchema() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_Role() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_Show() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_Title() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMultiSurfacePropertyType_Type() {
        return (EAttribute)getMultiSurfacePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMultiSurfaceType() {
        if (multiSurfaceTypeEClass == null) {
            multiSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(279);
        }
        return multiSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSurfaceType_SurfaceMember() {
        return (EReference)getMultiSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMultiSurfaceType_SurfaceMembers() {
        return (EReference)getMultiSurfaceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNodeType() {
        if (nodeTypeEClass == null) {
            nodeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(284);
        }
        return nodeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeType_DirectedEdge() {
        return (EReference)getNodeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeType_PointProperty() {
        return (EReference)getNodeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getObliqueCartesianCSRefType() {
        if (obliqueCartesianCSRefTypeEClass == null) {
            obliqueCartesianCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(290);
        }
        return obliqueCartesianCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getObliqueCartesianCSRefType_ObliqueCartesianCS() {
        return (EReference)getObliqueCartesianCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_Actuate() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_Arcrole() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_Href() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_RemoteSchema() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_Role() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_Show() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_Title() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObliqueCartesianCSRefType_Type() {
        return (EAttribute)getObliqueCartesianCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getObliqueCartesianCSType() {
        if (obliqueCartesianCSTypeEClass == null) {
            obliqueCartesianCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(291);
        }
        return obliqueCartesianCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getObservationType() {
        if (observationTypeEClass == null) {
            observationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(292);
        }
        return observationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getObservationType_ValidTime() {
        return (EReference)getObservationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getObservationType_Using() {
        return (EReference)getObservationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getObservationType_TargetGroup() {
        return (EAttribute)getObservationType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getObservationType_Target() {
        return (EReference)getObservationType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getObservationType_ResultOf() {
        return (EReference)getObservationType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOffsetCurveType() {
        if (offsetCurveTypeEClass == null) {
            offsetCurveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(293);
        }
        return offsetCurveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOffsetCurveType_OffsetBase() {
        return (EReference)getOffsetCurveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOffsetCurveType_Distance() {
        return (EReference)getOffsetCurveType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOffsetCurveType_RefDirection() {
        return (EReference)getOffsetCurveType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationMethodBaseType() {
        if (operationMethodBaseTypeEClass == null) {
            operationMethodBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(294);
        }
        return operationMethodBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationMethodBaseType_MethodName() {
        return (EReference)getOperationMethodBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationMethodRefType() {
        if (operationMethodRefTypeEClass == null) {
            operationMethodRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(295);
        }
        return operationMethodRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationMethodRefType_OperationMethod() {
        return (EReference)getOperationMethodRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_Actuate() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_Arcrole() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_Href() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_RemoteSchema() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_Role() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_Show() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_Title() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodRefType_Type() {
        return (EAttribute)getOperationMethodRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationMethodType() {
        if (operationMethodTypeEClass == null) {
            operationMethodTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(296);
        }
        return operationMethodTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationMethodType_MethodID() {
        return (EReference)getOperationMethodType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationMethodType_Remarks() {
        return (EReference)getOperationMethodType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationMethodType_MethodFormula() {
        return (EReference)getOperationMethodType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodType_SourceDimensions() {
        return (EAttribute)getOperationMethodType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationMethodType_TargetDimensions() {
        return (EAttribute)getOperationMethodType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationMethodType_UsesParameter() {
        return (EReference)getOperationMethodType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationParameterBaseType() {
        if (operationParameterBaseTypeEClass == null) {
            operationParameterBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(297);
        }
        return operationParameterBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterBaseType_ParameterName() {
        return (EReference)getOperationParameterBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationParameterGroupBaseType() {
        if (operationParameterGroupBaseTypeEClass == null) {
            operationParameterGroupBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(298);
        }
        return operationParameterGroupBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterGroupBaseType_GroupName() {
        return (EReference)getOperationParameterGroupBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationParameterGroupRefType() {
        if (operationParameterGroupRefTypeEClass == null) {
            operationParameterGroupRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(299);
        }
        return operationParameterGroupRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterGroupRefType_OperationParameterGroup() {
        return (EReference)getOperationParameterGroupRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_Actuate() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_Arcrole() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_Href() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_RemoteSchema() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_Role() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_Show() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_Title() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupRefType_Type() {
        return (EAttribute)getOperationParameterGroupRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationParameterGroupType() {
        if (operationParameterGroupTypeEClass == null) {
            operationParameterGroupTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(300);
        }
        return operationParameterGroupTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterGroupType_GroupID() {
        return (EReference)getOperationParameterGroupType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterGroupType_Remarks() {
        return (EReference)getOperationParameterGroupType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterGroupType_MaximumOccurs() {
        return (EAttribute)getOperationParameterGroupType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterGroupType_IncludesParameter() {
        return (EReference)getOperationParameterGroupType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationParameterRefType() {
        if (operationParameterRefTypeEClass == null) {
            operationParameterRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(301);
        }
        return operationParameterRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterRefType_OperationParameter() {
        return (EReference)getOperationParameterRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_Actuate() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_Arcrole() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_Href() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_RemoteSchema() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_Role() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_Show() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_Title() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationParameterRefType_Type() {
        return (EAttribute)getOperationParameterRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationParameterType() {
        if (operationParameterTypeEClass == null) {
            operationParameterTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(302);
        }
        return operationParameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterType_ParameterID() {
        return (EReference)getOperationParameterType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationParameterType_Remarks() {
        return (EReference)getOperationParameterType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationRefType() {
        if (operationRefTypeEClass == null) {
            operationRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(303);
        }
        return operationRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_OperationGroup() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationRefType_Operation() {
        return (EReference)getOperationRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_Actuate() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_Arcrole() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_Href() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_RemoteSchema() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_Role() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_Show() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_Title() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationRefType_Type() {
        return (EAttribute)getOperationRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOrientableCurveType() {
        if (orientableCurveTypeEClass == null) {
            orientableCurveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(304);
        }
        return orientableCurveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOrientableCurveType_BaseCurve() {
        return (EReference)getOrientableCurveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOrientableCurveType_Orientation() {
        return (EAttribute)getOrientableCurveType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOrientableSurfaceType() {
        if (orientableSurfaceTypeEClass == null) {
            orientableSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(305);
        }
        return orientableSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOrientableSurfaceType_BaseSurface() {
        return (EReference)getOrientableSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOrientableSurfaceType_Orientation() {
        return (EAttribute)getOrientableSurfaceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getParameterValueGroupType() {
        if (parameterValueGroupTypeEClass == null) {
            parameterValueGroupTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(306);
        }
        return parameterValueGroupTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterValueGroupType_IncludesValue() {
        return (EReference)getParameterValueGroupType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterValueGroupType_ValuesOfGroup() {
        return (EReference)getParameterValueGroupType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getParameterValueType() {
        if (parameterValueTypeEClass == null) {
            parameterValueTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(307);
        }
        return parameterValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterValueType_Value() {
        return (EReference)getParameterValueType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterValueType_DmsAngleValue() {
        return (EReference)getParameterValueType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterValueType_StringValue() {
        return (EAttribute)getParameterValueType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterValueType_IntegerValue() {
        return (EAttribute)getParameterValueType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterValueType_BooleanValue() {
        return (EAttribute)getParameterValueType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterValueType_ValueList() {
        return (EReference)getParameterValueType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterValueType_IntegerValueList() {
        return (EAttribute)getParameterValueType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterValueType_ValueFile() {
        return (EAttribute)getParameterValueType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterValueType_ValueOfParameter() {
        return (EReference)getParameterValueType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPassThroughOperationRefType() {
        if (passThroughOperationRefTypeEClass == null) {
            passThroughOperationRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(308);
        }
        return passThroughOperationRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPassThroughOperationRefType_PassThroughOperation() {
        return (EReference)getPassThroughOperationRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_Actuate() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_Arcrole() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_Href() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_RemoteSchema() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_Role() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_Show() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_Title() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationRefType_Type() {
        return (EAttribute)getPassThroughOperationRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPassThroughOperationType() {
        if (passThroughOperationTypeEClass == null) {
            passThroughOperationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(309);
        }
        return passThroughOperationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPassThroughOperationType_ModifiedCoordinate() {
        return (EAttribute)getPassThroughOperationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPassThroughOperationType_UsesOperation() {
        return (EReference)getPassThroughOperationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPixelInCellType() {
        if (pixelInCellTypeEClass == null) {
            pixelInCellTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(310);
        }
        return pixelInCellTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPointArrayPropertyType() {
        if (pointArrayPropertyTypeEClass == null) {
            pointArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(311);
        }
        return pointArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPointArrayPropertyType_Point() {
        return (EReference)getPointArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPointPropertyType() {
        if (pointPropertyTypeEClass == null) {
            pointPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(312);
        }
        return pointPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPointPropertyType_Point() {
        return (EReference)getPointPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_Actuate() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_Arcrole() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_Href() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_RemoteSchema() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_Role() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_Show() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_Title() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPointPropertyType_Type() {
        return (EAttribute)getPointPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPointType() {
        if (pointTypeEClass == null) {
            pointTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(313);
        }
        return pointTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPointType_Pos() {
        return (EReference)getPointType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPointType_Coordinates() {
        return (EReference)getPointType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPointType_Coord() {
        return (EReference)getPointType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPolarCSRefType() {
        if (polarCSRefTypeEClass == null) {
            polarCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(314);
        }
        return polarCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolarCSRefType_PolarCS() {
        return (EReference)getPolarCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_Actuate() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_Arcrole() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_Href() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_RemoteSchema() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_Role() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_Show() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_Title() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolarCSRefType_Type() {
        return (EAttribute)getPolarCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPolarCSType() {
        if (polarCSTypeEClass == null) {
            polarCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(315);
        }
        return polarCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPolygonPatchArrayPropertyType() {
        if (polygonPatchArrayPropertyTypeEClass == null) {
            polygonPatchArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(316);
        }
        return polygonPatchArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolygonPatchArrayPropertyType_PolygonPatch() {
        return (EReference)getPolygonPatchArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPolygonPatchType() {
        if (polygonPatchTypeEClass == null) {
            polygonPatchTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(317);
        }
        return polygonPatchTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPatchType_ExteriorGroup() {
        return (EAttribute)getPolygonPatchType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolygonPatchType_Exterior() {
        return (EReference)getPolygonPatchType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPatchType_InteriorGroup() {
        return (EAttribute)getPolygonPatchType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolygonPatchType_Interior() {
        return (EReference)getPolygonPatchType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPatchType_Interpolation() {
        return (EAttribute)getPolygonPatchType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPolygonPropertyType() {
        if (polygonPropertyTypeEClass == null) {
            polygonPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(318);
        }
        return polygonPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolygonPropertyType_Polygon() {
        return (EReference)getPolygonPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_Actuate() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_Arcrole() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_Href() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_RemoteSchema() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_Role() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_Show() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_Title() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonPropertyType_Type() {
        return (EAttribute)getPolygonPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPolygonType() {
        if (polygonTypeEClass == null) {
            polygonTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(319);
        }
        return polygonTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonType_ExteriorGroup() {
        return (EAttribute)getPolygonType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolygonType_Exterior() {
        return (EReference)getPolygonType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPolygonType_InteriorGroup() {
        return (EAttribute)getPolygonType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolygonType_Interior() {
        return (EReference)getPolygonType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPolyhedralSurfaceType() {
        if (polyhedralSurfaceTypeEClass == null) {
            polyhedralSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(320);
        }
        return polyhedralSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPolyhedralSurfaceType_PolygonPatches() {
        return (EReference)getPolyhedralSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPrimeMeridianBaseType() {
        if (primeMeridianBaseTypeEClass == null) {
            primeMeridianBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(321);
        }
        return primeMeridianBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPrimeMeridianBaseType_MeridianName() {
        return (EReference)getPrimeMeridianBaseType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPrimeMeridianRefType() {
        if (primeMeridianRefTypeEClass == null) {
            primeMeridianRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(322);
        }
        return primeMeridianRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPrimeMeridianRefType_PrimeMeridian() {
        return (EReference)getPrimeMeridianRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_Actuate() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_Arcrole() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_Href() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_RemoteSchema() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_Role() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_Show() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_Title() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPrimeMeridianRefType_Type() {
        return (EAttribute)getPrimeMeridianRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPrimeMeridianType() {
        if (primeMeridianTypeEClass == null) {
            primeMeridianTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(323);
        }
        return primeMeridianTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPrimeMeridianType_MeridianID() {
        return (EReference)getPrimeMeridianType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPrimeMeridianType_Remarks() {
        return (EReference)getPrimeMeridianType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPrimeMeridianType_GreenwichLongitude() {
        return (EReference)getPrimeMeridianType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPriorityLocationPropertyType() {
        if (priorityLocationPropertyTypeEClass == null) {
            priorityLocationPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(324);
        }
        return priorityLocationPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPriorityLocationPropertyType_Priority() {
        return (EAttribute)getPriorityLocationPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProjectedCRSRefType() {
        if (projectedCRSRefTypeEClass == null) {
            projectedCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(325);
        }
        return projectedCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProjectedCRSRefType_ProjectedCRS() {
        return (EReference)getProjectedCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_Actuate() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_Arcrole() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_Href() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_RemoteSchema() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_Role() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_Show() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_Title() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getProjectedCRSRefType_Type() {
        return (EAttribute)getProjectedCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getProjectedCRSType() {
        if (projectedCRSTypeEClass == null) {
            projectedCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(326);
        }
        return projectedCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getProjectedCRSType_UsesCartesianCS() {
        return (EReference)getProjectedCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getQuantityExtentType() {
        if (quantityExtentTypeEClass == null) {
            quantityExtentTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(328);
        }
        return quantityExtentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getQuantityPropertyType() {
        if (quantityPropertyTypeEClass == null) {
            quantityPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(329);
        }
        return quantityPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRangeParametersType() {
        if (rangeParametersTypeEClass == null) {
            rangeParametersTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(332);
        }
        return rangeParametersTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Boolean() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeParametersType_Category() {
        return (EReference)getRangeParametersType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeParametersType_Quantity() {
        return (EReference)getRangeParametersType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Count() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_BooleanList() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeParametersType_CategoryList() {
        return (EReference)getRangeParametersType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeParametersType_QuantityList() {
        return (EReference)getRangeParametersType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_CountList() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeParametersType_CategoryExtent() {
        return (EReference)getRangeParametersType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeParametersType_QuantityExtent() {
        return (EReference)getRangeParametersType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_CountExtent() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_CompositeValueGroup() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeParametersType_CompositeValue() {
        return (EReference)getRangeParametersType().getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Actuate() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Arcrole() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Href() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_RemoteSchema() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Role() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Show() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Title() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeParametersType_Type() {
        return (EAttribute)getRangeParametersType().getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRangeSetType() {
        if (rangeSetTypeEClass == null) {
            rangeSetTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(333);
        }
        return rangeSetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeSetType_ValueArray() {
        return (EReference)getRangeSetType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeSetType_ScalarValueList() {
        return (EAttribute)getRangeSetType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeSetType_BooleanList() {
        return (EAttribute)getRangeSetType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeSetType_CategoryList() {
        return (EReference)getRangeSetType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeSetType_QuantityList() {
        return (EReference)getRangeSetType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeSetType_CountList() {
        return (EAttribute)getRangeSetType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeSetType_DataBlock() {
        return (EReference)getRangeSetType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeSetType_File() {
        return (EReference)getRangeSetType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRectangleType() {
        if (rectangleTypeEClass == null) {
            rectangleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(334);
        }
        return rectangleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRectangleType_ExteriorGroup() {
        return (EAttribute)getRectangleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRectangleType_Exterior() {
        return (EReference)getRectangleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRectangleType_Interpolation() {
        return (EAttribute)getRectangleType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRectifiedGridCoverageType() {
        if (rectifiedGridCoverageTypeEClass == null) {
            rectifiedGridCoverageTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(335);
        }
        return rectifiedGridCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRectifiedGridCoverageType_RectifiedGridDomain() {
        return (EReference)getRectifiedGridCoverageType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRectifiedGridDomainType() {
        if (rectifiedGridDomainTypeEClass == null) {
            rectifiedGridDomainTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(336);
        }
        return rectifiedGridDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRectifiedGridDomainType_RectifiedGrid() {
        return (EReference)getRectifiedGridDomainType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRectifiedGridType() {
        if (rectifiedGridTypeEClass == null) {
            rectifiedGridTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(337);
        }
        return rectifiedGridTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRectifiedGridType_Origin() {
        return (EReference)getRectifiedGridType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRectifiedGridType_OffsetVector() {
        return (EReference)getRectifiedGridType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getReferenceSystemRefType() {
        if (referenceSystemRefTypeEClass == null) {
            referenceSystemRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(338);
        }
        return referenceSystemRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_ReferenceSystemGroup() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getReferenceSystemRefType_ReferenceSystem() {
        return (EReference)getReferenceSystemRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_Actuate() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_Arcrole() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_Href() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_RemoteSchema() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_Role() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_Show() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_Title() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceSystemRefType_Type() {
        return (EAttribute)getReferenceSystemRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getReferenceType() {
        if (referenceTypeEClass == null) {
            referenceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(339);
        }
        return referenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Actuate() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Arcrole() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Href() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_RemoteSchema() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Role() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Show() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Title() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Type() {
        return (EAttribute)getReferenceType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRefLocationType() {
        if (refLocationTypeEClass == null) {
            refLocationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(340);
        }
        return refLocationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRefLocationType_AffinePlacement() {
        return (EReference)getRefLocationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRelatedTimeType() {
        if (relatedTimeTypeEClass == null) {
            relatedTimeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(341);
        }
        return relatedTimeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRelatedTimeType_RelativePosition() {
        return (EAttribute)getRelatedTimeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRelativeInternalPositionalAccuracyType() {
        if (relativeInternalPositionalAccuracyTypeEClass == null) {
            relativeInternalPositionalAccuracyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(342);
        }
        return relativeInternalPositionalAccuracyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRelativeInternalPositionalAccuracyType_Result() {
        return (EReference)getRelativeInternalPositionalAccuracyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRingPropertyType() {
        if (ringPropertyTypeEClass == null) {
            ringPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(345);
        }
        return ringPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRingPropertyType_Ring() {
        return (EReference)getRingPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRingType() {
        if (ringTypeEClass == null) {
            ringTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(346);
        }
        return ringTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRingType_CurveMember() {
        return (EReference)getRingType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRowType() {
        if (rowTypeEClass == null) {
            rowTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(347);
        }
        return rowTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRowType_PosList() {
        return (EReference)getRowType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRowType_GeometricPositionGroup() {
        return (EAttribute)getRowType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRowType_Pos() {
        return (EReference)getRowType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRowType_PointProperty() {
        return (EReference)getRowType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getScalarValuePropertyType() {
        if (scalarValuePropertyTypeEClass == null) {
            scalarValuePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(348);
        }
        return scalarValuePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getScaleType() {
        if (scaleTypeEClass == null) {
            scaleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(349);
        }
        return scaleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSecondDefiningParameterType() {
        if (secondDefiningParameterTypeEClass == null) {
            secondDefiningParameterTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(350);
        }
        return secondDefiningParameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSecondDefiningParameterType_InverseFlattening() {
        return (EReference)getSecondDefiningParameterType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSecondDefiningParameterType_SemiMinorAxis() {
        return (EReference)getSecondDefiningParameterType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSecondDefiningParameterType_IsSphere() {
        return (EAttribute)getSecondDefiningParameterType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSequenceRuleType() {
        if (sequenceRuleTypeEClass == null) {
            sequenceRuleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(353);
        }
        return sequenceRuleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSequenceRuleType_Value() {
        return (EAttribute)getSequenceRuleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSequenceRuleType_Order() {
        return (EAttribute)getSequenceRuleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSingleOperationRefType() {
        if (singleOperationRefTypeEClass == null) {
            singleOperationRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(356);
        }
        return singleOperationRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_SingleOperationGroup() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSingleOperationRefType_SingleOperation() {
        return (EReference)getSingleOperationRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_Actuate() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_Arcrole() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_Href() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_RemoteSchema() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_Role() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_Show() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_Title() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSingleOperationRefType_Type() {
        return (EAttribute)getSingleOperationRefType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSolidArrayPropertyType() {
        if (solidArrayPropertyTypeEClass == null) {
            solidArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(357);
        }
        return solidArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidArrayPropertyType_Group() {
        return (EAttribute)getSolidArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidArrayPropertyType_SolidGroup() {
        return (EAttribute)getSolidArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSolidArrayPropertyType_Solid() {
        return (EReference)getSolidArrayPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSolidPropertyType() {
        if (solidPropertyTypeEClass == null) {
            solidPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(358);
        }
        return solidPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_SolidGroup() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSolidPropertyType_Solid() {
        return (EReference)getSolidPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_Actuate() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_Arcrole() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_Href() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_RemoteSchema() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_Role() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_Show() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_Title() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSolidPropertyType_Type() {
        return (EAttribute)getSolidPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSolidType() {
        if (solidTypeEClass == null) {
            solidTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(359);
        }
        return solidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSolidType_Exterior() {
        return (EReference)getSolidType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSolidType_Interior() {
        return (EReference)getSolidType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpeedType() {
        if (speedTypeEClass == null) {
            speedTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(360);
        }
        return speedTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSphereType() {
        if (sphereTypeEClass == null) {
            sphereTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(361);
        }
        return sphereTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphereType_HorizontalCurveType() {
        return (EAttribute)getSphereType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphereType_VerticalCurveType() {
        return (EAttribute)getSphereType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSphericalCSRefType() {
        if (sphericalCSRefTypeEClass == null) {
            sphericalCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(362);
        }
        return sphericalCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSphericalCSRefType_SphericalCS() {
        return (EReference)getSphericalCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_Actuate() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_Arcrole() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_Href() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_RemoteSchema() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_Role() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_Show() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_Title() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSphericalCSRefType_Type() {
        return (EAttribute)getSphericalCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSphericalCSType() {
        if (sphericalCSTypeEClass == null) {
            sphericalCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(363);
        }
        return sphericalCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStringOrRefType() {
        if (stringOrRefTypeEClass == null) {
            stringOrRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(365);
        }
        return stringOrRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Value() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Actuate() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Arcrole() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Href() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_RemoteSchema() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Role() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Show() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Title() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStringOrRefType_Type() {
        return (EAttribute)getStringOrRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStyleType() {
        if (styleTypeEClass == null) {
            styleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(366);
        }
        return styleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStyleType_FeatureStyle() {
        return (EReference)getStyleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStyleType_GraphStyle() {
        return (EReference)getStyleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStyleVariationType() {
        if (styleVariationTypeEClass == null) {
            styleVariationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(367);
        }
        return styleVariationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStyleVariationType_Value() {
        return (EAttribute)getStyleVariationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStyleVariationType_FeaturePropertyRange() {
        return (EAttribute)getStyleVariationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStyleVariationType_StyleProperty() {
        return (EAttribute)getStyleVariationType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSurfaceArrayPropertyType() {
        if (surfaceArrayPropertyTypeEClass == null) {
            surfaceArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(370);
        }
        return surfaceArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfaceArrayPropertyType_SurfaceGroup() {
        return (EAttribute)getSurfaceArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSurfaceArrayPropertyType_Surface() {
        return (EReference)getSurfaceArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSurfacePatchArrayPropertyType() {
        if (surfacePatchArrayPropertyTypeEClass == null) {
            surfacePatchArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(373);
        }
        return surfacePatchArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePatchArrayPropertyType_Group() {
        return (EAttribute)getSurfacePatchArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePatchArrayPropertyType_SurfacePatchGroup() {
        return (EAttribute)getSurfacePatchArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSurfacePatchArrayPropertyType_SurfacePatch() {
        return (EReference)getSurfacePatchArrayPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSurfacePropertyType() {
        if (surfacePropertyTypeEClass == null) {
            surfacePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(374);
        }
        return surfacePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_SurfaceGroup() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSurfacePropertyType_Surface() {
        return (EReference)getSurfacePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_Actuate() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_Arcrole() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_Href() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_RemoteSchema() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_Role() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_Show() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_Title() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfacePropertyType_Type() {
        return (EAttribute)getSurfacePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSurfaceType() {
        if (surfaceTypeEClass == null) {
            surfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(375);
        }
        return surfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurfaceType_PatchesGroup() {
        return (EAttribute)getSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSurfaceType_Patches() {
        return (EReference)getSurfaceType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSymbolType() {
        if (symbolTypeEClass == null) {
            symbolTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(376);
        }
        return symbolTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Any() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_About() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Actuate() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Arcrole() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Href() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_RemoteSchema() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Role() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Show() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_SymbolType() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Title() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Transform() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSymbolType_Type() {
        return (EAttribute)getSymbolType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTargetPropertyType() {
        if (targetPropertyTypeEClass == null) {
            targetPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(379);
        }
        return targetPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_FeatureGroup() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTargetPropertyType_Feature() {
        return (EReference)getTargetPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_GeometryGroup() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTargetPropertyType_Geometry() {
        return (EReference)getTargetPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_Actuate() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_Arcrole() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_Href() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_RemoteSchema() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_Role() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_Show() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_Title() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTargetPropertyType_Type() {
        return (EAttribute)getTargetPropertyType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalCRSRefType() {
        if (temporalCRSRefTypeEClass == null) {
            temporalCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(380);
        }
        return temporalCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalCRSRefType_TemporalCRS() {
        return (EReference)getTemporalCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_Actuate() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_Arcrole() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_Href() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_RemoteSchema() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_Role() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_Show() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_Title() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCRSRefType_Type() {
        return (EAttribute)getTemporalCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalCRSType() {
        if (temporalCRSTypeEClass == null) {
            temporalCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(381);
        }
        return temporalCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalCRSType_UsesTemporalCS() {
        return (EReference)getTemporalCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalCRSType_UsesTemporalDatum() {
        return (EReference)getTemporalCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalCSRefType() {
        if (temporalCSRefTypeEClass == null) {
            temporalCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(382);
        }
        return temporalCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalCSRefType_TemporalCS() {
        return (EReference)getTemporalCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_Actuate() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_Arcrole() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_Href() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_RemoteSchema() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_Role() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_Show() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_Title() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalCSRefType_Type() {
        return (EAttribute)getTemporalCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalCSType() {
        if (temporalCSTypeEClass == null) {
            temporalCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(383);
        }
        return temporalCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalDatumBaseType() {
        if (temporalDatumBaseTypeEClass == null) {
            temporalDatumBaseTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(384);
        }
        return temporalDatumBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalDatumRefType() {
        if (temporalDatumRefTypeEClass == null) {
            temporalDatumRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(385);
        }
        return temporalDatumRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalDatumRefType_TemporalDatum() {
        return (EReference)getTemporalDatumRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_Actuate() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_Arcrole() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_Href() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_RemoteSchema() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_Role() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_Show() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_Title() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumRefType_Type() {
        return (EAttribute)getTemporalDatumRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalDatumType() {
        if (temporalDatumTypeEClass == null) {
            temporalDatumTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(386);
        }
        return temporalDatumTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalDatumType_Origin() {
        return (EAttribute)getTemporalDatumType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeCalendarEraPropertyType() {
        if (timeCalendarEraPropertyTypeEClass == null) {
            timeCalendarEraPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(387);
        }
        return timeCalendarEraPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCalendarEraPropertyType_TimeCalendarEra() {
        return (EReference)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_Actuate() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_Arcrole() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_Href() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_RemoteSchema() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_Role() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_Show() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_Title() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraPropertyType_Type() {
        return (EAttribute)getTimeCalendarEraPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeCalendarEraType() {
        if (timeCalendarEraTypeEClass == null) {
            timeCalendarEraTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(388);
        }
        return timeCalendarEraTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCalendarEraType_ReferenceEvent() {
        return (EReference)getTimeCalendarEraType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraType_ReferenceDate() {
        return (EAttribute)getTimeCalendarEraType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarEraType_JulianReference() {
        return (EAttribute)getTimeCalendarEraType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCalendarEraType_EpochOfUse() {
        return (EReference)getTimeCalendarEraType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeCalendarPropertyType() {
        if (timeCalendarPropertyTypeEClass == null) {
            timeCalendarPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(389);
        }
        return timeCalendarPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCalendarPropertyType_TimeCalendar() {
        return (EReference)getTimeCalendarPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_Actuate() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_Arcrole() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_Href() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_RemoteSchema() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_Role() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_Show() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_Title() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeCalendarPropertyType_Type() {
        return (EAttribute)getTimeCalendarPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeCalendarType() {
        if (timeCalendarTypeEClass == null) {
            timeCalendarTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(390);
        }
        return timeCalendarTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCalendarType_ReferenceFrame() {
        return (EReference)getTimeCalendarType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeClockPropertyType() {
        if (timeClockPropertyTypeEClass == null) {
            timeClockPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(391);
        }
        return timeClockPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeClockPropertyType_TimeClock() {
        return (EReference)getTimeClockPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_Actuate() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_Arcrole() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_Href() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_RemoteSchema() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_Role() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_Show() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_Title() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockPropertyType_Type() {
        return (EAttribute)getTimeClockPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeClockType() {
        if (timeClockTypeEClass == null) {
            timeClockTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(392);
        }
        return timeClockTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeClockType_ReferenceEvent() {
        return (EReference)getTimeClockType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockType_ReferenceTime() {
        return (EAttribute)getTimeClockType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeClockType_UtcReference() {
        return (EAttribute)getTimeClockType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeClockType_DateBasis() {
        return (EReference)getTimeClockType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeCoordinateSystemType() {
        if (timeCoordinateSystemTypeEClass == null) {
            timeCoordinateSystemTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(393);
        }
        return timeCoordinateSystemTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCoordinateSystemType_OriginPosition() {
        return (EReference)getTimeCoordinateSystemType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCoordinateSystemType_Origin() {
        return (EReference)getTimeCoordinateSystemType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeCoordinateSystemType_Interval() {
        return (EReference)getTimeCoordinateSystemType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeEdgePropertyType() {
        if (timeEdgePropertyTypeEClass == null) {
            timeEdgePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(394);
        }
        return timeEdgePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeEdgePropertyType_TimeEdge() {
        return (EReference)getTimeEdgePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_Actuate() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_Arcrole() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_Href() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_RemoteSchema() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_Role() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_Show() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_Title() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeEdgePropertyType_Type() {
        return (EAttribute)getTimeEdgePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeEdgeType() {
        if (timeEdgeTypeEClass == null) {
            timeEdgeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(395);
        }
        return timeEdgeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeEdgeType_Start() {
        return (EReference)getTimeEdgeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeEdgeType_End() {
        return (EReference)getTimeEdgeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeEdgeType_Extent() {
        return (EReference)getTimeEdgeType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeGeometricPrimitivePropertyType() {
        if (timeGeometricPrimitivePropertyTypeEClass == null) {
            timeGeometricPrimitivePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(396);
        }
        return timeGeometricPrimitivePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_TimeGeometricPrimitiveGroup() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeGeometricPrimitivePropertyType_TimeGeometricPrimitive() {
        return (EReference)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_Actuate() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_Arcrole() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_Href() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_RemoteSchema() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_Role() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_Show() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_Title() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeGeometricPrimitivePropertyType_Type() {
        return (EAttribute)getTimeGeometricPrimitivePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeInstantPropertyType() {
        if (timeInstantPropertyTypeEClass == null) {
            timeInstantPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(399);
        }
        return timeInstantPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeInstantPropertyType_TimeInstant() {
        return (EReference)getTimeInstantPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_Actuate() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_Arcrole() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_Href() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_RemoteSchema() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_Role() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_Show() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_Title() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeInstantPropertyType_Type() {
        return (EAttribute)getTimeInstantPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeInstantType() {
        if (timeInstantTypeEClass == null) {
            timeInstantTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(400);
        }
        return timeInstantTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeInstantType_TimePosition() {
        return (EReference)getTimeInstantType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeIntervalLengthType() {
        if (timeIntervalLengthTypeEClass == null) {
            timeIntervalLengthTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(401);
        }
        return timeIntervalLengthTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeIntervalLengthType_Value() {
        return (EAttribute)getTimeIntervalLengthType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeIntervalLengthType_Factor() {
        return (EAttribute)getTimeIntervalLengthType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeIntervalLengthType_Radix() {
        return (EAttribute)getTimeIntervalLengthType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeIntervalLengthType_Unit() {
        return (EAttribute)getTimeIntervalLengthType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeNodePropertyType() {
        if (timeNodePropertyTypeEClass == null) {
            timeNodePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(402);
        }
        return timeNodePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeNodePropertyType_TimeNode() {
        return (EReference)getTimeNodePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_Actuate() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_Arcrole() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_Href() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_RemoteSchema() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_Role() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_Show() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_Title() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeNodePropertyType_Type() {
        return (EAttribute)getTimeNodePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeNodeType() {
        if (timeNodeTypeEClass == null) {
            timeNodeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(403);
        }
        return timeNodeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeNodeType_PreviousEdge() {
        return (EReference)getTimeNodeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeNodeType_NextEdge() {
        return (EReference)getTimeNodeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeNodeType_Position() {
        return (EReference)getTimeNodeType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeOrdinalEraPropertyType() {
        if (timeOrdinalEraPropertyTypeEClass == null) {
            timeOrdinalEraPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(404);
        }
        return timeOrdinalEraPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalEraPropertyType_TimeOrdinalEra() {
        return (EReference)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_Actuate() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_Arcrole() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_Href() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_RemoteSchema() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_Role() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_Show() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_Title() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeOrdinalEraPropertyType_Type() {
        return (EAttribute)getTimeOrdinalEraPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeOrdinalEraType() {
        if (timeOrdinalEraTypeEClass == null) {
            timeOrdinalEraTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(405);
        }
        return timeOrdinalEraTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalEraType_RelatedTime() {
        return (EReference)getTimeOrdinalEraType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalEraType_Start() {
        return (EReference)getTimeOrdinalEraType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalEraType_End() {
        return (EReference)getTimeOrdinalEraType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalEraType_Extent() {
        return (EReference)getTimeOrdinalEraType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalEraType_Member() {
        return (EReference)getTimeOrdinalEraType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalEraType_Group() {
        return (EReference)getTimeOrdinalEraType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeOrdinalReferenceSystemType() {
        if (timeOrdinalReferenceSystemTypeEClass == null) {
            timeOrdinalReferenceSystemTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(406);
        }
        return timeOrdinalReferenceSystemTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeOrdinalReferenceSystemType_Component() {
        return (EReference)getTimeOrdinalReferenceSystemType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimePeriodPropertyType() {
        if (timePeriodPropertyTypeEClass == null) {
            timePeriodPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(407);
        }
        return timePeriodPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimePeriodPropertyType_TimePeriod() {
        return (EReference)getTimePeriodPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_Actuate() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_Arcrole() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_Href() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_RemoteSchema() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_Role() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_Show() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_Title() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodPropertyType_Type() {
        return (EAttribute)getTimePeriodPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimePeriodType() {
        if (timePeriodTypeEClass == null) {
            timePeriodTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(408);
        }
        return timePeriodTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimePeriodType_BeginPosition() {
        return (EReference)getTimePeriodType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimePeriodType_Begin() {
        return (EReference)getTimePeriodType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimePeriodType_EndPosition() {
        return (EReference)getTimePeriodType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimePeriodType_End() {
        return (EReference)getTimePeriodType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodType_Duration() {
        return (EAttribute)getTimePeriodType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimePeriodType_TimeInterval() {
        return (EReference)getTimePeriodType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimePositionType() {
        if (timePositionTypeEClass == null) {
            timePositionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(409);
        }
        return timePositionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePositionType_Value() {
        return (EAttribute)getTimePositionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePositionType_CalendarEraName() {
        return (EAttribute)getTimePositionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePositionType_Frame() {
        return (EAttribute)getTimePositionType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePositionType_IndeterminatePosition() {
        return (EAttribute)getTimePositionType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimePrimitivePropertyType() {
        if (timePrimitivePropertyTypeEClass == null) {
            timePrimitivePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(411);
        }
        return timePrimitivePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_TimePrimitiveGroup() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimePrimitivePropertyType_TimePrimitive() {
        return (EReference)getTimePrimitivePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_Actuate() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_Arcrole() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_Href() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_RemoteSchema() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_Role() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_Show() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_Title() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePrimitivePropertyType_Type() {
        return (EAttribute)getTimePrimitivePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeTopologyComplexPropertyType() {
        if (timeTopologyComplexPropertyTypeEClass == null) {
            timeTopologyComplexPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(412);
        }
        return timeTopologyComplexPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeTopologyComplexPropertyType_TimeTopologyComplex() {
        return (EReference)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_Actuate() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_Arcrole() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_Href() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_RemoteSchema() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_Role() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_Show() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_Title() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyComplexPropertyType_Type() {
        return (EAttribute)getTimeTopologyComplexPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeTopologyComplexType() {
        if (timeTopologyComplexTypeEClass == null) {
            timeTopologyComplexTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(413);
        }
        return timeTopologyComplexTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeTopologyComplexType_Primitive() {
        return (EReference)getTimeTopologyComplexType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeTopologyPrimitivePropertyType() {
        if (timeTopologyPrimitivePropertyTypeEClass == null) {
            timeTopologyPrimitivePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(414);
        }
        return timeTopologyPrimitivePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_TimeTopologyPrimitiveGroup() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeTopologyPrimitivePropertyType_TimeTopologyPrimitive() {
        return (EReference)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_Actuate() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_Arcrole() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_Href() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_RemoteSchema() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_Role() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_Show() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_Title() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeTopologyPrimitivePropertyType_Type() {
        return (EAttribute)getTimeTopologyPrimitivePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeType() {
        if (timeTypeEClass == null) {
            timeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(415);
        }
        return timeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTinType() {
        if (tinTypeEClass == null) {
            tinTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(420);
        }
        return tinTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTinType_StopLines() {
        return (EReference)getTinType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTinType_BreakLines() {
        return (EReference)getTinType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTinType_MaxLength() {
        return (EReference)getTinType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTinType_ControlPoint() {
        return (EReference)getTinType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoComplexMemberType() {
        if (topoComplexMemberTypeEClass == null) {
            topoComplexMemberTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(421);
        }
        return topoComplexMemberTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoComplexMemberType_TopoComplex() {
        return (EReference)getTopoComplexMemberType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_Actuate() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_Arcrole() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_Href() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_RemoteSchema() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_Role() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_Show() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_Title() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexMemberType_Type() {
        return (EAttribute)getTopoComplexMemberType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoComplexType() {
        if (topoComplexTypeEClass == null) {
            topoComplexTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(422);
        }
        return topoComplexTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoComplexType_MaximalComplex() {
        return (EReference)getTopoComplexType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoComplexType_SuperComplex() {
        return (EReference)getTopoComplexType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoComplexType_SubComplex() {
        return (EReference)getTopoComplexType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoComplexType_TopoPrimitiveMember() {
        return (EReference)getTopoComplexType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoComplexType_TopoPrimitiveMembers() {
        return (EReference)getTopoComplexType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoComplexType_IsMaximal() {
        return (EAttribute)getTopoComplexType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoCurvePropertyType() {
        if (topoCurvePropertyTypeEClass == null) {
            topoCurvePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(423);
        }
        return topoCurvePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoCurvePropertyType_TopoCurve() {
        return (EReference)getTopoCurvePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoCurveType() {
        if (topoCurveTypeEClass == null) {
            topoCurveTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(424);
        }
        return topoCurveTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoCurveType_DirectedEdge() {
        return (EReference)getTopoCurveType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopologyStylePropertyType() {
        if (topologyStylePropertyTypeEClass == null) {
            topologyStylePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(425);
        }
        return topologyStylePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopologyStylePropertyType_TopologyStyle() {
        return (EReference)getTopologyStylePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_About() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_Actuate() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_Arcrole() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_Href() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_RemoteSchema() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_Role() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_Show() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_Title() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStylePropertyType_Type() {
        return (EAttribute)getTopologyStylePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopologyStyleType() {
        if (topologyStyleTypeEClass == null) {
            topologyStyleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(426);
        }
        return topologyStyleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopologyStyleType_Symbol() {
        return (EReference)getTopologyStyleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStyleType_Style() {
        return (EAttribute)getTopologyStyleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopologyStyleType_LabelStyle() {
        return (EReference)getTopologyStyleType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStyleType_TopologyProperty() {
        return (EAttribute)getTopologyStyleType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopologyStyleType_TopologyType() {
        return (EAttribute)getTopologyStyleType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoPointPropertyType() {
        if (topoPointPropertyTypeEClass == null) {
            topoPointPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(427);
        }
        return topoPointPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoPointPropertyType_TopoPoint() {
        return (EReference)getTopoPointPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoPointType() {
        if (topoPointTypeEClass == null) {
            topoPointTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(428);
        }
        return topoPointTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoPointType_DirectedNode() {
        return (EReference)getTopoPointType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoPrimitiveArrayAssociationType() {
        if (topoPrimitiveArrayAssociationTypeEClass == null) {
            topoPrimitiveArrayAssociationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(429);
        }
        return topoPrimitiveArrayAssociationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveArrayAssociationType_Group() {
        return (EAttribute)getTopoPrimitiveArrayAssociationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveArrayAssociationType_TopoPrimitiveGroup() {
        return (EAttribute)getTopoPrimitiveArrayAssociationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoPrimitiveArrayAssociationType_TopoPrimitive() {
        return (EReference)getTopoPrimitiveArrayAssociationType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoPrimitiveMemberType() {
        if (topoPrimitiveMemberTypeEClass == null) {
            topoPrimitiveMemberTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(430);
        }
        return topoPrimitiveMemberTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_TopoPrimitiveGroup() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoPrimitiveMemberType_TopoPrimitive() {
        return (EReference)getTopoPrimitiveMemberType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_Actuate() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_Arcrole() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_Href() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_RemoteSchema() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_Role() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_Show() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_Title() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTopoPrimitiveMemberType_Type() {
        return (EAttribute)getTopoPrimitiveMemberType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoSolidType() {
        if (topoSolidTypeEClass == null) {
            topoSolidTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(431);
        }
        return topoSolidTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoSolidType_DirectedFace() {
        return (EReference)getTopoSolidType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoSurfacePropertyType() {
        if (topoSurfacePropertyTypeEClass == null) {
            topoSurfacePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(432);
        }
        return topoSurfacePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoSurfacePropertyType_TopoSurface() {
        return (EReference)getTopoSurfacePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoSurfaceType() {
        if (topoSurfaceTypeEClass == null) {
            topoSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(433);
        }
        return topoSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoSurfaceType_DirectedFace() {
        return (EReference)getTopoSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoVolumePropertyType() {
        if (topoVolumePropertyTypeEClass == null) {
            topoVolumePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(434);
        }
        return topoVolumePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoVolumePropertyType_TopoVolume() {
        return (EReference)getTopoVolumePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTopoVolumeType() {
        if (topoVolumeTypeEClass == null) {
            topoVolumeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(435);
        }
        return topoVolumeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTopoVolumeType_DirectedTopoSolid() {
        return (EReference)getTopoVolumeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTrackType() {
        if (trackTypeEClass == null) {
            trackTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(436);
        }
        return trackTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTrackType_MovingObjectStatus() {
        return (EReference)getTrackType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTransformationRefType() {
        if (transformationRefTypeEClass == null) {
            transformationRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(437);
        }
        return transformationRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransformationRefType_Transformation() {
        return (EReference)getTransformationRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_Actuate() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_Arcrole() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_Href() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_RemoteSchema() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_Role() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_Show() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_Title() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransformationRefType_Type() {
        return (EAttribute)getTransformationRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTransformationType() {
        if (transformationTypeEClass == null) {
            transformationTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(438);
        }
        return transformationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransformationType_UsesMethod() {
        return (EReference)getTransformationType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransformationType_UsesValue() {
        return (EReference)getTransformationType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTrianglePatchArrayPropertyType() {
        if (trianglePatchArrayPropertyTypeEClass == null) {
            trianglePatchArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(439);
        }
        return trianglePatchArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTrianglePatchArrayPropertyType_Triangle() {
        return (EReference)getTrianglePatchArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTriangleType() {
        if (triangleTypeEClass == null) {
            triangleTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(440);
        }
        return triangleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTriangleType_ExteriorGroup() {
        return (EAttribute)getTriangleType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTriangleType_Exterior() {
        return (EReference)getTriangleType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTriangleType_Interpolation() {
        return (EAttribute)getTriangleType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTriangulatedSurfaceType() {
        if (triangulatedSurfaceTypeEClass == null) {
            triangulatedSurfaceTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(441);
        }
        return triangulatedSurfaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTriangulatedSurfaceType_TrianglePatches() {
        return (EReference)getTriangulatedSurfaceType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUnitDefinitionType() {
        if (unitDefinitionTypeEClass == null) {
            unitDefinitionTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(442);
        }
        return unitDefinitionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnitDefinitionType_QuantityType() {
        return (EReference)getUnitDefinitionType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnitDefinitionType_CatalogSymbol() {
        return (EReference)getUnitDefinitionType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUnitOfMeasureType() {
        if (unitOfMeasureTypeEClass == null) {
            unitOfMeasureTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(443);
        }
        return unitOfMeasureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnitOfMeasureType_Uom() {
        return (EAttribute)getUnitOfMeasureType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUserDefinedCSRefType() {
        if (userDefinedCSRefTypeEClass == null) {
            userDefinedCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(444);
        }
        return userDefinedCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUserDefinedCSRefType_UserDefinedCS() {
        return (EReference)getUserDefinedCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_Actuate() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_Arcrole() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_Href() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_RemoteSchema() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_Role() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_Show() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_Title() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUserDefinedCSRefType_Type() {
        return (EAttribute)getUserDefinedCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUserDefinedCSType() {
        if (userDefinedCSTypeEClass == null) {
            userDefinedCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(445);
        }
        return userDefinedCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValueArrayPropertyType() {
        if (valueArrayPropertyTypeEClass == null) {
            valueArrayPropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(446);
        }
        return valueArrayPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_Value() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_Boolean() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_Category() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_Quantity() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_Count() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_BooleanList() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_CategoryList() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_QuantityList() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_CountList() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_CategoryExtent() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_QuantityExtent() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_CountExtent() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_CompositeValueGroup() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_CompositeValue() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_ObjectGroup() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueArrayPropertyType_Object() {
        return (EReference)getValueArrayPropertyType().getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayPropertyType_Null() {
        return (EAttribute)getValueArrayPropertyType().getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValueArrayType() {
        if (valueArrayTypeEClass == null) {
            valueArrayTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(447);
        }
        return valueArrayTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayType_CodeSpace() {
        return (EAttribute)getValueArrayType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueArrayType_Uom() {
        return (EAttribute)getValueArrayType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValuePropertyType() {
        if (valuePropertyTypeEClass == null) {
            valuePropertyTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(448);
        }
        return valuePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Boolean() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_Category() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_Quantity() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Count() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_BooleanList() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_CategoryList() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_QuantityList() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_CountList() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_CategoryExtent() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_QuantityExtent() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_CountExtent() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_CompositeValueGroup() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_CompositeValue() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_ObjectGroup() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValuePropertyType_Object() {
        return (EReference)getValuePropertyType().getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Null() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Actuate() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Arcrole() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Href() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_RemoteSchema() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Role() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Show() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Title() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuePropertyType_Type() {
        return (EAttribute)getValuePropertyType().getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVectorType() {
        if (vectorTypeEClass == null) {
            vectorTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(449);
        }
        return vectorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVectorType_Value() {
        return (EAttribute)getVectorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVectorType_AxisLabels() {
        return (EAttribute)getVectorType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVectorType_SrsDimension() {
        return (EAttribute)getVectorType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVectorType_SrsName() {
        return (EAttribute)getVectorType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVectorType_UomLabels() {
        return (EAttribute)getVectorType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVerticalCRSRefType() {
        if (verticalCRSRefTypeEClass == null) {
            verticalCRSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(450);
        }
        return verticalCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getVerticalCRSRefType_VerticalCRS() {
        return (EReference)getVerticalCRSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_Actuate() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_Arcrole() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_Href() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_RemoteSchema() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_Role() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_Show() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_Title() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCRSRefType_Type() {
        return (EAttribute)getVerticalCRSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVerticalCRSType() {
        if (verticalCRSTypeEClass == null) {
            verticalCRSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(451);
        }
        return verticalCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getVerticalCRSType_UsesVerticalCS() {
        return (EReference)getVerticalCRSType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getVerticalCRSType_UsesVerticalDatum() {
        return (EReference)getVerticalCRSType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVerticalCSRefType() {
        if (verticalCSRefTypeEClass == null) {
            verticalCSRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(452);
        }
        return verticalCSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getVerticalCSRefType_VerticalCS() {
        return (EReference)getVerticalCSRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_Actuate() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_Arcrole() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_Href() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_RemoteSchema() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_Role() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_Show() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_Title() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalCSRefType_Type() {
        return (EAttribute)getVerticalCSRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVerticalCSType() {
        if (verticalCSTypeEClass == null) {
            verticalCSTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(453);
        }
        return verticalCSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVerticalDatumRefType() {
        if (verticalDatumRefTypeEClass == null) {
            verticalDatumRefTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(454);
        }
        return verticalDatumRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getVerticalDatumRefType_VerticalDatum() {
        return (EReference)getVerticalDatumRefType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_Actuate() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_Arcrole() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_Href() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_RemoteSchema() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_Role() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_Show() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_Title() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVerticalDatumRefType_Type() {
        return (EAttribute)getVerticalDatumRefType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVerticalDatumType() {
        if (verticalDatumTypeEClass == null) {
            verticalDatumTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(455);
        }
        return verticalDatumTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getVerticalDatumType_VerticalDatumType() {
        return (EReference)getVerticalDatumType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVerticalDatumTypeType() {
        if (verticalDatumTypeTypeEClass == null) {
            verticalDatumTypeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(456);
        }
        return verticalDatumTypeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVolumeType() {
        if (volumeTypeEClass == null) {
            volumeTypeEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(457);
        }
        return volumeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getAesheticCriteriaType() {
        if (aesheticCriteriaTypeEEnum == null) {
            aesheticCriteriaTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(45);
        }
        return aesheticCriteriaTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getCompassPointEnumeration() {
        if (compassPointEnumerationEEnum == null) {
            compassPointEnumerationEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(83);
        }
        return compassPointEnumerationEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getCurveInterpolationType() {
        if (curveInterpolationTypeEEnum == null) {
            curveInterpolationTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(119);
        }
        return curveInterpolationTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getDirectionTypeMember0() {
        if (directionTypeMember0EEnum == null) {
            directionTypeMember0EEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(150);
        }
        return directionTypeMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getDrawingTypeType() {
        if (drawingTypeTypeEEnum == null) {
            drawingTypeTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(162);
        }
        return drawingTypeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFileValueModelType() {
        if (fileValueModelTypeEEnum == null) {
            fileValueModelTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(186);
        }
        return fileValueModelTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getGraphTypeType() {
        if (graphTypeTypeEEnum == null) {
            graphTypeTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(209);
        }
        return graphTypeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getIncrementOrder() {
        if (incrementOrderEEnum == null) {
            incrementOrderEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(224);
        }
        return incrementOrderEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getIsSphereType() {
        if (isSphereTypeEEnum == null) {
            isSphereTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(232);
        }
        return isSphereTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getKnotTypesType() {
        if (knotTypesTypeEEnum == null) {
            knotTypesTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(236);
        }
        return knotTypesTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getLineTypeType() {
        if (lineTypeTypeEEnum == null) {
            lineTypeTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(250);
        }
        return lineTypeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getNullEnumerationMember0() {
        if (nullEnumerationMember0EEnum == null) {
            nullEnumerationMember0EEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(286);
        }
        return nullEnumerationMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getQueryGrammarEnumeration() {
        if (queryGrammarEnumerationEEnum == null) {
            queryGrammarEnumerationEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(330);
        }
        return queryGrammarEnumerationEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getRelativePositionType() {
        if (relativePositionTypeEEnum == null) {
            relativePositionTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(343);
        }
        return relativePositionTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSequenceRuleNames() {
        if (sequenceRuleNamesEEnum == null) {
            sequenceRuleNamesEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(351);
        }
        return sequenceRuleNamesEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSignType() {
        if (signTypeEEnum == null) {
            signTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(354);
        }
        return signTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSuccessionType() {
        if (successionTypeEEnum == null) {
            successionTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(368);
        }
        return successionTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSurfaceInterpolationType() {
        if (surfaceInterpolationTypeEEnum == null) {
            surfaceInterpolationTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(371);
        }
        return surfaceInterpolationTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSymbolTypeEnumeration() {
        if (symbolTypeEnumerationEEnum == null) {
            symbolTypeEnumerationEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(377);
        }
        return symbolTypeEnumerationEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getTimeIndeterminateValueType() {
        if (timeIndeterminateValueTypeEEnum == null) {
            timeIndeterminateValueTypeEEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(397);
        }
        return timeIndeterminateValueTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getTimeUnitTypeMember0() {
        if (timeUnitTypeMember0EEnum == null) {
            timeUnitTypeMember0EEnum = (EEnum)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(417);
        }
        return timeUnitTypeMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAesheticCriteriaTypeObject() {
        if (aesheticCriteriaTypeObjectEDataType == null) {
            aesheticCriteriaTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(46);
        }
        return aesheticCriteriaTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getArcMinutesType() {
        if (arcMinutesTypeEDataType == null) {
            arcMinutesTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(52);
        }
        return arcMinutesTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getArcSecondsType() {
        if (arcSecondsTypeEDataType == null) {
            arcSecondsTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(53);
        }
        return arcSecondsTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getBooleanList() {
        if (booleanListEDataType == null) {
            booleanListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(65);
        }
        return booleanListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getBooleanOrNull() {
        if (booleanOrNullEDataType == null) {
            booleanOrNullEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(66);
        }
        return booleanOrNullEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getBooleanOrNullList() {
        if (booleanOrNullListEDataType == null) {
            booleanOrNullListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(67);
        }
        return booleanOrNullListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getCalDate() {
        if (calDateEDataType == null) {
            calDateEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(72);
        }
        return calDateEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getCompassPointEnumerationObject() {
        if (compassPointEnumerationObjectEDataType == null) {
            compassPointEnumerationObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(84);
        }
        return compassPointEnumerationObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getCountExtentType() {
        if (countExtentTypeEDataType == null) {
            countExtentTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(111);
        }
        return countExtentTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getCurveInterpolationTypeObject() {
        if (curveInterpolationTypeObjectEDataType == null) {
            curveInterpolationTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(120);
        }
        return curveInterpolationTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDecimalMinutesType() {
        if (decimalMinutesTypeEDataType == null) {
            decimalMinutesTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(129);
        }
        return decimalMinutesTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDegreeValueType() {
        if (degreeValueTypeEDataType == null) {
            degreeValueTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(134);
        }
        return degreeValueTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDirectionType() {
        if (directionTypeEDataType == null) {
            directionTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(149);
        }
        return directionTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDirectionTypeMember0Object() {
        if (directionTypeMember0ObjectEDataType == null) {
            directionTypeMember0ObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(151);
        }
        return directionTypeMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDirectionTypeMember1() {
        if (directionTypeMember1EDataType == null) {
            directionTypeMember1EDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(152);
        }
        return directionTypeMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDoubleList() {
        if (doubleListEDataType == null) {
            doubleListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(159);
        }
        return doubleListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDoubleOrNull() {
        if (doubleOrNullEDataType == null) {
            doubleOrNullEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(160);
        }
        return doubleOrNullEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDoubleOrNullList() {
        if (doubleOrNullListEDataType == null) {
            doubleOrNullListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(161);
        }
        return doubleOrNullListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDrawingTypeTypeObject() {
        if (drawingTypeTypeObjectEDataType == null) {
            drawingTypeTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(163);
        }
        return drawingTypeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFileValueModelTypeObject() {
        if (fileValueModelTypeObjectEDataType == null) {
            fileValueModelTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(187);
        }
        return fileValueModelTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getGraphTypeTypeObject() {
        if (graphTypeTypeObjectEDataType == null) {
            graphTypeTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(210);
        }
        return graphTypeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getIncrementOrderObject() {
        if (incrementOrderObjectEDataType == null) {
            incrementOrderObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(225);
        }
        return incrementOrderObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getIntegerList() {
        if (integerListEDataType == null) {
            integerListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(228);
        }
        return integerListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getIntegerOrNull() {
        if (integerOrNullEDataType == null) {
            integerOrNullEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(229);
        }
        return integerOrNullEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getIntegerOrNullList() {
        if (integerOrNullListEDataType == null) {
            integerOrNullListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(230);
        }
        return integerOrNullListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getIsSphereTypeObject() {
        if (isSphereTypeObjectEDataType == null) {
            isSphereTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(233);
        }
        return isSphereTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getKnotTypesTypeObject() {
        if (knotTypesTypeObjectEDataType == null) {
            knotTypesTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(237);
        }
        return knotTypesTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getLineTypeTypeObject() {
        if (lineTypeTypeObjectEDataType == null) {
            lineTypeTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(251);
        }
        return lineTypeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNameList() {
        if (nameListEDataType == null) {
            nameListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(280);
        }
        return nameListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNameOrNull() {
        if (nameOrNullEDataType == null) {
            nameOrNullEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(281);
        }
        return nameOrNullEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNameOrNullList() {
        if (nameOrNullListEDataType == null) {
            nameOrNullListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(282);
        }
        return nameOrNullListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNCNameList() {
        if (ncNameListEDataType == null) {
            ncNameListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(283);
        }
        return ncNameListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNullEnumeration() {
        if (nullEnumerationEDataType == null) {
            nullEnumerationEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(285);
        }
        return nullEnumerationEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNullEnumerationMember0Object() {
        if (nullEnumerationMember0ObjectEDataType == null) {
            nullEnumerationMember0ObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(287);
        }
        return nullEnumerationMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNullEnumerationMember1() {
        if (nullEnumerationMember1EDataType == null) {
            nullEnumerationMember1EDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(288);
        }
        return nullEnumerationMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNullType() {
        if (nullTypeEDataType == null) {
            nullTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(289);
        }
        return nullTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getQNameList() {
        if (qNameListEDataType == null) {
            qNameListEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(327);
        }
        return qNameListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getQueryGrammarEnumerationObject() {
        if (queryGrammarEnumerationObjectEDataType == null) {
            queryGrammarEnumerationObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(331);
        }
        return queryGrammarEnumerationObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getRelativePositionTypeObject() {
        if (relativePositionTypeObjectEDataType == null) {
            relativePositionTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(344);
        }
        return relativePositionTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSequenceRuleNamesObject() {
        if (sequenceRuleNamesObjectEDataType == null) {
            sequenceRuleNamesObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(352);
        }
        return sequenceRuleNamesObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSignTypeObject() {
        if (signTypeObjectEDataType == null) {
            signTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(355);
        }
        return signTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getStringOrNull() {
        if (stringOrNullEDataType == null) {
            stringOrNullEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(364);
        }
        return stringOrNullEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSuccessionTypeObject() {
        if (successionTypeObjectEDataType == null) {
            successionTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(369);
        }
        return successionTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSurfaceInterpolationTypeObject() {
        if (surfaceInterpolationTypeObjectEDataType == null) {
            surfaceInterpolationTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(372);
        }
        return surfaceInterpolationTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSymbolTypeEnumerationObject() {
        if (symbolTypeEnumerationObjectEDataType == null) {
            symbolTypeEnumerationObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(378);
        }
        return symbolTypeEnumerationObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTimeIndeterminateValueTypeObject() {
        if (timeIndeterminateValueTypeObjectEDataType == null) {
            timeIndeterminateValueTypeObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(398);
        }
        return timeIndeterminateValueTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTimePositionUnion() {
        if (timePositionUnionEDataType == null) {
            timePositionUnionEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(410);
        }
        return timePositionUnionEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTimeUnitType() {
        if (timeUnitTypeEDataType == null) {
            timeUnitTypeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(416);
        }
        return timeUnitTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTimeUnitTypeMember0Object() {
        if (timeUnitTypeMember0ObjectEDataType == null) {
            timeUnitTypeMember0ObjectEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(418);
        }
        return timeUnitTypeMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTimeUnitTypeMember1() {
        if (timeUnitTypeMember1EDataType == null) {
            timeUnitTypeMember1EDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI).getEClassifiers().get(419);
        }
        return timeUnitTypeMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Gml311Factory getGml311Factory() {
        return (Gml311Factory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isLoaded = false;

    /**
     * Laods the package and any sub-packages from their serialized form.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void loadPackage() {
        if (isLoaded) return;
        isLoaded = true;

        URL url = getClass().getResource(packageFilename);
        if (url == null) {
            throw new RuntimeException("Missing serialized package: " + packageFilename);
        }
        URI uri = URI.createURI(url.toString());
        Resource resource = new EcoreResourceFactoryImpl().createResource(uri);
        try {
            resource.load(null);
        }
        catch (IOException exception) {
            throw new WrappedException(exception);
        }
        initializeFromLoadedEPackage(this, (EPackage)resource.getContents().get(0));
        createResource(eNS_URI);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isFixed = false;

    /**
     * Fixes up the loaded package, to make it appear as if it had been programmatically built.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void fixPackageContents() {
        if (isFixed) return;
        isFixed = true;
        fixEClassifiers();
    }

    /**
     * Sets the instance class on the given classifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void fixInstanceClass(EClassifier eClassifier) {
        if (eClassifier.getInstanceClassName() == null) {
            eClassifier.setInstanceClassName("net.opengis.gml311." + eClassifier.getName());
            setGeneratedClassName(eClassifier);
        }
    }

} //Gml311PackageImpl
