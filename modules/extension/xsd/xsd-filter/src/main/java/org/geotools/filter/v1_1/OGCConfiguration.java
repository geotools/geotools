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
package org.geotools.filter.v1_1;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.picocontainer.MutablePicoContainer;
import org.opengis.filter.FilterFactory;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.v1_0.OGCAddBinding;
import org.geotools.filter.v1_0.OGCAndBinding;
import org.geotools.filter.v1_0.OGCBBOXTypeBinding;
import org.geotools.filter.v1_0.OGCBeyondBinding;
import org.geotools.filter.v1_0.OGCBinaryComparisonOpTypeBinding;
import org.geotools.filter.v1_0.OGCBinaryLogicOpTypeBinding;
import org.geotools.filter.v1_0.OGCBinaryOperatorTypeBinding;
import org.geotools.filter.v1_0.OGCBinarySpatialOpTypeBinding;
import org.geotools.filter.v1_0.OGCContainsBinding;
import org.geotools.filter.v1_0.OGCCrossesBinding;
import org.geotools.filter.v1_0.OGCDWithinBinding;
import org.geotools.filter.v1_0.OGCDisjointBinding;
import org.geotools.filter.v1_0.OGCDistanceBufferTypeBinding;
import org.geotools.filter.v1_0.OGCDistanceTypeBinding;
import org.geotools.filter.v1_0.OGCDivBinding;
import org.geotools.filter.v1_0.OGCEqualsBinding;
import org.geotools.filter.v1_0.OGCExpressionTypeBinding;
import org.geotools.filter.v1_0.OGCFeatureIdTypeBinding;
import org.geotools.filter.v1_0.OGCFunctionTypeBinding;
import org.geotools.filter.v1_0.OGCIntersectsBinding;
import org.geotools.filter.v1_0.OGCLiteralTypeBinding;
import org.geotools.filter.v1_0.OGCLowerBoundaryTypeBinding;
import org.geotools.filter.v1_0.OGCMulBinding;
import org.geotools.filter.v1_0.OGCNotBinding;
import org.geotools.filter.v1_0.OGCOrBinding;
import org.geotools.filter.v1_0.OGCOverlapsBinding;
import org.geotools.filter.v1_0.OGCPropertyIsBetweenTypeBinding;
import org.geotools.filter.v1_0.OGCPropertyIsEqualToBinding;
import org.geotools.filter.v1_0.OGCPropertyIsGreaterThanBinding;
import org.geotools.filter.v1_0.OGCPropertyIsGreaterThanOrEqualToBinding;
import org.geotools.filter.v1_0.OGCPropertyIsLessThanBinding;
import org.geotools.filter.v1_0.OGCPropertyIsLessThanOrEqualToBinding;
import org.geotools.filter.v1_0.OGCPropertyIsLikeTypeBinding;
import org.geotools.filter.v1_0.OGCPropertyIsNotEqualToBinding;
import org.geotools.filter.v1_0.OGCPropertyIsNullTypeBinding;
import org.geotools.filter.v1_0.OGCPropertyNameTypeBinding;
import org.geotools.filter.v1_0.OGCSubBinding;
import org.geotools.filter.v1_0.OGCTouchesBinding;
import org.geotools.filter.v1_0.OGCUpperBoundaryTypeBinding;
import org.geotools.filter.v1_0.OGCWithinBinding;
import org.geotools.filter.v1_0.capabilities.Arithmetic_OperatorsTypeBinding;
import org.geotools.filter.v1_0.capabilities.Function_NameTypeBinding;
import org.geotools.filter.v1_0.capabilities.Function_NamesTypeBinding;
import org.geotools.filter.v1_0.capabilities.FunctionsTypeBinding;
import org.geotools.filter.v1_0.capabilities.Scalar_CapabilitiesTypeBinding;
import org.geotools.filter.v1_1.capabilities.ComparisonOperatorTypeBinding;
import org.geotools.filter.v1_1.capabilities.ComparisonOperatorsTypeBinding;
import org.geotools.filter.v1_1.capabilities.GeometryOperandTypeBinding;
import org.geotools.filter.v1_1.capabilities.GeometryOperandsTypeBinding;
import org.geotools.filter.v1_1.capabilities.Id_CapabilitiesTypeBinding;
import org.geotools.filter.v1_1.capabilities.SpatialOperatorTypeBinding;
import org.geotools.filter.v1_1.capabilities.SpatialOperatorsTypeBinding;
import org.geotools.filter.v1_1.capabilities.Spatial_CapabilitiesTypeBinding;
import org.geotools.filter.v1_1.capabilities._Filter_CapabilitiesBinding;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Configuration;


/**
 * Parser configuration for the filter 1.1 schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class OGCConfiguration extends Configuration {
    /**
     * Adds a dependency on {@link GMLConfiguration}
     */
    public OGCConfiguration() {
        super(OGC.getInstance());

        addDependency(new GMLConfiguration());
    }

    protected void registerBindings(MutablePicoContainer container) {
        //Types
        //container.registerComponentImplementation(OGC.ABSTRACTIDTYPE,AbstractIdTypeBinding.class);
        //container.registerComponentImplementation(OGC.ArithmeticOperatorsType,
        //    ArithmeticOperatorsTypeBinding.class);
        //container.registerComponentImplementation(OGC.BBOXTYPE,BBOXTypeBinding.class);
        container.registerComponentImplementation(OGC.BBOXType, OGCBBOXTypeBinding.class);
        container.registerComponentImplementation(OGC.BinaryComparisonOpType,
            OGCBinaryComparisonOpTypeBinding.class);
        container.registerComponentImplementation(OGC.BinaryLogicOpType,
            OGCBinaryLogicOpTypeBinding.class);
        container.registerComponentImplementation(OGC.BinaryOperatorType,
            OGCBinaryOperatorTypeBinding.class);
        container.registerComponentImplementation(OGC.BinarySpatialOpType,
            OGCBinarySpatialOpTypeBinding.class);
        //container.registerComponentImplementation(OGC.ComparisonOperatorsType,
        //   ComparisonOperatorsTypeBinding.class);
        //container.registerComponentImplementation(OGC.ComparisonOperatorType,
        //    ComparisonOperatorTypeBinding.class);
        //container.registerComponentImplementation(OGC.COMPARISONOPSTYPE,ComparisonOpsTypeBinding.class);
        container.registerComponentImplementation(OGC.DistanceBufferType,
            OGCDistanceBufferTypeBinding.class);
        //container.registerComponentImplementation(OGC.DISTANCETYPE,DistanceTypeBinding.class);
        container.registerComponentImplementation(OGC.DistanceType, OGCDistanceTypeBinding.class);

        //container.registerComponentImplementation(OGC.EXPRESSIONTYPE, ExpressionTypeBinding.class);
        container.registerComponentImplementation(OGC.ExpressionType, OGCExpressionTypeBinding.class);

        container.registerComponentImplementation(OGC.FeatureIdType, OGCFeatureIdTypeBinding.class);
        container.registerComponentImplementation(OGC.FilterType, FilterTypeBinding.class);
        //container.registerComponentImplementation(OGC.FILTERTYPE,FilterTypeBinding.class);
        //container.registerComponentImplementation(OGC.FunctionNamesType,
        //    FunctionNamesTypeBinding.class);
        //container.registerComponentImplementation(OGC.FunctionNameType,
        //    FunctionNameTypeBinding.class);
        //container.registerComponentImplementation(OGC.FunctionsType, FunctionsTypeBinding.class);
        container.registerComponentImplementation(OGC.FunctionType, OGCFunctionTypeBinding.class);
        //container.registerComponentImplementation(OGC.FUNCTIONTYPE,FunctionTypeBinding.class);
        //container.registerComponentImplementation(OGC.GeometryOperandsType,
        //    GeometryOperandsTypeBinding.class);
        //container.registerComponentImplementation(OGC.GeometryOperandType,
        //    GeometryOperandTypeBinding.class);
        container.registerComponentImplementation(OGC.GmlObjectIdType, GmlObjectIdTypeBinding.class);
        //container.registerComponentImplementation(OGC.Id_CapabilitiesType,
        //    Id_CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OGC.LiteralType, OGCLiteralTypeBinding.class);
        //container.registerComponentImplementation(OGC.LITERALTYPE,LiteralTypeBinding.class);
        //container.registerComponentImplementation(OGC.LOGICOPSTYPE,LogicOpsTypeBinding.class);
        container.registerComponentImplementation(OGC.LowerBoundaryType,
            OGCLowerBoundaryTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsBetweenType,
            OGCPropertyIsBetweenTypeBinding.class);
        //container.registerComponentImplementation(OGC.PROPERTYISBETWEENTYPE,PropertyIsBetweenTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsLikeType,
            OGCPropertyIsLikeTypeBinding.class);
        //container.registerComponentImplementation(OGC.PROPERTYISNULLTYPE,PropertyIsNullTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsNullType,
            OGCPropertyIsNullTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyNameType,
            OGCPropertyNameTypeBinding.class);
        //container.registerComponentImplementation(OGC.Scalar_CapabilitiesType,
        //    Scalar_CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OGC.SortByType, SortByTypeBinding.class);
        container.registerComponentImplementation(OGC.SortOrderType, SortOrderTypeBinding.class);
        container.registerComponentImplementation(OGC.SortPropertyType,
            SortPropertyTypeBinding.class);
        //container.registerComponentImplementation(OGC.Spatial_CapabilitiesType,
        //    Spatial_CapabilitiesTypeBinding.class);
        //container.registerComponentImplementation(OGC.SpatialOperatorNameType,
        //    SpatialOperatorNameTypeBinding.class);
        //container.registerComponentImplementation(OGC.SpatialOperatorsType,
        //    SpatialOperatorsTypeBinding.class);
        //container.registerComponentImplementation(OGC.SpatialOperatorType,
        //    SpatialOperatorTypeBinding.class);
        //container.registerComponentImplementation(OGC.SPATIALOPSTYPE,SpatialOpsTypeBinding.class);
        //container.registerComponentImplementation(OGC.UNARYLOGICOPTYPE,
        //    UnaryLogicOpTypeBinding.class);
        container.registerComponentImplementation(OGC.UpperBoundaryType,
            OGCUpperBoundaryTypeBinding.class);

        //Elements
        //container.registerComponentImplementation(OGC._ID,_IdBinding.class);
        container.registerComponentImplementation(OGC.Add, OGCAddBinding.class);
        container.registerComponentImplementation(OGC.And, OGCAndBinding.class);
        //container.registerComponentImplementation(OGC.BBOX,BBOXBinding.class);
        container.registerComponentImplementation(OGC.Beyond, OGCBeyondBinding.class);
        //container.registerComponentImplementation(OGC.COMPARISONOPS,ComparisonOpsBinding.class);
        container.registerComponentImplementation(OGC.Contains, OGCContainsBinding.class);
        container.registerComponentImplementation(OGC.Crosses, OGCCrossesBinding.class);
        container.registerComponentImplementation(OGC.Disjoint, OGCDisjointBinding.class);
        container.registerComponentImplementation(OGC.Div, OGCDivBinding.class);
        container.registerComponentImplementation(OGC.DWithin, OGCDWithinBinding.class);
        //container.registerComponentImplementation(OGC.EID, EIDBinding.class);
        container.registerComponentImplementation(OGC.Equals, OGCEqualsBinding.class);
        //container.registerComponentImplementation(OGC.EXPRESSION, ExpressionBinding.class);
        //container.registerComponentImplementation(OGC.FEATUREID, FeatureIdBinding.class);
        //container.registerComponentImplementation(OGC.FID, FIDBinding.class);
        //container.registerComponentImplementation(OGC.FILTER,FilterBinding.class);
        //container.registerComponentImplementation(OGC.Filter_Capabilities,
        //    Filter_CapabilitiesBinding.class);
        //container.registerComponentImplementation(OGC.FUNCTION, FunctionBinding.class);
        //container.registerComponentImplementation(OGC.GMLOBJECTID, GmlObjectIdBinding.class);
        container.registerComponentImplementation(OGC.Intersects, OGCIntersectsBinding.class);
        //container.registerComponentImplementation(OGC.LITERAL,LiteralBinding.class);
        //container.registerComponentImplementation(OGC.LogicalOperators,
        //   LogicalOperatorsBinding.class);
        //container.registerComponentImplementation(OGC.LOGICOPS,LogicOpsBinding.class);
        container.registerComponentImplementation(OGC.Mul, OGCMulBinding.class);
        container.registerComponentImplementation(OGC.Not, OGCNotBinding.class);
        container.registerComponentImplementation(OGC.Or, OGCOrBinding.class);
        container.registerComponentImplementation(OGC.Overlaps, OGCOverlapsBinding.class);
        //container.registerComponentImplementation(OGC.PROPERTYISBETWEEN,PropertyIsBetweenBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsEqualTo,
            OGCPropertyIsEqualToBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsGreaterThan,
            OGCPropertyIsGreaterThanBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsGreaterThanOrEqualTo,
            OGCPropertyIsGreaterThanOrEqualToBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsLessThan,
            OGCPropertyIsLessThanBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsLessThanOrEqualTo,
            OGCPropertyIsLessThanOrEqualToBinding.class);
        //container.registerComponentImplementation(OGC.PROPERTYISLIKE, PropertyIsLikeBinding.class);
        container.registerComponentImplementation(OGC.PropertyIsNotEqualTo,
            OGCPropertyIsNotEqualToBinding.class);
        //container.registerComponentImplementation(OGC.PROPERTYISNULL,PropertyIsNullBinding.class);
        //        container.registerComponentImplementation(OGC.PROPERTYNAME,
        //            PropertyNameBinding.class);
        //container.registerComponentImplementation(OGC.SimpleArithmetic,
        //    SimpleArithmeticBinding.class);
        //container.registerComponentImplementation(OGC.SORTBY,SortByBinding.class);
        //container.registerComponentImplementation(OGC.SPATIALOPS,SpatialOpsBinding.class);
        container.registerComponentImplementation(OGC.Sub, OGCSubBinding.class);
        container.registerComponentImplementation(OGC.Touches, OGCTouchesBinding.class);
        container.registerComponentImplementation(OGC.Within, OGCWithinBinding.class);

        //capabilities
        //Types
        container.registerComponentImplementation(OGC.ArithmeticOperatorsType,
            Arithmetic_OperatorsTypeBinding.class);
        container.registerComponentImplementation(OGC.ComparisonOperatorsType,
            ComparisonOperatorsTypeBinding.class);
        container.registerComponentImplementation(OGC.ComparisonOperatorType,
            ComparisonOperatorTypeBinding.class);
        container.registerComponentImplementation(OGC.FunctionNamesType,
            Function_NamesTypeBinding.class);
        container.registerComponentImplementation(OGC.FunctionNameType,
            Function_NameTypeBinding.class);
        container.registerComponentImplementation(OGC.FunctionsType, FunctionsTypeBinding.class);
        container.registerComponentImplementation(OGC.GeometryOperandsType,
            GeometryOperandsTypeBinding.class);
        container.registerComponentImplementation(OGC.GeometryOperandType,
            GeometryOperandTypeBinding.class);
        container.registerComponentImplementation(OGC.Id_CapabilitiesType,
            Id_CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OGC.Scalar_CapabilitiesType,
            Scalar_CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OGC.Spatial_CapabilitiesType,
            Spatial_CapabilitiesTypeBinding.class);
        //container.registerComponentImplementation(OGC.SpatialOperatorNameType,SpatialOperatorNameTypeBinding.class);
        container.registerComponentImplementation(OGC.SpatialOperatorsType,
            SpatialOperatorsTypeBinding.class);
        container.registerComponentImplementation(OGC.SpatialOperatorType,
            SpatialOperatorTypeBinding.class);
        //container.registerComponentImplementation(OGC._EID,_EIDBinding.class);
        //container.registerComponentImplementation(OGC._FID,_FIDBinding.class);
        container.registerComponentImplementation(OGC._Filter_Capabilities,
            _Filter_CapabilitiesBinding.class);

        //container.registerComponentImplementation(OGC._LogicalOperators,_LogicalOperatorsBinding.class);
        //container.registerComponentImplementation(OGC._SimpleArithmetic,_SimpleArithmeticBinding.class);
    }

    /**
     * Configures the filter context.
     * <p>
     * The following factories are registered:
     * <ul>
     * <li>{@link FilterFactoryImpl} under {@link FilterFactory}
     * </ul>
     * </p>
     */
    public void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentImplementation(FilterFactory.class, FilterFactoryImpl.class);
    }
}
