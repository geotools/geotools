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
package org.geotools.gml3.v3_2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.Schema;
import org.geotools.feature.NameImpl;
import org.geotools.gml2.ReferencingDirectiveLeakPreventer;
import org.geotools.gml2.SubstitutionGroupLeakPreventer;
import org.geotools.gml3.v3_2.gmd.GMD;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and attributes in the
 * http://www.opengis.net/gml/3.2 schema.
 *
 * @generated
 */
public final class GML extends XSD {

    /** singleton instance */
    private static final GML instance = new GML();

    /** Returns the singleton instance. */
    public static final GML getInstance() {
        return instance;
    }

    /** private constructor */
    private GML() {}

    @Override
    protected void addDependencies(Set<XSD> dependencies) {
        dependencies.add(XLINK.getInstance());
        dependencies.add(GMD.getInstance());
    }

    /** Returns 'http://www.opengis.net/gml/3.2'. */
    @Override
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /** Returns the location of 'gml.xsd.'. */
    @Override
    public String getSchemaLocation() {
        return getClass().getResource("gml.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/gml/3.2";

    public static final String CANONICAL_SCHEMA_LOCATION = "http://schemas.opengis.net/gml/3.2.1/gml.xsd";

    /* Type Definitions */
    /** @generated */
    public static final QName AbstractContinuousCoverageType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractContinuousCoverageType");
    /** @generated */
    public static final QName AbstractCoordinateOperationType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractCoordinateOperationType");
    /** @generated */
    public static final QName AbstractCoordinateSystemType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractCoordinateSystemType");
    /** @generated */
    public static final QName AbstractCoverageType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractCoverageType");
    /** @generated */
    public static final QName AbstractCRSType = new QName("http://www.opengis.net/gml/3.2", "AbstractCRSType");
    /** @generated */
    public static final QName AbstractCurveSegmentType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractCurveSegmentType");
    /** @generated */
    public static final QName AbstractCurveType = new QName("http://www.opengis.net/gml/3.2", "AbstractCurveType");
    /** @generated */
    public static final QName AbstractDatumType = new QName("http://www.opengis.net/gml/3.2", "AbstractDatumType");
    /** @generated */
    public static final QName AbstractFeatureCollectionType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractFeatureCollectionType");
    /** @generated */
    public static final QName AbstractFeatureMemberType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractFeatureMemberType");
    /** @generated */
    public static final QName AbstractFeatureType = new QName("http://www.opengis.net/gml/3.2", "AbstractFeatureType");
    /** @generated */
    public static final QName AbstractGeneralConversionType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralConversionType");
    /** @generated */
    public static final QName AbstractGeneralDerivedCRSType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralDerivedCRSType");
    /** @generated */
    public static final QName AbstractGeneralOperationParameterPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralOperationParameterPropertyType");
    /** @generated */
    public static final QName AbstractGeneralOperationParameterType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralOperationParameterType");
    /** @generated */
    public static final QName AbstractGeneralParameterValuePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralParameterValuePropertyType");
    /** @generated */
    public static final QName AbstractGeneralParameterValueType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralParameterValueType");
    /** @generated */
    public static final QName AbstractGeneralTransformationType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralTransformationType");
    /** @generated */
    public static final QName AbstractGeometricAggregateType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeometricAggregateType");
    /** @generated */
    public static final QName AbstractGeometricPrimitiveType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeometricPrimitiveType");
    /** @generated */
    public static final QName AbstractGeometryType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeometryType");
    /** @generated */
    public static final QName AbstractGMLType = new QName("http://www.opengis.net/gml/3.2", "AbstractGMLType");
    /** @generated */
    public static final QName AbstractGriddedSurfaceType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGriddedSurfaceType");
    /** @generated */
    public static final QName AbstractMemberType = new QName("http://www.opengis.net/gml/3.2", "AbstractMemberType");
    /** @generated */
    public static final QName AbstractMetadataPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractMetadataPropertyType");
    /** @generated */
    public static final QName AbstractMetaDataType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractMetaDataType");
    /** @generated */
    public static final QName AbstractParametricCurveSurfaceType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractParametricCurveSurfaceType");
    /** @generated */
    public static final QName AbstractRingPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractRingPropertyType");
    /** @generated */
    public static final QName AbstractRingType = new QName("http://www.opengis.net/gml/3.2", "AbstractRingType");
    /** @generated */
    public static final QName AbstractSolidType = new QName("http://www.opengis.net/gml/3.2", "AbstractSolidType");
    /** @generated */
    public static final QName AbstractSurfacePatchType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractSurfacePatchType");
    /** @generated */
    public static final QName AbstractSurfaceType = new QName("http://www.opengis.net/gml/3.2", "AbstractSurfaceType");
    /** @generated */
    public static final QName AbstractTimeComplexType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimeComplexType");
    /** @generated */
    public static final QName AbstractTimeGeometricPrimitiveType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimeGeometricPrimitiveType");
    /** @generated */
    public static final QName AbstractTimeObjectType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimeObjectType");
    /** @generated */
    public static final QName AbstractTimePrimitiveType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimePrimitiveType");
    /** @generated */
    public static final QName AbstractTimeSliceType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimeSliceType");
    /** @generated */
    public static final QName AbstractTimeTopologyPrimitiveType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimeTopologyPrimitiveType");
    /** @generated */
    public static final QName AbstractTopologyType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTopologyType");
    /** @generated */
    public static final QName AbstractTopoPrimitiveType =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTopoPrimitiveType");
    /** @generated */
    public static final QName AffineCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "AffineCSPropertyType");
    /** @generated */
    public static final QName AffineCSType = new QName("http://www.opengis.net/gml/3.2", "AffineCSType");
    /** @generated */
    public static final QName AffinePlacementType = new QName("http://www.opengis.net/gml/3.2", "AffinePlacementType");
    /** @generated */
    public static final QName AggregationType = new QName("http://www.opengis.net/gml/3.2", "AggregationType");
    /** @generated */
    public static final QName AngleChoiceType = new QName("http://www.opengis.net/gml/3.2", "AngleChoiceType");
    /** @generated */
    public static final QName AngleType = new QName("http://www.opengis.net/gml/3.2", "AngleType");
    /** @generated */
    public static final QName ArcByBulgeType = new QName("http://www.opengis.net/gml/3.2", "ArcByBulgeType");
    /** @generated */
    public static final QName ArcByCenterPointType =
            new QName("http://www.opengis.net/gml/3.2", "ArcByCenterPointType");
    /** @generated */
    public static final QName ArcMinutesType = new QName("http://www.opengis.net/gml/3.2", "ArcMinutesType");
    /** @generated */
    public static final QName ArcSecondsType = new QName("http://www.opengis.net/gml/3.2", "ArcSecondsType");
    /** @generated */
    public static final QName ArcStringByBulgeType =
            new QName("http://www.opengis.net/gml/3.2", "ArcStringByBulgeType");
    /** @generated */
    public static final QName ArcStringType = new QName("http://www.opengis.net/gml/3.2", "ArcStringType");
    /** @generated */
    public static final QName ArcType = new QName("http://www.opengis.net/gml/3.2", "ArcType");
    /** @generated */
    public static final QName AreaType = new QName("http://www.opengis.net/gml/3.2", "AreaType");
    /** @generated */
    public static final QName ArrayAssociationType =
            new QName("http://www.opengis.net/gml/3.2", "ArrayAssociationType");
    /** @generated */
    public static final QName ArrayType = new QName("http://www.opengis.net/gml/3.2", "ArrayType");
    /** @generated */
    public static final QName AssociationRoleType = new QName("http://www.opengis.net/gml/3.2", "AssociationRoleType");
    /** @generated */
    public static final QName AxisDirection = new QName("http://www.opengis.net/gml/3.2", "AxisDirection");
    /** @generated */
    public static final QName AxisDirectionList = new QName("http://www.opengis.net/gml/3.2", "AxisDirectionList");
    /** @generated */
    public static final QName BagType = new QName("http://www.opengis.net/gml/3.2", "BagType");
    /** @generated */
    public static final QName BaseUnitType = new QName("http://www.opengis.net/gml/3.2", "BaseUnitType");
    /** @generated */
    public static final QName BezierType = new QName("http://www.opengis.net/gml/3.2", "BezierType");
    /** @generated */
    public static final QName booleanList = new QName("http://www.opengis.net/gml/3.2", "booleanList");
    /** @generated */
    public static final QName booleanOrNilReason = new QName("http://www.opengis.net/gml/3.2", "booleanOrNilReason");
    /** @generated */
    public static final QName booleanOrNilReasonList =
            new QName("http://www.opengis.net/gml/3.2", "booleanOrNilReasonList");
    /** @generated */
    public static final QName BooleanPropertyType = new QName("http://www.opengis.net/gml/3.2", "BooleanPropertyType");
    /** @generated */
    public static final QName BoundedFeatureType = new QName("http://www.opengis.net/gml/3.2", "BoundedFeatureType");
    /** @generated */
    public static final QName BoundingShapeType = new QName("http://www.opengis.net/gml/3.2", "BoundingShapeType");
    /** @generated */
    public static final QName BSplineType = new QName("http://www.opengis.net/gml/3.2", "BSplineType");
    /** @generated */
    public static final QName CalDate = new QName("http://www.opengis.net/gml/3.2", "CalDate");
    /** @generated */
    public static final QName CartesianCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CartesianCSPropertyType");
    /** @generated */
    public static final QName CartesianCSType = new QName("http://www.opengis.net/gml/3.2", "CartesianCSType");
    /** @generated */
    public static final QName CategoryExtentType = new QName("http://www.opengis.net/gml/3.2", "CategoryExtentType");
    /** @generated */
    public static final QName CategoryPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CategoryPropertyType");
    /** @generated */
    public static final QName CircleByCenterPointType =
            new QName("http://www.opengis.net/gml/3.2", "CircleByCenterPointType");
    /** @generated */
    public static final QName CircleType = new QName("http://www.opengis.net/gml/3.2", "CircleType");
    /** @generated */
    public static final QName ClothoidType = new QName("http://www.opengis.net/gml/3.2", "ClothoidType");
    /** @generated */
    public static final QName CodeListType = new QName("http://www.opengis.net/gml/3.2", "CodeListType");
    /** @generated */
    public static final QName CodeOrNilReasonListType =
            new QName("http://www.opengis.net/gml/3.2", "CodeOrNilReasonListType");
    /** @generated */
    public static final QName CodeType = new QName("http://www.opengis.net/gml/3.2", "CodeType");
    /** @generated */
    public static final QName CodeWithAuthorityType =
            new QName("http://www.opengis.net/gml/3.2", "CodeWithAuthorityType");
    /** @generated */
    public static final QName CompassPointEnumeration =
            new QName("http://www.opengis.net/gml/3.2", "CompassPointEnumeration");
    /** @generated */
    public static final QName CompositeCurveType = new QName("http://www.opengis.net/gml/3.2", "CompositeCurveType");
    /** @generated */
    public static final QName CompositeSolidType = new QName("http://www.opengis.net/gml/3.2", "CompositeSolidType");
    /** @generated */
    public static final QName CompositeSurfaceType =
            new QName("http://www.opengis.net/gml/3.2", "CompositeSurfaceType");
    /** @generated */
    public static final QName CompositeValueType = new QName("http://www.opengis.net/gml/3.2", "CompositeValueType");
    /** @generated */
    public static final QName CompoundCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CompoundCRSPropertyType");
    /** @generated */
    public static final QName CompoundCRSType = new QName("http://www.opengis.net/gml/3.2", "CompoundCRSType");
    /** @generated */
    public static final QName ConcatenatedOperationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ConcatenatedOperationPropertyType");
    /** @generated */
    public static final QName ConcatenatedOperationType =
            new QName("http://www.opengis.net/gml/3.2", "ConcatenatedOperationType");
    /** @generated */
    public static final QName ConeType = new QName("http://www.opengis.net/gml/3.2", "ConeType");
    /** @generated */
    public static final QName ConventionalUnitType =
            new QName("http://www.opengis.net/gml/3.2", "ConventionalUnitType");
    /** @generated */
    public static final QName ConversionPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ConversionPropertyType");
    /** @generated */
    public static final QName ConversionToPreferredUnitType =
            new QName("http://www.opengis.net/gml/3.2", "ConversionToPreferredUnitType");
    /** @generated */
    public static final QName ConversionType = new QName("http://www.opengis.net/gml/3.2", "ConversionType");
    /** @generated */
    public static final QName CoordinateOperationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CoordinateOperationPropertyType");
    /** @generated */
    public static final QName CoordinatesType = new QName("http://www.opengis.net/gml/3.2", "CoordinatesType");
    /** @generated */
    public static final QName CoordinateSystemAxisPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CoordinateSystemAxisPropertyType");
    /** @generated */
    public static final QName CoordinateSystemAxisType =
            new QName("http://www.opengis.net/gml/3.2", "CoordinateSystemAxisType");
    /** @generated */
    public static final QName CoordinateSystemPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CoordinateSystemPropertyType");
    /** @generated */
    public static final QName CountExtentType = new QName("http://www.opengis.net/gml/3.2", "CountExtentType");
    /** @generated */
    public static final QName CountPropertyType = new QName("http://www.opengis.net/gml/3.2", "CountPropertyType");
    /** @generated */
    public static final QName CoverageFunctionType =
            new QName("http://www.opengis.net/gml/3.2", "CoverageFunctionType");
    /** @generated */
    public static final QName CRSPropertyType = new QName("http://www.opengis.net/gml/3.2", "CRSPropertyType");
    /** @generated */
    public static final QName CubicSplineType = new QName("http://www.opengis.net/gml/3.2", "CubicSplineType");
    /** @generated */
    public static final QName CurveArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CurveArrayPropertyType");
    /** @generated */
    public static final QName CurveInterpolationType =
            new QName("http://www.opengis.net/gml/3.2", "CurveInterpolationType");
    /** @generated */
    public static final QName CurvePropertyType = new QName("http://www.opengis.net/gml/3.2", "CurvePropertyType");
    /** @generated */
    public static final QName CurveSegmentArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CurveSegmentArrayPropertyType");
    /** @generated */
    public static final QName CurveType = new QName("http://www.opengis.net/gml/3.2", "CurveType");
    /** @generated */
    public static final QName CylinderType = new QName("http://www.opengis.net/gml/3.2", "CylinderType");
    /** @generated */
    public static final QName CylindricalCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "CylindricalCSPropertyType");
    /** @generated */
    public static final QName CylindricalCSType = new QName("http://www.opengis.net/gml/3.2", "CylindricalCSType");
    /** @generated */
    public static final QName DataBlockType = new QName("http://www.opengis.net/gml/3.2", "DataBlockType");
    /** @generated */
    public static final QName DatumPropertyType = new QName("http://www.opengis.net/gml/3.2", "DatumPropertyType");
    /** @generated */
    public static final QName DecimalMinutesType = new QName("http://www.opengis.net/gml/3.2", "DecimalMinutesType");
    /** @generated */
    public static final QName DefinitionBaseType = new QName("http://www.opengis.net/gml/3.2", "DefinitionBaseType");
    /** @generated */
    public static final QName DefinitionProxyType = new QName("http://www.opengis.net/gml/3.2", "DefinitionProxyType");
    /** @generated */
    public static final QName DefinitionType = new QName("http://www.opengis.net/gml/3.2", "DefinitionType");
    /** @generated */
    public static final QName DegreesType = new QName("http://www.opengis.net/gml/3.2", "DegreesType");
    /** @generated */
    public static final QName DegreeValueType = new QName("http://www.opengis.net/gml/3.2", "DegreeValueType");
    /** @generated */
    public static final QName DerivationUnitTermType =
            new QName("http://www.opengis.net/gml/3.2", "DerivationUnitTermType");
    /** @generated */
    public static final QName DerivedCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "DerivedCRSPropertyType");
    /** @generated */
    public static final QName DerivedCRSType = new QName("http://www.opengis.net/gml/3.2", "DerivedCRSType");
    /** @generated */
    public static final QName DerivedUnitType = new QName("http://www.opengis.net/gml/3.2", "DerivedUnitType");
    /** @generated */
    public static final QName DictionaryEntryType = new QName("http://www.opengis.net/gml/3.2", "DictionaryEntryType");
    /** @generated */
    public static final QName DictionaryType = new QName("http://www.opengis.net/gml/3.2", "DictionaryType");
    /** @generated */
    public static final QName DirectedEdgePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "DirectedEdgePropertyType");
    /** @generated */
    public static final QName DirectedFacePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "DirectedFacePropertyType");
    /** @generated */
    public static final QName DirectedNodePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "DirectedNodePropertyType");
    /** @generated */
    public static final QName DirectedObservationAtDistanceType =
            new QName("http://www.opengis.net/gml/3.2", "DirectedObservationAtDistanceType");
    /** @generated */
    public static final QName DirectedObservationType =
            new QName("http://www.opengis.net/gml/3.2", "DirectedObservationType");
    /** @generated */
    public static final QName DirectedTopoSolidPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "DirectedTopoSolidPropertyType");
    /** @generated */
    public static final QName DirectionDescriptionType =
            new QName("http://www.opengis.net/gml/3.2", "DirectionDescriptionType");
    /** @generated */
    public static final QName DirectionPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "DirectionPropertyType");
    /** @generated */
    public static final QName DirectionVectorType = new QName("http://www.opengis.net/gml/3.2", "DirectionVectorType");
    /** @generated */
    public static final QName DirectPositionListType =
            new QName("http://www.opengis.net/gml/3.2", "DirectPositionListType");
    /** @generated */
    public static final QName DirectPositionType = new QName("http://www.opengis.net/gml/3.2", "DirectPositionType");
    /** @generated */
    public static final QName DiscreteCoverageType =
            new QName("http://www.opengis.net/gml/3.2", "DiscreteCoverageType");
    /** @generated */
    public static final QName DMSAngleType = new QName("http://www.opengis.net/gml/3.2", "DMSAngleType");
    /** @generated */
    public static final QName DomainSetType = new QName("http://www.opengis.net/gml/3.2", "DomainSetType");
    /** @generated */
    public static final QName doubleList = new QName("http://www.opengis.net/gml/3.2", "doubleList");
    /** @generated */
    public static final QName doubleOrNilReason = new QName("http://www.opengis.net/gml/3.2", "doubleOrNilReason");
    /** @generated */
    public static final QName doubleOrNilReasonList =
            new QName("http://www.opengis.net/gml/3.2", "doubleOrNilReasonList");
    /** @generated */
    public static final QName DynamicFeatureCollectionType =
            new QName("http://www.opengis.net/gml/3.2", "DynamicFeatureCollectionType");
    /** @generated */
    public static final QName DynamicFeatureMemberType =
            new QName("http://www.opengis.net/gml/3.2", "DynamicFeatureMemberType");
    /** @generated */
    public static final QName DynamicFeatureType = new QName("http://www.opengis.net/gml/3.2", "DynamicFeatureType");
    /** @generated */
    public static final QName EdgeType = new QName("http://www.opengis.net/gml/3.2", "EdgeType");
    /** @generated */
    public static final QName EllipsoidalCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "EllipsoidalCSPropertyType");
    /** @generated */
    public static final QName EllipsoidalCSType = new QName("http://www.opengis.net/gml/3.2", "EllipsoidalCSType");
    /** @generated */
    public static final QName EllipsoidPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "EllipsoidPropertyType");
    /** @generated */
    public static final QName EllipsoidType = new QName("http://www.opengis.net/gml/3.2", "EllipsoidType");
    /** @generated */
    public static final QName EngineeringCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "EngineeringCRSPropertyType");
    /** @generated */
    public static final QName EngineeringCRSType = new QName("http://www.opengis.net/gml/3.2", "EngineeringCRSType");
    /** @generated */
    public static final QName EngineeringDatumPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "EngineeringDatumPropertyType");
    /** @generated */
    public static final QName EngineeringDatumType =
            new QName("http://www.opengis.net/gml/3.2", "EngineeringDatumType");
    /** @generated */
    public static final QName EnvelopeType = new QName("http://www.opengis.net/gml/3.2", "EnvelopeType");
    /** @generated */
    public static final QName EnvelopeWithTimePeriodType =
            new QName("http://www.opengis.net/gml/3.2", "EnvelopeWithTimePeriodType");
    /** @generated */
    public static final QName FaceOrTopoSolidPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "FaceOrTopoSolidPropertyType");
    /** @generated */
    public static final QName FaceType = new QName("http://www.opengis.net/gml/3.2", "FaceType");
    /** @generated */
    public static final QName FeatureArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "FeatureArrayPropertyType");
    /** @generated */
    public static final QName FeatureCollectionType =
            new QName("http://www.opengis.net/gml/3.2", "FeatureCollectionType");
    /** @generated */
    public static final QName FeaturePropertyType = new QName("http://www.opengis.net/gml/3.2", "FeaturePropertyType");
    /** @generated */
    public static final QName FileType = new QName("http://www.opengis.net/gml/3.2", "FileType");
    /** @generated */
    public static final QName FormulaType = new QName("http://www.opengis.net/gml/3.2", "FormulaType");
    /** @generated */
    public static final QName GeneralConversionPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeneralConversionPropertyType");
    /** @generated */
    public static final QName GeneralTransformationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeneralTransformationPropertyType");
    /** @generated */
    public static final QName GenericMetaDataType = new QName("http://www.opengis.net/gml/3.2", "GenericMetaDataType");
    /** @generated */
    public static final QName GeocentricCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeocentricCRSPropertyType");
    /** @generated */
    public static final QName GeocentricCRSType = new QName("http://www.opengis.net/gml/3.2", "GeocentricCRSType");
    /** @generated */
    public static final QName GeodesicStringType = new QName("http://www.opengis.net/gml/3.2", "GeodesicStringType");
    /** @generated */
    public static final QName GeodesicType = new QName("http://www.opengis.net/gml/3.2", "GeodesicType");
    /** @generated */
    public static final QName GeodeticCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeodeticCRSPropertyType");
    /** @generated */
    public static final QName GeodeticCRSType = new QName("http://www.opengis.net/gml/3.2", "GeodeticCRSType");
    /** @generated */
    public static final QName GeodeticDatumPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeodeticDatumPropertyType");
    /** @generated */
    public static final QName GeodeticDatumType = new QName("http://www.opengis.net/gml/3.2", "GeodeticDatumType");
    /** @generated */
    public static final QName GeographicCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeographicCRSPropertyType");
    /** @generated */
    public static final QName GeographicCRSType = new QName("http://www.opengis.net/gml/3.2", "GeographicCRSType");
    /** @generated */
    public static final QName GeometricComplexPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeometricComplexPropertyType");
    /** @generated */
    public static final QName GeometricComplexType =
            new QName("http://www.opengis.net/gml/3.2", "GeometricComplexType");
    /** @generated */
    public static final QName GeometricPrimitivePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeometricPrimitivePropertyType");
    /** @generated */
    public static final QName GeometryArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeometryArrayPropertyType");
    /** @generated */
    public static final QName GeometryPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "GeometryPropertyType");
    /** @generated */
    public static final QName GridEnvelopeType = new QName("http://www.opengis.net/gml/3.2", "GridEnvelopeType");
    /** @generated */
    public static final QName GridFunctionType = new QName("http://www.opengis.net/gml/3.2", "GridFunctionType");
    /** @generated */
    public static final QName GridLengthType = new QName("http://www.opengis.net/gml/3.2", "GridLengthType");
    /** @generated */
    public static final QName GridLimitsType = new QName("http://www.opengis.net/gml/3.2", "GridLimitsType");
    /** @generated */
    public static final QName GridType = new QName("http://www.opengis.net/gml/3.2", "GridType");
    /** @generated */
    public static final QName HistoryPropertyType = new QName("http://www.opengis.net/gml/3.2", "HistoryPropertyType");
    /** @generated */
    public static final QName IdentifiedObjectType =
            new QName("http://www.opengis.net/gml/3.2", "IdentifiedObjectType");
    /** @generated */
    public static final QName ImageCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ImageCRSPropertyType");
    /** @generated */
    public static final QName ImageCRSType = new QName("http://www.opengis.net/gml/3.2", "ImageCRSType");
    /** @generated */
    public static final QName ImageDatumPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ImageDatumPropertyType");
    /** @generated */
    public static final QName ImageDatumType = new QName("http://www.opengis.net/gml/3.2", "ImageDatumType");
    /** @generated */
    public static final QName IncrementOrder = new QName("http://www.opengis.net/gml/3.2", "IncrementOrder");
    /** @generated */
    public static final QName IndirectEntryType = new QName("http://www.opengis.net/gml/3.2", "IndirectEntryType");
    /** @generated */
    public static final QName InlinePropertyType = new QName("http://www.opengis.net/gml/3.2", "InlinePropertyType");
    /** @generated */
    public static final QName integerList = new QName("http://www.opengis.net/gml/3.2", "integerList");
    /** @generated */
    public static final QName integerOrNilReason = new QName("http://www.opengis.net/gml/3.2", "integerOrNilReason");
    /** @generated */
    public static final QName integerOrNilReasonList =
            new QName("http://www.opengis.net/gml/3.2", "integerOrNilReasonList");
    /** @generated */
    public static final QName KnotPropertyType = new QName("http://www.opengis.net/gml/3.2", "KnotPropertyType");
    /** @generated */
    public static final QName KnotType = new QName("http://www.opengis.net/gml/3.2", "KnotType");
    /** @generated */
    public static final QName KnotTypesType = new QName("http://www.opengis.net/gml/3.2", "KnotTypesType");
    /** @generated */
    public static final QName LengthType = new QName("http://www.opengis.net/gml/3.2", "LengthType");
    /** @generated */
    public static final QName LinearCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "LinearCSPropertyType");
    /** @generated */
    public static final QName LinearCSType = new QName("http://www.opengis.net/gml/3.2", "LinearCSType");
    /** @generated */
    public static final QName LinearRingPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "LinearRingPropertyType");
    /** @generated */
    public static final QName LinearRingType = new QName("http://www.opengis.net/gml/3.2", "LinearRingType");
    /** @generated */
    public static final QName LineStringSegmentArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "LineStringSegmentArrayPropertyType");
    /** @generated */
    public static final QName LineStringSegmentType =
            new QName("http://www.opengis.net/gml/3.2", "LineStringSegmentType");
    /** @generated */
    public static final QName LineStringType = new QName("http://www.opengis.net/gml/3.2", "LineStringType");
    /** @generated */
    public static final QName LocationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "LocationPropertyType");
    /** @generated */
    public static final QName MappingRuleType = new QName("http://www.opengis.net/gml/3.2", "MappingRuleType");
    /** @generated */
    public static final QName MeasureListType = new QName("http://www.opengis.net/gml/3.2", "MeasureListType");
    /** @generated */
    public static final QName MeasureOrNilReasonListType =
            new QName("http://www.opengis.net/gml/3.2", "MeasureOrNilReasonListType");
    /** @generated */
    public static final QName MeasureType = new QName("http://www.opengis.net/gml/3.2", "MeasureType");
    /** @generated */
    public static final QName MetaDataPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "MetaDataPropertyType");
    /** @generated */
    public static final QName MovingObjectStatusType =
            new QName("http://www.opengis.net/gml/3.2", "MovingObjectStatusType");
    /** @generated */
    public static final QName MultiCurvePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "MultiCurvePropertyType");
    /** @generated */
    public static final QName MultiCurveType = new QName("http://www.opengis.net/gml/3.2", "MultiCurveType");
    /** @generated */
    public static final QName MultiGeometryPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "MultiGeometryPropertyType");
    /** @generated */
    public static final QName MultiGeometryType = new QName("http://www.opengis.net/gml/3.2", "MultiGeometryType");
    /** @generated */
    public static final QName MultiPointPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "MultiPointPropertyType");
    /** @generated */
    public static final QName MultiPointType = new QName("http://www.opengis.net/gml/3.2", "MultiPointType");
    /** @generated */
    public static final QName MultiSolidPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "MultiSolidPropertyType");
    /** @generated */
    public static final QName MultiSolidType = new QName("http://www.opengis.net/gml/3.2", "MultiSolidType");
    /** @generated */
    public static final QName MultiSurfacePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "MultiSurfacePropertyType");
    /** @generated */
    public static final QName MultiSurfaceType = new QName("http://www.opengis.net/gml/3.2", "MultiSurfaceType");
    /** @generated */
    public static final QName NameList = new QName("http://www.opengis.net/gml/3.2", "NameList");
    /** @generated */
    public static final QName NameOrNilReason = new QName("http://www.opengis.net/gml/3.2", "NameOrNilReason");
    /** @generated */
    public static final QName NameOrNilReasonList = new QName("http://www.opengis.net/gml/3.2", "NameOrNilReasonList");
    /** @generated */
    public static final QName NCNameList = new QName("http://www.opengis.net/gml/3.2", "NCNameList");
    /** @generated */
    public static final QName NilReasonEnumeration =
            new QName("http://www.opengis.net/gml/3.2", "NilReasonEnumeration");
    /** @generated */
    public static final QName NilReasonType = new QName("http://www.opengis.net/gml/3.2", "NilReasonType");
    /** @generated */
    public static final QName NodeOrEdgePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "NodeOrEdgePropertyType");
    /** @generated */
    public static final QName NodePropertyType = new QName("http://www.opengis.net/gml/3.2", "NodePropertyType");
    /** @generated */
    public static final QName NodeType = new QName("http://www.opengis.net/gml/3.2", "NodeType");
    /** @generated */
    public static final QName ObliqueCartesianCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ObliqueCartesianCSPropertyType");
    /** @generated */
    public static final QName ObliqueCartesianCSType =
            new QName("http://www.opengis.net/gml/3.2", "ObliqueCartesianCSType");
    /** @generated */
    public static final QName ObservationType = new QName("http://www.opengis.net/gml/3.2", "ObservationType");
    /** @generated */
    public static final QName OffsetCurveType = new QName("http://www.opengis.net/gml/3.2", "OffsetCurveType");
    /** @generated */
    public static final QName OperationMethodPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "OperationMethodPropertyType");
    /** @generated */
    public static final QName OperationMethodType = new QName("http://www.opengis.net/gml/3.2", "OperationMethodType");
    /** @generated */
    public static final QName OperationParameterGroupPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "OperationParameterGroupPropertyType");
    /** @generated */
    public static final QName OperationParameterGroupType =
            new QName("http://www.opengis.net/gml/3.2", "OperationParameterGroupType");
    /** @generated */
    public static final QName OperationParameterPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "OperationParameterPropertyType");
    /** @generated */
    public static final QName OperationParameterType =
            new QName("http://www.opengis.net/gml/3.2", "OperationParameterType");
    /** @generated */
    public static final QName OperationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "OperationPropertyType");
    /** @generated */
    public static final QName OrientableCurveType = new QName("http://www.opengis.net/gml/3.2", "OrientableCurveType");
    /** @generated */
    public static final QName OrientableSurfaceType =
            new QName("http://www.opengis.net/gml/3.2", "OrientableSurfaceType");
    /** @generated */
    public static final QName ParameterValueGroupType =
            new QName("http://www.opengis.net/gml/3.2", "ParameterValueGroupType");
    /** @generated */
    public static final QName ParameterValueType = new QName("http://www.opengis.net/gml/3.2", "ParameterValueType");
    /** @generated */
    public static final QName PassThroughOperationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "PassThroughOperationPropertyType");
    /** @generated */
    public static final QName PassThroughOperationType =
            new QName("http://www.opengis.net/gml/3.2", "PassThroughOperationType");
    /** @generated */
    public static final QName PointArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "PointArrayPropertyType");
    /** @generated */
    public static final QName PointPropertyType = new QName("http://www.opengis.net/gml/3.2", "PointPropertyType");
    /** @generated */
    public static final QName PointType = new QName("http://www.opengis.net/gml/3.2", "PointType");
    /** @generated */
    public static final QName PolarCSPropertyType = new QName("http://www.opengis.net/gml/3.2", "PolarCSPropertyType");
    /** @generated */
    public static final QName PolarCSType = new QName("http://www.opengis.net/gml/3.2", "PolarCSType");
    /** @generated */
    public static final QName PolygonPatchType = new QName("http://www.opengis.net/gml/3.2", "PolygonPatchType");
    /** @generated */
    public static final QName PolygonType = new QName("http://www.opengis.net/gml/3.2", "PolygonType");
    /** @generated */
    public static final QName PrimeMeridianPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "PrimeMeridianPropertyType");
    /** @generated */
    public static final QName PrimeMeridianType = new QName("http://www.opengis.net/gml/3.2", "PrimeMeridianType");
    /** @generated */
    public static final QName PriorityLocationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "PriorityLocationPropertyType");
    /** @generated */
    public static final QName ProcedurePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ProcedurePropertyType");
    /** @generated */
    public static final QName ProjectedCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ProjectedCRSPropertyType");
    /** @generated */
    public static final QName ProjectedCRSType = new QName("http://www.opengis.net/gml/3.2", "ProjectedCRSType");
    /** @generated */
    public static final QName QNameList = new QName("http://www.opengis.net/gml/3.2", "QNameList");
    /** @generated */
    public static final QName QuantityExtentType = new QName("http://www.opengis.net/gml/3.2", "QuantityExtentType");
    /** @generated */
    public static final QName QuantityPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "QuantityPropertyType");
    /** @generated */
    public static final QName RangeSetType = new QName("http://www.opengis.net/gml/3.2", "RangeSetType");
    /** @generated */
    public static final QName RectangleType = new QName("http://www.opengis.net/gml/3.2", "RectangleType");
    /** @generated */
    public static final QName RectifiedGridType = new QName("http://www.opengis.net/gml/3.2", "RectifiedGridType");
    /** @generated */
    public static final QName ReferenceType = new QName("http://www.opengis.net/gml/3.2", "ReferenceType");
    /** @generated */
    public static final QName RelatedTimeType = new QName("http://www.opengis.net/gml/3.2", "RelatedTimeType");
    /** @generated */
    public static final QName ResultType = new QName("http://www.opengis.net/gml/3.2", "ResultType");
    /** @generated */
    public static final QName RingPropertyType = new QName("http://www.opengis.net/gml/3.2", "RingPropertyType");
    /** @generated */
    public static final QName RingType = new QName("http://www.opengis.net/gml/3.2", "RingType");
    /** @generated */
    public static final QName ScaleType = new QName("http://www.opengis.net/gml/3.2", "ScaleType");
    /** @generated */
    public static final QName SequenceRuleEnumeration =
            new QName("http://www.opengis.net/gml/3.2", "SequenceRuleEnumeration");
    /** @generated */
    public static final QName SequenceRuleType = new QName("http://www.opengis.net/gml/3.2", "SequenceRuleType");
    /** @generated */
    public static final QName ShellPropertyType = new QName("http://www.opengis.net/gml/3.2", "ShellPropertyType");
    /** @generated */
    public static final QName ShellType = new QName("http://www.opengis.net/gml/3.2", "ShellType");
    /** @generated */
    public static final QName SignType = new QName("http://www.opengis.net/gml/3.2", "SignType");
    /** @generated */
    public static final QName SingleCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "SingleCRSPropertyType");
    /** @generated */
    public static final QName SingleOperationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "SingleOperationPropertyType");
    /** @generated */
    public static final QName SolidArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "SolidArrayPropertyType");
    /** @generated */
    public static final QName SolidPropertyType = new QName("http://www.opengis.net/gml/3.2", "SolidPropertyType");
    /** @generated */
    public static final QName SolidType = new QName("http://www.opengis.net/gml/3.2", "SolidType");
    /** @generated */
    public static final QName SpeedType = new QName("http://www.opengis.net/gml/3.2", "SpeedType");
    /** @generated */
    public static final QName SphereType = new QName("http://www.opengis.net/gml/3.2", "SphereType");
    /** @generated */
    public static final QName SphericalCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "SphericalCSPropertyType");
    /** @generated */
    public static final QName SphericalCSType = new QName("http://www.opengis.net/gml/3.2", "SphericalCSType");
    /** @generated */
    public static final QName stringOrNilReason = new QName("http://www.opengis.net/gml/3.2", "stringOrNilReason");
    /** @generated */
    public static final QName StringOrRefType = new QName("http://www.opengis.net/gml/3.2", "StringOrRefType");
    /** @generated */
    public static final QName SuccessionType = new QName("http://www.opengis.net/gml/3.2", "SuccessionType");
    /** @generated */
    public static final QName SurfaceArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "SurfaceArrayPropertyType");
    /** @generated */
    public static final QName SurfaceInterpolationType =
            new QName("http://www.opengis.net/gml/3.2", "SurfaceInterpolationType");
    /** @generated */
    public static final QName SurfacePatchArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "SurfacePatchArrayPropertyType");
    /** @generated */
    public static final QName SurfacePropertyType = new QName("http://www.opengis.net/gml/3.2", "SurfacePropertyType");
    /** @generated */
    public static final QName SurfaceType = new QName("http://www.opengis.net/gml/3.2", "SurfaceType");
    /** @generated */
    public static final QName TargetPropertyType = new QName("http://www.opengis.net/gml/3.2", "TargetPropertyType");
    /** @generated */
    public static final QName TemporalCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TemporalCRSPropertyType");
    /** @generated */
    public static final QName TemporalCRSType = new QName("http://www.opengis.net/gml/3.2", "TemporalCRSType");
    /** @generated */
    public static final QName TemporalCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TemporalCSPropertyType");
    /** @generated */
    public static final QName TemporalCSType = new QName("http://www.opengis.net/gml/3.2", "TemporalCSType");
    /** @generated */
    public static final QName TemporalDatumBaseType =
            new QName("http://www.opengis.net/gml/3.2", "TemporalDatumBaseType");
    /** @generated */
    public static final QName TemporalDatumPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TemporalDatumPropertyType");
    /** @generated */
    public static final QName TemporalDatumType = new QName("http://www.opengis.net/gml/3.2", "TemporalDatumType");
    /** @generated */
    public static final QName TimeCalendarEraPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeCalendarEraPropertyType");
    /** @generated */
    public static final QName TimeCalendarEraType = new QName("http://www.opengis.net/gml/3.2", "TimeCalendarEraType");
    /** @generated */
    public static final QName TimeCalendarPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeCalendarPropertyType");
    /** @generated */
    public static final QName TimeCalendarType = new QName("http://www.opengis.net/gml/3.2", "TimeCalendarType");
    /** @generated */
    public static final QName TimeClockPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeClockPropertyType");
    /** @generated */
    public static final QName TimeClockType = new QName("http://www.opengis.net/gml/3.2", "TimeClockType");
    /** @generated */
    public static final QName TimeCoordinateSystemType =
            new QName("http://www.opengis.net/gml/3.2", "TimeCoordinateSystemType");
    /** @generated */
    public static final QName TimeCSPropertyType = new QName("http://www.opengis.net/gml/3.2", "TimeCSPropertyType");
    /** @generated */
    public static final QName TimeCSType = new QName("http://www.opengis.net/gml/3.2", "TimeCSType");
    /** @generated */
    public static final QName TimeEdgePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeEdgePropertyType");
    /** @generated */
    public static final QName TimeEdgeType = new QName("http://www.opengis.net/gml/3.2", "TimeEdgeType");
    /** @generated */
    public static final QName TimeIndeterminateValueType =
            new QName("http://www.opengis.net/gml/3.2", "TimeIndeterminateValueType");
    /** @generated */
    public static final QName TimeInstantPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeInstantPropertyType");
    /** @generated */
    public static final QName TimeInstantType = new QName("http://www.opengis.net/gml/3.2", "TimeInstantType");
    /** @generated */
    public static final QName TimeIntervalLengthType =
            new QName("http://www.opengis.net/gml/3.2", "TimeIntervalLengthType");
    /** @generated */
    public static final QName TimeNodePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeNodePropertyType");
    /** @generated */
    public static final QName TimeNodeType = new QName("http://www.opengis.net/gml/3.2", "TimeNodeType");
    /** @generated */
    public static final QName TimeOrdinalEraPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeOrdinalEraPropertyType");
    /** @generated */
    public static final QName TimeOrdinalEraType = new QName("http://www.opengis.net/gml/3.2", "TimeOrdinalEraType");
    /** @generated */
    public static final QName TimeOrdinalReferenceSystemType =
            new QName("http://www.opengis.net/gml/3.2", "TimeOrdinalReferenceSystemType");
    /** @generated */
    public static final QName TimePeriodPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimePeriodPropertyType");
    /** @generated */
    public static final QName TimePeriodType = new QName("http://www.opengis.net/gml/3.2", "TimePeriodType");
    /** @generated */
    public static final QName TimePositionType = new QName("http://www.opengis.net/gml/3.2", "TimePositionType");
    /** @generated */
    public static final QName TimePositionUnion = new QName("http://www.opengis.net/gml/3.2", "TimePositionUnion");
    /** @generated */
    public static final QName TimePrimitivePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimePrimitivePropertyType");
    /** @generated */
    public static final QName TimeReferenceSystemType =
            new QName("http://www.opengis.net/gml/3.2", "TimeReferenceSystemType");
    /** @generated */
    public static final QName TimeTopologyComplexPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeTopologyComplexPropertyType");
    /** @generated */
    public static final QName TimeTopologyComplexType =
            new QName("http://www.opengis.net/gml/3.2", "TimeTopologyComplexType");
    /** @generated */
    public static final QName TimeTopologyPrimitivePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TimeTopologyPrimitivePropertyType");
    /** @generated */
    public static final QName TimeType = new QName("http://www.opengis.net/gml/3.2", "TimeType");
    /** @generated */
    public static final QName TimeUnitType = new QName("http://www.opengis.net/gml/3.2", "TimeUnitType");
    /** @generated */
    public static final QName TinType = new QName("http://www.opengis.net/gml/3.2", "TinType");
    /** @generated */
    public static final QName TopoComplexPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TopoComplexPropertyType");
    /** @generated */
    public static final QName TopoComplexType = new QName("http://www.opengis.net/gml/3.2", "TopoComplexType");
    /** @generated */
    public static final QName TopoCurvePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TopoCurvePropertyType");
    /** @generated */
    public static final QName TopoCurveType = new QName("http://www.opengis.net/gml/3.2", "TopoCurveType");
    /** @generated */
    public static final QName TopoPointPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TopoPointPropertyType");
    /** @generated */
    public static final QName TopoPointType = new QName("http://www.opengis.net/gml/3.2", "TopoPointType");
    /** @generated */
    public static final QName TopoPrimitiveArrayAssociationType =
            new QName("http://www.opengis.net/gml/3.2", "TopoPrimitiveArrayAssociationType");
    /** @generated */
    public static final QName TopoPrimitiveMemberType =
            new QName("http://www.opengis.net/gml/3.2", "TopoPrimitiveMemberType");
    /** @generated */
    public static final QName TopoSolidPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TopoSolidPropertyType");
    /** @generated */
    public static final QName TopoSolidType = new QName("http://www.opengis.net/gml/3.2", "TopoSolidType");
    /** @generated */
    public static final QName TopoSurfacePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TopoSurfacePropertyType");
    /** @generated */
    public static final QName TopoSurfaceType = new QName("http://www.opengis.net/gml/3.2", "TopoSurfaceType");
    /** @generated */
    public static final QName TopoVolumePropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TopoVolumePropertyType");
    /** @generated */
    public static final QName TopoVolumeType = new QName("http://www.opengis.net/gml/3.2", "TopoVolumeType");
    /** @generated */
    public static final QName TransformationPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "TransformationPropertyType");
    /** @generated */
    public static final QName TransformationType = new QName("http://www.opengis.net/gml/3.2", "TransformationType");
    /** @generated */
    public static final QName TriangleType = new QName("http://www.opengis.net/gml/3.2", "TriangleType");
    /** @generated */
    public static final QName UnitDefinitionType = new QName("http://www.opengis.net/gml/3.2", "UnitDefinitionType");
    /** @generated */
    public static final QName UnitOfMeasureType = new QName("http://www.opengis.net/gml/3.2", "UnitOfMeasureType");
    /** @generated */
    public static final QName UomIdentifier = new QName("http://www.opengis.net/gml/3.2", "UomIdentifier");
    /** @generated */
    public static final QName UomSymbol = new QName("http://www.opengis.net/gml/3.2", "UomSymbol");
    /** @generated */
    public static final QName UomURI = new QName("http://www.opengis.net/gml/3.2", "UomURI");
    /** @generated */
    public static final QName UserDefinedCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "UserDefinedCSPropertyType");
    /** @generated */
    public static final QName UserDefinedCSType = new QName("http://www.opengis.net/gml/3.2", "UserDefinedCSType");
    /** @generated */
    public static final QName ValueArrayPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "ValueArrayPropertyType");
    /** @generated */
    public static final QName ValueArrayType = new QName("http://www.opengis.net/gml/3.2", "ValueArrayType");
    /** @generated */
    public static final QName ValuePropertyType = new QName("http://www.opengis.net/gml/3.2", "ValuePropertyType");
    /** @generated */
    public static final QName VectorType = new QName("http://www.opengis.net/gml/3.2", "VectorType");
    /** @generated */
    public static final QName VerticalCRSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "VerticalCRSPropertyType");
    /** @generated */
    public static final QName VerticalCRSType = new QName("http://www.opengis.net/gml/3.2", "VerticalCRSType");
    /** @generated */
    public static final QName VerticalCSPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "VerticalCSPropertyType");
    /** @generated */
    public static final QName VerticalCSType = new QName("http://www.opengis.net/gml/3.2", "VerticalCSType");
    /** @generated */
    public static final QName VerticalDatumPropertyType =
            new QName("http://www.opengis.net/gml/3.2", "VerticalDatumPropertyType");
    /** @generated */
    public static final QName VerticalDatumType = new QName("http://www.opengis.net/gml/3.2", "VerticalDatumType");
    /** @generated */
    public static final QName VolumeType = new QName("http://www.opengis.net/gml/3.2", "VolumeType");
    /** @generated */
    public static final QName _Boolean = new QName("http://www.opengis.net/gml/3.2", "_Boolean");
    /** @generated */
    public static final QName _Category = new QName("http://www.opengis.net/gml/3.2", "_Category");
    /** @generated */
    public static final QName _coordinateOperationAccuracy =
            new QName("http://www.opengis.net/gml/3.2", "_coordinateOperationAccuracy");
    /** @generated */
    public static final QName _Count = new QName("http://www.opengis.net/gml/3.2", "_Count");
    /** @generated */
    public static final QName _domainOfValidity = new QName("http://www.opengis.net/gml/3.2", "_domainOfValidity");
    /** @generated */
    public static final QName _formulaCitation = new QName("http://www.opengis.net/gml/3.2", "_formulaCitation");
    /** @generated */
    public static final QName _Quantity = new QName("http://www.opengis.net/gml/3.2", "_Quantity");
    /** @generated */
    public static final QName _secondDefiningParameter =
            new QName("http://www.opengis.net/gml/3.2", "_secondDefiningParameter");
    /** @generated */
    public static final QName _SecondDefiningParameter =
            new QName("http://www.opengis.net/gml/3.2", "_SecondDefiningParameter");
    /** @generated */
    public static final QName AbstractGriddedSurfaceType_rows =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGriddedSurfaceType_rows");
    /** @generated */
    public static final QName ClothoidType_refLocation =
            new QName("http://www.opengis.net/gml/3.2", "ClothoidType_refLocation");
    /** @generated */
    public static final QName TinType_controlPoint =
            new QName("http://www.opengis.net/gml/3.2", "TinType_controlPoint");

    /* Elements */
    /** @generated */
    public static final QName abstractAssociationRole =
            new QName("http://www.opengis.net/gml/3.2", "abstractAssociationRole");
    /** @generated */
    public static final QName AbstractContinuousCoverage =
            new QName("http://www.opengis.net/gml/3.2", "AbstractContinuousCoverage");
    /** @generated */
    public static final QName AbstractCoordinateOperation =
            new QName("http://www.opengis.net/gml/3.2", "AbstractCoordinateOperation");
    /** @generated */
    public static final QName AbstractCoordinateSystem =
            new QName("http://www.opengis.net/gml/3.2", "AbstractCoordinateSystem");
    /** @generated */
    public static final QName AbstractCoverage = new QName("http://www.opengis.net/gml/3.2", "AbstractCoverage");
    /** @generated */
    public static final QName AbstractCRS = new QName("http://www.opengis.net/gml/3.2", "AbstractCRS");
    /** @generated */
    public static final QName AbstractCurve = new QName("http://www.opengis.net/gml/3.2", "AbstractCurve");
    /** @generated */
    public static final QName AbstractCurveSegment =
            new QName("http://www.opengis.net/gml/3.2", "AbstractCurveSegment");
    /** @generated */
    public static final QName AbstractDatum = new QName("http://www.opengis.net/gml/3.2", "AbstractDatum");
    /** @generated */
    public static final QName AbstractDiscreteCoverage =
            new QName("http://www.opengis.net/gml/3.2", "AbstractDiscreteCoverage");
    /** @generated */
    public static final QName AbstractFeature = new QName("http://www.opengis.net/gml/3.2", "AbstractFeature");
    /** @generated */
    public static final QName AbstractFeatureCollection =
            new QName("http://www.opengis.net/gml/3.2", "AbstractFeatureCollection");
    /** @generated */
    public static final QName AbstractGeneralConversion =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralConversion");
    /** @generated */
    public static final QName AbstractGeneralDerivedCRS =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralDerivedCRS");
    /** @generated */
    public static final QName AbstractGeneralOperationParameter =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralOperationParameter");
    /** @generated */
    public static final QName abstractGeneralOperationParameterRef =
            new QName("http://www.opengis.net/gml/3.2", "abstractGeneralOperationParameterRef");
    /** @generated */
    public static final QName AbstractGeneralParameterValue =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralParameterValue");
    /** @generated */
    public static final QName AbstractGeneralTransformation =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeneralTransformation");
    /** @generated */
    public static final QName AbstractGeometricAggregate =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeometricAggregate");
    /** @generated */
    public static final QName AbstractGeometricPrimitive =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGeometricPrimitive");
    /** @generated */
    public static final QName AbstractGeometry = new QName("http://www.opengis.net/gml/3.2", "AbstractGeometry");
    /** @generated */
    public static final QName AbstractGML = new QName("http://www.opengis.net/gml/3.2", "AbstractGML");
    /** @generated */
    public static final QName AbstractGriddedSurface =
            new QName("http://www.opengis.net/gml/3.2", "AbstractGriddedSurface");
    /** @generated */
    public static final QName AbstractImplicitGeometry =
            new QName("http://www.opengis.net/gml/3.2", "AbstractImplicitGeometry");
    /** @generated */
    public static final QName abstractInlineProperty =
            new QName("http://www.opengis.net/gml/3.2", "abstractInlineProperty");
    /** @generated */
    public static final QName AbstractMetaData = new QName("http://www.opengis.net/gml/3.2", "AbstractMetaData");
    /** @generated */
    public static final QName AbstractObject = new QName("http://www.opengis.net/gml/3.2", "AbstractObject");
    /** @generated */
    public static final QName AbstractOperation = new QName("http://www.opengis.net/gml/3.2", "AbstractOperation");
    /** @generated */
    public static final QName AbstractParametricCurveSurface =
            new QName("http://www.opengis.net/gml/3.2", "AbstractParametricCurveSurface");
    /** @generated */
    public static final QName abstractReference = new QName("http://www.opengis.net/gml/3.2", "abstractReference");
    /** @generated */
    public static final QName AbstractRing = new QName("http://www.opengis.net/gml/3.2", "AbstractRing");
    /** @generated */
    public static final QName AbstractScalarValue = new QName("http://www.opengis.net/gml/3.2", "AbstractScalarValue");
    /** @generated */
    public static final QName AbstractScalarValueList =
            new QName("http://www.opengis.net/gml/3.2", "AbstractScalarValueList");
    /** @generated */
    public static final QName AbstractSingleCRS = new QName("http://www.opengis.net/gml/3.2", "AbstractSingleCRS");
    /** @generated */
    public static final QName AbstractSingleOperation =
            new QName("http://www.opengis.net/gml/3.2", "AbstractSingleOperation");
    /** @generated */
    public static final QName AbstractSolid = new QName("http://www.opengis.net/gml/3.2", "AbstractSolid");
    /** @generated */
    public static final QName abstractStrictAssociationRole =
            new QName("http://www.opengis.net/gml/3.2", "abstractStrictAssociationRole");
    /** @generated */
    public static final QName AbstractSurface = new QName("http://www.opengis.net/gml/3.2", "AbstractSurface");
    /** @generated */
    public static final QName AbstractSurfacePatch =
            new QName("http://www.opengis.net/gml/3.2", "AbstractSurfacePatch");
    /** @generated */
    public static final QName AbstractTimeComplex = new QName("http://www.opengis.net/gml/3.2", "AbstractTimeComplex");
    /** @generated */
    public static final QName AbstractTimeGeometricPrimitive =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimeGeometricPrimitive");
    /** @generated */
    public static final QName AbstractTimeObject = new QName("http://www.opengis.net/gml/3.2", "AbstractTimeObject");
    /** @generated */
    public static final QName AbstractTimePrimitive =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimePrimitive");
    /** @generated */
    public static final QName AbstractTimeSlice = new QName("http://www.opengis.net/gml/3.2", "AbstractTimeSlice");
    /** @generated */
    public static final QName AbstractTimeTopologyPrimitive =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTimeTopologyPrimitive");
    /** @generated */
    public static final QName AbstractTopology = new QName("http://www.opengis.net/gml/3.2", "AbstractTopology");
    /** @generated */
    public static final QName AbstractTopoPrimitive =
            new QName("http://www.opengis.net/gml/3.2", "AbstractTopoPrimitive");
    /** @generated */
    public static final QName AbstractValue = new QName("http://www.opengis.net/gml/3.2", "AbstractValue");
    /** @generated */
    public static final QName affineCS = new QName("http://www.opengis.net/gml/3.2", "affineCS");
    /** @generated */
    public static final QName AffineCS = new QName("http://www.opengis.net/gml/3.2", "AffineCS");
    /** @generated */
    public static final QName AffinePlacement = new QName("http://www.opengis.net/gml/3.2", "AffinePlacement");
    /** @generated */
    public static final QName anchorDefinition = new QName("http://www.opengis.net/gml/3.2", "anchorDefinition");
    /** @generated */
    public static final QName anchorPoint = new QName("http://www.opengis.net/gml/3.2", "anchorPoint");
    /** @generated */
    public static final QName angle = new QName("http://www.opengis.net/gml/3.2", "angle");
    /** @generated */
    public static final QName Arc = new QName("http://www.opengis.net/gml/3.2", "Arc");
    /** @generated */
    public static final QName ArcByBulge = new QName("http://www.opengis.net/gml/3.2", "ArcByBulge");
    /** @generated */
    public static final QName ArcByCenterPoint = new QName("http://www.opengis.net/gml/3.2", "ArcByCenterPoint");
    /** @generated */
    public static final QName ArcString = new QName("http://www.opengis.net/gml/3.2", "ArcString");
    /** @generated */
    public static final QName ArcStringByBulge = new QName("http://www.opengis.net/gml/3.2", "ArcStringByBulge");
    /** @generated */
    public static final QName Array = new QName("http://www.opengis.net/gml/3.2", "Array");
    /** @generated */
    public static final QName associationName = new QName("http://www.opengis.net/gml/3.2", "associationName");
    /** @generated */
    public static final QName axis = new QName("http://www.opengis.net/gml/3.2", "axis");
    /** @generated */
    public static final QName axisAbbrev = new QName("http://www.opengis.net/gml/3.2", "axisAbbrev");
    /** @generated */
    public static final QName axisDirection = new QName("http://www.opengis.net/gml/3.2", "axisDirection");
    /** @generated */
    public static final QName Bag = new QName("http://www.opengis.net/gml/3.2", "Bag");
    /** @generated */
    public static final QName baseCRS = new QName("http://www.opengis.net/gml/3.2", "baseCRS");
    /** @generated */
    public static final QName baseCurve = new QName("http://www.opengis.net/gml/3.2", "baseCurve");
    /** @generated */
    public static final QName baseGeodeticCRS = new QName("http://www.opengis.net/gml/3.2", "baseGeodeticCRS");
    /** @generated */
    public static final QName baseGeographicCRS = new QName("http://www.opengis.net/gml/3.2", "baseGeographicCRS");
    /** @generated */
    public static final QName baseSurface = new QName("http://www.opengis.net/gml/3.2", "baseSurface");
    /** @generated */
    public static final QName BaseUnit = new QName("http://www.opengis.net/gml/3.2", "BaseUnit");
    /** @generated */
    public static final QName Bezier = new QName("http://www.opengis.net/gml/3.2", "Bezier");
    /** @generated */
    public static final QName Boolean = new QName("http://www.opengis.net/gml/3.2", "Boolean");
    /** @generated */
    public static final QName BooleanList = new QName("http://www.opengis.net/gml/3.2", "BooleanList");
    /** @generated */
    public static final QName booleanValue = new QName("http://www.opengis.net/gml/3.2", "booleanValue");
    /** @generated */
    public static final QName boundedBy = new QName("http://www.opengis.net/gml/3.2", "boundedBy");
    /** @generated */
    public static final QName BSpline = new QName("http://www.opengis.net/gml/3.2", "BSpline");
    /** @generated */
    public static final QName cartesianCS = new QName("http://www.opengis.net/gml/3.2", "cartesianCS");
    /** @generated */
    public static final QName CartesianCS = new QName("http://www.opengis.net/gml/3.2", "CartesianCS");
    /** @generated */
    public static final QName cartesianCSRef = new QName("http://www.opengis.net/gml/3.2", "cartesianCSRef");
    /** @generated */
    public static final QName catalogSymbol = new QName("http://www.opengis.net/gml/3.2", "catalogSymbol");
    /** @generated */
    public static final QName Category = new QName("http://www.opengis.net/gml/3.2", "Category");
    /** @generated */
    public static final QName CategoryExtent = new QName("http://www.opengis.net/gml/3.2", "CategoryExtent");
    /** @generated */
    public static final QName CategoryList = new QName("http://www.opengis.net/gml/3.2", "CategoryList");
    /** @generated */
    public static final QName centerLineOf = new QName("http://www.opengis.net/gml/3.2", "centerLineOf");
    /** @generated */
    public static final QName centerOf = new QName("http://www.opengis.net/gml/3.2", "centerOf");
    /** @generated */
    public static final QName Circle = new QName("http://www.opengis.net/gml/3.2", "Circle");
    /** @generated */
    public static final QName CircleByCenterPoint = new QName("http://www.opengis.net/gml/3.2", "CircleByCenterPoint");
    /** @generated */
    public static final QName Clothoid = new QName("http://www.opengis.net/gml/3.2", "Clothoid");
    /** @generated */
    public static final QName componentReferenceSystem =
            new QName("http://www.opengis.net/gml/3.2", "componentReferenceSystem");
    /** @generated */
    public static final QName CompositeCurve = new QName("http://www.opengis.net/gml/3.2", "CompositeCurve");
    /** @generated */
    public static final QName CompositeSolid = new QName("http://www.opengis.net/gml/3.2", "CompositeSolid");
    /** @generated */
    public static final QName CompositeSurface = new QName("http://www.opengis.net/gml/3.2", "CompositeSurface");
    /** @generated */
    public static final QName CompositeValue = new QName("http://www.opengis.net/gml/3.2", "CompositeValue");
    /** @generated */
    public static final QName CompoundCRS = new QName("http://www.opengis.net/gml/3.2", "CompoundCRS");
    /** @generated */
    public static final QName compoundCRSRef = new QName("http://www.opengis.net/gml/3.2", "compoundCRSRef");
    /** @generated */
    public static final QName ConcatenatedOperation =
            new QName("http://www.opengis.net/gml/3.2", "ConcatenatedOperation");
    /** @generated */
    public static final QName concatenatedOperationRef =
            new QName("http://www.opengis.net/gml/3.2", "concatenatedOperationRef");
    /** @generated */
    public static final QName Cone = new QName("http://www.opengis.net/gml/3.2", "Cone");
    /** @generated */
    public static final QName ConventionalUnit = new QName("http://www.opengis.net/gml/3.2", "ConventionalUnit");
    /** @generated */
    public static final QName conversion = new QName("http://www.opengis.net/gml/3.2", "conversion");
    /** @generated */
    public static final QName Conversion = new QName("http://www.opengis.net/gml/3.2", "Conversion");
    /** @generated */
    public static final QName conversionRef = new QName("http://www.opengis.net/gml/3.2", "conversionRef");
    /** @generated */
    public static final QName conversionToPreferredUnit =
            new QName("http://www.opengis.net/gml/3.2", "conversionToPreferredUnit");
    /** @generated */
    public static final QName coordinateOperationAccuracy =
            new QName("http://www.opengis.net/gml/3.2", "coordinateOperationAccuracy");
    /** @generated */
    public static final QName coordinateOperationRef =
            new QName("http://www.opengis.net/gml/3.2", "coordinateOperationRef");
    /** @generated */
    public static final QName coordinates = new QName("http://www.opengis.net/gml/3.2", "coordinates");
    /** @generated */
    public static final QName coordinateSystem = new QName("http://www.opengis.net/gml/3.2", "coordinateSystem");
    /** @generated */
    public static final QName CoordinateSystemAxis =
            new QName("http://www.opengis.net/gml/3.2", "CoordinateSystemAxis");
    /** @generated */
    public static final QName coordinateSystemAxisRef =
            new QName("http://www.opengis.net/gml/3.2", "coordinateSystemAxisRef");
    /** @generated */
    public static final QName coordinateSystemRef = new QName("http://www.opengis.net/gml/3.2", "coordinateSystemRef");
    /** @generated */
    public static final QName coordOperation = new QName("http://www.opengis.net/gml/3.2", "coordOperation");
    /** @generated */
    public static final QName Count = new QName("http://www.opengis.net/gml/3.2", "Count");
    /** @generated */
    public static final QName CountExtent = new QName("http://www.opengis.net/gml/3.2", "CountExtent");
    /** @generated */
    public static final QName CountList = new QName("http://www.opengis.net/gml/3.2", "CountList");
    /** @generated */
    public static final QName coverageFunction = new QName("http://www.opengis.net/gml/3.2", "coverageFunction");
    /** @generated */
    public static final QName CoverageMappingRule = new QName("http://www.opengis.net/gml/3.2", "CoverageMappingRule");
    /** @generated */
    public static final QName crsRef = new QName("http://www.opengis.net/gml/3.2", "crsRef");
    /** @generated */
    public static final QName CubicSpline = new QName("http://www.opengis.net/gml/3.2", "CubicSpline");
    /** @generated */
    public static final QName Curve = new QName("http://www.opengis.net/gml/3.2", "Curve");
    /** @generated */
    public static final QName curveArrayProperty = new QName("http://www.opengis.net/gml/3.2", "curveArrayProperty");
    /** @generated */
    public static final QName curveMember = new QName("http://www.opengis.net/gml/3.2", "curveMember");
    /** @generated */
    public static final QName curveMembers = new QName("http://www.opengis.net/gml/3.2", "curveMembers");
    /** @generated */
    public static final QName curveProperty = new QName("http://www.opengis.net/gml/3.2", "curveProperty");
    /** @generated */
    public static final QName Cylinder = new QName("http://www.opengis.net/gml/3.2", "Cylinder");
    /** @generated */
    public static final QName cylindricalCS = new QName("http://www.opengis.net/gml/3.2", "cylindricalCS");
    /** @generated */
    public static final QName CylindricalCS = new QName("http://www.opengis.net/gml/3.2", "CylindricalCS");
    /** @generated */
    public static final QName cylindricalCSRef = new QName("http://www.opengis.net/gml/3.2", "cylindricalCSRef");
    /** @generated */
    public static final QName DataBlock = new QName("http://www.opengis.net/gml/3.2", "DataBlock");
    /** @generated */
    public static final QName dataSource = new QName("http://www.opengis.net/gml/3.2", "dataSource");
    /** @generated */
    public static final QName dataSourceReference = new QName("http://www.opengis.net/gml/3.2", "dataSourceReference");
    /** @generated */
    public static final QName datumRef = new QName("http://www.opengis.net/gml/3.2", "datumRef");
    /** @generated */
    public static final QName decimalMinutes = new QName("http://www.opengis.net/gml/3.2", "decimalMinutes");
    /** @generated */
    public static final QName defaultCodeSpace = new QName("http://www.opengis.net/gml/3.2", "defaultCodeSpace");
    /** @generated */
    public static final QName definedByConversion = new QName("http://www.opengis.net/gml/3.2", "definedByConversion");
    /** @generated */
    public static final QName Definition = new QName("http://www.opengis.net/gml/3.2", "Definition");
    /** @generated */
    public static final QName DefinitionCollection =
            new QName("http://www.opengis.net/gml/3.2", "DefinitionCollection");
    /** @generated */
    public static final QName definitionMember = new QName("http://www.opengis.net/gml/3.2", "definitionMember");
    /** @generated */
    public static final QName DefinitionProxy = new QName("http://www.opengis.net/gml/3.2", "DefinitionProxy");
    /** @generated */
    public static final QName definitionRef = new QName("http://www.opengis.net/gml/3.2", "definitionRef");
    /** @generated */
    public static final QName degrees = new QName("http://www.opengis.net/gml/3.2", "degrees");
    /** @generated */
    public static final QName derivationUnitTerm = new QName("http://www.opengis.net/gml/3.2", "derivationUnitTerm");
    /** @generated */
    public static final QName DerivedCRS = new QName("http://www.opengis.net/gml/3.2", "DerivedCRS");
    /** @generated */
    public static final QName derivedCRSRef = new QName("http://www.opengis.net/gml/3.2", "derivedCRSRef");
    /** @generated */
    public static final QName derivedCRSType = new QName("http://www.opengis.net/gml/3.2", "derivedCRSType");
    /** @generated */
    public static final QName DerivedUnit = new QName("http://www.opengis.net/gml/3.2", "DerivedUnit");
    /** @generated */
    public static final QName description = new QName("http://www.opengis.net/gml/3.2", "description");
    /** @generated */
    public static final QName descriptionReference =
            new QName("http://www.opengis.net/gml/3.2", "descriptionReference");
    /** @generated */
    public static final QName Dictionary = new QName("http://www.opengis.net/gml/3.2", "Dictionary");
    /** @generated */
    public static final QName dictionaryEntry = new QName("http://www.opengis.net/gml/3.2", "dictionaryEntry");
    /** @generated */
    public static final QName directedEdge = new QName("http://www.opengis.net/gml/3.2", "directedEdge");
    /** @generated */
    public static final QName directedFace = new QName("http://www.opengis.net/gml/3.2", "directedFace");
    /** @generated */
    public static final QName directedNode = new QName("http://www.opengis.net/gml/3.2", "directedNode");
    /** @generated */
    public static final QName DirectedObservation = new QName("http://www.opengis.net/gml/3.2", "DirectedObservation");
    /** @generated */
    public static final QName DirectedObservationAtDistance =
            new QName("http://www.opengis.net/gml/3.2", "DirectedObservationAtDistance");
    /** @generated */
    public static final QName directedTopoSolid = new QName("http://www.opengis.net/gml/3.2", "directedTopoSolid");
    /** @generated */
    public static final QName direction = new QName("http://www.opengis.net/gml/3.2", "direction");
    /** @generated */
    public static final QName dmsAngle = new QName("http://www.opengis.net/gml/3.2", "dmsAngle");
    /** @generated */
    public static final QName dmsAngleValue = new QName("http://www.opengis.net/gml/3.2", "dmsAngleValue");
    /** @generated */
    public static final QName domainOfValidity = new QName("http://www.opengis.net/gml/3.2", "domainOfValidity");
    /** @generated */
    public static final QName domainSet = new QName("http://www.opengis.net/gml/3.2", "domainSet");
    /** @generated */
    public static final QName doubleOrNilReasonTupleList =
            new QName("http://www.opengis.net/gml/3.2", "doubleOrNilReasonTupleList");
    /** @generated */
    public static final QName duration = new QName("http://www.opengis.net/gml/3.2", "duration");
    /** @generated */
    public static final QName DynamicFeature = new QName("http://www.opengis.net/gml/3.2", "DynamicFeature");
    /** @generated */
    public static final QName DynamicFeatureCollection =
            new QName("http://www.opengis.net/gml/3.2", "DynamicFeatureCollection");
    /** @generated */
    public static final QName dynamicMembers = new QName("http://www.opengis.net/gml/3.2", "dynamicMembers");
    /** @generated */
    public static final QName Edge = new QName("http://www.opengis.net/gml/3.2", "Edge");
    /** @generated */
    public static final QName edgeOf = new QName("http://www.opengis.net/gml/3.2", "edgeOf");
    /** @generated */
    public static final QName ellipsoid = new QName("http://www.opengis.net/gml/3.2", "ellipsoid");
    /** @generated */
    public static final QName Ellipsoid = new QName("http://www.opengis.net/gml/3.2", "Ellipsoid");
    /** @generated */
    public static final QName ellipsoidalCS = new QName("http://www.opengis.net/gml/3.2", "ellipsoidalCS");
    /** @generated */
    public static final QName EllipsoidalCS = new QName("http://www.opengis.net/gml/3.2", "EllipsoidalCS");
    /** @generated */
    public static final QName ellipsoidalCSRef = new QName("http://www.opengis.net/gml/3.2", "ellipsoidalCSRef");
    /** @generated */
    public static final QName ellipsoidRef = new QName("http://www.opengis.net/gml/3.2", "ellipsoidRef");
    /** @generated */
    public static final QName EngineeringCRS = new QName("http://www.opengis.net/gml/3.2", "EngineeringCRS");
    /** @generated */
    public static final QName engineeringCRSRef = new QName("http://www.opengis.net/gml/3.2", "engineeringCRSRef");
    /** @generated */
    public static final QName engineeringDatum = new QName("http://www.opengis.net/gml/3.2", "engineeringDatum");
    /** @generated */
    public static final QName EngineeringDatum = new QName("http://www.opengis.net/gml/3.2", "EngineeringDatum");
    /** @generated */
    public static final QName engineeringDatumRef = new QName("http://www.opengis.net/gml/3.2", "engineeringDatumRef");
    /** @generated */
    public static final QName Envelope = new QName("http://www.opengis.net/gml/3.2", "Envelope");
    /** @generated */
    public static final QName EnvelopeWithTimePeriod =
            new QName("http://www.opengis.net/gml/3.2", "EnvelopeWithTimePeriod");
    /** @generated */
    public static final QName extentOf = new QName("http://www.opengis.net/gml/3.2", "extentOf");
    /** @generated */
    public static final QName exterior = new QName("http://www.opengis.net/gml/3.2", "exterior");
    /** @generated */
    public static final QName Face = new QName("http://www.opengis.net/gml/3.2", "Face");
    /** @generated */
    public static final QName FeatureCollection = new QName("http://www.opengis.net/gml/3.2", "FeatureCollection");
    /** @generated */
    public static final QName featureMember = new QName("http://www.opengis.net/gml/3.2", "featureMember");
    /** @generated */
    public static final QName featureMembers = new QName("http://www.opengis.net/gml/3.2", "featureMembers");
    /** @generated */
    public static final QName featureProperty = new QName("http://www.opengis.net/gml/3.2", "featureProperty");
    /** @generated */
    public static final QName File = new QName("http://www.opengis.net/gml/3.2", "File");
    /** @generated */
    public static final QName formula = new QName("http://www.opengis.net/gml/3.2", "formula");
    /** @generated */
    public static final QName formulaCitation = new QName("http://www.opengis.net/gml/3.2", "formulaCitation");
    /** @generated */
    public static final QName generalConversionRef =
            new QName("http://www.opengis.net/gml/3.2", "generalConversionRef");
    /** @generated */
    public static final QName generalOperationParameter =
            new QName("http://www.opengis.net/gml/3.2", "generalOperationParameter");
    /** @generated */
    public static final QName generalTransformationRef =
            new QName("http://www.opengis.net/gml/3.2", "generalTransformationRef");
    /** @generated */
    public static final QName GenericMetaData = new QName("http://www.opengis.net/gml/3.2", "GenericMetaData");
    /** @generated */
    public static final QName GeocentricCRS = new QName("http://www.opengis.net/gml/3.2", "GeocentricCRS");
    /** @generated */
    public static final QName geocentricCRSRef = new QName("http://www.opengis.net/gml/3.2", "geocentricCRSRef");
    /** @generated */
    public static final QName Geodesic = new QName("http://www.opengis.net/gml/3.2", "Geodesic");
    /** @generated */
    public static final QName GeodesicString = new QName("http://www.opengis.net/gml/3.2", "GeodesicString");
    /** @generated */
    public static final QName GeodeticCRS = new QName("http://www.opengis.net/gml/3.2", "GeodeticCRS");
    /** @generated */
    public static final QName geodeticDatum = new QName("http://www.opengis.net/gml/3.2", "geodeticDatum");
    /** @generated */
    public static final QName GeodeticDatum = new QName("http://www.opengis.net/gml/3.2", "GeodeticDatum");
    /** @generated */
    public static final QName geodeticDatumRef = new QName("http://www.opengis.net/gml/3.2", "geodeticDatumRef");
    /** @generated */
    public static final QName GeographicCRS = new QName("http://www.opengis.net/gml/3.2", "GeographicCRS");
    /** @generated */
    public static final QName geographicCRSRef = new QName("http://www.opengis.net/gml/3.2", "geographicCRSRef");
    /** @generated */
    public static final QName GeometricComplex = new QName("http://www.opengis.net/gml/3.2", "GeometricComplex");
    /** @generated */
    public static final QName geometryMember = new QName("http://www.opengis.net/gml/3.2", "geometryMember");
    /** @generated */
    public static final QName geometryMembers = new QName("http://www.opengis.net/gml/3.2", "geometryMembers");
    /** @generated */
    public static final QName gmlProfileSchema = new QName("http://www.opengis.net/gml/3.2", "gmlProfileSchema");
    /** @generated */
    public static final QName greenwichLongitude = new QName("http://www.opengis.net/gml/3.2", "greenwichLongitude");
    /** @generated */
    public static final QName Grid = new QName("http://www.opengis.net/gml/3.2", "Grid");
    /** @generated */
    public static final QName GridCoverage = new QName("http://www.opengis.net/gml/3.2", "GridCoverage");
    /** @generated */
    public static final QName gridDomain = new QName("http://www.opengis.net/gml/3.2", "gridDomain");
    /** @generated */
    public static final QName GridFunction = new QName("http://www.opengis.net/gml/3.2", "GridFunction");
    /** @generated */
    public static final QName group = new QName("http://www.opengis.net/gml/3.2", "group");
    /** @generated */
    public static final QName history = new QName("http://www.opengis.net/gml/3.2", "history");
    /** @generated */
    public static final QName identifier = new QName("http://www.opengis.net/gml/3.2", "identifier");
    /** @generated */
    public static final QName ImageCRS = new QName("http://www.opengis.net/gml/3.2", "ImageCRS");
    /** @generated */
    public static final QName imageCRSRef = new QName("http://www.opengis.net/gml/3.2", "imageCRSRef");
    /** @generated */
    public static final QName imageDatum = new QName("http://www.opengis.net/gml/3.2", "imageDatum");
    /** @generated */
    public static final QName ImageDatum = new QName("http://www.opengis.net/gml/3.2", "ImageDatum");
    /** @generated */
    public static final QName imageDatumRef = new QName("http://www.opengis.net/gml/3.2", "imageDatumRef");
    /** @generated */
    public static final QName includesParameter = new QName("http://www.opengis.net/gml/3.2", "includesParameter");
    /** @generated */
    public static final QName includesSingleCRS = new QName("http://www.opengis.net/gml/3.2", "includesSingleCRS");
    /** @generated */
    public static final QName includesValue = new QName("http://www.opengis.net/gml/3.2", "includesValue");
    /** @generated */
    public static final QName indirectEntry = new QName("http://www.opengis.net/gml/3.2", "indirectEntry");
    /** @generated */
    public static final QName integerValue = new QName("http://www.opengis.net/gml/3.2", "integerValue");
    /** @generated */
    public static final QName integerValueList = new QName("http://www.opengis.net/gml/3.2", "integerValueList");
    /** @generated */
    public static final QName interior = new QName("http://www.opengis.net/gml/3.2", "interior");
    /** @generated */
    public static final QName linearCS = new QName("http://www.opengis.net/gml/3.2", "linearCS");
    /** @generated */
    public static final QName LinearCS = new QName("http://www.opengis.net/gml/3.2", "LinearCS");
    /** @generated */
    public static final QName linearCSRef = new QName("http://www.opengis.net/gml/3.2", "linearCSRef");
    /** @generated */
    public static final QName LinearRing = new QName("http://www.opengis.net/gml/3.2", "LinearRing");
    /** @generated */
    public static final QName LineString = new QName("http://www.opengis.net/gml/3.2", "LineString");
    /** @generated */
    public static final QName LineStringSegment = new QName("http://www.opengis.net/gml/3.2", "LineStringSegment");
    /** @generated */
    public static final QName location = new QName("http://www.opengis.net/gml/3.2", "location");
    /** @generated */
    public static final QName LocationKeyWord = new QName("http://www.opengis.net/gml/3.2", "LocationKeyWord");
    /** @generated */
    public static final QName locationName = new QName("http://www.opengis.net/gml/3.2", "locationName");
    /** @generated */
    public static final QName locationReference = new QName("http://www.opengis.net/gml/3.2", "locationReference");
    /** @generated */
    public static final QName LocationString = new QName("http://www.opengis.net/gml/3.2", "LocationString");
    /** @generated */
    public static final QName MappingRule = new QName("http://www.opengis.net/gml/3.2", "MappingRule");
    /** @generated */
    public static final QName maximalComplex = new QName("http://www.opengis.net/gml/3.2", "maximalComplex");
    /** @generated */
    public static final QName maximumOccurs = new QName("http://www.opengis.net/gml/3.2", "maximumOccurs");
    /** @generated */
    public static final QName maximumValue = new QName("http://www.opengis.net/gml/3.2", "maximumValue");
    /** @generated */
    public static final QName measure = new QName("http://www.opengis.net/gml/3.2", "measure");
    /** @generated */
    public static final QName member = new QName("http://www.opengis.net/gml/3.2", "member");
    /** @generated */
    public static final QName members = new QName("http://www.opengis.net/gml/3.2", "members");
    /** @generated */
    public static final QName metaDataProperty = new QName("http://www.opengis.net/gml/3.2", "metaDataProperty");
    /** @generated */
    public static final QName method = new QName("http://www.opengis.net/gml/3.2", "method");
    /** @generated */
    public static final QName methodFormula = new QName("http://www.opengis.net/gml/3.2", "methodFormula");
    /** @generated */
    public static final QName minimumOccurs = new QName("http://www.opengis.net/gml/3.2", "minimumOccurs");
    /** @generated */
    public static final QName minimumValue = new QName("http://www.opengis.net/gml/3.2", "minimumValue");
    /** @generated */
    public static final QName minutes = new QName("http://www.opengis.net/gml/3.2", "minutes");
    /** @generated */
    public static final QName modifiedCoordinate = new QName("http://www.opengis.net/gml/3.2", "modifiedCoordinate");
    /** @generated */
    public static final QName MovingObjectStatus = new QName("http://www.opengis.net/gml/3.2", "MovingObjectStatus");
    /** @generated */
    public static final QName multiCenterLineOf = new QName("http://www.opengis.net/gml/3.2", "multiCenterLineOf");
    /** @generated */
    public static final QName multiCenterOf = new QName("http://www.opengis.net/gml/3.2", "multiCenterOf");
    /** @generated */
    public static final QName multiCoverage = new QName("http://www.opengis.net/gml/3.2", "multiCoverage");
    /** @generated */
    public static final QName MultiCurve = new QName("http://www.opengis.net/gml/3.2", "MultiCurve");
    /** @generated */
    public static final QName MultiCurveCoverage = new QName("http://www.opengis.net/gml/3.2", "MultiCurveCoverage");
    /** @generated */
    public static final QName multiCurveDomain = new QName("http://www.opengis.net/gml/3.2", "multiCurveDomain");
    /** @generated */
    public static final QName multiCurveProperty = new QName("http://www.opengis.net/gml/3.2", "multiCurveProperty");
    /** @generated */
    public static final QName multiEdgeOf = new QName("http://www.opengis.net/gml/3.2", "multiEdgeOf");
    /** @generated */
    public static final QName multiExtentOf = new QName("http://www.opengis.net/gml/3.2", "multiExtentOf");
    /** @generated */
    public static final QName MultiGeometry = new QName("http://www.opengis.net/gml/3.2", "MultiGeometry");
    /** @generated */
    public static final QName multiGeometryProperty =
            new QName("http://www.opengis.net/gml/3.2", "multiGeometryProperty");
    /** @generated */
    public static final QName multiLocation = new QName("http://www.opengis.net/gml/3.2", "multiLocation");
    /** @generated */
    public static final QName MultiPoint = new QName("http://www.opengis.net/gml/3.2", "MultiPoint");
    /** @generated */
    public static final QName MultiPointCoverage = new QName("http://www.opengis.net/gml/3.2", "MultiPointCoverage");
    /** @generated */
    public static final QName multiPointDomain = new QName("http://www.opengis.net/gml/3.2", "multiPointDomain");
    /** @generated */
    public static final QName multiPointProperty = new QName("http://www.opengis.net/gml/3.2", "multiPointProperty");
    /** @generated */
    public static final QName multiPosition = new QName("http://www.opengis.net/gml/3.2", "multiPosition");
    /** @generated */
    public static final QName MultiSolid = new QName("http://www.opengis.net/gml/3.2", "MultiSolid");
    /** @generated */
    public static final QName MultiSolidCoverage = new QName("http://www.opengis.net/gml/3.2", "MultiSolidCoverage");
    /** @generated */
    public static final QName multiSolidDomain = new QName("http://www.opengis.net/gml/3.2", "multiSolidDomain");
    /** @generated */
    public static final QName multiSolidProperty = new QName("http://www.opengis.net/gml/3.2", "multiSolidProperty");
    /** @generated */
    public static final QName MultiSurface = new QName("http://www.opengis.net/gml/3.2", "MultiSurface");
    /** @generated */
    public static final QName MultiSurfaceCoverage =
            new QName("http://www.opengis.net/gml/3.2", "MultiSurfaceCoverage");
    /** @generated */
    public static final QName multiSurfaceDomain = new QName("http://www.opengis.net/gml/3.2", "multiSurfaceDomain");
    /** @generated */
    public static final QName multiSurfaceProperty =
            new QName("http://www.opengis.net/gml/3.2", "multiSurfaceProperty");
    /** @generated */
    public static final QName name = new QName("http://www.opengis.net/gml/3.2", "name");
    /** @generated */
    public static final QName Node = new QName("http://www.opengis.net/gml/3.2", "Node");
    /** @generated */
    public static final QName Null = new QName("http://www.opengis.net/gml/3.2", "Null");
    /** @generated */
    public static final QName ObliqueCartesianCS = new QName("http://www.opengis.net/gml/3.2", "ObliqueCartesianCS");
    /** @generated */
    public static final QName obliqueCartesianCSRef =
            new QName("http://www.opengis.net/gml/3.2", "obliqueCartesianCSRef");
    /** @generated */
    public static final QName Observation = new QName("http://www.opengis.net/gml/3.2", "Observation");
    /** @generated */
    public static final QName OffsetCurve = new QName("http://www.opengis.net/gml/3.2", "OffsetCurve");
    /** @generated */
    public static final QName OperationMethod = new QName("http://www.opengis.net/gml/3.2", "OperationMethod");
    /** @generated */
    public static final QName operationMethodRef = new QName("http://www.opengis.net/gml/3.2", "operationMethodRef");
    /** @generated */
    public static final QName operationParameter = new QName("http://www.opengis.net/gml/3.2", "operationParameter");
    /** @generated */
    public static final QName OperationParameter = new QName("http://www.opengis.net/gml/3.2", "OperationParameter");
    /** @generated */
    public static final QName OperationParameterGroup =
            new QName("http://www.opengis.net/gml/3.2", "OperationParameterGroup");
    /** @generated */
    public static final QName operationParameterGroupRef =
            new QName("http://www.opengis.net/gml/3.2", "operationParameterGroupRef");
    /** @generated */
    public static final QName operationParameterRef =
            new QName("http://www.opengis.net/gml/3.2", "operationParameterRef");
    /** @generated */
    public static final QName operationRef = new QName("http://www.opengis.net/gml/3.2", "operationRef");
    /** @generated */
    public static final QName operationVersion = new QName("http://www.opengis.net/gml/3.2", "operationVersion");
    /** @generated */
    public static final QName OrientableCurve = new QName("http://www.opengis.net/gml/3.2", "OrientableCurve");
    /** @generated */
    public static final QName OrientableSurface = new QName("http://www.opengis.net/gml/3.2", "OrientableSurface");
    /** @generated */
    public static final QName origin = new QName("http://www.opengis.net/gml/3.2", "origin");
    /** @generated */
    public static final QName parameter = new QName("http://www.opengis.net/gml/3.2", "parameter");
    /** @generated */
    public static final QName parameterValue = new QName("http://www.opengis.net/gml/3.2", "parameterValue");
    /** @generated */
    public static final QName ParameterValue = new QName("http://www.opengis.net/gml/3.2", "ParameterValue");
    /** @generated */
    public static final QName ParameterValueGroup = new QName("http://www.opengis.net/gml/3.2", "ParameterValueGroup");
    /** @generated */
    public static final QName PassThroughOperation =
            new QName("http://www.opengis.net/gml/3.2", "PassThroughOperation");
    /** @generated */
    public static final QName passThroughOperationRef =
            new QName("http://www.opengis.net/gml/3.2", "passThroughOperationRef");
    /** @generated */
    public static final QName patches = new QName("http://www.opengis.net/gml/3.2", "patches");
    /** @generated */
    public static final QName pixelInCell = new QName("http://www.opengis.net/gml/3.2", "pixelInCell");
    /** @generated */
    public static final QName Point = new QName("http://www.opengis.net/gml/3.2", "Point");
    /** @generated */
    public static final QName pointArrayProperty = new QName("http://www.opengis.net/gml/3.2", "pointArrayProperty");
    /** @generated */
    public static final QName pointMember = new QName("http://www.opengis.net/gml/3.2", "pointMember");
    /** @generated */
    public static final QName pointMembers = new QName("http://www.opengis.net/gml/3.2", "pointMembers");
    /** @generated */
    public static final QName pointProperty = new QName("http://www.opengis.net/gml/3.2", "pointProperty");
    /** @generated */
    public static final QName pointRep = new QName("http://www.opengis.net/gml/3.2", "pointRep");
    /** @generated */
    public static final QName polarCS = new QName("http://www.opengis.net/gml/3.2", "polarCS");
    /** @generated */
    public static final QName PolarCS = new QName("http://www.opengis.net/gml/3.2", "PolarCS");
    /** @generated */
    public static final QName polarCSRef = new QName("http://www.opengis.net/gml/3.2", "polarCSRef");
    /** @generated */
    public static final QName Polygon = new QName("http://www.opengis.net/gml/3.2", "Polygon");
    /** @generated */
    public static final QName PolygonPatch = new QName("http://www.opengis.net/gml/3.2", "PolygonPatch");
    /** @generated */
    public static final QName polygonPatches = new QName("http://www.opengis.net/gml/3.2", "polygonPatches");
    /** @generated */
    public static final QName PolyhedralSurface = new QName("http://www.opengis.net/gml/3.2", "PolyhedralSurface");
    /** @generated */
    public static final QName pos = new QName("http://www.opengis.net/gml/3.2", "pos");
    /** @generated */
    public static final QName position = new QName("http://www.opengis.net/gml/3.2", "position");
    /** @generated */
    public static final QName posList = new QName("http://www.opengis.net/gml/3.2", "posList");
    /** @generated */
    public static final QName primeMeridian = new QName("http://www.opengis.net/gml/3.2", "primeMeridian");
    /** @generated */
    public static final QName PrimeMeridian = new QName("http://www.opengis.net/gml/3.2", "PrimeMeridian");
    /** @generated */
    public static final QName primeMeridianRef = new QName("http://www.opengis.net/gml/3.2", "primeMeridianRef");
    /** @generated */
    public static final QName priorityLocation = new QName("http://www.opengis.net/gml/3.2", "priorityLocation");
    /** @generated */
    public static final QName ProjectedCRS = new QName("http://www.opengis.net/gml/3.2", "ProjectedCRS");
    /** @generated */
    public static final QName projectedCRSRef = new QName("http://www.opengis.net/gml/3.2", "projectedCRSRef");
    /** @generated */
    public static final QName Quantity = new QName("http://www.opengis.net/gml/3.2", "Quantity");
    /** @generated */
    public static final QName QuantityExtent = new QName("http://www.opengis.net/gml/3.2", "QuantityExtent");
    /** @generated */
    public static final QName QuantityList = new QName("http://www.opengis.net/gml/3.2", "QuantityList");
    /** @generated */
    public static final QName quantityType = new QName("http://www.opengis.net/gml/3.2", "quantityType");
    /** @generated */
    public static final QName quantityTypeReference =
            new QName("http://www.opengis.net/gml/3.2", "quantityTypeReference");
    /** @generated */
    public static final QName rangeMeaning = new QName("http://www.opengis.net/gml/3.2", "rangeMeaning");
    /** @generated */
    public static final QName rangeParameters = new QName("http://www.opengis.net/gml/3.2", "rangeParameters");
    /** @generated */
    public static final QName rangeSet = new QName("http://www.opengis.net/gml/3.2", "rangeSet");
    /** @generated */
    public static final QName realizationEpoch = new QName("http://www.opengis.net/gml/3.2", "realizationEpoch");
    /** @generated */
    public static final QName Rectangle = new QName("http://www.opengis.net/gml/3.2", "Rectangle");
    /** @generated */
    public static final QName RectifiedGrid = new QName("http://www.opengis.net/gml/3.2", "RectifiedGrid");
    /** @generated */
    public static final QName RectifiedGridCoverage =
            new QName("http://www.opengis.net/gml/3.2", "RectifiedGridCoverage");
    /** @generated */
    public static final QName rectifiedGridDomain = new QName("http://www.opengis.net/gml/3.2", "rectifiedGridDomain");
    /** @generated */
    public static final QName referenceSystemRef = new QName("http://www.opengis.net/gml/3.2", "referenceSystemRef");
    /** @generated */
    public static final QName remarks = new QName("http://www.opengis.net/gml/3.2", "remarks");
    /** @generated */
    public static final QName resultOf = new QName("http://www.opengis.net/gml/3.2", "resultOf");
    /** @generated */
    public static final QName reversePropertyName = new QName("http://www.opengis.net/gml/3.2", "reversePropertyName");
    /** @generated */
    public static final QName Ring = new QName("http://www.opengis.net/gml/3.2", "Ring");
    /** @generated */
    public static final QName roughConversionToPreferredUnit =
            new QName("http://www.opengis.net/gml/3.2", "roughConversionToPreferredUnit");
    /** @generated */
    public static final QName scope = new QName("http://www.opengis.net/gml/3.2", "scope");
    /** @generated */
    public static final QName secondDefiningParameter =
            new QName("http://www.opengis.net/gml/3.2", "secondDefiningParameter");
    /** @generated */
    public static final QName SecondDefiningParameter =
            new QName("http://www.opengis.net/gml/3.2", "SecondDefiningParameter");
    /** @generated */
    public static final QName seconds = new QName("http://www.opengis.net/gml/3.2", "seconds");
    /** @generated */
    public static final QName segments = new QName("http://www.opengis.net/gml/3.2", "segments");
    /** @generated */
    public static final QName semiMajorAxis = new QName("http://www.opengis.net/gml/3.2", "semiMajorAxis");
    /** @generated */
    public static final QName Shell = new QName("http://www.opengis.net/gml/3.2", "Shell");
    /** @generated */
    public static final QName singleCRSRef = new QName("http://www.opengis.net/gml/3.2", "singleCRSRef");
    /** @generated */
    public static final QName singleOperationRef = new QName("http://www.opengis.net/gml/3.2", "singleOperationRef");
    /** @generated */
    public static final QName Solid = new QName("http://www.opengis.net/gml/3.2", "Solid");
    /** @generated */
    public static final QName solidArrayProperty = new QName("http://www.opengis.net/gml/3.2", "solidArrayProperty");
    /** @generated */
    public static final QName solidMember = new QName("http://www.opengis.net/gml/3.2", "solidMember");
    /** @generated */
    public static final QName solidMembers = new QName("http://www.opengis.net/gml/3.2", "solidMembers");
    /** @generated */
    public static final QName solidProperty = new QName("http://www.opengis.net/gml/3.2", "solidProperty");
    /** @generated */
    public static final QName sourceCRS = new QName("http://www.opengis.net/gml/3.2", "sourceCRS");
    /** @generated */
    public static final QName sourceDimensions = new QName("http://www.opengis.net/gml/3.2", "sourceDimensions");
    /** @generated */
    public static final QName Sphere = new QName("http://www.opengis.net/gml/3.2", "Sphere");
    /** @generated */
    public static final QName sphericalCS = new QName("http://www.opengis.net/gml/3.2", "sphericalCS");
    /** @generated */
    public static final QName SphericalCS = new QName("http://www.opengis.net/gml/3.2", "SphericalCS");
    /** @generated */
    public static final QName sphericalCSRef = new QName("http://www.opengis.net/gml/3.2", "sphericalCSRef");
    /** @generated */
    public static final QName status = new QName("http://www.opengis.net/gml/3.2", "status");
    /** @generated */
    public static final QName statusReference = new QName("http://www.opengis.net/gml/3.2", "statusReference");
    /** @generated */
    public static final QName stringValue = new QName("http://www.opengis.net/gml/3.2", "stringValue");
    /** @generated */
    public static final QName subComplex = new QName("http://www.opengis.net/gml/3.2", "subComplex");
    /** @generated */
    public static final QName subject = new QName("http://www.opengis.net/gml/3.2", "subject");
    /** @generated */
    public static final QName superComplex = new QName("http://www.opengis.net/gml/3.2", "superComplex");
    /** @generated */
    public static final QName Surface = new QName("http://www.opengis.net/gml/3.2", "Surface");
    /** @generated */
    public static final QName surfaceArrayProperty =
            new QName("http://www.opengis.net/gml/3.2", "surfaceArrayProperty");
    /** @generated */
    public static final QName surfaceMember = new QName("http://www.opengis.net/gml/3.2", "surfaceMember");
    /** @generated */
    public static final QName surfaceMembers = new QName("http://www.opengis.net/gml/3.2", "surfaceMembers");
    /** @generated */
    public static final QName surfaceProperty = new QName("http://www.opengis.net/gml/3.2", "surfaceProperty");
    /** @generated */
    public static final QName target = new QName("http://www.opengis.net/gml/3.2", "target");
    /** @generated */
    public static final QName targetCRS = new QName("http://www.opengis.net/gml/3.2", "targetCRS");
    /** @generated */
    public static final QName targetDimensions = new QName("http://www.opengis.net/gml/3.2", "targetDimensions");
    /** @generated */
    public static final QName targetElement = new QName("http://www.opengis.net/gml/3.2", "targetElement");
    /** @generated */
    public static final QName TemporalCRS = new QName("http://www.opengis.net/gml/3.2", "TemporalCRS");
    /** @generated */
    public static final QName temporalCRSRef = new QName("http://www.opengis.net/gml/3.2", "temporalCRSRef");
    /** @generated */
    public static final QName TemporalCS = new QName("http://www.opengis.net/gml/3.2", "TemporalCS");
    /** @generated */
    public static final QName temporalCSRef = new QName("http://www.opengis.net/gml/3.2", "temporalCSRef");
    /** @generated */
    public static final QName temporalDatum = new QName("http://www.opengis.net/gml/3.2", "temporalDatum");
    /** @generated */
    public static final QName TemporalDatum = new QName("http://www.opengis.net/gml/3.2", "TemporalDatum");
    /** @generated */
    public static final QName temporalDatumRef = new QName("http://www.opengis.net/gml/3.2", "temporalDatumRef");
    /** @generated */
    public static final QName TimeCalendar = new QName("http://www.opengis.net/gml/3.2", "TimeCalendar");
    /** @generated */
    public static final QName TimeCalendarEra = new QName("http://www.opengis.net/gml/3.2", "TimeCalendarEra");
    /** @generated */
    public static final QName TimeClock = new QName("http://www.opengis.net/gml/3.2", "TimeClock");
    /** @generated */
    public static final QName TimeCoordinateSystem =
            new QName("http://www.opengis.net/gml/3.2", "TimeCoordinateSystem");
    /** @generated */
    public static final QName timeCS = new QName("http://www.opengis.net/gml/3.2", "timeCS");
    /** @generated */
    public static final QName TimeCS = new QName("http://www.opengis.net/gml/3.2", "TimeCS");
    /** @generated */
    public static final QName TimeEdge = new QName("http://www.opengis.net/gml/3.2", "TimeEdge");
    /** @generated */
    public static final QName TimeInstant = new QName("http://www.opengis.net/gml/3.2", "TimeInstant");
    /** @generated */
    public static final QName timeInterval = new QName("http://www.opengis.net/gml/3.2", "timeInterval");
    /** @generated */
    public static final QName TimeNode = new QName("http://www.opengis.net/gml/3.2", "TimeNode");
    /** @generated */
    public static final QName TimeOrdinalEra = new QName("http://www.opengis.net/gml/3.2", "TimeOrdinalEra");
    /** @generated */
    public static final QName TimeOrdinalReferenceSystem =
            new QName("http://www.opengis.net/gml/3.2", "TimeOrdinalReferenceSystem");
    /** @generated */
    public static final QName TimePeriod = new QName("http://www.opengis.net/gml/3.2", "TimePeriod");
    /** @generated */
    public static final QName timePosition = new QName("http://www.opengis.net/gml/3.2", "timePosition");
    /** @generated */
    public static final QName TimeReferenceSystem = new QName("http://www.opengis.net/gml/3.2", "TimeReferenceSystem");
    /** @generated */
    public static final QName TimeTopologyComplex = new QName("http://www.opengis.net/gml/3.2", "TimeTopologyComplex");
    /** @generated */
    public static final QName Tin = new QName("http://www.opengis.net/gml/3.2", "Tin");
    /** @generated */
    public static final QName TopoComplex = new QName("http://www.opengis.net/gml/3.2", "TopoComplex");
    /** @generated */
    public static final QName topoComplexProperty = new QName("http://www.opengis.net/gml/3.2", "topoComplexProperty");
    /** @generated */
    public static final QName TopoCurve = new QName("http://www.opengis.net/gml/3.2", "TopoCurve");
    /** @generated */
    public static final QName topoCurveProperty = new QName("http://www.opengis.net/gml/3.2", "topoCurveProperty");
    /** @generated */
    public static final QName TopoPoint = new QName("http://www.opengis.net/gml/3.2", "TopoPoint");
    /** @generated */
    public static final QName topoPointProperty = new QName("http://www.opengis.net/gml/3.2", "topoPointProperty");
    /** @generated */
    public static final QName topoPrimitiveMember = new QName("http://www.opengis.net/gml/3.2", "topoPrimitiveMember");
    /** @generated */
    public static final QName topoPrimitiveMembers =
            new QName("http://www.opengis.net/gml/3.2", "topoPrimitiveMembers");
    /** @generated */
    public static final QName TopoSolid = new QName("http://www.opengis.net/gml/3.2", "TopoSolid");
    /** @generated */
    public static final QName TopoSurface = new QName("http://www.opengis.net/gml/3.2", "TopoSurface");
    /** @generated */
    public static final QName topoSurfaceProperty = new QName("http://www.opengis.net/gml/3.2", "topoSurfaceProperty");
    /** @generated */
    public static final QName TopoVolume = new QName("http://www.opengis.net/gml/3.2", "TopoVolume");
    /** @generated */
    public static final QName topoVolumeProperty = new QName("http://www.opengis.net/gml/3.2", "topoVolumeProperty");
    /** @generated */
    public static final QName track = new QName("http://www.opengis.net/gml/3.2", "track");
    /** @generated */
    public static final QName Transformation = new QName("http://www.opengis.net/gml/3.2", "Transformation");
    /** @generated */
    public static final QName transformationRef = new QName("http://www.opengis.net/gml/3.2", "transformationRef");
    /** @generated */
    public static final QName Triangle = new QName("http://www.opengis.net/gml/3.2", "Triangle");
    /** @generated */
    public static final QName trianglePatches = new QName("http://www.opengis.net/gml/3.2", "trianglePatches");
    /** @generated */
    public static final QName TriangulatedSurface = new QName("http://www.opengis.net/gml/3.2", "TriangulatedSurface");
    /** @generated */
    public static final QName tupleList = new QName("http://www.opengis.net/gml/3.2", "tupleList");
    /** @generated */
    public static final QName UnitDefinition = new QName("http://www.opengis.net/gml/3.2", "UnitDefinition");
    /** @generated */
    public static final QName unitOfMeasure = new QName("http://www.opengis.net/gml/3.2", "unitOfMeasure");
    /** @generated */
    public static final QName userDefinedCS = new QName("http://www.opengis.net/gml/3.2", "userDefinedCS");
    /** @generated */
    public static final QName UserDefinedCS = new QName("http://www.opengis.net/gml/3.2", "UserDefinedCS");
    /** @generated */
    public static final QName userDefinedCSRef = new QName("http://www.opengis.net/gml/3.2", "userDefinedCSRef");
    /** @generated */
    public static final QName usesAffineCS = new QName("http://www.opengis.net/gml/3.2", "usesAffineCS");
    /** @generated */
    public static final QName usesAxis = new QName("http://www.opengis.net/gml/3.2", "usesAxis");
    /** @generated */
    public static final QName usesCartesianCS = new QName("http://www.opengis.net/gml/3.2", "usesCartesianCS");
    /** @generated */
    public static final QName usesCS = new QName("http://www.opengis.net/gml/3.2", "usesCS");
    /** @generated */
    public static final QName usesEllipsoid = new QName("http://www.opengis.net/gml/3.2", "usesEllipsoid");
    /** @generated */
    public static final QName usesEllipsoidalCS = new QName("http://www.opengis.net/gml/3.2", "usesEllipsoidalCS");
    /** @generated */
    public static final QName usesEngineeringDatum =
            new QName("http://www.opengis.net/gml/3.2", "usesEngineeringDatum");
    /** @generated */
    public static final QName usesGeodeticDatum = new QName("http://www.opengis.net/gml/3.2", "usesGeodeticDatum");
    /** @generated */
    public static final QName usesImageDatum = new QName("http://www.opengis.net/gml/3.2", "usesImageDatum");
    /** @generated */
    public static final QName usesMethod = new QName("http://www.opengis.net/gml/3.2", "usesMethod");
    /** @generated */
    public static final QName usesObliqueCartesianCS =
            new QName("http://www.opengis.net/gml/3.2", "usesObliqueCartesianCS");
    /** @generated */
    public static final QName usesOperation = new QName("http://www.opengis.net/gml/3.2", "usesOperation");
    /** @generated */
    public static final QName usesParameter = new QName("http://www.opengis.net/gml/3.2", "usesParameter");
    /** @generated */
    public static final QName usesPrimeMeridian = new QName("http://www.opengis.net/gml/3.2", "usesPrimeMeridian");
    /** @generated */
    public static final QName usesSingleOperation = new QName("http://www.opengis.net/gml/3.2", "usesSingleOperation");
    /** @generated */
    public static final QName usesSphericalCS = new QName("http://www.opengis.net/gml/3.2", "usesSphericalCS");
    /** @generated */
    public static final QName usesTemporalCS = new QName("http://www.opengis.net/gml/3.2", "usesTemporalCS");
    /** @generated */
    public static final QName usesTemporalDatum = new QName("http://www.opengis.net/gml/3.2", "usesTemporalDatum");
    /** @generated */
    public static final QName usesTimeCS = new QName("http://www.opengis.net/gml/3.2", "usesTimeCS");
    /** @generated */
    public static final QName usesValue = new QName("http://www.opengis.net/gml/3.2", "usesValue");
    /** @generated */
    public static final QName usesVerticalCS = new QName("http://www.opengis.net/gml/3.2", "usesVerticalCS");
    /** @generated */
    public static final QName usesVerticalDatum = new QName("http://www.opengis.net/gml/3.2", "usesVerticalDatum");
    /** @generated */
    public static final QName using = new QName("http://www.opengis.net/gml/3.2", "using");
    /** @generated */
    public static final QName validTime = new QName("http://www.opengis.net/gml/3.2", "validTime");
    /** @generated */
    public static final QName value = new QName("http://www.opengis.net/gml/3.2", "value");
    /** @generated */
    public static final QName ValueArray = new QName("http://www.opengis.net/gml/3.2", "ValueArray");
    /** @generated */
    public static final QName valueComponent = new QName("http://www.opengis.net/gml/3.2", "valueComponent");
    /** @generated */
    public static final QName valueComponents = new QName("http://www.opengis.net/gml/3.2", "valueComponents");
    /** @generated */
    public static final QName valueFile = new QName("http://www.opengis.net/gml/3.2", "valueFile");
    /** @generated */
    public static final QName valueList = new QName("http://www.opengis.net/gml/3.2", "valueList");
    /** @generated */
    public static final QName valueOfParameter = new QName("http://www.opengis.net/gml/3.2", "valueOfParameter");
    /** @generated */
    public static final QName valueProperty = new QName("http://www.opengis.net/gml/3.2", "valueProperty");
    /** @generated */
    public static final QName valuesOfGroup = new QName("http://www.opengis.net/gml/3.2", "valuesOfGroup");
    /** @generated */
    public static final QName vector = new QName("http://www.opengis.net/gml/3.2", "vector");
    /** @generated */
    public static final QName VerticalCRS = new QName("http://www.opengis.net/gml/3.2", "VerticalCRS");
    /** @generated */
    public static final QName verticalCRSRef = new QName("http://www.opengis.net/gml/3.2", "verticalCRSRef");
    /** @generated */
    public static final QName verticalCS = new QName("http://www.opengis.net/gml/3.2", "verticalCS");
    /** @generated */
    public static final QName VerticalCS = new QName("http://www.opengis.net/gml/3.2", "VerticalCS");
    /** @generated */
    public static final QName verticalCSRef = new QName("http://www.opengis.net/gml/3.2", "verticalCSRef");
    /** @generated */
    public static final QName verticalDatum = new QName("http://www.opengis.net/gml/3.2", "verticalDatum");
    /** @generated */
    public static final QName VerticalDatum = new QName("http://www.opengis.net/gml/3.2", "VerticalDatum");
    /** @generated */
    public static final QName verticalDatumRef = new QName("http://www.opengis.net/gml/3.2", "verticalDatumRef");

    /* Attributes */
    /** @generated */
    public static final QName id = new QName("http://www.opengis.net/gml/3.2", "id");
    /** @generated */
    public static final QName remoteSchema = new QName("http://www.opengis.net/gml/3.2", "remoteSchema");
    /** @generated */
    public static final QName uom = new QName("http://www.opengis.net/gml/3.2", "uom");

    @Override
    protected Schema buildTypeSchema() {
        return new GMLSchema();
    }

    @Override
    protected Schema buildTypeMappingProfile(Schema typeSchema) {
        // reuse the regular gml3 type mapping profile bindings, but override
        // the namespace uri
        Schema gml3Profile = org.geotools.gml3.GML.getInstance().getTypeMappingProfile();
        Set<Name> profile = new LinkedHashSet<>();
        for (Name n : gml3Profile.keySet()) {
            n = new NameImpl(NAMESPACE, n.getLocalPart());
            if (typeSchema.get(n) != null) {
                profile.add(n);
            }
        }

        return typeSchema.profile(profile);
    }

    /**
     * Instead of building the XSDSchema using the declared dependencies, handle cyclic dependencies by loading all the
     * schemas in the GML 3.2 suite at once by forcing use of a resolver. This all-in-one XSDSchema is later shared by
     * the other XSD implementations in the suite.
     *
     * @see XSD#buildSchema()
     */
    @Override
    protected XSDSchema buildSchema() throws IOException {
        schema = Schemas.parse(
                getSchemaLocation(),
                Collections.emptyList(),
                Collections.singletonList(new XSDSchemaLocationResolver() {
                    @Override
                    public String resolveSchemaLocation(
                            XSDSchema xsdSchema, String namespaceURI, String schemaLocationURI) {
                        try {
                            URI contextUri = new URI(xsdSchema.getSchemaLocation());
                            if (contextUri.isOpaque()) {
                                // probably a jar:file: URL, which is opaque and
                                // thus not
                                // supported by URI.resolve()
                                URL contextUrl = new URL(xsdSchema.getSchemaLocation());
                                return new URL(contextUrl, schemaLocationURI).toString();
                            } else {
                                return contextUri.resolve(schemaLocationURI).toString();
                            }
                        } catch (URISyntaxException | MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
        // leak prevention
        schema.resolveElementDeclaration(NAMESPACE, "_Feature").eAdapters().add(new SubstitutionGroupLeakPreventer());
        schema.eAdapters().add(new ReferencingDirectiveLeakPreventer());
        return schema;
    }

    /**
     * An {@link XSD} that delegates to GML for its XSDSchema. This allows us to load the full schema, including cyclic
     * dependencies, into the top-level GML schema, and have the other namespaces represented by their own XSD which
     * uses the same GML schema. We override {@link #buildSchema()} rather than {@link #getSchema()} to ensure the
     * internal implementation of XSD works correctly, as it makes direct access to the schema via the schema field.
     */
    public abstract static class DelegatingXSD extends XSD {

        /** @see XSD#buildSchema() */
        @Override
        protected XSDSchema buildSchema() throws IOException {
            return GML.getInstance().getSchema();
        }
    }
}
