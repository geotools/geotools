package org.geotools.filter.v2_0;

import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.GML;
import org.geotools.ows.v1_1.OWS;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/fes/2.0 schema.
 *
 * @generated
 */
public final class FES extends XSD {

    /** singleton instance */
    private static final FES instance = new FES();
    
    /**
     * Returns the singleton instance.
     */
    public static final FES getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private FES() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add(OWS.getInstance());
        dependencies.add(GML.getInstance());
    }
    
    /**
     * Returns 'http://www.opengis.net/fes/2.0'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'filterAll.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("filterAll.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/fes/2.0";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractAdhocQueryExpressionType = 
        new QName("http://www.opengis.net/fes/2.0","AbstractAdhocQueryExpressionType");
    /** @generated */
    public static final QName AbstractIdType = 
        new QName("http://www.opengis.net/fes/2.0","AbstractIdType");
    /** @generated */
    public static final QName AbstractProjectionClauseType = 
        new QName("http://www.opengis.net/fes/2.0","AbstractProjectionClauseType");
    /** @generated */
    public static final QName AbstractQueryExpressionType = 
        new QName("http://www.opengis.net/fes/2.0","AbstractQueryExpressionType");
    /** @generated */
    public static final QName AbstractSelectionClauseType = 
        new QName("http://www.opengis.net/fes/2.0","AbstractSelectionClauseType");
    /** @generated */
    public static final QName AbstractSortingClauseType = 
        new QName("http://www.opengis.net/fes/2.0","AbstractSortingClauseType");
    /** @generated */
    public static final QName AdditionalOperatorsType = 
        new QName("http://www.opengis.net/fes/2.0","AdditionalOperatorsType");
    /** @generated */
    public static final QName AliasesType = 
        new QName("http://www.opengis.net/fes/2.0","AliasesType");
    /** @generated */
    public static final QName ArgumentsType = 
        new QName("http://www.opengis.net/fes/2.0","ArgumentsType");
    /** @generated */
    public static final QName ArgumentType = 
        new QName("http://www.opengis.net/fes/2.0","ArgumentType");
    /** @generated */
    public static final QName AvailableFunctionsType = 
        new QName("http://www.opengis.net/fes/2.0","AvailableFunctionsType");
    /** @generated */
    public static final QName AvailableFunctionType = 
        new QName("http://www.opengis.net/fes/2.0","AvailableFunctionType");
    /** @generated */
    public static final QName BBOXType = 
        new QName("http://www.opengis.net/fes/2.0","BBOXType");
    /** @generated */
    public static final QName BinaryComparisonOpType = 
        new QName("http://www.opengis.net/fes/2.0","BinaryComparisonOpType");
    /** @generated */
    public static final QName BinaryLogicOpType = 
        new QName("http://www.opengis.net/fes/2.0","BinaryLogicOpType");
    /** @generated */
    public static final QName BinarySpatialOpType = 
        new QName("http://www.opengis.net/fes/2.0","BinarySpatialOpType");
    /** @generated */
    public static final QName BinaryTemporalOpType = 
        new QName("http://www.opengis.net/fes/2.0","BinaryTemporalOpType");
    /** @generated */
    public static final QName ComparisonOperatorNameType = 
        new QName("http://www.opengis.net/fes/2.0","ComparisonOperatorNameType");
    /** @generated */
    public static final QName ComparisonOperatorsType = 
        new QName("http://www.opengis.net/fes/2.0","ComparisonOperatorsType");
    /** @generated */
    public static final QName ComparisonOperatorType = 
        new QName("http://www.opengis.net/fes/2.0","ComparisonOperatorType");
    /** @generated */
    public static final QName ComparisonOpsType = 
        new QName("http://www.opengis.net/fes/2.0","ComparisonOpsType");
    /** @generated */
    public static final QName ConformanceType = 
        new QName("http://www.opengis.net/fes/2.0","ConformanceType");
    /** @generated */
    public static final QName DistanceBufferType = 
        new QName("http://www.opengis.net/fes/2.0","DistanceBufferType");
    /** @generated */
    public static final QName Extended_CapabilitiesType = 
        new QName("http://www.opengis.net/fes/2.0","Extended_CapabilitiesType");
    /** @generated */
    public static final QName ExtensionOperatorType = 
        new QName("http://www.opengis.net/fes/2.0","ExtensionOperatorType");
    /** @generated */
    public static final QName ExtensionOpsType = 
        new QName("http://www.opengis.net/fes/2.0","ExtensionOpsType");
    /** @generated */
    public static final QName FilterType = 
        new QName("http://www.opengis.net/fes/2.0","FilterType");
    /** @generated */
    public static final QName FunctionType = 
        new QName("http://www.opengis.net/fes/2.0","FunctionType");
    /** @generated */
    public static final QName GeometryOperandsType = 
        new QName("http://www.opengis.net/fes/2.0","GeometryOperandsType");
    /** @generated */
    public static final QName Id_CapabilitiesType = 
        new QName("http://www.opengis.net/fes/2.0","Id_CapabilitiesType");
    /** @generated */
    public static final QName LiteralType = 
        new QName("http://www.opengis.net/fes/2.0","LiteralType");
    /** @generated */
    public static final QName LogicOpsType = 
        new QName("http://www.opengis.net/fes/2.0","LogicOpsType");
    /** @generated */
    public static final QName LowerBoundaryType = 
        new QName("http://www.opengis.net/fes/2.0","LowerBoundaryType");
    /** @generated */
    public static final QName MatchActionType = 
        new QName("http://www.opengis.net/fes/2.0","MatchActionType");
    /** @generated */
    public static final QName MeasureType = 
        new QName("http://www.opengis.net/fes/2.0","MeasureType");
    /** @generated */
    public static final QName PropertyIsBetweenType = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsBetweenType");
    /** @generated */
    public static final QName PropertyIsLikeType = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsLikeType");
    /** @generated */
    public static final QName PropertyIsNilType = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsNilType");
    /** @generated */
    public static final QName PropertyIsNullType = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsNullType");
    /** @generated */
    public static final QName ResourceIdentifierType = 
        new QName("http://www.opengis.net/fes/2.0","ResourceIdentifierType");
    /** @generated */
    public static final QName ResourceIdType = 
        new QName("http://www.opengis.net/fes/2.0","ResourceIdType");
    /** @generated */
    public static final QName Scalar_CapabilitiesType = 
        new QName("http://www.opengis.net/fes/2.0","Scalar_CapabilitiesType");
    /** @generated */
    public static final QName SchemaElement = 
        new QName("http://www.opengis.net/fes/2.0","SchemaElement");
    /** @generated */
    public static final QName SortByType = 
        new QName("http://www.opengis.net/fes/2.0","SortByType");
    /** @generated */
    public static final QName SortOrderType = 
        new QName("http://www.opengis.net/fes/2.0","SortOrderType");
    /** @generated */
    public static final QName SortPropertyType = 
        new QName("http://www.opengis.net/fes/2.0","SortPropertyType");
    /** @generated */
    public static final QName Spatial_CapabilitiesType = 
        new QName("http://www.opengis.net/fes/2.0","Spatial_CapabilitiesType");
    /** @generated */
    public static final QName SpatialOperatorNameType = 
        new QName("http://www.opengis.net/fes/2.0","SpatialOperatorNameType");
    /** @generated */
    public static final QName SpatialOperatorsType = 
        new QName("http://www.opengis.net/fes/2.0","SpatialOperatorsType");
    /** @generated */
    public static final QName SpatialOperatorType = 
        new QName("http://www.opengis.net/fes/2.0","SpatialOperatorType");
    /** @generated */
    public static final QName SpatialOpsType = 
        new QName("http://www.opengis.net/fes/2.0","SpatialOpsType");
    /** @generated */
    public static final QName Temporal_CapabilitiesType = 
        new QName("http://www.opengis.net/fes/2.0","Temporal_CapabilitiesType");
    /** @generated */
    public static final QName TemporalOperandsType = 
        new QName("http://www.opengis.net/fes/2.0","TemporalOperandsType");
    /** @generated */
    public static final QName TemporalOperatorNameType = 
        new QName("http://www.opengis.net/fes/2.0","TemporalOperatorNameType");
    /** @generated */
    public static final QName TemporalOperatorsType = 
        new QName("http://www.opengis.net/fes/2.0","TemporalOperatorsType");
    /** @generated */
    public static final QName TemporalOperatorType = 
        new QName("http://www.opengis.net/fes/2.0","TemporalOperatorType");
    /** @generated */
    public static final QName TemporalOpsType = 
        new QName("http://www.opengis.net/fes/2.0","TemporalOpsType");
    /** @generated */
    public static final QName TypeNamesListType = 
        new QName("http://www.opengis.net/fes/2.0","TypeNamesListType");
    /** @generated */
    public static final QName TypeNamesType = 
        new QName("http://www.opengis.net/fes/2.0","TypeNamesType");
    /** @generated */
    public static final QName UnaryLogicOpType = 
        new QName("http://www.opengis.net/fes/2.0","UnaryLogicOpType");
    /** @generated */
    public static final QName UomIdentifier = 
        new QName("http://www.opengis.net/fes/2.0","UomIdentifier");
    /** @generated */
    public static final QName UomSymbol = 
        new QName("http://www.opengis.net/fes/2.0","UomSymbol");
    /** @generated */
    public static final QName UomURI = 
        new QName("http://www.opengis.net/fes/2.0","UomURI");
    /** @generated */
    public static final QName UpperBoundaryType = 
        new QName("http://www.opengis.net/fes/2.0","UpperBoundaryType");
    /** @generated */
    public static final QName VersionActionTokens = 
        new QName("http://www.opengis.net/fes/2.0","VersionActionTokens");
    /** @generated */
    public static final QName VersionType = 
        new QName("http://www.opengis.net/fes/2.0","VersionType");
    /** @generated */
    public static final QName _Filter_Capabilities = 
        new QName("http://www.opengis.net/fes/2.0","_Filter_Capabilities");
    /** @generated */
    public static final QName _LogicalOperators = 
        new QName("http://www.opengis.net/fes/2.0","_LogicalOperators");
    /** @generated */
    public static final QName GeometryOperandsType_GeometryOperand = 
        new QName("http://www.opengis.net/fes/2.0","GeometryOperandsType_GeometryOperand");
    /** @generated */
    public static final QName TemporalOperandsType_TemporalOperand = 
        new QName("http://www.opengis.net/fes/2.0","TemporalOperandsType_TemporalOperand");

    /* Elements */
    /** @generated */
    public static final QName _Id = 
        new QName("http://www.opengis.net/fes/2.0","_Id");
    /** @generated */
    public static final QName AbstractAdhocQueryExpression = 
        new QName("http://www.opengis.net/fes/2.0","AbstractAdhocQueryExpression");
    /** @generated */
    public static final QName AbstractProjectionClause = 
        new QName("http://www.opengis.net/fes/2.0","AbstractProjectionClause");
    /** @generated */
    public static final QName AbstractQueryExpression = 
        new QName("http://www.opengis.net/fes/2.0","AbstractQueryExpression");
    /** @generated */
    public static final QName AbstractSelectionClause = 
        new QName("http://www.opengis.net/fes/2.0","AbstractSelectionClause");
    /** @generated */
    public static final QName AbstractSortingClause = 
        new QName("http://www.opengis.net/fes/2.0","AbstractSortingClause");
    /** @generated */
    public static final QName After = 
        new QName("http://www.opengis.net/fes/2.0","After");
    /** @generated */
    public static final QName And = 
        new QName("http://www.opengis.net/fes/2.0","And");
    /** @generated */
    public static final QName AnyInteracts = 
        new QName("http://www.opengis.net/fes/2.0","AnyInteracts");
    /** @generated */
    public static final QName BBOX = 
        new QName("http://www.opengis.net/fes/2.0","BBOX");
    /** @generated */
    public static final QName Before = 
        new QName("http://www.opengis.net/fes/2.0","Before");
    /** @generated */
    public static final QName Begins = 
        new QName("http://www.opengis.net/fes/2.0","Begins");
    /** @generated */
    public static final QName BegunBy = 
        new QName("http://www.opengis.net/fes/2.0","BegunBy");
    /** @generated */
    public static final QName Beyond = 
        new QName("http://www.opengis.net/fes/2.0","Beyond");
    /** @generated */
    public static final QName comparisonOps = 
        new QName("http://www.opengis.net/fes/2.0","comparisonOps");
    /** @generated */
    public static final QName Contains = 
        new QName("http://www.opengis.net/fes/2.0","Contains");
    /** @generated */
    public static final QName Crosses = 
        new QName("http://www.opengis.net/fes/2.0","Crosses");
    /** @generated */
    public static final QName Disjoint = 
        new QName("http://www.opengis.net/fes/2.0","Disjoint");
    /** @generated */
    public static final QName During = 
        new QName("http://www.opengis.net/fes/2.0","During");
    /** @generated */
    public static final QName DWithin = 
        new QName("http://www.opengis.net/fes/2.0","DWithin");
    /** @generated */
    public static final QName EndedBy = 
        new QName("http://www.opengis.net/fes/2.0","EndedBy");
    /** @generated */
    public static final QName Ends = 
        new QName("http://www.opengis.net/fes/2.0","Ends");
    /** @generated */
    public static final QName Equals = 
        new QName("http://www.opengis.net/fes/2.0","Equals");
    /** @generated */
    public static final QName expression = 
        new QName("http://www.opengis.net/fes/2.0","expression");
    /** @generated */
    public static final QName extensionOps = 
        new QName("http://www.opengis.net/fes/2.0","extensionOps");
    /** @generated */
    public static final QName Filter = 
        new QName("http://www.opengis.net/fes/2.0","Filter");
    /** @generated */
    public static final QName Filter_Capabilities = 
        new QName("http://www.opengis.net/fes/2.0","Filter_Capabilities");
    /** @generated */
    public static final QName Function = 
        new QName("http://www.opengis.net/fes/2.0","Function");
    /** @generated */
    public static final QName Intersects = 
        new QName("http://www.opengis.net/fes/2.0","Intersects");
    /** @generated */
    public static final QName Literal = 
        new QName("http://www.opengis.net/fes/2.0","Literal");
    /** @generated */
    public static final QName LogicalOperators = 
        new QName("http://www.opengis.net/fes/2.0","LogicalOperators");
    /** @generated */
    public static final QName logicOps = 
        new QName("http://www.opengis.net/fes/2.0","logicOps");
    /** @generated */
    public static final QName Meets = 
        new QName("http://www.opengis.net/fes/2.0","Meets");
    /** @generated */
    public static final QName MetBy = 
        new QName("http://www.opengis.net/fes/2.0","MetBy");
    /** @generated */
    public static final QName Not = 
        new QName("http://www.opengis.net/fes/2.0","Not");
    /** @generated */
    public static final QName Or = 
        new QName("http://www.opengis.net/fes/2.0","Or");
    /** @generated */
    public static final QName OverlappedBy = 
        new QName("http://www.opengis.net/fes/2.0","OverlappedBy");
    /** @generated */
    public static final QName Overlaps = 
        new QName("http://www.opengis.net/fes/2.0","Overlaps");
    /** @generated */
    public static final QName PropertyIsBetween = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsBetween");
    /** @generated */
    public static final QName PropertyIsEqualTo = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsEqualTo");
    /** @generated */
    public static final QName PropertyIsGreaterThan = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsGreaterThan");
    /** @generated */
    public static final QName PropertyIsGreaterThanOrEqualTo = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsGreaterThanOrEqualTo");
    /** @generated */
    public static final QName PropertyIsLessThan = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsLessThan");
    /** @generated */
    public static final QName PropertyIsLessThanOrEqualTo = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsLessThanOrEqualTo");
    /** @generated */
    public static final QName PropertyIsLike = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsLike");
    /** @generated */
    public static final QName PropertyIsNil = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsNil");
    /** @generated */
    public static final QName PropertyIsNotEqualTo = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsNotEqualTo");
    /** @generated */
    public static final QName PropertyIsNull = 
        new QName("http://www.opengis.net/fes/2.0","PropertyIsNull");
    /** @generated */
    public static final QName ResourceId = 
        new QName("http://www.opengis.net/fes/2.0","ResourceId");
    /** @generated */
    public static final QName SortBy = 
        new QName("http://www.opengis.net/fes/2.0","SortBy");
    /** @generated */
    public static final QName spatialOps = 
        new QName("http://www.opengis.net/fes/2.0","spatialOps");
    /** @generated */
    public static final QName TContains = 
        new QName("http://www.opengis.net/fes/2.0","TContains");
    /** @generated */
    public static final QName temporalOps = 
        new QName("http://www.opengis.net/fes/2.0","temporalOps");
    /** @generated */
    public static final QName TEquals = 
        new QName("http://www.opengis.net/fes/2.0","TEquals");
    /** @generated */
    public static final QName Touches = 
        new QName("http://www.opengis.net/fes/2.0","Touches");
    /** @generated */
    public static final QName TOverlaps = 
        new QName("http://www.opengis.net/fes/2.0","TOverlaps");
    /** @generated */
    public static final QName ValueReference = 
        new QName("http://www.opengis.net/fes/2.0","ValueReference");
    /** @generated */
    public static final QName Within = 
        new QName("http://www.opengis.net/fes/2.0","Within");

    /* Attributes */

}
    