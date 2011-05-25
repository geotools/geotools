package org.geotools.filter.v2_0;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;	
import org.geotools.gml3.v3_2.GMLConfiguration;
import org.geotools.ows.v1_1.OWSConfiguration;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/fes/2.0 schema.
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-fes/src/main/java/org/geotools/filter/v2_0/FESConfiguration.java $
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
//        container.registerComponentImplementation(FES.BBOXType,BBOXTypeBinding.class);
//        container.registerComponentImplementation(FES.BinaryComparisonOpType,BinaryComparisonOpTypeBinding.class);
//        container.registerComponentImplementation(FES.BinaryLogicOpType,BinaryLogicOpTypeBinding.class);
//        container.registerComponentImplementation(FES.BinarySpatialOpType,BinarySpatialOpTypeBinding.class);
//        container.registerComponentImplementation(FES.BinaryTemporalOpType,BinaryTemporalOpTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOperatorNameType,ComparisonOperatorNameTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOperatorsType,ComparisonOperatorsTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOperatorType,ComparisonOperatorTypeBinding.class);
//        container.registerComponentImplementation(FES.ComparisonOpsType,ComparisonOpsTypeBinding.class);
//        container.registerComponentImplementation(FES.DistanceBufferType,DistanceBufferTypeBinding.class);
//        container.registerComponentImplementation(FES.FilterType,FilterTypeBinding.class);
//        container.registerComponentImplementation(FES.FunctionType,FunctionTypeBinding.class);
//        container.registerComponentImplementation(FES.GeometryOperandsType,GeometryOperandsTypeBinding.class);
//        container.registerComponentImplementation(FES.Id_CapabilitiesType,Id_CapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(FES.LiteralType,LiteralTypeBinding.class);
//        container.registerComponentImplementation(FES.LogicOpsType,LogicOpsTypeBinding.class);
//        container.registerComponentImplementation(FES.LowerBoundaryType,LowerBoundaryTypeBinding.class);
//        container.registerComponentImplementation(FES.MatchActionType,MatchActionTypeBinding.class);
//        container.registerComponentImplementation(FES.PropertyIsBetweenType,PropertyIsBetweenTypeBinding.class);
//        container.registerComponentImplementation(FES.PropertyIsLikeType,PropertyIsLikeTypeBinding.class);
//        container.registerComponentImplementation(FES.PropertyIsNilType,PropertyIsNilTypeBinding.class);
//        container.registerComponentImplementation(FES.PropertyIsNullType,PropertyIsNullTypeBinding.class);
//        container.registerComponentImplementation(FES.ResourceIdentifierType,ResourceIdentifierTypeBinding.class);
//        container.registerComponentImplementation(FES.ResourceIdType,ResourceIdTypeBinding.class);
//        container.registerComponentImplementation(FES.Scalar_CapabilitiesType,Scalar_CapabilitiesTypeBinding.class);
//        container.registerComponentImplementation(FES.SchemaElement,SchemaElementBinding.class);
//        container.registerComponentImplementation(FES.SortByType,SortByTypeBinding.class);
//        container.registerComponentImplementation(FES.SortOrderType,SortOrderTypeBinding.class);
//        container.registerComponentImplementation(FES.SortPropertyType,SortPropertyTypeBinding.class);
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
    }
} 
