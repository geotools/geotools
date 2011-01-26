/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml3;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDSchema;
import org.geotools.gml2.ReferencingDirectiveLeakPreventer;
import org.geotools.gml2.SubstitutionGroupLeakPreventer;
import org.geotools.gml3.smil.SMIL20;
import org.geotools.gml3.smil.SMIL20LANG;
import org.geotools.xlink.XLINK;
import org.geotools.xml.XSD;
import org.opengis.feature.type.Schema;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/gml schema.
 *
 * @generated
 *
 * @source $URL$
 */
public final class GML extends XSD {
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/gml";

    /* Type Definitions */
    /** @generated */
    public static final QName AbsoluteExternalPositionalAccuracyType = new QName("http://www.opengis.net/gml",
            "AbsoluteExternalPositionalAccuracyType");

    /** @generated */
    public static final QName AbstractContinuousCoverageType = new QName("http://www.opengis.net/gml",
            "AbstractContinuousCoverageType");

    /** @generated */
    public static final QName AbstractCoordinateOperationBaseType = new QName("http://www.opengis.net/gml",
            "AbstractCoordinateOperationBaseType");

    /** @generated */
    public static final QName AbstractCoordinateOperationType = new QName("http://www.opengis.net/gml",
            "AbstractCoordinateOperationType");

    /** @generated */
    public static final QName AbstractCoordinateSystemBaseType = new QName("http://www.opengis.net/gml",
            "AbstractCoordinateSystemBaseType");

    /** @generated */
    public static final QName AbstractCoordinateSystemType = new QName("http://www.opengis.net/gml",
            "AbstractCoordinateSystemType");

    /** @generated */
    public static final QName AbstractCoverageType = new QName("http://www.opengis.net/gml",
            "AbstractCoverageType");

    /** @generated */
    public static final QName AbstractCurveSegmentType = new QName("http://www.opengis.net/gml",
            "AbstractCurveSegmentType");

    /** @generated */
    public static final QName AbstractCurveType = new QName("http://www.opengis.net/gml",
            "AbstractCurveType");

    /** @generated */
    public static final QName AbstractDatumBaseType = new QName("http://www.opengis.net/gml",
            "AbstractDatumBaseType");

    /** @generated */
    public static final QName AbstractDatumType = new QName("http://www.opengis.net/gml",
            "AbstractDatumType");

    /** @generated */
    public static final QName AbstractDiscreteCoverageType = new QName("http://www.opengis.net/gml",
            "AbstractDiscreteCoverageType");

    /** @generated */
    public static final QName AbstractFeatureCollectionType = new QName("http://www.opengis.net/gml",
            "AbstractFeatureCollectionType");

    /** @generated */
    public static final QName AbstractFeatureType = new QName("http://www.opengis.net/gml",
            "AbstractFeatureType");

    /** @generated */
    public static final QName AbstractGeneralConversionType = new QName("http://www.opengis.net/gml",
            "AbstractGeneralConversionType");

    /** @generated */
    public static final QName AbstractGeneralDerivedCRSType = new QName("http://www.opengis.net/gml",
            "AbstractGeneralDerivedCRSType");

    /** @generated */
    public static final QName AbstractGeneralOperationParameterRefType = new QName("http://www.opengis.net/gml",
            "AbstractGeneralOperationParameterRefType");

    /** @generated */
    public static final QName AbstractGeneralOperationParameterType = new QName("http://www.opengis.net/gml",
            "AbstractGeneralOperationParameterType");

    /** @generated */
    public static final QName AbstractGeneralParameterValueType = new QName("http://www.opengis.net/gml",
            "AbstractGeneralParameterValueType");

    /** @generated */
    public static final QName AbstractGeneralTransformationType = new QName("http://www.opengis.net/gml",
            "AbstractGeneralTransformationType");

    /** @generated */
    public static final QName AbstractGeometricAggregateType = new QName("http://www.opengis.net/gml",
            "AbstractGeometricAggregateType");

    /** @generated */
    public static final QName AbstractGeometricPrimitiveType = new QName("http://www.opengis.net/gml",
            "AbstractGeometricPrimitiveType");

    /** @generated */
    public static final QName AbstractGeometryType = new QName("http://www.opengis.net/gml",
            "AbstractGeometryType");

    /** @generated */
    public static final QName AbstractGMLType = new QName("http://www.opengis.net/gml",
            "AbstractGMLType");

    /** @generated */
    public static final QName AbstractGriddedSurfaceType = new QName("http://www.opengis.net/gml",
            "AbstractGriddedSurfaceType");

    /** @generated */
    public static final QName AbstractMetaDataType = new QName("http://www.opengis.net/gml",
            "AbstractMetaDataType");

    /** @generated */
    public static final QName AbstractParametricCurveSurfaceType = new QName("http://www.opengis.net/gml",
            "AbstractParametricCurveSurfaceType");

    /** @generated */
    public static final QName AbstractPositionalAccuracyType = new QName("http://www.opengis.net/gml",
            "AbstractPositionalAccuracyType");

    /** @generated */
    public static final QName AbstractReferenceSystemBaseType = new QName("http://www.opengis.net/gml",
            "AbstractReferenceSystemBaseType");

    /** @generated */
    public static final QName AbstractReferenceSystemType = new QName("http://www.opengis.net/gml",
            "AbstractReferenceSystemType");

    /** @generated */
    public static final QName AbstractRingPropertyType = new QName("http://www.opengis.net/gml",
            "AbstractRingPropertyType");

    /** @generated */
    public static final QName AbstractRingType = new QName("http://www.opengis.net/gml",
            "AbstractRingType");

    /** @generated */
    public static final QName AbstractSolidType = new QName("http://www.opengis.net/gml",
            "AbstractSolidType");

    /** @generated */
    public static final QName AbstractStyleType = new QName("http://www.opengis.net/gml",
            "AbstractStyleType");

    /** @generated */
    public static final QName AbstractSurfacePatchType = new QName("http://www.opengis.net/gml",
            "AbstractSurfacePatchType");

    /** @generated */
    public static final QName AbstractSurfaceType = new QName("http://www.opengis.net/gml",
            "AbstractSurfaceType");

    /** @generated */
    public static final QName AbstractTimeComplexType = new QName("http://www.opengis.net/gml",
            "AbstractTimeComplexType");

    /** @generated */
    public static final QName AbstractTimeGeometricPrimitiveType = new QName("http://www.opengis.net/gml",
            "AbstractTimeGeometricPrimitiveType");

    /** @generated */
    public static final QName AbstractTimeObjectType = new QName("http://www.opengis.net/gml",
            "AbstractTimeObjectType");

    /** @generated */
    public static final QName AbstractTimePrimitiveType = new QName("http://www.opengis.net/gml",
            "AbstractTimePrimitiveType");

    /** @generated */
    public static final QName AbstractTimeReferenceSystemType = new QName("http://www.opengis.net/gml",
            "AbstractTimeReferenceSystemType");

    /** @generated */
    public static final QName AbstractTimeSliceType = new QName("http://www.opengis.net/gml",
            "AbstractTimeSliceType");

    /** @generated */
    public static final QName AbstractTimeTopologyPrimitiveType = new QName("http://www.opengis.net/gml",
            "AbstractTimeTopologyPrimitiveType");

    /** @generated */
    public static final QName AbstractTopologyType = new QName("http://www.opengis.net/gml",
            "AbstractTopologyType");

    /** @generated */
    public static final QName AbstractTopoPrimitiveType = new QName("http://www.opengis.net/gml",
            "AbstractTopoPrimitiveType");

    /** @generated */
    public static final QName AesheticCriteriaType = new QName("http://www.opengis.net/gml",
            "AesheticCriteriaType");

    /** @generated */
    public static final QName AffinePlacementType = new QName("http://www.opengis.net/gml",
            "AffinePlacementType");

    /** @generated */
    public static final QName AngleChoiceType = new QName("http://www.opengis.net/gml",
            "AngleChoiceType");

    /** @generated */
    public static final QName AngleType = new QName("http://www.opengis.net/gml", "AngleType");

    /** @generated */
    public static final QName ArcByBulgeType = new QName("http://www.opengis.net/gml",
            "ArcByBulgeType");

    /** @generated */
    public static final QName ArcByCenterPointType = new QName("http://www.opengis.net/gml",
            "ArcByCenterPointType");

    /** @generated */
    public static final QName ArcMinutesType = new QName("http://www.opengis.net/gml",
            "ArcMinutesType");

    /** @generated */
    public static final QName ArcSecondsType = new QName("http://www.opengis.net/gml",
            "ArcSecondsType");

    /** @generated */
    public static final QName ArcStringByBulgeType = new QName("http://www.opengis.net/gml",
            "ArcStringByBulgeType");

    /** @generated */
    public static final QName ArcStringType = new QName("http://www.opengis.net/gml",
            "ArcStringType");

    /** @generated */
    public static final QName ArcType = new QName("http://www.opengis.net/gml", "ArcType");

    /** @generated */
    public static final QName AreaType = new QName("http://www.opengis.net/gml", "AreaType");

    /** @generated */
    public static final QName ArrayAssociationType = new QName("http://www.opengis.net/gml",
            "ArrayAssociationType");

    /** @generated */
    public static final QName ArrayType = new QName("http://www.opengis.net/gml", "ArrayType");

    /** @generated */
    public static final QName AssociationType = new QName("http://www.opengis.net/gml",
            "AssociationType");

    /** @generated */
    public static final QName BagType = new QName("http://www.opengis.net/gml", "BagType");

    /** @generated */
    public static final QName BaseStyleDescriptorType = new QName("http://www.opengis.net/gml",
            "BaseStyleDescriptorType");

    /** @generated */
    public static final QName BaseUnitType = new QName("http://www.opengis.net/gml", "BaseUnitType");

    /** @generated */
    public static final QName BezierType = new QName("http://www.opengis.net/gml", "BezierType");

    /** @generated */
    public static final QName booleanList = new QName("http://www.opengis.net/gml", "booleanList");

    /** @generated */
    public static final QName booleanOrNull = new QName("http://www.opengis.net/gml",
            "booleanOrNull");

    /** @generated */
    public static final QName booleanOrNullList = new QName("http://www.opengis.net/gml",
            "booleanOrNullList");

    /** @generated */
    public static final QName BooleanPropertyType = new QName("http://www.opengis.net/gml",
            "BooleanPropertyType");

    /** @generated */
    public static final QName BoundedFeatureType = new QName("http://www.opengis.net/gml",
            "BoundedFeatureType");

    /** @generated */
    public static final QName BoundingShapeType = new QName("http://www.opengis.net/gml",
            "BoundingShapeType");

    /** @generated */
    public static final QName BSplineType = new QName("http://www.opengis.net/gml", "BSplineType");

    /** @generated */
    public static final QName CalDate = new QName("http://www.opengis.net/gml", "CalDate");

    /** @generated */
    public static final QName CartesianCSRefType = new QName("http://www.opengis.net/gml",
            "CartesianCSRefType");

    /** @generated */
    public static final QName CartesianCSType = new QName("http://www.opengis.net/gml",
            "CartesianCSType");

    /** @generated */
    public static final QName CategoryExtentType = new QName("http://www.opengis.net/gml",
            "CategoryExtentType");

    /** @generated */
    public static final QName CategoryPropertyType = new QName("http://www.opengis.net/gml",
            "CategoryPropertyType");

    /** @generated */
    public static final QName CircleByCenterPointType = new QName("http://www.opengis.net/gml",
            "CircleByCenterPointType");

    /** @generated */
    public static final QName CircleType = new QName("http://www.opengis.net/gml", "CircleType");

    /** @generated */
    public static final QName ClothoidType = new QName("http://www.opengis.net/gml", "ClothoidType");

    /** @generated */
    public static final QName CodeListType = new QName("http://www.opengis.net/gml", "CodeListType");

    /** @generated */
    public static final QName CodeOrNullListType = new QName("http://www.opengis.net/gml",
            "CodeOrNullListType");

    /** @generated */
    public static final QName CodeType = new QName("http://www.opengis.net/gml", "CodeType");

    /** @generated */
    public static final QName CompassPointEnumeration = new QName("http://www.opengis.net/gml",
            "CompassPointEnumeration");

    /** @generated */
    public static final QName CompositeCurvePropertyType = new QName("http://www.opengis.net/gml",
            "CompositeCurvePropertyType");

    /** @generated */
    public static final QName CompositeCurveType = new QName("http://www.opengis.net/gml",
            "CompositeCurveType");

    /** @generated */
    public static final QName CompositeSolidPropertyType = new QName("http://www.opengis.net/gml",
            "CompositeSolidPropertyType");

    /** @generated */
    public static final QName CompositeSolidType = new QName("http://www.opengis.net/gml",
            "CompositeSolidType");

    /** @generated */
    public static final QName CompositeSurfacePropertyType = new QName("http://www.opengis.net/gml",
            "CompositeSurfacePropertyType");

    /** @generated */
    public static final QName CompositeSurfaceType = new QName("http://www.opengis.net/gml",
            "CompositeSurfaceType");

    /** @generated */
    public static final QName CompositeValueType = new QName("http://www.opengis.net/gml",
            "CompositeValueType");

    /** @generated */
    public static final QName CompoundCRSRefType = new QName("http://www.opengis.net/gml",
            "CompoundCRSRefType");

    /** @generated */
    public static final QName CompoundCRSType = new QName("http://www.opengis.net/gml",
            "CompoundCRSType");

    /** @generated */
    public static final QName ConcatenatedOperationRefType = new QName("http://www.opengis.net/gml",
            "ConcatenatedOperationRefType");

    /** @generated */
    public static final QName ConcatenatedOperationType = new QName("http://www.opengis.net/gml",
            "ConcatenatedOperationType");

    /** @generated */
    public static final QName ConeType = new QName("http://www.opengis.net/gml", "ConeType");

    /** @generated */
    public static final QName ContainerPropertyType = new QName("http://www.opengis.net/gml",
            "ContainerPropertyType");

    /** @generated */
    public static final QName ConventionalUnitType = new QName("http://www.opengis.net/gml",
            "ConventionalUnitType");

    /** @generated */
    public static final QName ConversionRefType = new QName("http://www.opengis.net/gml",
            "ConversionRefType");

    /** @generated */
    public static final QName ConversionToPreferredUnitType = new QName("http://www.opengis.net/gml",
            "ConversionToPreferredUnitType");

    /** @generated */
    public static final QName ConversionType = new QName("http://www.opengis.net/gml",
            "ConversionType");

    /** @generated */
    public static final QName CoordinateOperationRefType = new QName("http://www.opengis.net/gml",
            "CoordinateOperationRefType");

    /** @generated */
    public static final QName CoordinateReferenceSystemRefType = new QName("http://www.opengis.net/gml",
            "CoordinateReferenceSystemRefType");

    /** @generated */
    public static final QName CoordinatesType = new QName("http://www.opengis.net/gml",
            "CoordinatesType");

    /** @generated */
    public static final QName CoordinateSystemAxisBaseType = new QName("http://www.opengis.net/gml",
            "CoordinateSystemAxisBaseType");

    /** @generated */
    public static final QName CoordinateSystemAxisRefType = new QName("http://www.opengis.net/gml",
            "CoordinateSystemAxisRefType");

    /** @generated */
    public static final QName CoordinateSystemAxisType = new QName("http://www.opengis.net/gml",
            "CoordinateSystemAxisType");

    /** @generated */
    public static final QName CoordinateSystemRefType = new QName("http://www.opengis.net/gml",
            "CoordinateSystemRefType");

    /** @generated */
    public static final QName CoordType = new QName("http://www.opengis.net/gml", "CoordType");

    /** @generated */
    public static final QName CountExtentType = new QName("http://www.opengis.net/gml",
            "CountExtentType");

    /** @generated */
    public static final QName CountPropertyType = new QName("http://www.opengis.net/gml",
            "CountPropertyType");

    /** @generated */
    public static final QName CovarianceElementType = new QName("http://www.opengis.net/gml",
            "CovarianceElementType");

    /** @generated */
    public static final QName CovarianceMatrixType = new QName("http://www.opengis.net/gml",
            "CovarianceMatrixType");

    /** @generated */
    public static final QName CoverageFunctionType = new QName("http://www.opengis.net/gml",
            "CoverageFunctionType");

    /** @generated */
    public static final QName CRSRefType = new QName("http://www.opengis.net/gml", "CRSRefType");

    /** @generated */
    public static final QName CubicSplineType = new QName("http://www.opengis.net/gml",
            "CubicSplineType");

    /** @generated */
    public static final QName CurveArrayPropertyType = new QName("http://www.opengis.net/gml",
            "CurveArrayPropertyType");

    /** @generated */
    public static final QName CurveInterpolationType = new QName("http://www.opengis.net/gml",
            "CurveInterpolationType");

    /** @generated */
    public static final QName CurvePropertyType = new QName("http://www.opengis.net/gml",
            "CurvePropertyType");

    /** @generated */
    public static final QName CurveSegmentArrayPropertyType = new QName("http://www.opengis.net/gml",
            "CurveSegmentArrayPropertyType");

    /** @generated */
    public static final QName CurveType = new QName("http://www.opengis.net/gml", "CurveType");

    /** @generated */
    public static final QName CylinderType = new QName("http://www.opengis.net/gml", "CylinderType");

    /** @generated */
    public static final QName CylindricalCSRefType = new QName("http://www.opengis.net/gml",
            "CylindricalCSRefType");

    /** @generated */
    public static final QName CylindricalCSType = new QName("http://www.opengis.net/gml",
            "CylindricalCSType");

    /** @generated */
    public static final QName DataBlockType = new QName("http://www.opengis.net/gml",
            "DataBlockType");

    /** @generated */
    public static final QName DatumRefType = new QName("http://www.opengis.net/gml", "DatumRefType");

    /** @generated */
    public static final QName DecimalMinutesType = new QName("http://www.opengis.net/gml",
            "DecimalMinutesType");

    /** @generated */
    public static final QName DefaultStylePropertyType = new QName("http://www.opengis.net/gml",
            "DefaultStylePropertyType");

    /** @generated */
    public static final QName DefinitionProxyType = new QName("http://www.opengis.net/gml",
            "DefinitionProxyType");

    /** @generated */
    public static final QName DefinitionType = new QName("http://www.opengis.net/gml",
            "DefinitionType");

    /** @generated */
    public static final QName DegreesType = new QName("http://www.opengis.net/gml", "DegreesType");

    /** @generated */
    public static final QName DegreeValueType = new QName("http://www.opengis.net/gml",
            "DegreeValueType");

    /** @generated */
    public static final QName DerivationUnitTermType = new QName("http://www.opengis.net/gml",
            "DerivationUnitTermType");

    /** @generated */
    public static final QName DerivedCRSRefType = new QName("http://www.opengis.net/gml",
            "DerivedCRSRefType");

    /** @generated */
    public static final QName DerivedCRSType = new QName("http://www.opengis.net/gml",
            "DerivedCRSType");

    /** @generated */
    public static final QName DerivedCRSTypeType = new QName("http://www.opengis.net/gml",
            "DerivedCRSTypeType");

    /** @generated */
    public static final QName DerivedUnitType = new QName("http://www.opengis.net/gml",
            "DerivedUnitType");

    /** @generated */
    public static final QName DictionaryEntryType = new QName("http://www.opengis.net/gml",
            "DictionaryEntryType");

    /** @generated */
    public static final QName DictionaryType = new QName("http://www.opengis.net/gml",
            "DictionaryType");

    /** @generated */
    public static final QName DirectedEdgePropertyType = new QName("http://www.opengis.net/gml",
            "DirectedEdgePropertyType");

    /** @generated */
    public static final QName DirectedFacePropertyType = new QName("http://www.opengis.net/gml",
            "DirectedFacePropertyType");

    /** @generated */
    public static final QName DirectedNodePropertyType = new QName("http://www.opengis.net/gml",
            "DirectedNodePropertyType");

    /** @generated */
    public static final QName DirectedObservationAtDistanceType = new QName("http://www.opengis.net/gml",
            "DirectedObservationAtDistanceType");

    /** @generated */
    public static final QName DirectedObservationType = new QName("http://www.opengis.net/gml",
            "DirectedObservationType");

    /** @generated */
    public static final QName DirectedTopoSolidPropertyType = new QName("http://www.opengis.net/gml",
            "DirectedTopoSolidPropertyType");

    /** @generated */
    public static final QName DirectionPropertyType = new QName("http://www.opengis.net/gml",
            "DirectionPropertyType");

    /** @generated */
    public static final QName DirectionVectorType = new QName("http://www.opengis.net/gml",
            "DirectionVectorType");

    /** @generated */
    public static final QName DirectPositionListType = new QName("http://www.opengis.net/gml",
            "DirectPositionListType");

    /** @generated */
    public static final QName DirectPositionType = new QName("http://www.opengis.net/gml",
            "DirectPositionType");

    /** @generated */
    public static final QName DMSAngleType = new QName("http://www.opengis.net/gml", "DMSAngleType");

    /** @generated */
    public static final QName DomainSetType = new QName("http://www.opengis.net/gml",
            "DomainSetType");

    /** @generated */
    public static final QName doubleList = new QName("http://www.opengis.net/gml", "doubleList");

    /** @generated */
    public static final QName doubleOrNull = new QName("http://www.opengis.net/gml", "doubleOrNull");

    /** @generated */
    public static final QName doubleOrNullList = new QName("http://www.opengis.net/gml",
            "doubleOrNullList");

    /** @generated */
    public static final QName DrawingTypeType = new QName("http://www.opengis.net/gml",
            "DrawingTypeType");

    /** @generated */
    public static final QName DynamicFeatureCollectionType = new QName("http://www.opengis.net/gml",
            "DynamicFeatureCollectionType");

    /** @generated */
    public static final QName DynamicFeatureType = new QName("http://www.opengis.net/gml",
            "DynamicFeatureType");

    /** @generated */
    public static final QName EdgeType = new QName("http://www.opengis.net/gml", "EdgeType");

    /** @generated */
    public static final QName EllipsoidalCSRefType = new QName("http://www.opengis.net/gml",
            "EllipsoidalCSRefType");

    /** @generated */
    public static final QName EllipsoidalCSType = new QName("http://www.opengis.net/gml",
            "EllipsoidalCSType");

    /** @generated */
    public static final QName EllipsoidBaseType = new QName("http://www.opengis.net/gml",
            "EllipsoidBaseType");

    /** @generated */
    public static final QName EllipsoidRefType = new QName("http://www.opengis.net/gml",
            "EllipsoidRefType");

    /** @generated */
    public static final QName EllipsoidType = new QName("http://www.opengis.net/gml",
            "EllipsoidType");

    /** @generated */
    public static final QName EngineeringCRSRefType = new QName("http://www.opengis.net/gml",
            "EngineeringCRSRefType");

    /** @generated */
    public static final QName EngineeringCRSType = new QName("http://www.opengis.net/gml",
            "EngineeringCRSType");

    /** @generated */
    public static final QName EngineeringDatumRefType = new QName("http://www.opengis.net/gml",
            "EngineeringDatumRefType");

    /** @generated */
    public static final QName EngineeringDatumType = new QName("http://www.opengis.net/gml",
            "EngineeringDatumType");

    /** @generated */
    public static final QName EnvelopeType = new QName("http://www.opengis.net/gml", "EnvelopeType");

    /** @generated */
    public static final QName EnvelopeWithTimePeriodType = new QName("http://www.opengis.net/gml",
            "EnvelopeWithTimePeriodType");

    /** @generated */
    public static final QName ExtentType = new QName("http://www.opengis.net/gml", "ExtentType");

    /** @generated */
    public static final QName FaceType = new QName("http://www.opengis.net/gml", "FaceType");

    /** @generated */
    public static final QName FeatureArrayPropertyType = new QName("http://www.opengis.net/gml",
            "FeatureArrayPropertyType");

    /** @generated */
    public static final QName FeatureCollectionType = new QName("http://www.opengis.net/gml",
            "FeatureCollectionType");

    /** @generated */
    public static final QName FeaturePropertyType = new QName("http://www.opengis.net/gml",
            "FeaturePropertyType");

    /** @generated */
    public static final QName FeatureStylePropertyType = new QName("http://www.opengis.net/gml",
            "FeatureStylePropertyType");

    /** @generated */
    public static final QName FeatureStyleType = new QName("http://www.opengis.net/gml",
            "FeatureStyleType");

    /** @generated */
    public static final QName FileType = new QName("http://www.opengis.net/gml", "FileType");

    /** @generated */
    public static final QName FileValueModelType = new QName("http://www.opengis.net/gml",
            "FileValueModelType");

    /** @generated */
    public static final QName FormulaType = new QName("http://www.opengis.net/gml", "FormulaType");

    /** @generated */
    public static final QName GeneralConversionRefType = new QName("http://www.opengis.net/gml",
            "GeneralConversionRefType");

    /** @generated */
    public static final QName GeneralTransformationRefType = new QName("http://www.opengis.net/gml",
            "GeneralTransformationRefType");

    /** @generated */
    public static final QName GenericMetaDataType = new QName("http://www.opengis.net/gml",
            "GenericMetaDataType");

    /** @generated */
    public static final QName GeocentricCRSRefType = new QName("http://www.opengis.net/gml",
            "GeocentricCRSRefType");

    /** @generated */
    public static final QName GeocentricCRSType = new QName("http://www.opengis.net/gml",
            "GeocentricCRSType");

    /** @generated */
    public static final QName GeodesicStringType = new QName("http://www.opengis.net/gml",
            "GeodesicStringType");

    /** @generated */
    public static final QName GeodesicType = new QName("http://www.opengis.net/gml", "GeodesicType");

    /** @generated */
    public static final QName GeodeticDatumRefType = new QName("http://www.opengis.net/gml",
            "GeodeticDatumRefType");

    /** @generated */
    public static final QName GeodeticDatumType = new QName("http://www.opengis.net/gml",
            "GeodeticDatumType");

    /** @generated */
    public static final QName GeographicCRSRefType = new QName("http://www.opengis.net/gml",
            "GeographicCRSRefType");

    /** @generated */
    public static final QName GeographicCRSType = new QName("http://www.opengis.net/gml",
            "GeographicCRSType");

    /** @generated */
    public static final QName GeometricComplexPropertyType = new QName("http://www.opengis.net/gml",
            "GeometricComplexPropertyType");

    /** @generated */
    public static final QName GeometricComplexType = new QName("http://www.opengis.net/gml",
            "GeometricComplexType");

    /** @generated */
    public static final QName GeometricPrimitivePropertyType = new QName("http://www.opengis.net/gml",
            "GeometricPrimitivePropertyType");

    /** @generated */
    public static final QName GeometryArrayPropertyType = new QName("http://www.opengis.net/gml",
            "GeometryArrayPropertyType");

    /** @generated */
    public static final QName GeometryPropertyType = new QName("http://www.opengis.net/gml",
            "GeometryPropertyType");

    /** @generated */
    public static final QName GeometryStylePropertyType = new QName("http://www.opengis.net/gml",
            "GeometryStylePropertyType");

    /** @generated */
    public static final QName GeometryStyleType = new QName("http://www.opengis.net/gml",
            "GeometryStyleType");

    /** @generated */
    public static final QName GraphStylePropertyType = new QName("http://www.opengis.net/gml",
            "GraphStylePropertyType");

    /** @generated */
    public static final QName GraphStyleType = new QName("http://www.opengis.net/gml",
            "GraphStyleType");

    /** @generated */
    public static final QName GraphTypeType = new QName("http://www.opengis.net/gml",
            "GraphTypeType");

    /** @generated */
    public static final QName GridCoverageType = new QName("http://www.opengis.net/gml",
            "GridCoverageType");

    /** @generated */
    public static final QName GridDomainType = new QName("http://www.opengis.net/gml",
            "GridDomainType");

    /** @generated */
    public static final QName GridEnvelopeType = new QName("http://www.opengis.net/gml",
            "GridEnvelopeType");

    /** @generated */
    public static final QName GridFunctionType = new QName("http://www.opengis.net/gml",
            "GridFunctionType");

    /** @generated */
    public static final QName GridLengthType = new QName("http://www.opengis.net/gml",
            "GridLengthType");

    /** @generated */
    public static final QName GridLimitsType = new QName("http://www.opengis.net/gml",
            "GridLimitsType");

    /** @generated */
    public static final QName GridType = new QName("http://www.opengis.net/gml", "GridType");

    /** @generated */
    public static final QName HistoryPropertyType = new QName("http://www.opengis.net/gml",
            "HistoryPropertyType");

    /** @generated */
    public static final QName IdentifierType = new QName("http://www.opengis.net/gml",
            "IdentifierType");

    /** @generated */
    public static final QName ImageCRSRefType = new QName("http://www.opengis.net/gml",
            "ImageCRSRefType");

    /** @generated */
    public static final QName ImageCRSType = new QName("http://www.opengis.net/gml", "ImageCRSType");

    /** @generated */
    public static final QName ImageDatumRefType = new QName("http://www.opengis.net/gml",
            "ImageDatumRefType");

    /** @generated */
    public static final QName ImageDatumType = new QName("http://www.opengis.net/gml",
            "ImageDatumType");

    /** @generated */
    public static final QName IncrementOrder = new QName("http://www.opengis.net/gml",
            "IncrementOrder");

    /** @generated */
    public static final QName IndexMapType = new QName("http://www.opengis.net/gml", "IndexMapType");

    /** @generated */
    public static final QName IndirectEntryType = new QName("http://www.opengis.net/gml",
            "IndirectEntryType");

    /** @generated */
    public static final QName integerList = new QName("http://www.opengis.net/gml", "integerList");

    /** @generated */
    public static final QName integerOrNull = new QName("http://www.opengis.net/gml",
            "integerOrNull");

    /** @generated */
    public static final QName integerOrNullList = new QName("http://www.opengis.net/gml",
            "integerOrNullList");

    /** @generated */
    public static final QName IsolatedPropertyType = new QName("http://www.opengis.net/gml",
            "IsolatedPropertyType");

    /** @generated */
    public static final QName KnotPropertyType = new QName("http://www.opengis.net/gml",
            "KnotPropertyType");

    /** @generated */
    public static final QName KnotType = new QName("http://www.opengis.net/gml", "KnotType");

    /** @generated */
    public static final QName KnotTypesType = new QName("http://www.opengis.net/gml",
            "KnotTypesType");

    /** @generated */
    public static final QName LabelStylePropertyType = new QName("http://www.opengis.net/gml",
            "LabelStylePropertyType");

    /** @generated */
    public static final QName LabelStyleType = new QName("http://www.opengis.net/gml",
            "LabelStyleType");

    /** @generated */
    public static final QName LabelType = new QName("http://www.opengis.net/gml", "LabelType");

    /** @generated */
    public static final QName LengthType = new QName("http://www.opengis.net/gml", "LengthType");

    /** @generated */
    public static final QName LinearCSRefType = new QName("http://www.opengis.net/gml",
            "LinearCSRefType");

    /** @generated */
    public static final QName LinearCSType = new QName("http://www.opengis.net/gml", "LinearCSType");

    /** @generated */
    public static final QName LinearRingPropertyType = new QName("http://www.opengis.net/gml",
            "LinearRingPropertyType");

    /** @generated */
    public static final QName LinearRingType = new QName("http://www.opengis.net/gml",
            "LinearRingType");

    /** @generated */
    public static final QName LineStringPropertyType = new QName("http://www.opengis.net/gml",
            "LineStringPropertyType");

    /** @generated */
    public static final QName LineStringSegmentArrayPropertyType = new QName("http://www.opengis.net/gml",
            "LineStringSegmentArrayPropertyType");

    /** @generated */
    public static final QName LineStringSegmentType = new QName("http://www.opengis.net/gml",
            "LineStringSegmentType");

    /** @generated */
    public static final QName LineStringType = new QName("http://www.opengis.net/gml",
            "LineStringType");

    /** @generated */
    public static final QName LineTypeType = new QName("http://www.opengis.net/gml", "LineTypeType");

    /** @generated */
    public static final QName LocationPropertyType = new QName("http://www.opengis.net/gml",
            "LocationPropertyType");

    /** @generated */
    public static final QName MeasureListType = new QName("http://www.opengis.net/gml",
            "MeasureListType");

    /** @generated */
    public static final QName MeasureOrNullListType = new QName("http://www.opengis.net/gml",
            "MeasureOrNullListType");

    /** @generated */
    public static final QName MeasureType = new QName("http://www.opengis.net/gml", "MeasureType");

    /** @generated */
    public static final QName MetaDataPropertyType = new QName("http://www.opengis.net/gml",
            "MetaDataPropertyType");

    /** @generated */
    public static final QName MovingObjectStatusType = new QName("http://www.opengis.net/gml",
            "MovingObjectStatusType");

    /** @generated */
    public static final QName MultiCurveCoverageType = new QName("http://www.opengis.net/gml",
            "MultiCurveCoverageType");

    /** @generated */
    public static final QName MultiCurveDomainType = new QName("http://www.opengis.net/gml",
            "MultiCurveDomainType");

    /** @generated */
    public static final QName MultiCurvePropertyType = new QName("http://www.opengis.net/gml",
            "MultiCurvePropertyType");

    /** @generated */
    public static final QName MultiCurveType = new QName("http://www.opengis.net/gml",
            "MultiCurveType");

    /** @generated */
    public static final QName MultiGeometryPropertyType = new QName("http://www.opengis.net/gml",
            "MultiGeometryPropertyType");

    /** @generated */
    public static final QName MultiGeometryType = new QName("http://www.opengis.net/gml",
            "MultiGeometryType");

    /** @generated */
    public static final QName MultiLineStringPropertyType = new QName("http://www.opengis.net/gml",
            "MultiLineStringPropertyType");

    /** @generated */
    public static final QName MultiLineStringType = new QName("http://www.opengis.net/gml",
            "MultiLineStringType");

    /** @generated */
    public static final QName MultiPointCoverageType = new QName("http://www.opengis.net/gml",
            "MultiPointCoverageType");

    /** @generated */
    public static final QName MultiPointDomainType = new QName("http://www.opengis.net/gml",
            "MultiPointDomainType");

    /** @generated */
    public static final QName MultiPointPropertyType = new QName("http://www.opengis.net/gml",
            "MultiPointPropertyType");

    /** @generated */
    public static final QName MultiPointType = new QName("http://www.opengis.net/gml",
            "MultiPointType");

    /** @generated */
    public static final QName MultiPolygonPropertyType = new QName("http://www.opengis.net/gml",
            "MultiPolygonPropertyType");

    /** @generated */
    public static final QName MultiPolygonType = new QName("http://www.opengis.net/gml",
            "MultiPolygonType");

    /** @generated */
    public static final QName MultiSolidCoverageType = new QName("http://www.opengis.net/gml",
            "MultiSolidCoverageType");

    /** @generated */
    public static final QName MultiSolidDomainType = new QName("http://www.opengis.net/gml",
            "MultiSolidDomainType");

    /** @generated */
    public static final QName MultiSolidPropertyType = new QName("http://www.opengis.net/gml",
            "MultiSolidPropertyType");

    /** @generated */
    public static final QName MultiSolidType = new QName("http://www.opengis.net/gml",
            "MultiSolidType");

    /** @generated */
    public static final QName MultiSurfaceCoverageType = new QName("http://www.opengis.net/gml",
            "MultiSurfaceCoverageType");

    /** @generated */
    public static final QName MultiSurfaceDomainType = new QName("http://www.opengis.net/gml",
            "MultiSurfaceDomainType");

    /** @generated */
    public static final QName MultiSurfacePropertyType = new QName("http://www.opengis.net/gml",
            "MultiSurfacePropertyType");

    /** @generated */
    public static final QName MultiSurfaceType = new QName("http://www.opengis.net/gml",
            "MultiSurfaceType");

    /** @generated */
    public static final QName NameList = new QName("http://www.opengis.net/gml", "NameList");

    /** @generated */
    public static final QName NameOrNull = new QName("http://www.opengis.net/gml", "NameOrNull");

    /** @generated */
    public static final QName NameOrNullList = new QName("http://www.opengis.net/gml",
            "NameOrNullList");

    /** @generated */
    public static final QName NCNameList = new QName("http://www.opengis.net/gml", "NCNameList");

    /** @generated */
    public static final QName NodeType = new QName("http://www.opengis.net/gml", "NodeType");

    /** @generated */
    public static final QName NullEnumeration = new QName("http://www.opengis.net/gml",
            "NullEnumeration");

    /** @generated */
    public static final QName NullType = new QName("http://www.opengis.net/gml", "NullType");

    /** @generated */
    public static final QName ObliqueCartesianCSRefType = new QName("http://www.opengis.net/gml",
            "ObliqueCartesianCSRefType");

    /** @generated */
    public static final QName ObliqueCartesianCSType = new QName("http://www.opengis.net/gml",
            "ObliqueCartesianCSType");

    /** @generated */
    public static final QName ObservationType = new QName("http://www.opengis.net/gml",
            "ObservationType");

    /** @generated */
    public static final QName OffsetCurveType = new QName("http://www.opengis.net/gml",
            "OffsetCurveType");

    /** @generated */
    public static final QName OperationMethodBaseType = new QName("http://www.opengis.net/gml",
            "OperationMethodBaseType");

    /** @generated */
    public static final QName OperationMethodRefType = new QName("http://www.opengis.net/gml",
            "OperationMethodRefType");

    /** @generated */
    public static final QName OperationMethodType = new QName("http://www.opengis.net/gml",
            "OperationMethodType");

    /** @generated */
    public static final QName OperationParameterBaseType = new QName("http://www.opengis.net/gml",
            "OperationParameterBaseType");

    /** @generated */
    public static final QName OperationParameterGroupBaseType = new QName("http://www.opengis.net/gml",
            "OperationParameterGroupBaseType");

    /** @generated */
    public static final QName OperationParameterGroupRefType = new QName("http://www.opengis.net/gml",
            "OperationParameterGroupRefType");

    /** @generated */
    public static final QName OperationParameterGroupType = new QName("http://www.opengis.net/gml",
            "OperationParameterGroupType");

    /** @generated */
    public static final QName OperationParameterRefType = new QName("http://www.opengis.net/gml",
            "OperationParameterRefType");

    /** @generated */
    public static final QName OperationParameterType = new QName("http://www.opengis.net/gml",
            "OperationParameterType");

    /** @generated */
    public static final QName OperationRefType = new QName("http://www.opengis.net/gml",
            "OperationRefType");

    /** @generated */
    public static final QName OrientableCurveType = new QName("http://www.opengis.net/gml",
            "OrientableCurveType");

    /** @generated */
    public static final QName OrientableSurfaceType = new QName("http://www.opengis.net/gml",
            "OrientableSurfaceType");

    /** @generated */
    public static final QName ParameterValueGroupType = new QName("http://www.opengis.net/gml",
            "ParameterValueGroupType");

    /** @generated */
    public static final QName ParameterValueType = new QName("http://www.opengis.net/gml",
            "ParameterValueType");

    /** @generated */
    public static final QName PassThroughOperationRefType = new QName("http://www.opengis.net/gml",
            "PassThroughOperationRefType");

    /** @generated */
    public static final QName PassThroughOperationType = new QName("http://www.opengis.net/gml",
            "PassThroughOperationType");

    /** @generated */
    public static final QName PixelInCellType = new QName("http://www.opengis.net/gml",
            "PixelInCellType");

    /** @generated */
    public static final QName PointArrayPropertyType = new QName("http://www.opengis.net/gml",
            "PointArrayPropertyType");

    /** @generated */
    public static final QName PointPropertyType = new QName("http://www.opengis.net/gml",
            "PointPropertyType");

    /** @generated */
    public static final QName PointType = new QName("http://www.opengis.net/gml", "PointType");

    /** @generated */
    public static final QName PolarCSRefType = new QName("http://www.opengis.net/gml",
            "PolarCSRefType");

    /** @generated */
    public static final QName PolarCSType = new QName("http://www.opengis.net/gml", "PolarCSType");

    /** @generated */
    public static final QName PolygonPatchArrayPropertyType = new QName("http://www.opengis.net/gml",
            "PolygonPatchArrayPropertyType");

    /** @generated */
    public static final QName PolygonPatchType = new QName("http://www.opengis.net/gml",
            "PolygonPatchType");

    /** @generated */
    public static final QName PolygonPropertyType = new QName("http://www.opengis.net/gml",
            "PolygonPropertyType");

    /** @generated */
    public static final QName PolygonType = new QName("http://www.opengis.net/gml", "PolygonType");

    /** @generated */
    public static final QName PolyhedralSurfaceType = new QName("http://www.opengis.net/gml",
            "PolyhedralSurfaceType");

    /** @generated */
    public static final QName PrimeMeridianBaseType = new QName("http://www.opengis.net/gml",
            "PrimeMeridianBaseType");

    /** @generated */
    public static final QName PrimeMeridianRefType = new QName("http://www.opengis.net/gml",
            "PrimeMeridianRefType");

    /** @generated */
    public static final QName PrimeMeridianType = new QName("http://www.opengis.net/gml",
            "PrimeMeridianType");

    /** @generated */
    public static final QName PriorityLocationPropertyType = new QName("http://www.opengis.net/gml",
            "PriorityLocationPropertyType");

    /** @generated */
    public static final QName ProjectedCRSRefType = new QName("http://www.opengis.net/gml",
            "ProjectedCRSRefType");

    /** @generated */
    public static final QName ProjectedCRSType = new QName("http://www.opengis.net/gml",
            "ProjectedCRSType");

    /** @generated */
    public static final QName QNameList = new QName("http://www.opengis.net/gml", "QNameList");

    /** @generated */
    public static final QName QuantityExtentType = new QName("http://www.opengis.net/gml",
            "QuantityExtentType");

    /** @generated */
    public static final QName QuantityPropertyType = new QName("http://www.opengis.net/gml",
            "QuantityPropertyType");

    /** @generated */
    public static final QName QueryGrammarEnumeration = new QName("http://www.opengis.net/gml",
            "QueryGrammarEnumeration");

    /** @generated */
    public static final QName RangeParametersType = new QName("http://www.opengis.net/gml",
            "RangeParametersType");

    /** @generated */
    public static final QName RangeSetType = new QName("http://www.opengis.net/gml", "RangeSetType");

    /** @generated */
    public static final QName RectangleType = new QName("http://www.opengis.net/gml",
            "RectangleType");

    /** @generated */
    public static final QName RectifiedGridCoverageType = new QName("http://www.opengis.net/gml",
            "RectifiedGridCoverageType");

    /** @generated */
    public static final QName RectifiedGridDomainType = new QName("http://www.opengis.net/gml",
            "RectifiedGridDomainType");

    /** @generated */
    public static final QName RectifiedGridType = new QName("http://www.opengis.net/gml",
            "RectifiedGridType");

    /** @generated */
    public static final QName ReferenceSystemRefType = new QName("http://www.opengis.net/gml",
            "ReferenceSystemRefType");

    /** @generated */
    public static final QName ReferenceType = new QName("http://www.opengis.net/gml",
            "ReferenceType");

    /** @generated */
    public static final QName RelatedTimeType = new QName("http://www.opengis.net/gml",
            "RelatedTimeType");

    /** @generated */
    public static final QName RelativeInternalPositionalAccuracyType = new QName("http://www.opengis.net/gml",
            "RelativeInternalPositionalAccuracyType");

    /** @generated */
    public static final QName RingPropertyType = new QName("http://www.opengis.net/gml",
            "RingPropertyType");

    /** @generated */
    public static final QName RingType = new QName("http://www.opengis.net/gml", "RingType");

    /** @generated */
    public static final QName ScalarValuePropertyType = new QName("http://www.opengis.net/gml",
            "ScalarValuePropertyType");

    /** @generated */
    public static final QName ScaleType = new QName("http://www.opengis.net/gml", "ScaleType");

    /** @generated */
    public static final QName SecondDefiningParameterType = new QName("http://www.opengis.net/gml",
            "SecondDefiningParameterType");

    /** @generated */
    public static final QName SequenceRuleNames = new QName("http://www.opengis.net/gml",
            "SequenceRuleNames");

    /** @generated */
    public static final QName SequenceRuleType = new QName("http://www.opengis.net/gml",
            "SequenceRuleType");

    /** @generated */
    public static final QName SignType = new QName("http://www.opengis.net/gml", "SignType");

    /** @generated */
    public static final QName SingleOperationRefType = new QName("http://www.opengis.net/gml",
            "SingleOperationRefType");

    /** @generated */
    public static final QName SolidArrayPropertyType = new QName("http://www.opengis.net/gml",
            "SolidArrayPropertyType");

    /** @generated */
    public static final QName SolidPropertyType = new QName("http://www.opengis.net/gml",
            "SolidPropertyType");

    /** @generated */
    public static final QName SolidType = new QName("http://www.opengis.net/gml", "SolidType");

    /** @generated */
    public static final QName SpeedType = new QName("http://www.opengis.net/gml", "SpeedType");

    /** @generated */
    public static final QName SphereType = new QName("http://www.opengis.net/gml", "SphereType");

    /** @generated */
    public static final QName SphericalCSRefType = new QName("http://www.opengis.net/gml",
            "SphericalCSRefType");

    /** @generated */
    public static final QName SphericalCSType = new QName("http://www.opengis.net/gml",
            "SphericalCSType");

    /** @generated */
    public static final QName stringOrNull = new QName("http://www.opengis.net/gml", "stringOrNull");

    /** @generated */
    public static final QName StringOrRefType = new QName("http://www.opengis.net/gml",
            "StringOrRefType");

    /** @generated */
    public static final QName StyleType = new QName("http://www.opengis.net/gml", "StyleType");

    /** @generated */
    public static final QName StyleVariationType = new QName("http://www.opengis.net/gml",
            "StyleVariationType");

    /** @generated */
    public static final QName SuccessionType = new QName("http://www.opengis.net/gml",
            "SuccessionType");

    /** @generated */
    public static final QName SurfaceArrayPropertyType = new QName("http://www.opengis.net/gml",
            "SurfaceArrayPropertyType");

    /** @generated */
    public static final QName SurfaceInterpolationType = new QName("http://www.opengis.net/gml",
            "SurfaceInterpolationType");

    /** @generated */
    public static final QName SurfacePatchArrayPropertyType = new QName("http://www.opengis.net/gml",
            "SurfacePatchArrayPropertyType");

    /** @generated */
    public static final QName SurfacePropertyType = new QName("http://www.opengis.net/gml",
            "SurfacePropertyType");

    /** @generated */
    public static final QName SurfaceType = new QName("http://www.opengis.net/gml", "SurfaceType");

    /** @generated */
    public static final QName SymbolType = new QName("http://www.opengis.net/gml", "SymbolType");

    /** @generated */
    public static final QName SymbolTypeEnumeration = new QName("http://www.opengis.net/gml",
            "SymbolTypeEnumeration");

    /** @generated */
    public static final QName TargetPropertyType = new QName("http://www.opengis.net/gml",
            "TargetPropertyType");

    /** @generated */
    public static final QName TemporalCRSRefType = new QName("http://www.opengis.net/gml",
            "TemporalCRSRefType");

    /** @generated */
    public static final QName TemporalCRSType = new QName("http://www.opengis.net/gml",
            "TemporalCRSType");

    /** @generated */
    public static final QName TemporalCSRefType = new QName("http://www.opengis.net/gml",
            "TemporalCSRefType");

    /** @generated */
    public static final QName TemporalCSType = new QName("http://www.opengis.net/gml",
            "TemporalCSType");

    /** @generated */
    public static final QName TemporalDatumBaseType = new QName("http://www.opengis.net/gml",
            "TemporalDatumBaseType");

    /** @generated */
    public static final QName TemporalDatumRefType = new QName("http://www.opengis.net/gml",
            "TemporalDatumRefType");

    /** @generated */
    public static final QName TemporalDatumType = new QName("http://www.opengis.net/gml",
            "TemporalDatumType");

    /** @generated */
    public static final QName TimeCalendarEraPropertyType = new QName("http://www.opengis.net/gml",
            "TimeCalendarEraPropertyType");

    /** @generated */
    public static final QName TimeCalendarEraType = new QName("http://www.opengis.net/gml",
            "TimeCalendarEraType");

    /** @generated */
    public static final QName TimeCalendarPropertyType = new QName("http://www.opengis.net/gml",
            "TimeCalendarPropertyType");

    /** @generated */
    public static final QName TimeCalendarType = new QName("http://www.opengis.net/gml",
            "TimeCalendarType");

    /** @generated */
    public static final QName TimeClockPropertyType = new QName("http://www.opengis.net/gml",
            "TimeClockPropertyType");

    /** @generated */
    public static final QName TimeClockType = new QName("http://www.opengis.net/gml",
            "TimeClockType");

    /** @generated */
    public static final QName TimeCoordinateSystemType = new QName("http://www.opengis.net/gml",
            "TimeCoordinateSystemType");

    /** @generated */
    public static final QName TimeEdgePropertyType = new QName("http://www.opengis.net/gml",
            "TimeEdgePropertyType");

    /** @generated */
    public static final QName TimeEdgeType = new QName("http://www.opengis.net/gml", "TimeEdgeType");

    /** @generated */
    public static final QName TimeGeometricPrimitivePropertyType = new QName("http://www.opengis.net/gml",
            "TimeGeometricPrimitivePropertyType");

    /** @generated */
    public static final QName TimeIndeterminateValueType = new QName("http://www.opengis.net/gml",
            "TimeIndeterminateValueType");

    /** @generated */
    public static final QName TimeInstantPropertyType = new QName("http://www.opengis.net/gml",
            "TimeInstantPropertyType");

    /** @generated */
    public static final QName TimeInstantType = new QName("http://www.opengis.net/gml",
            "TimeInstantType");

    /** @generated */
    public static final QName TimeIntervalLengthType = new QName("http://www.opengis.net/gml",
            "TimeIntervalLengthType");

    /** @generated */
    public static final QName TimeNodePropertyType = new QName("http://www.opengis.net/gml",
            "TimeNodePropertyType");

    /** @generated */
    public static final QName TimeNodeType = new QName("http://www.opengis.net/gml", "TimeNodeType");

    /** @generated */
    public static final QName TimeOrdinalEraPropertyType = new QName("http://www.opengis.net/gml",
            "TimeOrdinalEraPropertyType");

    /** @generated */
    public static final QName TimeOrdinalEraType = new QName("http://www.opengis.net/gml",
            "TimeOrdinalEraType");

    /** @generated */
    public static final QName TimeOrdinalReferenceSystemType = new QName("http://www.opengis.net/gml",
            "TimeOrdinalReferenceSystemType");

    /** @generated */
    public static final QName TimePeriodPropertyType = new QName("http://www.opengis.net/gml",
            "TimePeriodPropertyType");

    /** @generated */
    public static final QName TimePeriodType = new QName("http://www.opengis.net/gml",
            "TimePeriodType");

    /** @generated */
    public static final QName TimePositionType = new QName("http://www.opengis.net/gml",
            "TimePositionType");

    /** @generated */
    public static final QName TimePositionUnion = new QName("http://www.opengis.net/gml",
            "TimePositionUnion");

    /** @generated */
    public static final QName TimePrimitivePropertyType = new QName("http://www.opengis.net/gml",
            "TimePrimitivePropertyType");

    /** @generated */
    public static final QName TimeTopologyComplexPropertyType = new QName("http://www.opengis.net/gml",
            "TimeTopologyComplexPropertyType");

    /** @generated */
    public static final QName TimeTopologyComplexType = new QName("http://www.opengis.net/gml",
            "TimeTopologyComplexType");

    /** @generated */
    public static final QName TimeTopologyPrimitivePropertyType = new QName("http://www.opengis.net/gml",
            "TimeTopologyPrimitivePropertyType");

    /** @generated */
    public static final QName TimeType = new QName("http://www.opengis.net/gml", "TimeType");

    /** @generated */
    public static final QName TimeUnitType = new QName("http://www.opengis.net/gml", "TimeUnitType");

    /** @generated */
    public static final QName TinType = new QName("http://www.opengis.net/gml", "TinType");

    /** @generated */
    public static final QName TopoComplexMemberType = new QName("http://www.opengis.net/gml",
            "TopoComplexMemberType");

    /** @generated */
    public static final QName TopoComplexType = new QName("http://www.opengis.net/gml",
            "TopoComplexType");

    /** @generated */
    public static final QName TopoCurvePropertyType = new QName("http://www.opengis.net/gml",
            "TopoCurvePropertyType");

    /** @generated */
    public static final QName TopoCurveType = new QName("http://www.opengis.net/gml",
            "TopoCurveType");

    /** @generated */
    public static final QName TopologyStylePropertyType = new QName("http://www.opengis.net/gml",
            "TopologyStylePropertyType");

    /** @generated */
    public static final QName TopologyStyleType = new QName("http://www.opengis.net/gml",
            "TopologyStyleType");

    /** @generated */
    public static final QName TopoPointPropertyType = new QName("http://www.opengis.net/gml",
            "TopoPointPropertyType");

    /** @generated */
    public static final QName TopoPointType = new QName("http://www.opengis.net/gml",
            "TopoPointType");

    /** @generated */
    public static final QName TopoPrimitiveArrayAssociationType = new QName("http://www.opengis.net/gml",
            "TopoPrimitiveArrayAssociationType");

    /** @generated */
    public static final QName TopoPrimitiveMemberType = new QName("http://www.opengis.net/gml",
            "TopoPrimitiveMemberType");

    /** @generated */
    public static final QName TopoSolidType = new QName("http://www.opengis.net/gml",
            "TopoSolidType");

    /** @generated */
    public static final QName TopoSurfacePropertyType = new QName("http://www.opengis.net/gml",
            "TopoSurfacePropertyType");

    /** @generated */
    public static final QName TopoSurfaceType = new QName("http://www.opengis.net/gml",
            "TopoSurfaceType");

    /** @generated */
    public static final QName TopoVolumePropertyType = new QName("http://www.opengis.net/gml",
            "TopoVolumePropertyType");

    /** @generated */
    public static final QName TopoVolumeType = new QName("http://www.opengis.net/gml",
            "TopoVolumeType");

    /** @generated */
    public static final QName TrackType = new QName("http://www.opengis.net/gml", "TrackType");

    /** @generated */
    public static final QName TransformationRefType = new QName("http://www.opengis.net/gml",
            "TransformationRefType");

    /** @generated */
    public static final QName TransformationType = new QName("http://www.opengis.net/gml",
            "TransformationType");

    /** @generated */
    public static final QName TrianglePatchArrayPropertyType = new QName("http://www.opengis.net/gml",
            "TrianglePatchArrayPropertyType");

    /** @generated */
    public static final QName TriangleType = new QName("http://www.opengis.net/gml", "TriangleType");

    /** @generated */
    public static final QName TriangulatedSurfaceType = new QName("http://www.opengis.net/gml",
            "TriangulatedSurfaceType");

    /** @generated */
    public static final QName UnitDefinitionType = new QName("http://www.opengis.net/gml",
            "UnitDefinitionType");

    /** @generated */
    public static final QName UnitOfMeasureType = new QName("http://www.opengis.net/gml",
            "UnitOfMeasureType");

    /** @generated */
    public static final QName UserDefinedCSRefType = new QName("http://www.opengis.net/gml",
            "UserDefinedCSRefType");

    /** @generated */
    public static final QName UserDefinedCSType = new QName("http://www.opengis.net/gml",
            "UserDefinedCSType");

    /** @generated */
    public static final QName ValueArrayPropertyType = new QName("http://www.opengis.net/gml",
            "ValueArrayPropertyType");

    /** @generated */
    public static final QName ValueArrayType = new QName("http://www.opengis.net/gml",
            "ValueArrayType");

    /** @generated */
    public static final QName ValuePropertyType = new QName("http://www.opengis.net/gml",
            "ValuePropertyType");

    /** @generated */
    public static final QName VectorType = new QName("http://www.opengis.net/gml", "VectorType");

    /** @generated */
    public static final QName VerticalCRSRefType = new QName("http://www.opengis.net/gml",
            "VerticalCRSRefType");

    /** @generated */
    public static final QName VerticalCRSType = new QName("http://www.opengis.net/gml",
            "VerticalCRSType");

    /** @generated */
    public static final QName VerticalCSRefType = new QName("http://www.opengis.net/gml",
            "VerticalCSRefType");

    /** @generated */
    public static final QName VerticalCSType = new QName("http://www.opengis.net/gml",
            "VerticalCSType");

    /** @generated */
    public static final QName VerticalDatumRefType = new QName("http://www.opengis.net/gml",
            "VerticalDatumRefType");

    /** @generated */
    public static final QName VerticalDatumType = new QName("http://www.opengis.net/gml",
            "VerticalDatumType");

    /** @generated */
    public static final QName VerticalDatumTypeType = new QName("http://www.opengis.net/gml",
            "VerticalDatumTypeType");

    /** @generated */
    public static final QName VolumeType = new QName("http://www.opengis.net/gml", "VolumeType");

    /* Elements */
    /** @generated */
    public static final QName _association = new QName("http://www.opengis.net/gml", "_association");

    /** @generated */
    public static final QName _ContinuousCoverage = new QName("http://www.opengis.net/gml",
            "_ContinuousCoverage");

    /** @generated */
    public static final QName _CoordinateOperation = new QName("http://www.opengis.net/gml",
            "_CoordinateOperation");

    /** @generated */
    public static final QName _CoordinateReferenceSystem = new QName("http://www.opengis.net/gml",
            "_CoordinateReferenceSystem");

    /** @generated */
    public static final QName _CoordinateSystem = new QName("http://www.opengis.net/gml",
            "_CoordinateSystem");

    /** @generated */
    public static final QName _Coverage = new QName("http://www.opengis.net/gml", "_Coverage");

    /** @generated */
    public static final QName _CRS = new QName("http://www.opengis.net/gml", "_CRS");

    /** @generated */
    public static final QName _Curve = new QName("http://www.opengis.net/gml", "_Curve");

    /** @generated */
    public static final QName _CurveSegment = new QName("http://www.opengis.net/gml",
            "_CurveSegment");

    /** @generated */
    public static final QName _Datum = new QName("http://www.opengis.net/gml", "_Datum");

    /** @generated */
    public static final QName _DiscreteCoverage = new QName("http://www.opengis.net/gml",
            "_DiscreteCoverage");

    /** @generated */
    public static final QName _Feature = new QName("http://www.opengis.net/gml", "_Feature");

    /** @generated */
    public static final QName _FeatureCollection = new QName("http://www.opengis.net/gml",
            "_FeatureCollection");

    /** @generated */
    public static final QName _GeneralConversion = new QName("http://www.opengis.net/gml",
            "_GeneralConversion");

    /** @generated */
    public static final QName _GeneralDerivedCRS = new QName("http://www.opengis.net/gml",
            "_GeneralDerivedCRS");

    /** @generated */
    public static final QName _GeneralOperationParameter = new QName("http://www.opengis.net/gml",
            "_GeneralOperationParameter");

    /** @generated */
    public static final QName _generalParameterValue = new QName("http://www.opengis.net/gml",
            "_generalParameterValue");

    /** @generated */
    public static final QName _GeneralTransformation = new QName("http://www.opengis.net/gml",
            "_GeneralTransformation");

    /** @generated */
    public static final QName _GeometricAggregate = new QName("http://www.opengis.net/gml",
            "_GeometricAggregate");

    /** @generated */
    public static final QName _GeometricPrimitive = new QName("http://www.opengis.net/gml",
            "_GeometricPrimitive");

    /** @generated */
    public static final QName _Geometry = new QName("http://www.opengis.net/gml", "_Geometry");

    /** @generated */
    public static final QName _GML = new QName("http://www.opengis.net/gml", "_GML");

    /** @generated */
    public static final QName _GriddedSurface = new QName("http://www.opengis.net/gml",
            "_GriddedSurface");

    /** @generated */
    public static final QName _ImplicitGeometry = new QName("http://www.opengis.net/gml",
            "_ImplicitGeometry");

    /** @generated */
    public static final QName _MetaData = new QName("http://www.opengis.net/gml", "_MetaData");

    /** @generated */
    public static final QName _Object = new QName("http://www.opengis.net/gml", "_Object");

    /** @generated */
    public static final QName _Operation = new QName("http://www.opengis.net/gml", "_Operation");

    /** @generated */
    public static final QName _ParametricCurveSurface = new QName("http://www.opengis.net/gml",
            "_ParametricCurveSurface");

    /** @generated */
    public static final QName _positionalAccuracy = new QName("http://www.opengis.net/gml",
            "_positionalAccuracy");

    /** @generated */
    public static final QName _reference = new QName("http://www.opengis.net/gml", "_reference");

    /** @generated */
    public static final QName _ReferenceSystem = new QName("http://www.opengis.net/gml",
            "_ReferenceSystem");

    /** @generated */
    public static final QName _Ring = new QName("http://www.opengis.net/gml", "_Ring");

    /** @generated */
    public static final QName _SingleOperation = new QName("http://www.opengis.net/gml",
            "_SingleOperation");

    /** @generated */
    public static final QName _Solid = new QName("http://www.opengis.net/gml", "_Solid");

    /** @generated */
    public static final QName _strictAssociation = new QName("http://www.opengis.net/gml",
            "_strictAssociation");

    /** @generated */
    public static final QName _Style = new QName("http://www.opengis.net/gml", "_Style");

    /** @generated */
    public static final QName _Surface = new QName("http://www.opengis.net/gml", "_Surface");

    /** @generated */
    public static final QName _SurfacePatch = new QName("http://www.opengis.net/gml",
            "_SurfacePatch");

    /** @generated */
    public static final QName _TimeComplex = new QName("http://www.opengis.net/gml", "_TimeComplex");

    /** @generated */
    public static final QName _TimeGeometricPrimitive = new QName("http://www.opengis.net/gml",
            "_TimeGeometricPrimitive");

    /** @generated */
    public static final QName _TimeObject = new QName("http://www.opengis.net/gml", "_TimeObject");

    /** @generated */
    public static final QName _TimePrimitive = new QName("http://www.opengis.net/gml",
            "_TimePrimitive");

    /** @generated */
    public static final QName _TimeReferenceSystem = new QName("http://www.opengis.net/gml",
            "_TimeReferenceSystem");

    /** @generated */
    public static final QName _TimeSlice = new QName("http://www.opengis.net/gml", "_TimeSlice");

    /** @generated */
    public static final QName _TimeTopologyPrimitive = new QName("http://www.opengis.net/gml",
            "_TimeTopologyPrimitive");

    /** @generated */
    public static final QName _Topology = new QName("http://www.opengis.net/gml", "_Topology");

    /** @generated */
    public static final QName _TopoPrimitive = new QName("http://www.opengis.net/gml",
            "_TopoPrimitive");

    /** @generated */
    public static final QName absoluteExternalPositionalAccuracy = new QName("http://www.opengis.net/gml",
            "absoluteExternalPositionalAccuracy");

    /** @generated */
    public static final QName abstractGeneralOperationParameterRef = new QName("http://www.opengis.net/gml",
            "abstractGeneralOperationParameterRef");

    /** @generated */
    public static final QName AffinePlacement = new QName("http://www.opengis.net/gml",
            "AffinePlacement");

    /** @generated */
    public static final QName anchorPoint = new QName("http://www.opengis.net/gml", "anchorPoint");

    /** @generated */
    public static final QName angle = new QName("http://www.opengis.net/gml", "angle");

    /** @generated */
    public static final QName Arc = new QName("http://www.opengis.net/gml", "Arc");

    /** @generated */
    public static final QName ArcByBulge = new QName("http://www.opengis.net/gml", "ArcByBulge");

    /** @generated */
    public static final QName ArcByCenterPoint = new QName("http://www.opengis.net/gml",
            "ArcByCenterPoint");

    /** @generated */
    public static final QName ArcString = new QName("http://www.opengis.net/gml", "ArcString");

    /** @generated */
    public static final QName ArcStringByBulge = new QName("http://www.opengis.net/gml",
            "ArcStringByBulge");

    /** @generated */
    public static final QName Array = new QName("http://www.opengis.net/gml", "Array");

    /** @generated */
    public static final QName axisAbbrev = new QName("http://www.opengis.net/gml", "axisAbbrev");

    /** @generated */
    public static final QName axisDirection = new QName("http://www.opengis.net/gml",
            "axisDirection");

    /** @generated */
    public static final QName axisID = new QName("http://www.opengis.net/gml", "axisID");

    /** @generated */
    public static final QName Bag = new QName("http://www.opengis.net/gml", "Bag");

    /** @generated */
    public static final QName baseCRS = new QName("http://www.opengis.net/gml", "baseCRS");

    /** @generated */
    public static final QName baseCurve = new QName("http://www.opengis.net/gml", "baseCurve");

    /** @generated */
    public static final QName baseSurface = new QName("http://www.opengis.net/gml", "baseSurface");

    /** @generated */
    public static final QName BaseUnit = new QName("http://www.opengis.net/gml", "BaseUnit");

    /** @generated */
    public static final QName Bezier = new QName("http://www.opengis.net/gml", "Bezier");

    /** @generated */
    public static final QName Boolean = new QName("http://www.opengis.net/gml", "Boolean");

    /** @generated */
    public static final QName BooleanList = new QName("http://www.opengis.net/gml", "BooleanList");

    /** @generated */
    public static final QName booleanValue = new QName("http://www.opengis.net/gml", "booleanValue");

    /** @generated */
    public static final QName boundedBy = new QName("http://www.opengis.net/gml", "boundedBy");

    /** @generated */
    public static final QName boundingBox = new QName("http://www.opengis.net/gml", "boundingBox");

    /** @generated */
    public static final QName boundingPolygon = new QName("http://www.opengis.net/gml",
            "boundingPolygon");

    /** @generated */
    public static final QName BSpline = new QName("http://www.opengis.net/gml", "BSpline");

    /** @generated */
    public static final QName CartesianCS = new QName("http://www.opengis.net/gml", "CartesianCS");

    /** @generated */
    public static final QName cartesianCSRef = new QName("http://www.opengis.net/gml",
            "cartesianCSRef");

    /** @generated */
    public static final QName catalogSymbol = new QName("http://www.opengis.net/gml",
            "catalogSymbol");

    /** @generated */
    public static final QName Category = new QName("http://www.opengis.net/gml", "Category");

    /** @generated */
    public static final QName CategoryExtent = new QName("http://www.opengis.net/gml",
            "CategoryExtent");

    /** @generated */
    public static final QName CategoryList = new QName("http://www.opengis.net/gml", "CategoryList");

    /** @generated */
    public static final QName centerLineOf = new QName("http://www.opengis.net/gml", "centerLineOf");

    /** @generated */
    public static final QName centerOf = new QName("http://www.opengis.net/gml", "centerOf");

    /** @generated */
    public static final QName Circle = new QName("http://www.opengis.net/gml", "Circle");

    /** @generated */
    public static final QName CircleByCenterPoint = new QName("http://www.opengis.net/gml",
            "CircleByCenterPoint");

    /** @generated */
    public static final QName Clothoid = new QName("http://www.opengis.net/gml", "Clothoid");

    /** @generated */
    public static final QName columnIndex = new QName("http://www.opengis.net/gml", "columnIndex");

    /** @generated */
    public static final QName CompassPoint = new QName("http://www.opengis.net/gml", "CompassPoint");

    /** @generated */
    public static final QName CompositeCurve = new QName("http://www.opengis.net/gml",
            "CompositeCurve");

    /** @generated */
    public static final QName CompositeSolid = new QName("http://www.opengis.net/gml",
            "CompositeSolid");

    /** @generated */
    public static final QName CompositeSurface = new QName("http://www.opengis.net/gml",
            "CompositeSurface");

    /** @generated */
    public static final QName CompositeValue = new QName("http://www.opengis.net/gml",
            "CompositeValue");

    /** @generated */
    public static final QName CompoundCRS = new QName("http://www.opengis.net/gml", "CompoundCRS");

    /** @generated */
    public static final QName compoundCRSRef = new QName("http://www.opengis.net/gml",
            "compoundCRSRef");

    /** @generated */
    public static final QName ConcatenatedOperation = new QName("http://www.opengis.net/gml",
            "ConcatenatedOperation");

    /** @generated */
    public static final QName concatenatedOperationRef = new QName("http://www.opengis.net/gml",
            "concatenatedOperationRef");

    /** @generated */
    public static final QName Cone = new QName("http://www.opengis.net/gml", "Cone");

    /** @generated */
    public static final QName container = new QName("http://www.opengis.net/gml", "container");

    /** @generated */
    public static final QName ConventionalUnit = new QName("http://www.opengis.net/gml",
            "ConventionalUnit");

    /** @generated */
    public static final QName Conversion = new QName("http://www.opengis.net/gml", "Conversion");

    /** @generated */
    public static final QName conversionRef = new QName("http://www.opengis.net/gml",
            "conversionRef");

    /** @generated */
    public static final QName conversionToPreferredUnit = new QName("http://www.opengis.net/gml",
            "conversionToPreferredUnit");

    /** @generated */
    public static final QName coord = new QName("http://www.opengis.net/gml", "coord");

    /** @generated */
    public static final QName coordinateOperationID = new QName("http://www.opengis.net/gml",
            "coordinateOperationID");

    /** @generated */
    public static final QName coordinateOperationName = new QName("http://www.opengis.net/gml",
            "coordinateOperationName");

    /** @generated */
    public static final QName coordinateOperationRef = new QName("http://www.opengis.net/gml",
            "coordinateOperationRef");

    /** @generated */
    public static final QName coordinateReferenceSystemRef = new QName("http://www.opengis.net/gml",
            "coordinateReferenceSystemRef");

    /** @generated */
    public static final QName coordinates = new QName("http://www.opengis.net/gml", "coordinates");

    /** @generated */
    public static final QName CoordinateSystemAxis = new QName("http://www.opengis.net/gml",
            "CoordinateSystemAxis");

    /** @generated */
    public static final QName coordinateSystemAxisRef = new QName("http://www.opengis.net/gml",
            "coordinateSystemAxisRef");

    /** @generated */
    public static final QName coordinateSystemRef = new QName("http://www.opengis.net/gml",
            "coordinateSystemRef");

    /** @generated */
    public static final QName Count = new QName("http://www.opengis.net/gml", "Count");

    /** @generated */
    public static final QName CountExtent = new QName("http://www.opengis.net/gml", "CountExtent");

    /** @generated */
    public static final QName CountList = new QName("http://www.opengis.net/gml", "CountList");

    /** @generated */
    public static final QName covariance = new QName("http://www.opengis.net/gml", "covariance");

    /** @generated */
    public static final QName covarianceMatrix = new QName("http://www.opengis.net/gml",
            "covarianceMatrix");

    /** @generated */
    public static final QName coverageFunction = new QName("http://www.opengis.net/gml",
            "coverageFunction");

    /** @generated */
    public static final QName crsRef = new QName("http://www.opengis.net/gml", "crsRef");

    /** @generated */
    public static final QName csID = new QName("http://www.opengis.net/gml", "csID");

    /** @generated */
    public static final QName csName = new QName("http://www.opengis.net/gml", "csName");

    /** @generated */
    public static final QName CubicSpline = new QName("http://www.opengis.net/gml", "CubicSpline");

    /** @generated */
    public static final QName Curve = new QName("http://www.opengis.net/gml", "Curve");

    /** @generated */
    public static final QName curveArrayProperty = new QName("http://www.opengis.net/gml",
            "curveArrayProperty");

    /** @generated */
    public static final QName curveMember = new QName("http://www.opengis.net/gml", "curveMember");

    /** @generated */
    public static final QName curveMembers = new QName("http://www.opengis.net/gml", "curveMembers");

    /** @generated */
    public static final QName curveProperty = new QName("http://www.opengis.net/gml",
            "curveProperty");

    /** @generated */
    public static final QName Cylinder = new QName("http://www.opengis.net/gml", "Cylinder");

    /** @generated */
    public static final QName CylindricalCS = new QName("http://www.opengis.net/gml",
            "CylindricalCS");

    /** @generated */
    public static final QName cylindricalCSRef = new QName("http://www.opengis.net/gml",
            "cylindricalCSRef");

    /** @generated */
    public static final QName DataBlock = new QName("http://www.opengis.net/gml", "DataBlock");

    /** @generated */
    public static final QName dataSource = new QName("http://www.opengis.net/gml", "dataSource");

    /** @generated */
    public static final QName datumID = new QName("http://www.opengis.net/gml", "datumID");

    /** @generated */
    public static final QName datumName = new QName("http://www.opengis.net/gml", "datumName");

    /** @generated */
    public static final QName datumRef = new QName("http://www.opengis.net/gml", "datumRef");

    /** @generated */
    public static final QName decimalMinutes = new QName("http://www.opengis.net/gml",
            "decimalMinutes");

    /** @generated */
    public static final QName defaultStyle = new QName("http://www.opengis.net/gml", "defaultStyle");

    /** @generated */
    public static final QName definedByConversion = new QName("http://www.opengis.net/gml",
            "definedByConversion");

    /** @generated */
    public static final QName Definition = new QName("http://www.opengis.net/gml", "Definition");

    /** @generated */
    public static final QName DefinitionCollection = new QName("http://www.opengis.net/gml",
            "DefinitionCollection");

    /** @generated */
    public static final QName definitionMember = new QName("http://www.opengis.net/gml",
            "definitionMember");

    /** @generated */
    public static final QName DefinitionProxy = new QName("http://www.opengis.net/gml",
            "DefinitionProxy");

    /** @generated */
    public static final QName definitionRef = new QName("http://www.opengis.net/gml",
            "definitionRef");

    /** @generated */
    public static final QName degrees = new QName("http://www.opengis.net/gml", "degrees");

    /** @generated */
    public static final QName derivationUnitTerm = new QName("http://www.opengis.net/gml",
            "derivationUnitTerm");

    /** @generated */
    public static final QName DerivedCRS = new QName("http://www.opengis.net/gml", "DerivedCRS");

    /** @generated */
    public static final QName derivedCRSRef = new QName("http://www.opengis.net/gml",
            "derivedCRSRef");

    /** @generated */
    public static final QName derivedCRSType = new QName("http://www.opengis.net/gml",
            "derivedCRSType");

    /** @generated */
    public static final QName DerivedUnit = new QName("http://www.opengis.net/gml", "DerivedUnit");

    /** @generated */
    public static final QName description = new QName("http://www.opengis.net/gml", "description");

    /** @generated */
    public static final QName Dictionary = new QName("http://www.opengis.net/gml", "Dictionary");

    /** @generated */
    public static final QName dictionaryEntry = new QName("http://www.opengis.net/gml",
            "dictionaryEntry");

    /** @generated */
    public static final QName directedEdge = new QName("http://www.opengis.net/gml", "directedEdge");

    /** @generated */
    public static final QName directedFace = new QName("http://www.opengis.net/gml", "directedFace");

    /** @generated */
    public static final QName directedNode = new QName("http://www.opengis.net/gml", "directedNode");

    /** @generated */
    public static final QName DirectedObservation = new QName("http://www.opengis.net/gml",
            "DirectedObservation");

    /** @generated */
    public static final QName DirectedObservationAtDistance = new QName("http://www.opengis.net/gml",
            "DirectedObservationAtDistance");

    /** @generated */
    public static final QName directedTopoSolid = new QName("http://www.opengis.net/gml",
            "directedTopoSolid");

    /** @generated */
    public static final QName direction = new QName("http://www.opengis.net/gml", "direction");

    /** @generated */
    public static final QName DirectionVector = new QName("http://www.opengis.net/gml",
            "DirectionVector");

    /** @generated */
    public static final QName dmsAngle = new QName("http://www.opengis.net/gml", "dmsAngle");

    /** @generated */
    public static final QName dmsAngleValue = new QName("http://www.opengis.net/gml",
            "dmsAngleValue");

    /** @generated */
    public static final QName domainSet = new QName("http://www.opengis.net/gml", "domainSet");

    /** @generated */
    public static final QName doubleOrNullTupleList = new QName("http://www.opengis.net/gml",
            "doubleOrNullTupleList");

    /** @generated */
    public static final QName duration = new QName("http://www.opengis.net/gml", "duration");

    /** @generated */
    public static final QName Edge = new QName("http://www.opengis.net/gml", "Edge");

    /** @generated */
    public static final QName edgeOf = new QName("http://www.opengis.net/gml", "edgeOf");

    /** @generated */
    public static final QName Ellipsoid = new QName("http://www.opengis.net/gml", "Ellipsoid");

    /** @generated */
    public static final QName EllipsoidalCS = new QName("http://www.opengis.net/gml",
            "EllipsoidalCS");

    /** @generated */
    public static final QName ellipsoidalCSRef = new QName("http://www.opengis.net/gml",
            "ellipsoidalCSRef");

    /** @generated */
    public static final QName ellipsoidID = new QName("http://www.opengis.net/gml", "ellipsoidID");

    /** @generated */
    public static final QName ellipsoidName = new QName("http://www.opengis.net/gml",
            "ellipsoidName");

    /** @generated */
    public static final QName ellipsoidRef = new QName("http://www.opengis.net/gml", "ellipsoidRef");

    /** @generated */
    public static final QName EngineeringCRS = new QName("http://www.opengis.net/gml",
            "EngineeringCRS");

    /** @generated */
    public static final QName engineeringCRSRef = new QName("http://www.opengis.net/gml",
            "engineeringCRSRef");

    /** @generated */
    public static final QName EngineeringDatum = new QName("http://www.opengis.net/gml",
            "EngineeringDatum");

    /** @generated */
    public static final QName engineeringDatumRef = new QName("http://www.opengis.net/gml",
            "engineeringDatumRef");

    /** @generated */
    public static final QName Envelope = new QName("http://www.opengis.net/gml", "Envelope");

    /** @generated */
    public static final QName EnvelopeWithTimePeriod = new QName("http://www.opengis.net/gml",
            "EnvelopeWithTimePeriod");

    /** @generated */
    public static final QName extentOf = new QName("http://www.opengis.net/gml", "extentOf");

    /** @generated */
    public static final QName exterior = new QName("http://www.opengis.net/gml", "exterior");

    /** @generated */
    public static final QName Face = new QName("http://www.opengis.net/gml", "Face");

    /** @generated */
    public static final QName FeatureCollection = new QName("http://www.opengis.net/gml",
            "FeatureCollection");

    /** @generated */
    public static final QName featureMember = new QName("http://www.opengis.net/gml",
            "featureMember");

    /** @generated */
    public static final QName featureMembers = new QName("http://www.opengis.net/gml",
            "featureMembers");

    /** @generated */
    public static final QName featureProperty = new QName("http://www.opengis.net/gml",
            "featureProperty");

    /** @generated */
    public static final QName featureStyle = new QName("http://www.opengis.net/gml", "featureStyle");

    /** @generated */
    public static final QName FeatureStyle = new QName("http://www.opengis.net/gml", "FeatureStyle");

    /** @generated */
    public static final QName File = new QName("http://www.opengis.net/gml", "File");

    /** @generated */
    public static final QName generalConversionRef = new QName("http://www.opengis.net/gml",
            "generalConversionRef");

    /** @generated */
    public static final QName generalTransformationRef = new QName("http://www.opengis.net/gml",
            "generalTransformationRef");

    /** @generated */
    public static final QName GenericMetaData = new QName("http://www.opengis.net/gml",
            "GenericMetaData");

    /** @generated */
    public static final QName GeocentricCRS = new QName("http://www.opengis.net/gml",
            "GeocentricCRS");

    /** @generated */
    public static final QName geocentricCRSRef = new QName("http://www.opengis.net/gml",
            "geocentricCRSRef");

    /** @generated */
    public static final QName Geodesic = new QName("http://www.opengis.net/gml", "Geodesic");

    /** @generated */
    public static final QName GeodesicString = new QName("http://www.opengis.net/gml",
            "GeodesicString");

    /** @generated */
    public static final QName GeodeticDatum = new QName("http://www.opengis.net/gml",
            "GeodeticDatum");

    /** @generated */
    public static final QName geodeticDatumRef = new QName("http://www.opengis.net/gml",
            "geodeticDatumRef");

    /** @generated */
    public static final QName GeographicCRS = new QName("http://www.opengis.net/gml",
            "GeographicCRS");

    /** @generated */
    public static final QName geographicCRSRef = new QName("http://www.opengis.net/gml",
            "geographicCRSRef");

    /** @generated */
    public static final QName GeometricComplex = new QName("http://www.opengis.net/gml",
            "GeometricComplex");

    /** @generated */
    public static final QName geometryMember = new QName("http://www.opengis.net/gml",
            "geometryMember");

    /** @generated */
    public static final QName geometryMembers = new QName("http://www.opengis.net/gml",
            "geometryMembers");

    /** @generated */
    public static final QName geometryStyle = new QName("http://www.opengis.net/gml",
            "geometryStyle");

    /** @generated */
    public static final QName GeometryStyle = new QName("http://www.opengis.net/gml",
            "GeometryStyle");

    /** @generated */
    public static final QName graphStyle = new QName("http://www.opengis.net/gml", "graphStyle");

    /** @generated */
    public static final QName GraphStyle = new QName("http://www.opengis.net/gml", "GraphStyle");

    /** @generated */
    public static final QName greenwichLongitude = new QName("http://www.opengis.net/gml",
            "greenwichLongitude");

    /** @generated */
    public static final QName Grid = new QName("http://www.opengis.net/gml", "Grid");

    /** @generated */
    public static final QName GridCoverage = new QName("http://www.opengis.net/gml", "GridCoverage");

    /** @generated */
    public static final QName gridDomain = new QName("http://www.opengis.net/gml", "gridDomain");

    /** @generated */
    public static final QName GridFunction = new QName("http://www.opengis.net/gml", "GridFunction");

    /** @generated */
    public static final QName groupID = new QName("http://www.opengis.net/gml", "groupID");

    /** @generated */
    public static final QName groupName = new QName("http://www.opengis.net/gml", "groupName");

    /** @generated */
    public static final QName history = new QName("http://www.opengis.net/gml", "history");

    /** @generated */
    public static final QName ImageCRS = new QName("http://www.opengis.net/gml", "ImageCRS");

    /** @generated */
    public static final QName imageCRSRef = new QName("http://www.opengis.net/gml", "imageCRSRef");

    /** @generated */
    public static final QName ImageDatum = new QName("http://www.opengis.net/gml", "ImageDatum");

    /** @generated */
    public static final QName imageDatumRef = new QName("http://www.opengis.net/gml",
            "imageDatumRef");

    /** @generated */
    public static final QName includesCRS = new QName("http://www.opengis.net/gml", "includesCRS");

    /** @generated */
    public static final QName includesElement = new QName("http://www.opengis.net/gml",
            "includesElement");

    /** @generated */
    public static final QName includesParameter = new QName("http://www.opengis.net/gml",
            "includesParameter");

    /** @generated */
    public static final QName includesValue = new QName("http://www.opengis.net/gml",
            "includesValue");

    /** @generated */
    public static final QName IndexMap = new QName("http://www.opengis.net/gml", "IndexMap");

    /** @generated */
    public static final QName indirectEntry = new QName("http://www.opengis.net/gml",
            "indirectEntry");

    /** @generated */
    public static final QName innerBoundaryIs = new QName("http://www.opengis.net/gml",
            "innerBoundaryIs");

    /** @generated */
    public static final QName integerValue = new QName("http://www.opengis.net/gml", "integerValue");

    /** @generated */
    public static final QName integerValueList = new QName("http://www.opengis.net/gml",
            "integerValueList");

    /** @generated */
    public static final QName interior = new QName("http://www.opengis.net/gml", "interior");

    /** @generated */
    public static final QName inverseFlattening = new QName("http://www.opengis.net/gml",
            "inverseFlattening");

    /** @generated */
    public static final QName isolated = new QName("http://www.opengis.net/gml", "isolated");

    /** @generated */
    public static final QName isSphere = new QName("http://www.opengis.net/gml", "isSphere");

    /** @generated */
    public static final QName labelStyle = new QName("http://www.opengis.net/gml", "labelStyle");

    /** @generated */
    public static final QName LabelStyle = new QName("http://www.opengis.net/gml", "LabelStyle");

    /** @generated */
    public static final QName LinearCS = new QName("http://www.opengis.net/gml", "LinearCS");

    /** @generated */
    public static final QName linearCSRef = new QName("http://www.opengis.net/gml", "linearCSRef");

    /** @generated */
    public static final QName LinearRing = new QName("http://www.opengis.net/gml", "LinearRing");

    /** @generated */
    public static final QName LineString = new QName("http://www.opengis.net/gml", "LineString");

    /** @generated */
    public static final QName lineStringMember = new QName("http://www.opengis.net/gml",
            "lineStringMember");

    /** @generated */
    public static final QName lineStringProperty = new QName("http://www.opengis.net/gml",
            "lineStringProperty");

    /** @generated */
    public static final QName LineStringSegment = new QName("http://www.opengis.net/gml",
            "LineStringSegment");

    /** @generated */
    public static final QName location = new QName("http://www.opengis.net/gml", "location");

    /** @generated */
    public static final QName LocationKeyWord = new QName("http://www.opengis.net/gml",
            "LocationKeyWord");

    /** @generated */
    public static final QName LocationString = new QName("http://www.opengis.net/gml",
            "LocationString");

    /** @generated */
    public static final QName MappingRule = new QName("http://www.opengis.net/gml", "MappingRule");

    /** @generated */
    public static final QName maximalComplex = new QName("http://www.opengis.net/gml",
            "maximalComplex");

    /** @generated */
    public static final QName maximumOccurs = new QName("http://www.opengis.net/gml",
            "maximumOccurs");

    /** @generated */
    public static final QName measure = new QName("http://www.opengis.net/gml", "measure");

    /** @generated */
    public static final QName measureDescription = new QName("http://www.opengis.net/gml",
            "measureDescription");

    /** @generated */
    public static final QName member = new QName("http://www.opengis.net/gml", "member");

    /** @generated */
    public static final QName members = new QName("http://www.opengis.net/gml", "members");

    /** @generated */
    public static final QName meridianID = new QName("http://www.opengis.net/gml", "meridianID");

    /** @generated */
    public static final QName meridianName = new QName("http://www.opengis.net/gml", "meridianName");

    /** @generated */
    public static final QName metaDataProperty = new QName("http://www.opengis.net/gml",
            "metaDataProperty");

    /** @generated */
    public static final QName methodFormula = new QName("http://www.opengis.net/gml",
            "methodFormula");

    /** @generated */
    public static final QName methodID = new QName("http://www.opengis.net/gml", "methodID");

    /** @generated */
    public static final QName methodName = new QName("http://www.opengis.net/gml", "methodName");

    /** @generated */
    public static final QName minimumOccurs = new QName("http://www.opengis.net/gml",
            "minimumOccurs");

    /** @generated */
    public static final QName minutes = new QName("http://www.opengis.net/gml", "minutes");

    /** @generated */
    public static final QName modifiedCoordinate = new QName("http://www.opengis.net/gml",
            "modifiedCoordinate");

    /** @generated */
    public static final QName MovingObjectStatus = new QName("http://www.opengis.net/gml",
            "MovingObjectStatus");

    /** @generated */
    public static final QName multiCenterLineOf = new QName("http://www.opengis.net/gml",
            "multiCenterLineOf");

    /** @generated */
    public static final QName multiCenterOf = new QName("http://www.opengis.net/gml",
            "multiCenterOf");

    /** @generated */
    public static final QName multiCoverage = new QName("http://www.opengis.net/gml",
            "multiCoverage");

    /** @generated */
    public static final QName MultiCurve = new QName("http://www.opengis.net/gml", "MultiCurve");

    /** @generated */
    public static final QName MultiCurveCoverage = new QName("http://www.opengis.net/gml",
            "MultiCurveCoverage");

    /** @generated */
    public static final QName multiCurveDomain = new QName("http://www.opengis.net/gml",
            "multiCurveDomain");

    /** @generated */
    public static final QName multiCurveProperty = new QName("http://www.opengis.net/gml",
            "multiCurveProperty");

    /** @generated */
    public static final QName multiEdgeOf = new QName("http://www.opengis.net/gml", "multiEdgeOf");

    /** @generated */
    public static final QName multiExtentOf = new QName("http://www.opengis.net/gml",
            "multiExtentOf");

    /** @generated */
    public static final QName MultiGeometry = new QName("http://www.opengis.net/gml",
            "MultiGeometry");

    /** @generated */
    public static final QName multiGeometryProperty = new QName("http://www.opengis.net/gml",
            "multiGeometryProperty");

    /** @generated */
    public static final QName MultiLineString = new QName("http://www.opengis.net/gml",
            "MultiLineString");

    /** @generated */
    public static final QName multiLocation = new QName("http://www.opengis.net/gml",
            "multiLocation");

    /** @generated */
    public static final QName MultiPoint = new QName("http://www.opengis.net/gml", "MultiPoint");

    /** @generated */
    public static final QName MultiPointCoverage = new QName("http://www.opengis.net/gml",
            "MultiPointCoverage");

    /** @generated */
    public static final QName multiPointDomain = new QName("http://www.opengis.net/gml",
            "multiPointDomain");

    /** @generated */
    public static final QName multiPointProperty = new QName("http://www.opengis.net/gml",
            "multiPointProperty");

    /** @generated */
    public static final QName MultiPolygon = new QName("http://www.opengis.net/gml", "MultiPolygon");

    /** @generated */
    public static final QName multiPosition = new QName("http://www.opengis.net/gml",
            "multiPosition");

    /** @generated */
    public static final QName MultiSolid = new QName("http://www.opengis.net/gml", "MultiSolid");

    /** @generated */
    public static final QName MultiSolidCoverage = new QName("http://www.opengis.net/gml",
            "MultiSolidCoverage");

    /** @generated */
    public static final QName multiSolidDomain = new QName("http://www.opengis.net/gml",
            "multiSolidDomain");

    /** @generated */
    public static final QName multiSolidProperty = new QName("http://www.opengis.net/gml",
            "multiSolidProperty");

    /** @generated */
    public static final QName MultiSurface = new QName("http://www.opengis.net/gml", "MultiSurface");

    /** @generated */
    public static final QName MultiSurfaceCoverage = new QName("http://www.opengis.net/gml",
            "MultiSurfaceCoverage");

    /** @generated */
    public static final QName multiSurfaceDomain = new QName("http://www.opengis.net/gml",
            "multiSurfaceDomain");

    /** @generated */
    public static final QName multiSurfaceProperty = new QName("http://www.opengis.net/gml",
            "multiSurfaceProperty");

    /** @generated */
    public static final QName name = new QName("http://www.opengis.net/gml", "name");

    /** @generated */
    public static final QName Node = new QName("http://www.opengis.net/gml", "Node");

    /** @generated */
    public static final QName Null = new QName("http://www.opengis.net/gml", "Null");

    /** @generated */
    public static final QName ObliqueCartesianCS = new QName("http://www.opengis.net/gml",
            "ObliqueCartesianCS");

    /** @generated */
    public static final QName obliqueCartesianCSRef = new QName("http://www.opengis.net/gml",
            "obliqueCartesianCSRef");

    /** @generated */
    public static final QName Observation = new QName("http://www.opengis.net/gml", "Observation");

    /** @generated */
    public static final QName OffsetCurve = new QName("http://www.opengis.net/gml", "OffsetCurve");

    /** @generated */
    public static final QName OperationMethod = new QName("http://www.opengis.net/gml",
            "OperationMethod");

    /** @generated */
    public static final QName operationMethodRef = new QName("http://www.opengis.net/gml",
            "operationMethodRef");

    /** @generated */
    public static final QName OperationParameter = new QName("http://www.opengis.net/gml",
            "OperationParameter");

    /** @generated */
    public static final QName OperationParameterGroup = new QName("http://www.opengis.net/gml",
            "OperationParameterGroup");

    /** @generated */
    public static final QName operationParameterGroupRef = new QName("http://www.opengis.net/gml",
            "operationParameterGroupRef");

    /** @generated */
    public static final QName operationParameterRef = new QName("http://www.opengis.net/gml",
            "operationParameterRef");

    /** @generated */
    public static final QName operationRef = new QName("http://www.opengis.net/gml", "operationRef");

    /** @generated */
    public static final QName operationVersion = new QName("http://www.opengis.net/gml",
            "operationVersion");

    /** @generated */
    public static final QName OrientableCurve = new QName("http://www.opengis.net/gml",
            "OrientableCurve");

    /** @generated */
    public static final QName OrientableSurface = new QName("http://www.opengis.net/gml",
            "OrientableSurface");

    /** @generated */
    public static final QName origin = new QName("http://www.opengis.net/gml", "origin");

    /** @generated */
    public static final QName outerBoundaryIs = new QName("http://www.opengis.net/gml",
            "outerBoundaryIs");

    /** @generated */
    public static final QName parameterID = new QName("http://www.opengis.net/gml", "parameterID");

    /** @generated */
    public static final QName parameterName = new QName("http://www.opengis.net/gml",
            "parameterName");

    /** @generated */
    public static final QName parameterValue = new QName("http://www.opengis.net/gml",
            "parameterValue");

    /** @generated */
    public static final QName parameterValueGroup = new QName("http://www.opengis.net/gml",
            "parameterValueGroup");

    /** @generated */
    public static final QName PassThroughOperation = new QName("http://www.opengis.net/gml",
            "PassThroughOperation");

    /** @generated */
    public static final QName passThroughOperationRef = new QName("http://www.opengis.net/gml",
            "passThroughOperationRef");

    /** @generated */
    public static final QName patches = new QName("http://www.opengis.net/gml", "patches");

    /** @generated */
    public static final QName pixelInCell = new QName("http://www.opengis.net/gml", "pixelInCell");

    /** @generated */
    public static final QName Point = new QName("http://www.opengis.net/gml", "Point");

    /** @generated */
    public static final QName pointArrayProperty = new QName("http://www.opengis.net/gml",
            "pointArrayProperty");

    /** @generated */
    public static final QName pointMember = new QName("http://www.opengis.net/gml", "pointMember");

    /** @generated */
    public static final QName pointMembers = new QName("http://www.opengis.net/gml", "pointMembers");

    /** @generated */
    public static final QName pointProperty = new QName("http://www.opengis.net/gml",
            "pointProperty");

    /** @generated */
    public static final QName pointRep = new QName("http://www.opengis.net/gml", "pointRep");

    /** @generated */
    public static final QName PolarCS = new QName("http://www.opengis.net/gml", "PolarCS");

    /** @generated */
    public static final QName polarCSRef = new QName("http://www.opengis.net/gml", "polarCSRef");

    /** @generated */
    public static final QName Polygon = new QName("http://www.opengis.net/gml", "Polygon");

    /** @generated */
    public static final QName polygonMember = new QName("http://www.opengis.net/gml",
            "polygonMember");

    /** @generated */
    public static final QName PolygonPatch = new QName("http://www.opengis.net/gml", "PolygonPatch");

    /** @generated */
    public static final QName polygonPatches = new QName("http://www.opengis.net/gml",
            "polygonPatches");

    /** @generated */
    public static final QName polygonProperty = new QName("http://www.opengis.net/gml",
            "polygonProperty");

    /** @generated */
    public static final QName PolyhedralSurface = new QName("http://www.opengis.net/gml",
            "PolyhedralSurface");

    /** @generated */
    public static final QName pos = new QName("http://www.opengis.net/gml", "pos");

    /** @generated */
    public static final QName position = new QName("http://www.opengis.net/gml", "position");

    /** @generated */
    public static final QName posList = new QName("http://www.opengis.net/gml", "posList");

    /** @generated */
    public static final QName PrimeMeridian = new QName("http://www.opengis.net/gml",
            "PrimeMeridian");

    /** @generated */
    public static final QName primeMeridianRef = new QName("http://www.opengis.net/gml",
            "primeMeridianRef");

    /** @generated */
    public static final QName priorityLocation = new QName("http://www.opengis.net/gml",
            "priorityLocation");

    /** @generated */
    public static final QName ProjectedCRS = new QName("http://www.opengis.net/gml", "ProjectedCRS");

    /** @generated */
    public static final QName projectedCRSRef = new QName("http://www.opengis.net/gml",
            "projectedCRSRef");

    /** @generated */
    public static final QName Quantity = new QName("http://www.opengis.net/gml", "Quantity");

    /** @generated */
    public static final QName QuantityExtent = new QName("http://www.opengis.net/gml",
            "QuantityExtent");

    /** @generated */
    public static final QName QuantityList = new QName("http://www.opengis.net/gml", "QuantityList");

    /** @generated */
    public static final QName quantityType = new QName("http://www.opengis.net/gml", "quantityType");

    /** @generated */
    public static final QName rangeParameters = new QName("http://www.opengis.net/gml",
            "rangeParameters");

    /** @generated */
    public static final QName rangeSet = new QName("http://www.opengis.net/gml", "rangeSet");

    /** @generated */
    public static final QName realizationEpoch = new QName("http://www.opengis.net/gml",
            "realizationEpoch");

    /** @generated */
    public static final QName Rectangle = new QName("http://www.opengis.net/gml", "Rectangle");

    /** @generated */
    public static final QName RectifiedGrid = new QName("http://www.opengis.net/gml",
            "RectifiedGrid");

    /** @generated */
    public static final QName RectifiedGridCoverage = new QName("http://www.opengis.net/gml",
            "RectifiedGridCoverage");

    /** @generated */
    public static final QName rectifiedGridDomain = new QName("http://www.opengis.net/gml",
            "rectifiedGridDomain");

    /** @generated */
    public static final QName referenceSystemRef = new QName("http://www.opengis.net/gml",
            "referenceSystemRef");

    /** @generated */
    public static final QName relativeInternalPositionalAccuracy = new QName("http://www.opengis.net/gml",
            "relativeInternalPositionalAccuracy");

    /** @generated */
    public static final QName remarks = new QName("http://www.opengis.net/gml", "remarks");

    /** @generated */
    public static final QName result = new QName("http://www.opengis.net/gml", "result");

    /** @generated */
    public static final QName resultOf = new QName("http://www.opengis.net/gml", "resultOf");

    /** @generated */
    public static final QName Ring = new QName("http://www.opengis.net/gml", "Ring");

    /** @generated */
    public static final QName roughConversionToPreferredUnit = new QName("http://www.opengis.net/gml",
            "roughConversionToPreferredUnit");

    /** @generated */
    public static final QName rowIndex = new QName("http://www.opengis.net/gml", "rowIndex");

    /** @generated */
    public static final QName scope = new QName("http://www.opengis.net/gml", "scope");

    /** @generated */
    public static final QName secondDefiningParameter = new QName("http://www.opengis.net/gml",
            "secondDefiningParameter");

    /** @generated */
    public static final QName seconds = new QName("http://www.opengis.net/gml", "seconds");

    /** @generated */
    public static final QName segments = new QName("http://www.opengis.net/gml", "segments");

    /** @generated */
    public static final QName semiMajorAxis = new QName("http://www.opengis.net/gml",
            "semiMajorAxis");

    /** @generated */
    public static final QName semiMinorAxis = new QName("http://www.opengis.net/gml",
            "semiMinorAxis");

    /** @generated */
    public static final QName singleOperationRef = new QName("http://www.opengis.net/gml",
            "singleOperationRef");

    /** @generated */
    public static final QName Solid = new QName("http://www.opengis.net/gml", "Solid");

    /** @generated */
    public static final QName solidArrayProperty = new QName("http://www.opengis.net/gml",
            "solidArrayProperty");

    /** @generated */
    public static final QName solidMember = new QName("http://www.opengis.net/gml", "solidMember");

    /** @generated */
    public static final QName solidMembers = new QName("http://www.opengis.net/gml", "solidMembers");

    /** @generated */
    public static final QName solidProperty = new QName("http://www.opengis.net/gml",
            "solidProperty");

    /** @generated */
    public static final QName sourceCRS = new QName("http://www.opengis.net/gml", "sourceCRS");

    /** @generated */
    public static final QName sourceDimensions = new QName("http://www.opengis.net/gml",
            "sourceDimensions");

    /** @generated */
    public static final QName Sphere = new QName("http://www.opengis.net/gml", "Sphere");

    /** @generated */
    public static final QName SphericalCS = new QName("http://www.opengis.net/gml", "SphericalCS");

    /** @generated */
    public static final QName sphericalCSRef = new QName("http://www.opengis.net/gml",
            "sphericalCSRef");

    /** @generated */
    public static final QName srsID = new QName("http://www.opengis.net/gml", "srsID");

    /** @generated */
    public static final QName srsName = new QName("http://www.opengis.net/gml", "srsName");

    /** @generated */
    public static final QName status = new QName("http://www.opengis.net/gml", "status");

    /** @generated */
    public static final QName stringValue = new QName("http://www.opengis.net/gml", "stringValue");

    /** @generated */
    public static final QName Style = new QName("http://www.opengis.net/gml", "Style");

    /** @generated */
    public static final QName subComplex = new QName("http://www.opengis.net/gml", "subComplex");

    /** @generated */
    public static final QName subject = new QName("http://www.opengis.net/gml", "subject");

    /** @generated */
    public static final QName superComplex = new QName("http://www.opengis.net/gml", "superComplex");

    /** @generated */
    public static final QName Surface = new QName("http://www.opengis.net/gml", "Surface");

    /** @generated */
    public static final QName surfaceArrayProperty = new QName("http://www.opengis.net/gml",
            "surfaceArrayProperty");

    /** @generated */
    public static final QName surfaceMember = new QName("http://www.opengis.net/gml",
            "surfaceMember");

    /** @generated */
    public static final QName surfaceMembers = new QName("http://www.opengis.net/gml",
            "surfaceMembers");

    /** @generated */
    public static final QName surfaceProperty = new QName("http://www.opengis.net/gml",
            "surfaceProperty");

    /** @generated */
    public static final QName symbol = new QName("http://www.opengis.net/gml", "symbol");

    /** @generated */
    public static final QName target = new QName("http://www.opengis.net/gml", "target");

    /** @generated */
    public static final QName targetCRS = new QName("http://www.opengis.net/gml", "targetCRS");

    /** @generated */
    public static final QName targetDimensions = new QName("http://www.opengis.net/gml",
            "targetDimensions");

    /** @generated */
    public static final QName TemporalCRS = new QName("http://www.opengis.net/gml", "TemporalCRS");

    /** @generated */
    public static final QName temporalCRSRef = new QName("http://www.opengis.net/gml",
            "temporalCRSRef");

    /** @generated */
    public static final QName TemporalCS = new QName("http://www.opengis.net/gml", "TemporalCS");

    /** @generated */
    public static final QName temporalCSRef = new QName("http://www.opengis.net/gml",
            "temporalCSRef");

    /** @generated */
    public static final QName TemporalDatum = new QName("http://www.opengis.net/gml",
            "TemporalDatum");

    /** @generated */
    public static final QName temporalDatumRef = new QName("http://www.opengis.net/gml",
            "temporalDatumRef");

    /** @generated */
    public static final QName temporalExtent = new QName("http://www.opengis.net/gml",
            "temporalExtent");

    /** @generated */
    public static final QName TimeCalendar = new QName("http://www.opengis.net/gml", "TimeCalendar");

    /** @generated */
    public static final QName TimeCalendarEra = new QName("http://www.opengis.net/gml",
            "TimeCalendarEra");

    /** @generated */
    public static final QName TimeClock = new QName("http://www.opengis.net/gml", "TimeClock");

    /** @generated */
    public static final QName TimeCoordinateSystem = new QName("http://www.opengis.net/gml",
            "TimeCoordinateSystem");

    /** @generated */
    public static final QName TimeEdge = new QName("http://www.opengis.net/gml", "TimeEdge");

    /** @generated */
    public static final QName TimeInstant = new QName("http://www.opengis.net/gml", "TimeInstant");

    /** @generated */
    public static final QName timeInterval = new QName("http://www.opengis.net/gml", "timeInterval");

    /** @generated */
    public static final QName TimeNode = new QName("http://www.opengis.net/gml", "TimeNode");

    /** @generated */
    public static final QName TimeOrdinalEra = new QName("http://www.opengis.net/gml",
            "TimeOrdinalEra");

    /** @generated */
    public static final QName TimeOrdinalReferenceSystem = new QName("http://www.opengis.net/gml",
            "TimeOrdinalReferenceSystem");

    /** @generated */
    public static final QName TimePeriod = new QName("http://www.opengis.net/gml", "TimePeriod");

    /** @generated */
    public static final QName timePosition = new QName("http://www.opengis.net/gml", "timePosition");

    /** @generated */
    public static final QName TimeTopologyComplex = new QName("http://www.opengis.net/gml",
            "TimeTopologyComplex");

    /** @generated */
    public static final QName Tin = new QName("http://www.opengis.net/gml", "Tin");

    /** @generated */
    public static final QName TopoComplex = new QName("http://www.opengis.net/gml", "TopoComplex");

    /** @generated */
    public static final QName topoComplexProperty = new QName("http://www.opengis.net/gml",
            "topoComplexProperty");

    /** @generated */
    public static final QName TopoCurve = new QName("http://www.opengis.net/gml", "TopoCurve");

    /** @generated */
    public static final QName topoCurveProperty = new QName("http://www.opengis.net/gml",
            "topoCurveProperty");

    /** @generated */
    public static final QName topologyStyle = new QName("http://www.opengis.net/gml",
            "topologyStyle");

    /** @generated */
    public static final QName TopologyStyle = new QName("http://www.opengis.net/gml",
            "TopologyStyle");

    /** @generated */
    public static final QName TopoPoint = new QName("http://www.opengis.net/gml", "TopoPoint");

    /** @generated */
    public static final QName topoPointProperty = new QName("http://www.opengis.net/gml",
            "topoPointProperty");

    /** @generated */
    public static final QName topoPrimitiveMember = new QName("http://www.opengis.net/gml",
            "topoPrimitiveMember");

    /** @generated */
    public static final QName topoPrimitiveMembers = new QName("http://www.opengis.net/gml",
            "topoPrimitiveMembers");

    /** @generated */
    public static final QName TopoSolid = new QName("http://www.opengis.net/gml", "TopoSolid");

    /** @generated */
    public static final QName TopoSurface = new QName("http://www.opengis.net/gml", "TopoSurface");

    /** @generated */
    public static final QName topoSurfaceProperty = new QName("http://www.opengis.net/gml",
            "topoSurfaceProperty");

    /** @generated */
    public static final QName TopoVolume = new QName("http://www.opengis.net/gml", "TopoVolume");

    /** @generated */
    public static final QName topoVolumeProperty = new QName("http://www.opengis.net/gml",
            "topoVolumeProperty");

    /** @generated */
    public static final QName track = new QName("http://www.opengis.net/gml", "track");

    /** @generated */
    public static final QName Transformation = new QName("http://www.opengis.net/gml",
            "Transformation");

    /** @generated */
    public static final QName transformationRef = new QName("http://www.opengis.net/gml",
            "transformationRef");

    /** @generated */
    public static final QName Triangle = new QName("http://www.opengis.net/gml", "Triangle");

    /** @generated */
    public static final QName trianglePatches = new QName("http://www.opengis.net/gml",
            "trianglePatches");

    /** @generated */
    public static final QName TriangulatedSurface = new QName("http://www.opengis.net/gml",
            "TriangulatedSurface");

    /** @generated */
    public static final QName tupleList = new QName("http://www.opengis.net/gml", "tupleList");

    /** @generated */
    public static final QName UnitDefinition = new QName("http://www.opengis.net/gml",
            "UnitDefinition");

    /** @generated */
    public static final QName unitOfMeasure = new QName("http://www.opengis.net/gml",
            "unitOfMeasure");

    /** @generated */
    public static final QName UserDefinedCS = new QName("http://www.opengis.net/gml",
            "UserDefinedCS");

    /** @generated */
    public static final QName userDefinedCSRef = new QName("http://www.opengis.net/gml",
            "userDefinedCSRef");

    /** @generated */
    public static final QName usesAxis = new QName("http://www.opengis.net/gml", "usesAxis");

    /** @generated */
    public static final QName usesCartesianCS = new QName("http://www.opengis.net/gml",
            "usesCartesianCS");

    /** @generated */
    public static final QName usesCS = new QName("http://www.opengis.net/gml", "usesCS");

    /** @generated */
    public static final QName usesEllipsoid = new QName("http://www.opengis.net/gml",
            "usesEllipsoid");

    /** @generated */
    public static final QName usesEllipsoidalCS = new QName("http://www.opengis.net/gml",
            "usesEllipsoidalCS");

    /** @generated */
    public static final QName usesEngineeringDatum = new QName("http://www.opengis.net/gml",
            "usesEngineeringDatum");

    /** @generated */
    public static final QName usesGeodeticDatum = new QName("http://www.opengis.net/gml",
            "usesGeodeticDatum");

    /** @generated */
    public static final QName usesImageDatum = new QName("http://www.opengis.net/gml",
            "usesImageDatum");

    /** @generated */
    public static final QName usesMethod = new QName("http://www.opengis.net/gml", "usesMethod");

    /** @generated */
    public static final QName usesObliqueCartesianCS = new QName("http://www.opengis.net/gml",
            "usesObliqueCartesianCS");

    /** @generated */
    public static final QName usesOperation = new QName("http://www.opengis.net/gml",
            "usesOperation");

    /** @generated */
    public static final QName usesParameter = new QName("http://www.opengis.net/gml",
            "usesParameter");

    /** @generated */
    public static final QName usesPrimeMeridian = new QName("http://www.opengis.net/gml",
            "usesPrimeMeridian");

    /** @generated */
    public static final QName usesSingleOperation = new QName("http://www.opengis.net/gml",
            "usesSingleOperation");

    /** @generated */
    public static final QName usesSphericalCS = new QName("http://www.opengis.net/gml",
            "usesSphericalCS");

    /** @generated */
    public static final QName usesTemporalCS = new QName("http://www.opengis.net/gml",
            "usesTemporalCS");

    /** @generated */
    public static final QName usesTemporalDatum = new QName("http://www.opengis.net/gml",
            "usesTemporalDatum");

    /** @generated */
    public static final QName usesValue = new QName("http://www.opengis.net/gml", "usesValue");

    /** @generated */
    public static final QName usesVerticalCS = new QName("http://www.opengis.net/gml",
            "usesVerticalCS");

    /** @generated */
    public static final QName usesVerticalDatum = new QName("http://www.opengis.net/gml",
            "usesVerticalDatum");

    /** @generated */
    public static final QName using = new QName("http://www.opengis.net/gml", "using");

    /** @generated */
    public static final QName validArea = new QName("http://www.opengis.net/gml", "validArea");

    /** @generated */
    public static final QName validTime = new QName("http://www.opengis.net/gml", "validTime");

    /** @generated */
    public static final QName value = new QName("http://www.opengis.net/gml", "value");

    /** @generated */
    public static final QName ValueArray = new QName("http://www.opengis.net/gml", "ValueArray");

    /** @generated */
    public static final QName valueComponent = new QName("http://www.opengis.net/gml",
            "valueComponent");

    /** @generated */
    public static final QName valueComponents = new QName("http://www.opengis.net/gml",
            "valueComponents");

    /** @generated */
    public static final QName valueFile = new QName("http://www.opengis.net/gml", "valueFile");

    /** @generated */
    public static final QName valueList = new QName("http://www.opengis.net/gml", "valueList");

    /** @generated */
    public static final QName valueOfParameter = new QName("http://www.opengis.net/gml",
            "valueOfParameter");

    /** @generated */
    public static final QName valueProperty = new QName("http://www.opengis.net/gml",
            "valueProperty");

    /** @generated */
    public static final QName valuesOfGroup = new QName("http://www.opengis.net/gml",
            "valuesOfGroup");

    /** @generated */
    public static final QName vector = new QName("http://www.opengis.net/gml", "vector");

    /** @generated */
    public static final QName version = new QName("http://www.opengis.net/gml", "version");

    /** @generated */
    public static final QName VerticalCRS = new QName("http://www.opengis.net/gml", "VerticalCRS");

    /** @generated */
    public static final QName verticalCRSRef = new QName("http://www.opengis.net/gml",
            "verticalCRSRef");

    /** @generated */
    public static final QName VerticalCS = new QName("http://www.opengis.net/gml", "VerticalCS");

    /** @generated */
    public static final QName verticalCSRef = new QName("http://www.opengis.net/gml",
            "verticalCSRef");

    /** @generated */
    public static final QName VerticalDatum = new QName("http://www.opengis.net/gml",
            "VerticalDatum");

    /** @generated */
    public static final QName verticalDatumRef = new QName("http://www.opengis.net/gml",
            "verticalDatumRef");

    /** @generated */
    public static final QName verticalDatumType = new QName("http://www.opengis.net/gml",
            "verticalDatumType");

    /** @generated */
    public static final QName verticalExtent = new QName("http://www.opengis.net/gml",
            "verticalExtent");

    /* Attributes */

    /** @generated */
    public static final QName id = new QName("http://www.opengis.net/gml", "id");

    /** @generated */
    public static final QName remoteSchema = new QName("http://www.opengis.net/gml", "remoteSchema");

    /** @generated */
    public static final QName transform = new QName("http://www.opengis.net/gml", "transform");

    /** @generated */
    public static final QName uom = new QName("http://www.opengis.net/gml", "uom");

    /**
     * Private constructor.
     */
    private GML() {
    }

    /**
     * singleton instance.
     */
    private static GML instance = new GML();

    /**
     * The singleton instance;
     */
    public static GML getInstance() {
        return instance;
    }

    @Override
    protected Schema buildTypeSchema() {
        return new GMLSchema();
    }
    
    @Override
    public Schema buildTypeMappingProfile(Schema typeSchema) {
        // set with guaranteed iteration order, so that we can put deprecated elements only
        // after the ones that replaced them
        Set profile = new LinkedHashSet();
        
        //basic
        profile.add(name(GML.MeasureType));

        //geomtetries
        profile.add(name(GML.PointPropertyType));
        profile.add(name(GML.MultiPointPropertyType));
        profile.add(name(GML.LineStringPropertyType));
        profile.add(name(GML.MultiLineStringPropertyType));
        profile.add(name(GML.CurvePropertyType));
        profile.add(name(GML.MultiCurvePropertyType));
        profile.add(name(GML.SurfacePropertyType));
        profile.add(name(GML.MultiSurfacePropertyType));

        // register polygon and multipolygon only after surface, the iteration order
        // will make sure surface is found before in any encoding attempt, this way we
        // are still able to handle polygons, but we don't use them by default
        profile.add(name(GML.PolygonPropertyType));
        profile.add(name(GML.MultiPolygonPropertyType));

        //profile.add( new NameImpl(  GML.NAMESPACE, GML.AbstractGeometryType ) );
        profile.add(name(GML.GeometryPropertyType));
        profile.add(name(GML.MultiGeometryPropertyType));
        
        return typeSchema.profile( profile );
        
    }
    
    protected void addDependencies(Set dependencies) {
        //add xlink dependency
        dependencies.add(XLINK.getInstance());

        //add smil dependency
        dependencies.add(SMIL20.getInstance());
        dependencies.add(SMIL20LANG.getInstance());
    }

    /**
     * Returns 'http://www.opengis.net/gml'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns The location of 'gml.xsd'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("gml.xsd").toString();
    }
    
    @Override
    protected XSDSchema buildSchema() throws IOException {
        XSDSchema schema =  super.buildSchema();
        
        schema.resolveElementDeclaration(NAMESPACE, "_Feature").eAdapters()
            .add(new SubstitutionGroupLeakPreventer());
        schema.eAdapters().add(new ReferencingDirectiveLeakPreventer());
        return schema;
    }
}
