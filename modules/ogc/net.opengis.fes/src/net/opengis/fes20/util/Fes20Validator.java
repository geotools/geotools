/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.util;

import java.math.BigInteger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.fes20.*;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
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
 * @see net.opengis.fes20.Fes20Package
 * @generated
 */
public class Fes20Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final Fes20Validator INSTANCE = new Fes20Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.fes20";

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
    public Fes20Validator() {
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
      return Fes20Package.eINSTANCE;
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
            case Fes20Package.ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE:
                return validateAbstractAdhocQueryExpressionType((AbstractAdhocQueryExpressionType)value, diagnostics, context);
            case Fes20Package.ABSTRACT_ID_TYPE:
                return validateAbstractIdType((AbstractIdType)value, diagnostics, context);
            case Fes20Package.ABSTRACT_PROJECTION_CLAUSE_TYPE:
                return validateAbstractProjectionClauseType((AbstractProjectionClauseType)value, diagnostics, context);
            case Fes20Package.ABSTRACT_QUERY_EXPRESSION_TYPE:
                return validateAbstractQueryExpressionType((AbstractQueryExpressionType)value, diagnostics, context);
            case Fes20Package.ABSTRACT_SELECTION_CLAUSE_TYPE:
                return validateAbstractSelectionClauseType((AbstractSelectionClauseType)value, diagnostics, context);
            case Fes20Package.ABSTRACT_SORTING_CLAUSE_TYPE:
                return validateAbstractSortingClauseType((AbstractSortingClauseType)value, diagnostics, context);
            case Fes20Package.ADDITIONAL_OPERATORS_TYPE:
                return validateAdditionalOperatorsType((AdditionalOperatorsType)value, diagnostics, context);
            case Fes20Package.ARGUMENTS_TYPE:
                return validateArgumentsType((ArgumentsType)value, diagnostics, context);
            case Fes20Package.ARGUMENT_TYPE:
                return validateArgumentType((ArgumentType)value, diagnostics, context);
            case Fes20Package.AVAILABLE_FUNCTIONS_TYPE:
                return validateAvailableFunctionsType((AvailableFunctionsType)value, diagnostics, context);
            case Fes20Package.AVAILABLE_FUNCTION_TYPE:
                return validateAvailableFunctionType((AvailableFunctionType)value, diagnostics, context);
            case Fes20Package.BBOX_TYPE:
                return validateBBOXType((BBOXType)value, diagnostics, context);
            case Fes20Package.BINARY_COMPARISON_OP_TYPE:
                return validateBinaryComparisonOpType((BinaryComparisonOpType)value, diagnostics, context);
            case Fes20Package.BINARY_LOGIC_OP_TYPE:
                return validateBinaryLogicOpType((BinaryLogicOpType)value, diagnostics, context);
            case Fes20Package.BINARY_SPATIAL_OP_TYPE:
                return validateBinarySpatialOpType((BinarySpatialOpType)value, diagnostics, context);
            case Fes20Package.BINARY_TEMPORAL_OP_TYPE:
                return validateBinaryTemporalOpType((BinaryTemporalOpType)value, diagnostics, context);
            case Fes20Package.COMPARISON_OPERATORS_TYPE:
                return validateComparisonOperatorsType((ComparisonOperatorsType)value, diagnostics, context);
            case Fes20Package.COMPARISON_OPERATOR_TYPE:
                return validateComparisonOperatorType((ComparisonOperatorType)value, diagnostics, context);
            case Fes20Package.COMPARISON_OPS_TYPE:
                return validateComparisonOpsType((ComparisonOpsType)value, diagnostics, context);
            case Fes20Package.CONFORMANCE_TYPE:
                return validateConformanceType((ConformanceType)value, diagnostics, context);
            case Fes20Package.DISTANCE_BUFFER_TYPE:
                return validateDistanceBufferType((DistanceBufferType)value, diagnostics, context);
            case Fes20Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case Fes20Package.EXTENDED_CAPABILITIES_TYPE:
                return validateExtendedCapabilitiesType((ExtendedCapabilitiesType)value, diagnostics, context);
            case Fes20Package.EXTENSION_OPERATOR_TYPE:
                return validateExtensionOperatorType((ExtensionOperatorType)value, diagnostics, context);
            case Fes20Package.EXTENSION_OPS_TYPE:
                return validateExtensionOpsType((ExtensionOpsType)value, diagnostics, context);
            case Fes20Package.FILTER_CAPABILITIES_TYPE:
                return validateFilterCapabilitiesType((FilterCapabilitiesType)value, diagnostics, context);
            case Fes20Package.FILTER_TYPE:
                return validateFilterType((FilterType)value, diagnostics, context);
            case Fes20Package.FUNCTION_TYPE:
                return validateFunctionType((FunctionType)value, diagnostics, context);
            case Fes20Package.GEOMETRY_OPERANDS_TYPE:
                return validateGeometryOperandsType((GeometryOperandsType)value, diagnostics, context);
            case Fes20Package.GEOMETRY_OPERAND_TYPE:
                return validateGeometryOperandType((GeometryOperandType)value, diagnostics, context);
            case Fes20Package.ID_CAPABILITIES_TYPE:
                return validateIdCapabilitiesType((IdCapabilitiesType)value, diagnostics, context);
            case Fes20Package.LITERAL_TYPE:
                return validateLiteralType((LiteralType)value, diagnostics, context);
            case Fes20Package.LOGICAL_OPERATORS_TYPE:
                return validateLogicalOperatorsType((LogicalOperatorsType)value, diagnostics, context);
            case Fes20Package.LOGIC_OPS_TYPE:
                return validateLogicOpsType((LogicOpsType)value, diagnostics, context);
            case Fes20Package.LOWER_BOUNDARY_TYPE:
                return validateLowerBoundaryType((LowerBoundaryType)value, diagnostics, context);
            case Fes20Package.MEASURE_TYPE:
                return validateMeasureType((MeasureType)value, diagnostics, context);
            case Fes20Package.PROPERTY_IS_BETWEEN_TYPE:
                return validatePropertyIsBetweenType((PropertyIsBetweenType)value, diagnostics, context);
            case Fes20Package.PROPERTY_IS_LIKE_TYPE:
                return validatePropertyIsLikeType((PropertyIsLikeType)value, diagnostics, context);
            case Fes20Package.PROPERTY_IS_NIL_TYPE:
                return validatePropertyIsNilType((PropertyIsNilType)value, diagnostics, context);
            case Fes20Package.PROPERTY_IS_NULL_TYPE:
                return validatePropertyIsNullType((PropertyIsNullType)value, diagnostics, context);
            case Fes20Package.RESOURCE_IDENTIFIER_TYPE:
                return validateResourceIdentifierType((ResourceIdentifierType)value, diagnostics, context);
            case Fes20Package.RESOURCE_ID_TYPE:
                return validateResourceIdType((ResourceIdType)value, diagnostics, context);
            case Fes20Package.SCALAR_CAPABILITIES_TYPE:
                return validateScalarCapabilitiesType((ScalarCapabilitiesType)value, diagnostics, context);
            case Fes20Package.SORT_BY_TYPE:
                return validateSortByType((SortByType)value, diagnostics, context);
            case Fes20Package.SORT_PROPERTY_TYPE:
                return validateSortPropertyType((SortPropertyType)value, diagnostics, context);
            case Fes20Package.SPATIAL_CAPABILITIES_TYPE:
                return validateSpatialCapabilitiesType((SpatialCapabilitiesType)value, diagnostics, context);
            case Fes20Package.SPATIAL_OPERATORS_TYPE:
                return validateSpatialOperatorsType((SpatialOperatorsType)value, diagnostics, context);
            case Fes20Package.SPATIAL_OPERATOR_TYPE:
                return validateSpatialOperatorType((SpatialOperatorType)value, diagnostics, context);
            case Fes20Package.SPATIAL_OPS_TYPE:
                return validateSpatialOpsType((SpatialOpsType)value, diagnostics, context);
            case Fes20Package.TEMPORAL_CAPABILITIES_TYPE:
                return validateTemporalCapabilitiesType((TemporalCapabilitiesType)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERANDS_TYPE:
                return validateTemporalOperandsType((TemporalOperandsType)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERAND_TYPE:
                return validateTemporalOperandType((TemporalOperandType)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERATORS_TYPE:
                return validateTemporalOperatorsType((TemporalOperatorsType)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERATOR_TYPE:
                return validateTemporalOperatorType((TemporalOperatorType)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPS_TYPE:
                return validateTemporalOpsType((TemporalOpsType)value, diagnostics, context);
            case Fes20Package.UNARY_LOGIC_OP_TYPE:
                return validateUnaryLogicOpType((UnaryLogicOpType)value, diagnostics, context);
            case Fes20Package.UPPER_BOUNDARY_TYPE:
                return validateUpperBoundaryType((UpperBoundaryType)value, diagnostics, context);
            case Fes20Package.COMPARISON_OPERATOR_NAME_TYPE_MEMBER0:
                return validateComparisonOperatorNameTypeMember0((ComparisonOperatorNameTypeMember0)value, diagnostics, context);
            case Fes20Package.MATCH_ACTION_TYPE:
                return validateMatchActionType((MatchActionType)value, diagnostics, context);
            case Fes20Package.SORT_ORDER_TYPE:
                return validateSortOrderType((SortOrderType)value, diagnostics, context);
            case Fes20Package.SPATIAL_OPERATOR_NAME_TYPE_MEMBER0:
                return validateSpatialOperatorNameTypeMember0((SpatialOperatorNameTypeMember0)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0:
                return validateTemporalOperatorNameTypeMember0((TemporalOperatorNameTypeMember0)value, diagnostics, context);
            case Fes20Package.VERSION_ACTION_TOKENS:
                return validateVersionActionTokens((VersionActionTokens)value, diagnostics, context);
            case Fes20Package.ALIASES_TYPE:
                return validateAliasesType((List<?>)value, diagnostics, context);
            case Fes20Package.COMPARISON_OPERATOR_NAME_TYPE:
                return validateComparisonOperatorNameType(value, diagnostics, context);
            case Fes20Package.COMPARISON_OPERATOR_NAME_TYPE_MEMBER0_OBJECT:
                return validateComparisonOperatorNameTypeMember0Object((ComparisonOperatorNameTypeMember0)value, diagnostics, context);
            case Fes20Package.COMPARISON_OPERATOR_NAME_TYPE_MEMBER1:
                return validateComparisonOperatorNameTypeMember1((String)value, diagnostics, context);
            case Fes20Package.MATCH_ACTION_TYPE_OBJECT:
                return validateMatchActionTypeObject((MatchActionType)value, diagnostics, context);
            case Fes20Package.SCHEMA_ELEMENT:
                return validateSchemaElement((String)value, diagnostics, context);
            case Fes20Package.SORT_ORDER_TYPE_OBJECT:
                return validateSortOrderTypeObject((SortOrderType)value, diagnostics, context);
            case Fes20Package.SPATIAL_OPERATOR_NAME_TYPE:
                return validateSpatialOperatorNameType(value, diagnostics, context);
            case Fes20Package.SPATIAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT:
                return validateSpatialOperatorNameTypeMember0Object((SpatialOperatorNameTypeMember0)value, diagnostics, context);
            case Fes20Package.SPATIAL_OPERATOR_NAME_TYPE_MEMBER1:
                return validateSpatialOperatorNameTypeMember1((String)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERATOR_NAME_TYPE:
                return validateTemporalOperatorNameType(value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT:
                return validateTemporalOperatorNameTypeMember0Object((TemporalOperatorNameTypeMember0)value, diagnostics, context);
            case Fes20Package.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1:
                return validateTemporalOperatorNameTypeMember1((String)value, diagnostics, context);
            case Fes20Package.TYPE_NAMES_LIST_TYPE:
                return validateTypeNamesListType((List<?>)value, diagnostics, context);
            case Fes20Package.TYPE_NAMES_TYPE:
                return validateTypeNamesType(value, diagnostics, context);
            case Fes20Package.UOM_IDENTIFIER:
                return validateUomIdentifier((String)value, diagnostics, context);
            case Fes20Package.UOM_SYMBOL:
                return validateUomSymbol((String)value, diagnostics, context);
            case Fes20Package.UOM_URI:
                return validateUomURI((String)value, diagnostics, context);
            case Fes20Package.VERSION_ACTION_TOKENS_OBJECT:
                return validateVersionActionTokensObject((VersionActionTokens)value, diagnostics, context);
            case Fes20Package.VERSION_TYPE:
                return validateVersionType(value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractAdhocQueryExpressionType(AbstractAdhocQueryExpressionType abstractAdhocQueryExpressionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractAdhocQueryExpressionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractIdType(AbstractIdType abstractIdType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractIdType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractProjectionClauseType(AbstractProjectionClauseType abstractProjectionClauseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractProjectionClauseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractQueryExpressionType(AbstractQueryExpressionType abstractQueryExpressionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractQueryExpressionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractSelectionClauseType(AbstractSelectionClauseType abstractSelectionClauseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractSelectionClauseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractSortingClauseType(AbstractSortingClauseType abstractSortingClauseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractSortingClauseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditionalOperatorsType(AdditionalOperatorsType additionalOperatorsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(additionalOperatorsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArgumentsType(ArgumentsType argumentsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(argumentsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArgumentType(ArgumentType argumentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(argumentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAvailableFunctionsType(AvailableFunctionsType availableFunctionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(availableFunctionsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAvailableFunctionType(AvailableFunctionType availableFunctionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(availableFunctionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBBOXType(BBOXType bboxType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(bboxType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBinaryComparisonOpType(BinaryComparisonOpType binaryComparisonOpType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(binaryComparisonOpType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBinaryLogicOpType(BinaryLogicOpType binaryLogicOpType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(binaryLogicOpType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBinarySpatialOpType(BinarySpatialOpType binarySpatialOpType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(binarySpatialOpType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBinaryTemporalOpType(BinaryTemporalOpType binaryTemporalOpType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(binaryTemporalOpType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOperatorsType(ComparisonOperatorsType comparisonOperatorsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(comparisonOperatorsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOperatorType(ComparisonOperatorType comparisonOperatorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(comparisonOperatorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOpsType(ComparisonOpsType comparisonOpsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(comparisonOpsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateConformanceType(ConformanceType conformanceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(conformanceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDistanceBufferType(DistanceBufferType distanceBufferType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(distanceBufferType, diagnostics, context);
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
    public boolean validateExtendedCapabilitiesType(ExtendedCapabilitiesType extendedCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(extendedCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExtensionOperatorType(ExtensionOperatorType extensionOperatorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(extensionOperatorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExtensionOpsType(ExtensionOpsType extensionOpsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(extensionOpsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFilterCapabilitiesType(FilterCapabilitiesType filterCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(filterCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFilterType(FilterType filterType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(filterType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFunctionType(FunctionType functionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(functionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometryOperandsType(GeometryOperandsType geometryOperandsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometryOperandsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGeometryOperandType(GeometryOperandType geometryOperandType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(geometryOperandType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIdCapabilitiesType(IdCapabilitiesType idCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(idCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLiteralType(LiteralType literalType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(literalType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLogicalOperatorsType(LogicalOperatorsType logicalOperatorsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(logicalOperatorsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLogicOpsType(LogicOpsType logicOpsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(logicOpsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLowerBoundaryType(LowerBoundaryType lowerBoundaryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lowerBoundaryType, diagnostics, context);
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
    public boolean validatePropertyIsBetweenType(PropertyIsBetweenType propertyIsBetweenType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(propertyIsBetweenType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePropertyIsLikeType(PropertyIsLikeType propertyIsLikeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(propertyIsLikeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePropertyIsNilType(PropertyIsNilType propertyIsNilType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(propertyIsNilType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePropertyIsNullType(PropertyIsNullType propertyIsNullType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(propertyIsNullType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResourceIdentifierType(ResourceIdentifierType resourceIdentifierType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(resourceIdentifierType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResourceIdType(ResourceIdType resourceIdType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(resourceIdType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateScalarCapabilitiesType(ScalarCapabilitiesType scalarCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(scalarCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSortByType(SortByType sortByType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(sortByType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSortPropertyType(SortPropertyType sortPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(sortPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialCapabilitiesType(SpatialCapabilitiesType spatialCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(spatialCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorsType(SpatialOperatorsType spatialOperatorsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(spatialOperatorsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorType(SpatialOperatorType spatialOperatorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(spatialOperatorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOpsType(SpatialOpsType spatialOpsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(spatialOpsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalCapabilitiesType(TemporalCapabilitiesType temporalCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperandsType(TemporalOperandsType temporalOperandsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalOperandsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperandType(TemporalOperandType temporalOperandType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalOperandType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorsType(TemporalOperatorsType temporalOperatorsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalOperatorsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorType(TemporalOperatorType temporalOperatorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalOperatorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOpsType(TemporalOpsType temporalOpsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(temporalOpsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnaryLogicOpType(UnaryLogicOpType unaryLogicOpType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(unaryLogicOpType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUpperBoundaryType(UpperBoundaryType upperBoundaryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(upperBoundaryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOperatorNameTypeMember0(ComparisonOperatorNameTypeMember0 comparisonOperatorNameTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMatchActionType(MatchActionType matchActionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSortOrderType(SortOrderType sortOrderType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorNameTypeMember0(SpatialOperatorNameTypeMember0 spatialOperatorNameTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorNameTypeMember0(TemporalOperatorNameTypeMember0 temporalOperatorNameTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionActionTokens(VersionActionTokens versionActionTokens, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAliasesType(List<?> aliasesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateAliasesType_ItemType(aliasesType, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Aliases Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAliasesType_ItemType(List<?> aliasesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = aliasesType.iterator(); i.hasNext() && (result || diagnostics != null); ) {
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
    public boolean validateComparisonOperatorNameType(Object comparisonOperatorNameType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateComparisonOperatorNameType_MemberTypes(comparisonOperatorNameType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Comparison Operator Name Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOperatorNameType_MemberTypes(Object comparisonOperatorNameType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Fes20Package.Literals.COMPARISON_OPERATOR_NAME_TYPE_MEMBER0.isInstance(comparisonOperatorNameType)) {
                if (validateComparisonOperatorNameTypeMember0((ComparisonOperatorNameTypeMember0)comparisonOperatorNameType, tempDiagnostics, context)) return true;
            }
            if (Fes20Package.Literals.COMPARISON_OPERATOR_NAME_TYPE_MEMBER1.isInstance(comparisonOperatorNameType)) {
                if (validateComparisonOperatorNameTypeMember1((String)comparisonOperatorNameType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Fes20Package.Literals.COMPARISON_OPERATOR_NAME_TYPE_MEMBER0.isInstance(comparisonOperatorNameType)) {
                if (validateComparisonOperatorNameTypeMember0((ComparisonOperatorNameTypeMember0)comparisonOperatorNameType, null, context)) return true;
            }
            if (Fes20Package.Literals.COMPARISON_OPERATOR_NAME_TYPE_MEMBER1.isInstance(comparisonOperatorNameType)) {
                if (validateComparisonOperatorNameTypeMember1((String)comparisonOperatorNameType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOperatorNameTypeMember0Object(ComparisonOperatorNameTypeMember0 comparisonOperatorNameTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOperatorNameTypeMember1(String comparisonOperatorNameTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateComparisonOperatorNameTypeMember1_Pattern(comparisonOperatorNameTypeMember1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateComparisonOperatorNameTypeMember1_Pattern
     */
    public static final  PatternMatcher [][] COMPARISON_OPERATOR_NAME_TYPE_MEMBER1__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("extension:\\w{2,}")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Comparison Operator Name Type Member1</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateComparisonOperatorNameTypeMember1_Pattern(String comparisonOperatorNameTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Fes20Package.Literals.COMPARISON_OPERATOR_NAME_TYPE_MEMBER1, comparisonOperatorNameTypeMember1, COMPARISON_OPERATOR_NAME_TYPE_MEMBER1__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMatchActionTypeObject(MatchActionType matchActionTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSchemaElement(String schemaElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateSchemaElement_Pattern(schemaElement, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateSchemaElement_Pattern
     */
    public static final  PatternMatcher [][] SCHEMA_ELEMENT__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("schema\\-element\\(.+\\)")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Schema Element</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSchemaElement_Pattern(String schemaElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Fes20Package.Literals.SCHEMA_ELEMENT, schemaElement, SCHEMA_ELEMENT__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSortOrderTypeObject(SortOrderType sortOrderTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorNameType(Object spatialOperatorNameType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateSpatialOperatorNameType_MemberTypes(spatialOperatorNameType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Spatial Operator Name Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorNameType_MemberTypes(Object spatialOperatorNameType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Fes20Package.Literals.SPATIAL_OPERATOR_NAME_TYPE_MEMBER0.isInstance(spatialOperatorNameType)) {
                if (validateSpatialOperatorNameTypeMember0((SpatialOperatorNameTypeMember0)spatialOperatorNameType, tempDiagnostics, context)) return true;
            }
            if (Fes20Package.Literals.SPATIAL_OPERATOR_NAME_TYPE_MEMBER1.isInstance(spatialOperatorNameType)) {
                if (validateSpatialOperatorNameTypeMember1((String)spatialOperatorNameType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Fes20Package.Literals.SPATIAL_OPERATOR_NAME_TYPE_MEMBER0.isInstance(spatialOperatorNameType)) {
                if (validateSpatialOperatorNameTypeMember0((SpatialOperatorNameTypeMember0)spatialOperatorNameType, null, context)) return true;
            }
            if (Fes20Package.Literals.SPATIAL_OPERATOR_NAME_TYPE_MEMBER1.isInstance(spatialOperatorNameType)) {
                if (validateSpatialOperatorNameTypeMember1((String)spatialOperatorNameType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorNameTypeMember0Object(SpatialOperatorNameTypeMember0 spatialOperatorNameTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorNameTypeMember1(String spatialOperatorNameTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateSpatialOperatorNameTypeMember1_Pattern(spatialOperatorNameTypeMember1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateSpatialOperatorNameTypeMember1_Pattern
     */
    public static final  PatternMatcher [][] SPATIAL_OPERATOR_NAME_TYPE_MEMBER1__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("extension:\\w{2,}")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Spatial Operator Name Type Member1</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSpatialOperatorNameTypeMember1_Pattern(String spatialOperatorNameTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Fes20Package.Literals.SPATIAL_OPERATOR_NAME_TYPE_MEMBER1, spatialOperatorNameTypeMember1, SPATIAL_OPERATOR_NAME_TYPE_MEMBER1__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorNameType(Object temporalOperatorNameType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTemporalOperatorNameType_MemberTypes(temporalOperatorNameType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Temporal Operator Name Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorNameType_MemberTypes(Object temporalOperatorNameType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Fes20Package.Literals.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0.isInstance(temporalOperatorNameType)) {
                if (validateTemporalOperatorNameTypeMember0((TemporalOperatorNameTypeMember0)temporalOperatorNameType, tempDiagnostics, context)) return true;
            }
            if (Fes20Package.Literals.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1.isInstance(temporalOperatorNameType)) {
                if (validateTemporalOperatorNameTypeMember1((String)temporalOperatorNameType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Fes20Package.Literals.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0.isInstance(temporalOperatorNameType)) {
                if (validateTemporalOperatorNameTypeMember0((TemporalOperatorNameTypeMember0)temporalOperatorNameType, null, context)) return true;
            }
            if (Fes20Package.Literals.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1.isInstance(temporalOperatorNameType)) {
                if (validateTemporalOperatorNameTypeMember1((String)temporalOperatorNameType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorNameTypeMember0Object(TemporalOperatorNameTypeMember0 temporalOperatorNameTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorNameTypeMember1(String temporalOperatorNameTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTemporalOperatorNameTypeMember1_Pattern(temporalOperatorNameTypeMember1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateTemporalOperatorNameTypeMember1_Pattern
     */
    public static final  PatternMatcher [][] TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("extension:\\w{2,}")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Temporal Operator Name Type Member1</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemporalOperatorNameTypeMember1_Pattern(String temporalOperatorNameTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Fes20Package.Literals.TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1, temporalOperatorNameTypeMember1, TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeNamesListType(List<?> typeNamesListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTypeNamesListType_ItemType(typeNamesListType, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Type Names List Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeNamesListType_ItemType(List<?> typeNamesListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = typeNamesListType.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (Fes20Package.Literals.TYPE_NAMES_TYPE.isInstance(item)) {
                result &= validateTypeNamesType(item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(Fes20Package.Literals.TYPE_NAMES_TYPE, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeNamesType(Object typeNamesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTypeNamesType_MemberTypes(typeNamesType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Type Names Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeNamesType_MemberTypes(Object typeNamesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Fes20Package.Literals.SCHEMA_ELEMENT.isInstance(typeNamesType)) {
                if (validateSchemaElement((String)typeNamesType, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.QNAME.isInstance(typeNamesType)) {
                if (xmlTypeValidator.validateQName(typeNamesType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Fes20Package.Literals.SCHEMA_ELEMENT.isInstance(typeNamesType)) {
                if (validateSchemaElement((String)typeNamesType, null, context)) return true;
            }
            if (XMLTypePackage.Literals.QNAME.isInstance(typeNamesType)) {
                if (xmlTypeValidator.validateQName(typeNamesType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUomIdentifier(String uomIdentifier, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateUomIdentifier_MemberTypes(uomIdentifier, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Uom Identifier</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUomIdentifier_MemberTypes(String uomIdentifier, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Fes20Package.Literals.UOM_SYMBOL.isInstance(uomIdentifier)) {
                if (validateUomSymbol(uomIdentifier, tempDiagnostics, context)) return true;
            }
            if (Fes20Package.Literals.UOM_URI.isInstance(uomIdentifier)) {
                if (validateUomURI(uomIdentifier, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Fes20Package.Literals.UOM_SYMBOL.isInstance(uomIdentifier)) {
                if (validateUomSymbol(uomIdentifier, null, context)) return true;
            }
            if (Fes20Package.Literals.UOM_URI.isInstance(uomIdentifier)) {
                if (validateUomURI(uomIdentifier, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUomSymbol(String uomSymbol, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateUomSymbol_Pattern(uomSymbol, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateUomSymbol_Pattern
     */
    public static final  PatternMatcher [][] UOM_SYMBOL__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("[^: \\n\\r\\t]+")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Uom Symbol</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUomSymbol_Pattern(String uomSymbol, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Fes20Package.Literals.UOM_SYMBOL, uomSymbol, UOM_SYMBOL__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUomURI(String uomURI, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateUomURI_Pattern(uomURI, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateUomURI_Pattern
     */
    public static final  PatternMatcher [][] UOM_URI__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("([a-zA-Z][a-zA-Z0-9\\-\\+\\.]*:|\\.\\./|\\./|#).*")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Uom URI</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUomURI_Pattern(String uomURI, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Fes20Package.Literals.UOM_URI, uomURI, UOM_URI__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionActionTokensObject(VersionActionTokens versionActionTokensObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionType(Object versionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateVersionType_MemberTypes(versionType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Version Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionType_MemberTypes(Object versionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Fes20Package.Literals.VERSION_ACTION_TOKENS.isInstance(versionType)) {
                if (validateVersionActionTokens((VersionActionTokens)versionType, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.POSITIVE_INTEGER.isInstance(versionType)) {
                if (xmlTypeValidator.validatePositiveInteger((BigInteger)versionType, tempDiagnostics, context)) return true;
            }
            if (XMLTypePackage.Literals.DATE_TIME.isInstance(versionType)) {
                if (xmlTypeValidator.validateDateTime(versionType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Fes20Package.Literals.VERSION_ACTION_TOKENS.isInstance(versionType)) {
                if (validateVersionActionTokens((VersionActionTokens)versionType, null, context)) return true;
            }
            if (XMLTypePackage.Literals.POSITIVE_INTEGER.isInstance(versionType)) {
                if (xmlTypeValidator.validatePositiveInteger((BigInteger)versionType, null, context)) return true;
            }
            if (XMLTypePackage.Literals.DATE_TIME.isInstance(versionType)) {
                if (xmlTypeValidator.validateDateTime(versionType, null, context)) return true;
            }
        }
        return false;
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

} //Fes20Validator
