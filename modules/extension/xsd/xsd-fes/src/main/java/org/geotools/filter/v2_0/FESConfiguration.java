/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0;

import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.v1_1.SortByTypeBinding;
import org.geotools.filter.v1_1.SortOrderTypeBinding;
import org.geotools.filter.v1_1.SortPropertyTypeBinding;
import org.geotools.filter.v2_0.bindings.AfterBinding;
import org.geotools.filter.v2_0.bindings.AndBinding;
import org.geotools.filter.v2_0.bindings.AnyInteractsBinding;
import org.geotools.filter.v2_0.bindings.BBOXTypeBinding;
import org.geotools.filter.v2_0.bindings.BeforeBinding;
import org.geotools.filter.v2_0.bindings.BeginsBinding;
import org.geotools.filter.v2_0.bindings.BegunByBinding;
import org.geotools.filter.v2_0.bindings.BeyondBinding;
import org.geotools.filter.v2_0.bindings.BinaryComparisonOpTypeBinding;
import org.geotools.filter.v2_0.bindings.BinaryLogicOpTypeBinding;
import org.geotools.filter.v2_0.bindings.BinarySpatialOpTypeBinding;
import org.geotools.filter.v2_0.bindings.BinaryTemporalOpTypeBinding;
import org.geotools.filter.v2_0.bindings.ContainsBinding;
import org.geotools.filter.v2_0.bindings.CrossesBinding;
import org.geotools.filter.v2_0.bindings.DWithinBinding;
import org.geotools.filter.v2_0.bindings.DisjointBinding;
import org.geotools.filter.v2_0.bindings.DistanceBufferTypeBinding;
import org.geotools.filter.v2_0.bindings.DuringBinding;
import org.geotools.filter.v2_0.bindings.EndedByBinding;
import org.geotools.filter.v2_0.bindings.EndsBinding;
import org.geotools.filter.v2_0.bindings.EqualsBinding;
import org.geotools.filter.v2_0.bindings.FilterTypeBinding;
import org.geotools.filter.v2_0.bindings.FunctionTypeBinding;
import org.geotools.filter.v2_0.bindings.IntersectsBinding;
import org.geotools.filter.v2_0.bindings.LiteralBinding;
import org.geotools.filter.v2_0.bindings.MeetsBinding;
import org.geotools.filter.v2_0.bindings.MetByBinding;
import org.geotools.filter.v2_0.bindings.NotBinding;
import org.geotools.filter.v2_0.bindings.OrBinding;
import org.geotools.filter.v2_0.bindings.OverlappedByBinding;
import org.geotools.filter.v2_0.bindings.OverlapsBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsBetweenTypeBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsEqualToBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsGreaterThanBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsGreaterThanOrEqualToBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsLessThanBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsLessThanOrEqualToBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsLikeTypeBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsNotEqualToBinding;
import org.geotools.filter.v2_0.bindings.PropertyIsNullTypeBinding;
import org.geotools.filter.v2_0.bindings.ResourceIdTypeBinding;
import org.geotools.filter.v2_0.bindings.TContainsBinding;
import org.geotools.filter.v2_0.bindings.TEqualsBinding;
import org.geotools.filter.v2_0.bindings.TOverlapsBinding;
import org.geotools.filter.v2_0.bindings.TouchesBinding;
import org.geotools.filter.v2_0.bindings.ValueReferenceBinding;
import org.geotools.filter.v2_0.bindings.WithinBinding;
import org.geotools.gml3.v3_2.GMLConfiguration;
import org.geotools.ows.v1_1.OWSConfiguration;
import org.geotools.xml.Configuration;
import org.opengis.filter.FilterFactory;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/fes/2.0 schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class FESConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public FESConfiguration() {
       super(FES.getInstance());
       
       addDependency(new OWSConfiguration());
       addDependency(new GMLConfiguration());
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Types
//        container.registerComponentImplementation(FES.AbstractAdhocQueryExpressionType,AbstractAdhocQueryExpressionTypeBinding.class);
//        container.registerComponentImplementation(FES.AbstractIdType,AbstractIdTypeBinding.class);
//        container.registerComponentImplementation(FES.AbstractProjectionClauseType,AbstractProjectionClauseTypeBinding.class);
//        container.registerComponentImplementation(FES.AbstractQueryExpressionType,AbstractQueryExpressionTypeBinding.class);
//        container.registerComponentImplementation(FES.AbstractSelectionClauseType,AbstractSelectionClauseTypeBinding.class);
//        container.registerComponentImplementation(FES.AbstractSortingClauseType,AbstractSortingClauseTypeBinding.class);
//        container.registerComponentImplementation(FES.AliasesType,AliasesTypeBinding.class);
//        container.registerComponentImplementation(FES.ArgumentsType,ArgumentsTypeBinding.class);
//        container.registerComponentImplementation(FES.ArgumentType,ArgumentTypeBinding.class);
//        container.registerComponentImplementation(FES.AvailableFunctionsType,AvailableFunctionsTypeBinding.class);
//        container.registerComponentImplementation(FES.AvailableFunctionType,AvailableFunctionTypeBinding.class);
        container.registerComponentImplementation(FES.BBOXType,BBOXTypeBinding.class);
        container.registerComponentImplementation(FES.BinaryComparisonOpType,BinaryComparisonOpTypeBinding.class);
        container.registerComponentImplementation(FES.BinaryLogicOpType,BinaryLogicOpTypeBinding.class);
        container.registerComponentImplementation(FES.BinarySpatialOpType,BinarySpatialOpTypeBinding.class);
        container.registerComponentImplementation(FES.BinaryTemporalOpType,BinaryTemporalOpTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOperatorNameType,ComparisonOperatorNameTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOperatorsType,ComparisonOperatorsTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOperatorType,ComparisonOperatorTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOpsType,ComparisonOpsTypeBinding.class);
        container.registerComponentImplementation(FES.DistanceBufferType,DistanceBufferTypeBinding.class);
        container.registerComponentImplementation(FES.FilterType,FilterTypeBinding.class);
        container.registerComponentImplementation(FES.FunctionType,FunctionTypeBinding.class);
//        container.registerComponentImplementation(FES.GeometryOperandsType,GeometryOperandsTypeBinding.class);
//        container.registerComponentImplementation(FES.Id_CapabilitiesType,Id_CapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(FES.LiteralType,LiteralTypeBinding.class);
//        container.registerComponentImplementation(FES.LogicOpsType,LogicOpsTypeBinding.class);
//        container.registerComponentImplementation(FES.LowerBoundaryType,LowerBoundaryTypeBinding.class);
//        container.registerComponentImplementation(FES.MatchActionType,MatchActionTypeBinding.class);
        
//        container.registerComponentImplementation(FES.PropertyIsNilType,PropertyIsNilTypeBinding.class);
        
//        container.registerComponentImplementation(FES.ResourceIdentifierType,ResourceIdentifierTypeBinding.class);
        container.registerComponentImplementation(FES.ResourceIdType,ResourceIdTypeBinding.class);
//        container.registerComponentImplementation(FES.Scalar_CapabilitiesType,Scalar_CapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(FES.SchemaElement,SchemaElementBinding.class);
        container.registerComponentImplementation(FES.SortByType,SortByTypeBinding.class);
        container.registerComponentImplementation(FES.SortOrderType,SortOrderTypeBinding.class);
        container.registerComponentImplementation(FES.SortPropertyType,SortPropertyTypeBinding.class);
//        container.registerComponentImplementation(FES.Spatial_CapabilitiesType,Spatial_CapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(FES.SpatialOperatorNameType,SpatialOperatorNameTypeBinding.class);
//        container.registerComponentImplementation(FES.SpatialOperatorsType,SpatialOperatorsTypeBinding.class);
//        container.registerComponentImplementation(FES.SpatialOperatorType,SpatialOperatorTypeBinding.class);
//        container.registerComponentImplementation(FES.SpatialOpsType,SpatialOpsTypeBinding.class);
//        container.registerComponentImplementation(FES.Temporal_CapabilitiesType,Temporal_CapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(FES.TemporalOperandsType,TemporalOperandsTypeBinding.class);
//        container.registerComponentImplementation(FES.TemporalOperatorNameType,TemporalOperatorNameTypeBinding.class);
//        container.registerComponentImplementation(FES.TemporalOperatorsType,TemporalOperatorsTypeBinding.class);
//        container.registerComponentImplementation(FES.TemporalOperatorType,TemporalOperatorTypeBinding.class);
//        container.registerComponentImplementation(FES.TemporalOpsType,TemporalOpsTypeBinding.class);
//        container.registerComponentImplementation(FES.TypeNamesListType,TypeNamesListTypeBinding.class);
//        container.registerComponentImplementation(FES.TypeNamesType,TypeNamesTypeBinding.class);
//        container.registerComponentImplementation(FES.UnaryLogicOpType,UnaryLogicOpTypeBinding.class);
//        container.registerComponentImplementation(FES.UpperBoundaryType,UpperBoundaryTypeBinding.class);
//        container.registerComponentImplementation(FES.VersionActionTokens,VersionActionTokensBinding.class);
//        container.registerComponentImplementation(FES.VersionType,VersionTypeBinding.class);
//        container.registerComponentImplementation(FES._Filter_Capabilities,_Filter_CapabilitiesBinding.class);
//        container.registerComponentImplementation(FES._LogicalOperators,_LogicalOperatorsBinding.class);
//        container.registerComponentImplementation(FES.GeometryOperandsType_GeometryOperand,GeometryOperandsType_GeometryOperandBinding.class);
//        container.registerComponentImplementation(FES.TemporalOperandsType_TemporalOperand,TemporalOperandsType_TemporalOperandBinding.class);
        
        //Elements
        
        //spatial
        container.registerComponentImplementation(FES.Beyond, BeyondBinding.class);
        container.registerComponentImplementation(FES.Contains, ContainsBinding.class);
        container.registerComponentImplementation(FES.Crosses, CrossesBinding.class);
        container.registerComponentImplementation(FES.Disjoint, DisjointBinding.class);
        container.registerComponentImplementation(FES.DWithin, DWithinBinding.class);
        container.registerComponentImplementation(FES.Equals, EqualsBinding.class);
        container.registerComponentImplementation(FES.Intersects, IntersectsBinding.class);
        container.registerComponentImplementation(FES.Overlaps, OverlapsBinding.class);
        container.registerComponentImplementation(FES.Touches, TouchesBinding.class);
        container.registerComponentImplementation(FES.Within, WithinBinding.class);
        
        //temporal
        container.registerComponentImplementation(FES.After, AfterBinding.class);
        container.registerComponentImplementation(FES.AnyInteracts, AnyInteractsBinding.class);
        container.registerComponentImplementation(FES.Before, BeforeBinding.class);
        container.registerComponentImplementation(FES.Begins, BeginsBinding.class);
        container.registerComponentImplementation(FES.BegunBy, BegunByBinding.class);
        container.registerComponentImplementation(FES.During, DuringBinding.class);
        container.registerComponentImplementation(FES.EndedBy, EndedByBinding.class);
        container.registerComponentImplementation(FES.Ends, EndsBinding.class);
        container.registerComponentImplementation(FES.Meets, MeetsBinding.class);
        container.registerComponentImplementation(FES.MetBy, MetByBinding.class);
        container.registerComponentImplementation(FES.OverlappedBy, OverlappedByBinding.class);
        container.registerComponentImplementation(FES.TContains, TContainsBinding.class);
        container.registerComponentImplementation(FES.TEquals, TEqualsBinding.class);
        container.registerComponentImplementation(FES.TOverlaps, TOverlapsBinding.class);

        //comparison
        container.registerComponentImplementation(FES.PropertyIsEqualTo, PropertyIsEqualToBinding.class);
        container.registerComponentImplementation(FES.PropertyIsGreaterThan, PropertyIsGreaterThanBinding.class);
        container.registerComponentImplementation(FES.PropertyIsGreaterThanOrEqualTo, PropertyIsGreaterThanOrEqualToBinding.class);
        container.registerComponentImplementation(FES.PropertyIsLessThan, PropertyIsLessThanBinding.class);
        container.registerComponentImplementation(FES.PropertyIsLessThanOrEqualTo, PropertyIsLessThanOrEqualToBinding.class);
        container.registerComponentImplementation(FES.PropertyIsNotEqualTo, PropertyIsNotEqualToBinding.class);
        container.registerComponentImplementation(FES.PropertyIsBetweenType,PropertyIsBetweenTypeBinding.class);
        container.registerComponentImplementation(FES.PropertyIsLikeType,PropertyIsLikeTypeBinding.class);
        container.registerComponentImplementation(FES.PropertyIsNullType,PropertyIsNullTypeBinding.class);
        
        //logical
        container.registerComponentImplementation(FES.And, AndBinding.class);
        container.registerComponentImplementation(FES.Or, OrBinding.class);
        container.registerComponentImplementation(FES.Not, NotBinding.class);
        
        container.registerComponentImplementation(FES.Literal, LiteralBinding.class);
        container.registerComponentImplementation(FES.ValueReference, ValueReferenceBinding.class);
    }
    
    @Override
    protected void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentImplementation(FilterFactory.class, FilterFactoryImpl.class);
    }
} 