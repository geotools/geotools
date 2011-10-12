/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import java.util.List;

import net.opengis.fes20.AbstractAdhocQueryExpressionType;
import net.opengis.fes20.AbstractIdType;
import net.opengis.fes20.AbstractProjectionClauseType;
import net.opengis.fes20.AbstractQueryExpressionType;
import net.opengis.fes20.AbstractSelectionClauseType;
import net.opengis.fes20.AbstractSortingClauseType;
import net.opengis.fes20.AdditionalOperatorsType;
import net.opengis.fes20.ArgumentType;
import net.opengis.fes20.ArgumentsType;
import net.opengis.fes20.AvailableFunctionType;
import net.opengis.fes20.AvailableFunctionsType;
import net.opengis.fes20.BBOXType;
import net.opengis.fes20.BinaryComparisonOpType;
import net.opengis.fes20.BinaryLogicOpType;
import net.opengis.fes20.BinarySpatialOpType;
import net.opengis.fes20.BinaryTemporalOpType;
import net.opengis.fes20.ComparisonOperatorNameTypeMember0;
import net.opengis.fes20.ComparisonOperatorType;
import net.opengis.fes20.ComparisonOperatorsType;
import net.opengis.fes20.ComparisonOpsType;
import net.opengis.fes20.ConformanceType;
import net.opengis.fes20.DistanceBufferType;
import net.opengis.fes20.DocumentRoot;
import net.opengis.fes20.ExtendedCapabilitiesType;
import net.opengis.fes20.ExtensionOperatorType;
import net.opengis.fes20.ExtensionOpsType;
import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.FilterCapabilitiesType;
import net.opengis.fes20.FilterType;
import net.opengis.fes20.FunctionType;
import net.opengis.fes20.GeometryOperandType;
import net.opengis.fes20.GeometryOperandsType;
import net.opengis.fes20.IdCapabilitiesType;
import net.opengis.fes20.LiteralType;
import net.opengis.fes20.LogicOpsType;
import net.opengis.fes20.LogicalOperatorsType;
import net.opengis.fes20.LowerBoundaryType;
import net.opengis.fes20.MatchActionType;
import net.opengis.fes20.MeasureType;
import net.opengis.fes20.PropertyIsBetweenType;
import net.opengis.fes20.PropertyIsLikeType;
import net.opengis.fes20.PropertyIsNilType;
import net.opengis.fes20.PropertyIsNullType;
import net.opengis.fes20.ResourceIdType;
import net.opengis.fes20.ResourceIdentifierType;
import net.opengis.fes20.ScalarCapabilitiesType;
import net.opengis.fes20.SortByType;
import net.opengis.fes20.SortOrderType;
import net.opengis.fes20.SortPropertyType;
import net.opengis.fes20.SpatialCapabilitiesType;
import net.opengis.fes20.SpatialOperatorNameTypeMember0;
import net.opengis.fes20.SpatialOperatorType;
import net.opengis.fes20.SpatialOperatorsType;
import net.opengis.fes20.SpatialOpsType;
import net.opengis.fes20.TemporalCapabilitiesType;
import net.opengis.fes20.TemporalOperandType;
import net.opengis.fes20.TemporalOperandsType;
import net.opengis.fes20.TemporalOperatorNameTypeMember0;
import net.opengis.fes20.TemporalOperatorType;
import net.opengis.fes20.TemporalOperatorsType;
import net.opengis.fes20.TemporalOpsType;
import net.opengis.fes20.UnaryLogicOpType;
import net.opengis.fes20.UpperBoundaryType;
import net.opengis.fes20.VersionActionTokens;

import net.opengis.fes20.util.Fes20Validator;

import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Fes20PackageImpl extends EPackageImpl implements Fes20Package {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractAdhocQueryExpressionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractIdTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractProjectionClauseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractQueryExpressionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractSelectionClauseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractSortingClauseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass additionalOperatorsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass argumentsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass argumentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass availableFunctionsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass availableFunctionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass bboxTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass binaryComparisonOpTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass binaryLogicOpTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass binarySpatialOpTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass binaryTemporalOpTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass comparisonOperatorsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass comparisonOperatorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass comparisonOpsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conformanceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass distanceBufferTypeEClass = null;

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
    private EClass extendedCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass extensionOperatorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass extensionOpsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass filterCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass filterTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometryOperandsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geometryOperandTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass idCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass logicalOperatorsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass logicOpsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lowerBoundaryTypeEClass = null;

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
    private EClass propertyIsBetweenTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass propertyIsLikeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass propertyIsNilTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass propertyIsNullTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass resourceIdentifierTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass resourceIdTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass scalarCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sortByTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sortPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass spatialCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass spatialOperatorsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass spatialOperatorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass spatialOpsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalOperandsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalOperandTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalOperatorsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalOperatorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass temporalOpsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unaryLogicOpTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass upperBoundaryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum comparisonOperatorNameTypeMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum matchActionTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum sortOrderTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum spatialOperatorNameTypeMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum temporalOperatorNameTypeMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum versionActionTokensEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType aliasesTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType comparisonOperatorNameTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType comparisonOperatorNameTypeMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType comparisonOperatorNameTypeMember1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType matchActionTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType schemaElementEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType sortOrderTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType spatialOperatorNameTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType spatialOperatorNameTypeMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType spatialOperatorNameTypeMember1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType temporalOperatorNameTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType temporalOperatorNameTypeMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType temporalOperatorNameTypeMember1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType typeNamesListTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType typeNamesTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType uomIdentifierEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType uomSymbolEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType uomURIEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType versionActionTokensObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType versionTypeEDataType = null;

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
     * @see net.opengis.fes20.Fes20Package#eNS_URI
     * @see #init()
     * @generated
     */
    private Fes20PackageImpl() {
        super(eNS_URI, Fes20Factory.eINSTANCE);
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
     * <p>This method is used to initialize {@link Fes20Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static Fes20Package init() {
        if (isInited) return (Fes20Package)EPackage.Registry.INSTANCE.getEPackage(Fes20Package.eNS_URI);

        // Obtain or create and register package
        Fes20PackageImpl theFes20Package = (Fes20PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Fes20PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Fes20PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows11Package.eINSTANCE.eClass();

        // Create package meta-data objects
        theFes20Package.createPackageContents();

        // Initialize created meta-data
        theFes20Package.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theFes20Package, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return Fes20Validator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theFes20Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Fes20Package.eNS_URI, theFes20Package);
        return theFes20Package;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractAdhocQueryExpressionType() {
        return abstractAdhocQueryExpressionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractAdhocQueryExpressionType_AbstractProjectionClause() {
        return (EAttribute)abstractAdhocQueryExpressionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractAdhocQueryExpressionType_AbstractSelectionClause() {
        return (EAttribute)abstractAdhocQueryExpressionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractAdhocQueryExpressionType_AbstractSortingClause() {
        return (EAttribute)abstractAdhocQueryExpressionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractAdhocQueryExpressionType_Aliases() {
        return (EAttribute)abstractAdhocQueryExpressionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractAdhocQueryExpressionType_TypeNames() {
        return (EAttribute)abstractAdhocQueryExpressionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractIdType() {
        return abstractIdTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractProjectionClauseType() {
        return abstractProjectionClauseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractQueryExpressionType() {
        return abstractQueryExpressionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractQueryExpressionType_Handle() {
        return (EAttribute)abstractQueryExpressionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractSelectionClauseType() {
        return abstractSelectionClauseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractSortingClauseType() {
        return abstractSortingClauseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAdditionalOperatorsType() {
        return additionalOperatorsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAdditionalOperatorsType_Operator() {
        return (EReference)additionalOperatorsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArgumentsType() {
        return argumentsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArgumentsType_Argument() {
        return (EReference)argumentsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArgumentType() {
        return argumentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArgumentType_Metadata() {
        return (EReference)argumentTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArgumentType_Type() {
        return (EAttribute)argumentTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArgumentType_Name() {
        return (EAttribute)argumentTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAvailableFunctionsType() {
        return availableFunctionsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAvailableFunctionsType_Function() {
        return (EReference)availableFunctionsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAvailableFunctionType() {
        return availableFunctionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAvailableFunctionType_Metadata() {
        return (EReference)availableFunctionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAvailableFunctionType_Returns() {
        return (EAttribute)availableFunctionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAvailableFunctionType_Arguments() {
        return (EReference)availableFunctionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAvailableFunctionType_Name() {
        return (EAttribute)availableFunctionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBBOXType() {
        return bboxTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBBOXType_ExpressionGroup() {
        return (EAttribute)bboxTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBBOXType_Expression() {
        return (EReference)bboxTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBBOXType_Any() {
        return (EAttribute)bboxTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBinaryComparisonOpType() {
        return binaryComparisonOpTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryComparisonOpType_ExpressionGroup() {
        return (EAttribute)binaryComparisonOpTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryComparisonOpType_Expression() {
        return (EReference)binaryComparisonOpTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryComparisonOpType_MatchAction() {
        return (EAttribute)binaryComparisonOpTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryComparisonOpType_MatchCase() {
        return (EAttribute)binaryComparisonOpTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBinaryLogicOpType() {
        return binaryLogicOpTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryLogicOpType_FilterPredicates() {
        return (EAttribute)binaryLogicOpTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryLogicOpType_ComparisonOpsGroup() {
        return (EAttribute)binaryLogicOpTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryLogicOpType_ComparisonOps() {
        return (EReference)binaryLogicOpTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryLogicOpType_SpatialOpsGroup() {
        return (EAttribute)binaryLogicOpTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryLogicOpType_SpatialOps() {
        return (EReference)binaryLogicOpTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryLogicOpType_TemporalOpsGroup() {
        return (EAttribute)binaryLogicOpTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryLogicOpType_TemporalOps() {
        return (EReference)binaryLogicOpTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryLogicOpType_LogicOpsGroup() {
        return (EAttribute)binaryLogicOpTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryLogicOpType_LogicOps() {
        return (EReference)binaryLogicOpTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryLogicOpType_ExtensionOpsGroup() {
        return (EAttribute)binaryLogicOpTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryLogicOpType_ExtensionOps() {
        return (EReference)binaryLogicOpTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryLogicOpType_Function() {
        return (EReference)binaryLogicOpTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryLogicOpType_IdGroup() {
        return (EAttribute)binaryLogicOpTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryLogicOpType_Id() {
        return (EReference)binaryLogicOpTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBinarySpatialOpType() {
        return binarySpatialOpTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinarySpatialOpType_ValueReference() {
        return (EAttribute)binarySpatialOpTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinarySpatialOpType_ExpressionGroup() {
        return (EAttribute)binarySpatialOpTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinarySpatialOpType_Expression() {
        return (EReference)binarySpatialOpTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinarySpatialOpType_Any() {
        return (EAttribute)binarySpatialOpTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBinaryTemporalOpType() {
        return binaryTemporalOpTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryTemporalOpType_ValueReference() {
        return (EAttribute)binaryTemporalOpTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryTemporalOpType_ExpressionGroup() {
        return (EAttribute)binaryTemporalOpTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBinaryTemporalOpType_Expression() {
        return (EReference)binaryTemporalOpTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryTemporalOpType_Any() {
        return (EAttribute)binaryTemporalOpTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComparisonOperatorsType() {
        return comparisonOperatorsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComparisonOperatorsType_Group() {
        return (EAttribute)comparisonOperatorsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComparisonOperatorsType_ComparisonOperator() {
        return (EReference)comparisonOperatorsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComparisonOperatorType() {
        return comparisonOperatorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComparisonOperatorType_Name() {
        return (EAttribute)comparisonOperatorTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComparisonOpsType() {
        return comparisonOpsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConformanceType() {
        return conformanceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConformanceType_Constraint() {
        return (EReference)conformanceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDistanceBufferType() {
        return distanceBufferTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDistanceBufferType_ExpressionGroup() {
        return (EAttribute)distanceBufferTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDistanceBufferType_Expression() {
        return (EReference)distanceBufferTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDistanceBufferType_Any() {
        return (EAttribute)distanceBufferTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDistanceBufferType_Distance() {
        return (EReference)distanceBufferTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDocumentRoot() {
        return documentRootEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Mixed() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XMLNSPrefixMap() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_XSISchemaLocation() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Id() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbstractAdhocQueryExpression() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbstractQueryExpression() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbstractProjectionClause() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbstractSelectionClause() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbstractSortingClause() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_After() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalOps() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_And() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LogicOps() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AnyInteracts() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BBOX() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SpatialOps() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Before() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Begins() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BegunBy() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Beyond() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ComparisonOps() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Contains() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Crosses() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Disjoint() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_During() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DWithin() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_EndedBy() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Ends() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Equals() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Expression() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ExtensionOps() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Filter() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FilterCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Function() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Intersects() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Literal() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LogicalOperators() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(36);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Meets() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(37);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MetBy() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(38);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Not() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(39);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Or() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(40);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OverlappedBy() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(41);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Overlaps() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(42);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsBetween() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(43);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsEqualTo() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(44);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsGreaterThan() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(45);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsGreaterThanOrEqualTo() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(46);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsLessThan() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(47);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsLessThanOrEqualTo() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(48);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsLike() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(49);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsNil() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(50);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsNotEqualTo() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(51);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyIsNull() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(52);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ResourceId() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(53);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SortBy() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(54);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TContains() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(55);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TEquals() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(56);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Touches() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(57);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TOverlaps() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(58);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_ValueReference() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(59);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Within() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(60);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExtendedCapabilitiesType() {
        return extendedCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtendedCapabilitiesType_AdditionalOperators() {
        return (EReference)extendedCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExtensionOperatorType() {
        return extensionOperatorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExtensionOperatorType_Name() {
        return (EAttribute)extensionOperatorTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExtensionOpsType() {
        return extensionOpsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFilterCapabilitiesType() {
        return filterCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterCapabilitiesType_Conformance() {
        return (EReference)filterCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterCapabilitiesType_IdCapabilities() {
        return (EReference)filterCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterCapabilitiesType_ScalarCapabilities() {
        return (EReference)filterCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterCapabilitiesType_SpatialCapabilities() {
        return (EReference)filterCapabilitiesTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterCapabilitiesType_TemporalCapabilities() {
        return (EReference)filterCapabilitiesTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterCapabilitiesType_Functions() {
        return (EReference)filterCapabilitiesTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterCapabilitiesType_ExtendedCapabilities() {
        return (EReference)filterCapabilitiesTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFilterType() {
        return filterTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFilterType_ComparisonOpsGroup() {
        return (EAttribute)filterTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterType_ComparisonOps() {
        return (EReference)filterTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFilterType_SpatialOpsGroup() {
        return (EAttribute)filterTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterType_SpatialOps() {
        return (EReference)filterTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFilterType_TemporalOpsGroup() {
        return (EAttribute)filterTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterType_TemporalOps() {
        return (EReference)filterTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFilterType_LogicOpsGroup() {
        return (EAttribute)filterTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterType_LogicOps() {
        return (EReference)filterTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFilterType_ExtensionOpsGroup() {
        return (EAttribute)filterTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterType_ExtensionOps() {
        return (EReference)filterTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterType_Function() {
        return (EReference)filterTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFilterType_IdGroup() {
        return (EAttribute)filterTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFilterType_Id() {
        return (EReference)filterTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFunctionType() {
        return functionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFunctionType_ExpressionGroup() {
        return (EAttribute)functionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFunctionType_Expression() {
        return (EReference)functionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFunctionType_Name() {
        return (EAttribute)functionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometryOperandsType() {
        return geometryOperandsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeometryOperandsType_GeometryOperand() {
        return (EReference)geometryOperandsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeometryOperandType() {
        return geometryOperandTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeometryOperandType_Name() {
        return (EAttribute)geometryOperandTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIdCapabilitiesType() {
        return idCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getIdCapabilitiesType_ResourceIdentifier() {
        return (EReference)idCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLiteralType() {
        return literalTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLiteralType_Mixed() {
        return (EAttribute)literalTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLiteralType_Any() {
        return (EAttribute)literalTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLiteralType_Type() {
        return (EAttribute)literalTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLogicalOperatorsType() {
        return logicalOperatorsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLogicOpsType() {
        return logicOpsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLowerBoundaryType() {
        return lowerBoundaryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLowerBoundaryType_ExpressionGroup() {
        return (EAttribute)lowerBoundaryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLowerBoundaryType_Expression() {
        return (EReference)lowerBoundaryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMeasureType() {
        return measureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureType_Value() {
        return (EAttribute)measureTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMeasureType_Uom() {
        return (EAttribute)measureTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPropertyIsBetweenType() {
        return propertyIsBetweenTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsBetweenType_ExpressionGroup() {
        return (EAttribute)propertyIsBetweenTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPropertyIsBetweenType_Expression() {
        return (EReference)propertyIsBetweenTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPropertyIsBetweenType_LowerBoundary() {
        return (EReference)propertyIsBetweenTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPropertyIsBetweenType_UpperBoundary() {
        return (EReference)propertyIsBetweenTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPropertyIsLikeType() {
        return propertyIsLikeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsLikeType_ExpressionGroup() {
        return (EAttribute)propertyIsLikeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPropertyIsLikeType_Expression() {
        return (EReference)propertyIsLikeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsLikeType_EscapeChar() {
        return (EAttribute)propertyIsLikeTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsLikeType_SingleChar() {
        return (EAttribute)propertyIsLikeTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsLikeType_WildCard() {
        return (EAttribute)propertyIsLikeTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPropertyIsNilType() {
        return propertyIsNilTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsNilType_ExpressionGroup() {
        return (EAttribute)propertyIsNilTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPropertyIsNilType_Expression() {
        return (EReference)propertyIsNilTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsNilType_NilReason() {
        return (EAttribute)propertyIsNilTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPropertyIsNullType() {
        return propertyIsNullTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyIsNullType_ExpressionGroup() {
        return (EAttribute)propertyIsNullTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPropertyIsNullType_Expression() {
        return (EReference)propertyIsNullTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResourceIdentifierType() {
        return resourceIdentifierTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResourceIdentifierType_Metadata() {
        return (EReference)resourceIdentifierTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceIdentifierType_Name() {
        return (EAttribute)resourceIdentifierTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResourceIdType() {
        return resourceIdTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceIdType_EndDate() {
        return (EAttribute)resourceIdTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceIdType_PreviousRid() {
        return (EAttribute)resourceIdTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceIdType_Rid() {
        return (EAttribute)resourceIdTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceIdType_StartDate() {
        return (EAttribute)resourceIdTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceIdType_Version() {
        return (EAttribute)resourceIdTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getScalarCapabilitiesType() {
        return scalarCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getScalarCapabilitiesType_LogicalOperators() {
        return (EReference)scalarCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getScalarCapabilitiesType_ComparisonOperators() {
        return (EReference)scalarCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSortByType() {
        return sortByTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSortByType_SortProperty() {
        return (EReference)sortByTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSortPropertyType() {
        return sortPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSortPropertyType_ValueReference() {
        return (EAttribute)sortPropertyTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSortPropertyType_SortOrder() {
        return (EAttribute)sortPropertyTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpatialCapabilitiesType() {
        return spatialCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpatialCapabilitiesType_GeometryOperands() {
        return (EReference)spatialCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpatialCapabilitiesType_SpatialOperators() {
        return (EReference)spatialCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpatialOperatorsType() {
        return spatialOperatorsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpatialOperatorsType_SpatialOperator() {
        return (EReference)spatialOperatorsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpatialOperatorType() {
        return spatialOperatorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpatialOperatorType_GeometryOperands() {
        return (EReference)spatialOperatorTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSpatialOperatorType_Name() {
        return (EAttribute)spatialOperatorTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpatialOpsType() {
        return spatialOpsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalCapabilitiesType() {
        return temporalCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalCapabilitiesType_TemporalOperands() {
        return (EReference)temporalCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalCapabilitiesType_TemporalOperators() {
        return (EReference)temporalCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalOperandsType() {
        return temporalOperandsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalOperandsType_TemporalOperand() {
        return (EReference)temporalOperandsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalOperandType() {
        return temporalOperandTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalOperandType_Name() {
        return (EAttribute)temporalOperandTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalOperatorsType() {
        return temporalOperatorsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalOperatorsType_TemporalOperator() {
        return (EReference)temporalOperatorsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalOperatorType() {
        return temporalOperatorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTemporalOperatorType_TemporalOperands() {
        return (EReference)temporalOperatorTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTemporalOperatorType_Name() {
        return (EAttribute)temporalOperatorTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTemporalOpsType() {
        return temporalOpsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUnaryLogicOpType() {
        return unaryLogicOpTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnaryLogicOpType_ComparisonOpsGroup() {
        return (EAttribute)unaryLogicOpTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnaryLogicOpType_ComparisonOps() {
        return (EReference)unaryLogicOpTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnaryLogicOpType_SpatialOpsGroup() {
        return (EAttribute)unaryLogicOpTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnaryLogicOpType_SpatialOps() {
        return (EReference)unaryLogicOpTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnaryLogicOpType_TemporalOpsGroup() {
        return (EAttribute)unaryLogicOpTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnaryLogicOpType_TemporalOps() {
        return (EReference)unaryLogicOpTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnaryLogicOpType_LogicOpsGroup() {
        return (EAttribute)unaryLogicOpTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnaryLogicOpType_LogicOps() {
        return (EReference)unaryLogicOpTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnaryLogicOpType_ExtensionOpsGroup() {
        return (EAttribute)unaryLogicOpTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnaryLogicOpType_ExtensionOps() {
        return (EReference)unaryLogicOpTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnaryLogicOpType_Function() {
        return (EReference)unaryLogicOpTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnaryLogicOpType_IdGroup() {
        return (EAttribute)unaryLogicOpTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnaryLogicOpType_Id() {
        return (EReference)unaryLogicOpTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUpperBoundaryType() {
        return upperBoundaryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUpperBoundaryType_ExpressionGroup() {
        return (EAttribute)upperBoundaryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUpperBoundaryType_Expression() {
        return (EReference)upperBoundaryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getComparisonOperatorNameTypeMember0() {
        return comparisonOperatorNameTypeMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getMatchActionType() {
        return matchActionTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSortOrderType() {
        return sortOrderTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSpatialOperatorNameTypeMember0() {
        return spatialOperatorNameTypeMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getTemporalOperatorNameTypeMember0() {
        return temporalOperatorNameTypeMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getVersionActionTokens() {
        return versionActionTokensEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAliasesType() {
        return aliasesTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getComparisonOperatorNameType() {
        return comparisonOperatorNameTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getComparisonOperatorNameTypeMember0Object() {
        return comparisonOperatorNameTypeMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getComparisonOperatorNameTypeMember1() {
        return comparisonOperatorNameTypeMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getMatchActionTypeObject() {
        return matchActionTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSchemaElement() {
        return schemaElementEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSortOrderTypeObject() {
        return sortOrderTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSpatialOperatorNameType() {
        return spatialOperatorNameTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSpatialOperatorNameTypeMember0Object() {
        return spatialOperatorNameTypeMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSpatialOperatorNameTypeMember1() {
        return spatialOperatorNameTypeMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTemporalOperatorNameType() {
        return temporalOperatorNameTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTemporalOperatorNameTypeMember0Object() {
        return temporalOperatorNameTypeMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTemporalOperatorNameTypeMember1() {
        return temporalOperatorNameTypeMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTypeNamesListType() {
        return typeNamesListTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTypeNamesType() {
        return typeNamesTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getUomIdentifier() {
        return uomIdentifierEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getUomSymbol() {
        return uomSymbolEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getUomURI() {
        return uomURIEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getVersionActionTokensObject() {
        return versionActionTokensObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getVersionType() {
        return versionTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Fes20Factory getFes20Factory() {
        return (Fes20Factory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        abstractAdhocQueryExpressionTypeEClass = createEClass(ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE);
        createEAttribute(abstractAdhocQueryExpressionTypeEClass, ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_PROJECTION_CLAUSE);
        createEAttribute(abstractAdhocQueryExpressionTypeEClass, ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SELECTION_CLAUSE);
        createEAttribute(abstractAdhocQueryExpressionTypeEClass, ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ABSTRACT_SORTING_CLAUSE);
        createEAttribute(abstractAdhocQueryExpressionTypeEClass, ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__ALIASES);
        createEAttribute(abstractAdhocQueryExpressionTypeEClass, ABSTRACT_ADHOC_QUERY_EXPRESSION_TYPE__TYPE_NAMES);

        abstractIdTypeEClass = createEClass(ABSTRACT_ID_TYPE);

        abstractProjectionClauseTypeEClass = createEClass(ABSTRACT_PROJECTION_CLAUSE_TYPE);

        abstractQueryExpressionTypeEClass = createEClass(ABSTRACT_QUERY_EXPRESSION_TYPE);
        createEAttribute(abstractQueryExpressionTypeEClass, ABSTRACT_QUERY_EXPRESSION_TYPE__HANDLE);

        abstractSelectionClauseTypeEClass = createEClass(ABSTRACT_SELECTION_CLAUSE_TYPE);

        abstractSortingClauseTypeEClass = createEClass(ABSTRACT_SORTING_CLAUSE_TYPE);

        additionalOperatorsTypeEClass = createEClass(ADDITIONAL_OPERATORS_TYPE);
        createEReference(additionalOperatorsTypeEClass, ADDITIONAL_OPERATORS_TYPE__OPERATOR);

        argumentsTypeEClass = createEClass(ARGUMENTS_TYPE);
        createEReference(argumentsTypeEClass, ARGUMENTS_TYPE__ARGUMENT);

        argumentTypeEClass = createEClass(ARGUMENT_TYPE);
        createEReference(argumentTypeEClass, ARGUMENT_TYPE__METADATA);
        createEAttribute(argumentTypeEClass, ARGUMENT_TYPE__TYPE);
        createEAttribute(argumentTypeEClass, ARGUMENT_TYPE__NAME);

        availableFunctionsTypeEClass = createEClass(AVAILABLE_FUNCTIONS_TYPE);
        createEReference(availableFunctionsTypeEClass, AVAILABLE_FUNCTIONS_TYPE__FUNCTION);

        availableFunctionTypeEClass = createEClass(AVAILABLE_FUNCTION_TYPE);
        createEReference(availableFunctionTypeEClass, AVAILABLE_FUNCTION_TYPE__METADATA);
        createEAttribute(availableFunctionTypeEClass, AVAILABLE_FUNCTION_TYPE__RETURNS);
        createEReference(availableFunctionTypeEClass, AVAILABLE_FUNCTION_TYPE__ARGUMENTS);
        createEAttribute(availableFunctionTypeEClass, AVAILABLE_FUNCTION_TYPE__NAME);

        bboxTypeEClass = createEClass(BBOX_TYPE);
        createEAttribute(bboxTypeEClass, BBOX_TYPE__EXPRESSION_GROUP);
        createEReference(bboxTypeEClass, BBOX_TYPE__EXPRESSION);
        createEAttribute(bboxTypeEClass, BBOX_TYPE__ANY);

        binaryComparisonOpTypeEClass = createEClass(BINARY_COMPARISON_OP_TYPE);
        createEAttribute(binaryComparisonOpTypeEClass, BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP);
        createEReference(binaryComparisonOpTypeEClass, BINARY_COMPARISON_OP_TYPE__EXPRESSION);
        createEAttribute(binaryComparisonOpTypeEClass, BINARY_COMPARISON_OP_TYPE__MATCH_ACTION);
        createEAttribute(binaryComparisonOpTypeEClass, BINARY_COMPARISON_OP_TYPE__MATCH_CASE);

        binaryLogicOpTypeEClass = createEClass(BINARY_LOGIC_OP_TYPE);
        createEAttribute(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__FILTER_PREDICATES);
        createEAttribute(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__COMPARISON_OPS_GROUP);
        createEReference(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__COMPARISON_OPS);
        createEAttribute(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__SPATIAL_OPS_GROUP);
        createEReference(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__SPATIAL_OPS);
        createEAttribute(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__TEMPORAL_OPS_GROUP);
        createEReference(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__TEMPORAL_OPS);
        createEAttribute(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__LOGIC_OPS_GROUP);
        createEReference(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__LOGIC_OPS);
        createEAttribute(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__EXTENSION_OPS_GROUP);
        createEReference(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__EXTENSION_OPS);
        createEReference(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__FUNCTION);
        createEAttribute(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__ID_GROUP);
        createEReference(binaryLogicOpTypeEClass, BINARY_LOGIC_OP_TYPE__ID);

        binarySpatialOpTypeEClass = createEClass(BINARY_SPATIAL_OP_TYPE);
        createEAttribute(binarySpatialOpTypeEClass, BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE);
        createEAttribute(binarySpatialOpTypeEClass, BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP);
        createEReference(binarySpatialOpTypeEClass, BINARY_SPATIAL_OP_TYPE__EXPRESSION);
        createEAttribute(binarySpatialOpTypeEClass, BINARY_SPATIAL_OP_TYPE__ANY);

        binaryTemporalOpTypeEClass = createEClass(BINARY_TEMPORAL_OP_TYPE);
        createEAttribute(binaryTemporalOpTypeEClass, BINARY_TEMPORAL_OP_TYPE__VALUE_REFERENCE);
        createEAttribute(binaryTemporalOpTypeEClass, BINARY_TEMPORAL_OP_TYPE__EXPRESSION_GROUP);
        createEReference(binaryTemporalOpTypeEClass, BINARY_TEMPORAL_OP_TYPE__EXPRESSION);
        createEAttribute(binaryTemporalOpTypeEClass, BINARY_TEMPORAL_OP_TYPE__ANY);

        comparisonOperatorsTypeEClass = createEClass(COMPARISON_OPERATORS_TYPE);
        createEAttribute(comparisonOperatorsTypeEClass, COMPARISON_OPERATORS_TYPE__GROUP);
        createEReference(comparisonOperatorsTypeEClass, COMPARISON_OPERATORS_TYPE__COMPARISON_OPERATOR);

        comparisonOperatorTypeEClass = createEClass(COMPARISON_OPERATOR_TYPE);
        createEAttribute(comparisonOperatorTypeEClass, COMPARISON_OPERATOR_TYPE__NAME);

        comparisonOpsTypeEClass = createEClass(COMPARISON_OPS_TYPE);

        conformanceTypeEClass = createEClass(CONFORMANCE_TYPE);
        createEReference(conformanceTypeEClass, CONFORMANCE_TYPE__CONSTRAINT);

        distanceBufferTypeEClass = createEClass(DISTANCE_BUFFER_TYPE);
        createEAttribute(distanceBufferTypeEClass, DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP);
        createEReference(distanceBufferTypeEClass, DISTANCE_BUFFER_TYPE__EXPRESSION);
        createEAttribute(distanceBufferTypeEClass, DISTANCE_BUFFER_TYPE__ANY);
        createEReference(distanceBufferTypeEClass, DISTANCE_BUFFER_TYPE__DISTANCE);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ID);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_ADHOC_QUERY_EXPRESSION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_QUERY_EXPRESSION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_PROJECTION_CLAUSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_SELECTION_CLAUSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_SORTING_CLAUSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__AFTER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TEMPORAL_OPS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__AND);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LOGIC_OPS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANY_INTERACTS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BBOX);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SPATIAL_OPS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BEFORE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BEGINS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BEGUN_BY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BEYOND);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COMPARISON_OPS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CONTAINS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CROSSES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DISJOINT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DURING);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DWITHIN);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ENDED_BY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ENDS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EQUALS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXPRESSION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXTENSION_OPS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FILTER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FILTER_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FUNCTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__INTERSECTS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LITERAL);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LOGICAL_OPERATORS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__MEETS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__MET_BY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__NOT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OR);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OVERLAPPED_BY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OVERLAPS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_BETWEEN);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_EQUAL_TO);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_LESS_THAN_OR_EQUAL_TO);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_LIKE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_NIL);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_NOT_EQUAL_TO);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_IS_NULL);
        createEReference(documentRootEClass, DOCUMENT_ROOT__RESOURCE_ID);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SORT_BY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TCONTAINS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TEQUALS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TOUCHES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TOVERLAPS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__VALUE_REFERENCE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__WITHIN);

        extendedCapabilitiesTypeEClass = createEClass(EXTENDED_CAPABILITIES_TYPE);
        createEReference(extendedCapabilitiesTypeEClass, EXTENDED_CAPABILITIES_TYPE__ADDITIONAL_OPERATORS);

        extensionOperatorTypeEClass = createEClass(EXTENSION_OPERATOR_TYPE);
        createEAttribute(extensionOperatorTypeEClass, EXTENSION_OPERATOR_TYPE__NAME);

        extensionOpsTypeEClass = createEClass(EXTENSION_OPS_TYPE);

        filterCapabilitiesTypeEClass = createEClass(FILTER_CAPABILITIES_TYPE);
        createEReference(filterCapabilitiesTypeEClass, FILTER_CAPABILITIES_TYPE__CONFORMANCE);
        createEReference(filterCapabilitiesTypeEClass, FILTER_CAPABILITIES_TYPE__ID_CAPABILITIES);
        createEReference(filterCapabilitiesTypeEClass, FILTER_CAPABILITIES_TYPE__SCALAR_CAPABILITIES);
        createEReference(filterCapabilitiesTypeEClass, FILTER_CAPABILITIES_TYPE__SPATIAL_CAPABILITIES);
        createEReference(filterCapabilitiesTypeEClass, FILTER_CAPABILITIES_TYPE__TEMPORAL_CAPABILITIES);
        createEReference(filterCapabilitiesTypeEClass, FILTER_CAPABILITIES_TYPE__FUNCTIONS);
        createEReference(filterCapabilitiesTypeEClass, FILTER_CAPABILITIES_TYPE__EXTENDED_CAPABILITIES);

        filterTypeEClass = createEClass(FILTER_TYPE);
        createEAttribute(filterTypeEClass, FILTER_TYPE__COMPARISON_OPS_GROUP);
        createEReference(filterTypeEClass, FILTER_TYPE__COMPARISON_OPS);
        createEAttribute(filterTypeEClass, FILTER_TYPE__SPATIAL_OPS_GROUP);
        createEReference(filterTypeEClass, FILTER_TYPE__SPATIAL_OPS);
        createEAttribute(filterTypeEClass, FILTER_TYPE__TEMPORAL_OPS_GROUP);
        createEReference(filterTypeEClass, FILTER_TYPE__TEMPORAL_OPS);
        createEAttribute(filterTypeEClass, FILTER_TYPE__LOGIC_OPS_GROUP);
        createEReference(filterTypeEClass, FILTER_TYPE__LOGIC_OPS);
        createEAttribute(filterTypeEClass, FILTER_TYPE__EXTENSION_OPS_GROUP);
        createEReference(filterTypeEClass, FILTER_TYPE__EXTENSION_OPS);
        createEReference(filterTypeEClass, FILTER_TYPE__FUNCTION);
        createEAttribute(filterTypeEClass, FILTER_TYPE__ID_GROUP);
        createEReference(filterTypeEClass, FILTER_TYPE__ID);

        functionTypeEClass = createEClass(FUNCTION_TYPE);
        createEAttribute(functionTypeEClass, FUNCTION_TYPE__EXPRESSION_GROUP);
        createEReference(functionTypeEClass, FUNCTION_TYPE__EXPRESSION);
        createEAttribute(functionTypeEClass, FUNCTION_TYPE__NAME);

        geometryOperandsTypeEClass = createEClass(GEOMETRY_OPERANDS_TYPE);
        createEReference(geometryOperandsTypeEClass, GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND);

        geometryOperandTypeEClass = createEClass(GEOMETRY_OPERAND_TYPE);
        createEAttribute(geometryOperandTypeEClass, GEOMETRY_OPERAND_TYPE__NAME);

        idCapabilitiesTypeEClass = createEClass(ID_CAPABILITIES_TYPE);
        createEReference(idCapabilitiesTypeEClass, ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER);

        literalTypeEClass = createEClass(LITERAL_TYPE);
        createEAttribute(literalTypeEClass, LITERAL_TYPE__MIXED);
        createEAttribute(literalTypeEClass, LITERAL_TYPE__ANY);
        createEAttribute(literalTypeEClass, LITERAL_TYPE__TYPE);

        logicalOperatorsTypeEClass = createEClass(LOGICAL_OPERATORS_TYPE);

        logicOpsTypeEClass = createEClass(LOGIC_OPS_TYPE);

        lowerBoundaryTypeEClass = createEClass(LOWER_BOUNDARY_TYPE);
        createEAttribute(lowerBoundaryTypeEClass, LOWER_BOUNDARY_TYPE__EXPRESSION_GROUP);
        createEReference(lowerBoundaryTypeEClass, LOWER_BOUNDARY_TYPE__EXPRESSION);

        measureTypeEClass = createEClass(MEASURE_TYPE);
        createEAttribute(measureTypeEClass, MEASURE_TYPE__VALUE);
        createEAttribute(measureTypeEClass, MEASURE_TYPE__UOM);

        propertyIsBetweenTypeEClass = createEClass(PROPERTY_IS_BETWEEN_TYPE);
        createEAttribute(propertyIsBetweenTypeEClass, PROPERTY_IS_BETWEEN_TYPE__EXPRESSION_GROUP);
        createEReference(propertyIsBetweenTypeEClass, PROPERTY_IS_BETWEEN_TYPE__EXPRESSION);
        createEReference(propertyIsBetweenTypeEClass, PROPERTY_IS_BETWEEN_TYPE__LOWER_BOUNDARY);
        createEReference(propertyIsBetweenTypeEClass, PROPERTY_IS_BETWEEN_TYPE__UPPER_BOUNDARY);

        propertyIsLikeTypeEClass = createEClass(PROPERTY_IS_LIKE_TYPE);
        createEAttribute(propertyIsLikeTypeEClass, PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP);
        createEReference(propertyIsLikeTypeEClass, PROPERTY_IS_LIKE_TYPE__EXPRESSION);
        createEAttribute(propertyIsLikeTypeEClass, PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR);
        createEAttribute(propertyIsLikeTypeEClass, PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR);
        createEAttribute(propertyIsLikeTypeEClass, PROPERTY_IS_LIKE_TYPE__WILD_CARD);

        propertyIsNilTypeEClass = createEClass(PROPERTY_IS_NIL_TYPE);
        createEAttribute(propertyIsNilTypeEClass, PROPERTY_IS_NIL_TYPE__EXPRESSION_GROUP);
        createEReference(propertyIsNilTypeEClass, PROPERTY_IS_NIL_TYPE__EXPRESSION);
        createEAttribute(propertyIsNilTypeEClass, PROPERTY_IS_NIL_TYPE__NIL_REASON);

        propertyIsNullTypeEClass = createEClass(PROPERTY_IS_NULL_TYPE);
        createEAttribute(propertyIsNullTypeEClass, PROPERTY_IS_NULL_TYPE__EXPRESSION_GROUP);
        createEReference(propertyIsNullTypeEClass, PROPERTY_IS_NULL_TYPE__EXPRESSION);

        resourceIdentifierTypeEClass = createEClass(RESOURCE_IDENTIFIER_TYPE);
        createEReference(resourceIdentifierTypeEClass, RESOURCE_IDENTIFIER_TYPE__METADATA);
        createEAttribute(resourceIdentifierTypeEClass, RESOURCE_IDENTIFIER_TYPE__NAME);

        resourceIdTypeEClass = createEClass(RESOURCE_ID_TYPE);
        createEAttribute(resourceIdTypeEClass, RESOURCE_ID_TYPE__END_DATE);
        createEAttribute(resourceIdTypeEClass, RESOURCE_ID_TYPE__PREVIOUS_RID);
        createEAttribute(resourceIdTypeEClass, RESOURCE_ID_TYPE__RID);
        createEAttribute(resourceIdTypeEClass, RESOURCE_ID_TYPE__START_DATE);
        createEAttribute(resourceIdTypeEClass, RESOURCE_ID_TYPE__VERSION);

        scalarCapabilitiesTypeEClass = createEClass(SCALAR_CAPABILITIES_TYPE);
        createEReference(scalarCapabilitiesTypeEClass, SCALAR_CAPABILITIES_TYPE__LOGICAL_OPERATORS);
        createEReference(scalarCapabilitiesTypeEClass, SCALAR_CAPABILITIES_TYPE__COMPARISON_OPERATORS);

        sortByTypeEClass = createEClass(SORT_BY_TYPE);
        createEReference(sortByTypeEClass, SORT_BY_TYPE__SORT_PROPERTY);

        sortPropertyTypeEClass = createEClass(SORT_PROPERTY_TYPE);
        createEAttribute(sortPropertyTypeEClass, SORT_PROPERTY_TYPE__VALUE_REFERENCE);
        createEAttribute(sortPropertyTypeEClass, SORT_PROPERTY_TYPE__SORT_ORDER);

        spatialCapabilitiesTypeEClass = createEClass(SPATIAL_CAPABILITIES_TYPE);
        createEReference(spatialCapabilitiesTypeEClass, SPATIAL_CAPABILITIES_TYPE__GEOMETRY_OPERANDS);
        createEReference(spatialCapabilitiesTypeEClass, SPATIAL_CAPABILITIES_TYPE__SPATIAL_OPERATORS);

        spatialOperatorsTypeEClass = createEClass(SPATIAL_OPERATORS_TYPE);
        createEReference(spatialOperatorsTypeEClass, SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR);

        spatialOperatorTypeEClass = createEClass(SPATIAL_OPERATOR_TYPE);
        createEReference(spatialOperatorTypeEClass, SPATIAL_OPERATOR_TYPE__GEOMETRY_OPERANDS);
        createEAttribute(spatialOperatorTypeEClass, SPATIAL_OPERATOR_TYPE__NAME);

        spatialOpsTypeEClass = createEClass(SPATIAL_OPS_TYPE);

        temporalCapabilitiesTypeEClass = createEClass(TEMPORAL_CAPABILITIES_TYPE);
        createEReference(temporalCapabilitiesTypeEClass, TEMPORAL_CAPABILITIES_TYPE__TEMPORAL_OPERANDS);
        createEReference(temporalCapabilitiesTypeEClass, TEMPORAL_CAPABILITIES_TYPE__TEMPORAL_OPERATORS);

        temporalOperandsTypeEClass = createEClass(TEMPORAL_OPERANDS_TYPE);
        createEReference(temporalOperandsTypeEClass, TEMPORAL_OPERANDS_TYPE__TEMPORAL_OPERAND);

        temporalOperandTypeEClass = createEClass(TEMPORAL_OPERAND_TYPE);
        createEAttribute(temporalOperandTypeEClass, TEMPORAL_OPERAND_TYPE__NAME);

        temporalOperatorsTypeEClass = createEClass(TEMPORAL_OPERATORS_TYPE);
        createEReference(temporalOperatorsTypeEClass, TEMPORAL_OPERATORS_TYPE__TEMPORAL_OPERATOR);

        temporalOperatorTypeEClass = createEClass(TEMPORAL_OPERATOR_TYPE);
        createEReference(temporalOperatorTypeEClass, TEMPORAL_OPERATOR_TYPE__TEMPORAL_OPERANDS);
        createEAttribute(temporalOperatorTypeEClass, TEMPORAL_OPERATOR_TYPE__NAME);

        temporalOpsTypeEClass = createEClass(TEMPORAL_OPS_TYPE);

        unaryLogicOpTypeEClass = createEClass(UNARY_LOGIC_OP_TYPE);
        createEAttribute(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__COMPARISON_OPS_GROUP);
        createEReference(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__COMPARISON_OPS);
        createEAttribute(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__SPATIAL_OPS_GROUP);
        createEReference(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__SPATIAL_OPS);
        createEAttribute(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__TEMPORAL_OPS_GROUP);
        createEReference(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__TEMPORAL_OPS);
        createEAttribute(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__LOGIC_OPS_GROUP);
        createEReference(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__LOGIC_OPS);
        createEAttribute(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__EXTENSION_OPS_GROUP);
        createEReference(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__EXTENSION_OPS);
        createEReference(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__FUNCTION);
        createEAttribute(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__ID_GROUP);
        createEReference(unaryLogicOpTypeEClass, UNARY_LOGIC_OP_TYPE__ID);

        upperBoundaryTypeEClass = createEClass(UPPER_BOUNDARY_TYPE);
        createEAttribute(upperBoundaryTypeEClass, UPPER_BOUNDARY_TYPE__EXPRESSION_GROUP);
        createEReference(upperBoundaryTypeEClass, UPPER_BOUNDARY_TYPE__EXPRESSION);

        // Create enums
        comparisonOperatorNameTypeMember0EEnum = createEEnum(COMPARISON_OPERATOR_NAME_TYPE_MEMBER0);
        matchActionTypeEEnum = createEEnum(MATCH_ACTION_TYPE);
        sortOrderTypeEEnum = createEEnum(SORT_ORDER_TYPE);
        spatialOperatorNameTypeMember0EEnum = createEEnum(SPATIAL_OPERATOR_NAME_TYPE_MEMBER0);
        temporalOperatorNameTypeMember0EEnum = createEEnum(TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0);
        versionActionTokensEEnum = createEEnum(VERSION_ACTION_TOKENS);

        // Create data types
        aliasesTypeEDataType = createEDataType(ALIASES_TYPE);
        comparisonOperatorNameTypeEDataType = createEDataType(COMPARISON_OPERATOR_NAME_TYPE);
        comparisonOperatorNameTypeMember0ObjectEDataType = createEDataType(COMPARISON_OPERATOR_NAME_TYPE_MEMBER0_OBJECT);
        comparisonOperatorNameTypeMember1EDataType = createEDataType(COMPARISON_OPERATOR_NAME_TYPE_MEMBER1);
        matchActionTypeObjectEDataType = createEDataType(MATCH_ACTION_TYPE_OBJECT);
        schemaElementEDataType = createEDataType(SCHEMA_ELEMENT);
        sortOrderTypeObjectEDataType = createEDataType(SORT_ORDER_TYPE_OBJECT);
        spatialOperatorNameTypeEDataType = createEDataType(SPATIAL_OPERATOR_NAME_TYPE);
        spatialOperatorNameTypeMember0ObjectEDataType = createEDataType(SPATIAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT);
        spatialOperatorNameTypeMember1EDataType = createEDataType(SPATIAL_OPERATOR_NAME_TYPE_MEMBER1);
        temporalOperatorNameTypeEDataType = createEDataType(TEMPORAL_OPERATOR_NAME_TYPE);
        temporalOperatorNameTypeMember0ObjectEDataType = createEDataType(TEMPORAL_OPERATOR_NAME_TYPE_MEMBER0_OBJECT);
        temporalOperatorNameTypeMember1EDataType = createEDataType(TEMPORAL_OPERATOR_NAME_TYPE_MEMBER1);
        typeNamesListTypeEDataType = createEDataType(TYPE_NAMES_LIST_TYPE);
        typeNamesTypeEDataType = createEDataType(TYPE_NAMES_TYPE);
        uomIdentifierEDataType = createEDataType(UOM_IDENTIFIER);
        uomSymbolEDataType = createEDataType(UOM_SYMBOL);
        uomURIEDataType = createEDataType(UOM_URI);
        versionActionTokensObjectEDataType = createEDataType(VERSION_ACTION_TOKENS_OBJECT);
        versionTypeEDataType = createEDataType(VERSION_TYPE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
        Ows11Package theOws11Package = (Ows11Package)EPackage.Registry.INSTANCE.getEPackage(Ows11Package.eNS_URI);
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        abstractAdhocQueryExpressionTypeEClass.getESuperTypes().add(this.getAbstractQueryExpressionType());
        bboxTypeEClass.getESuperTypes().add(this.getSpatialOpsType());
        binaryComparisonOpTypeEClass.getESuperTypes().add(this.getComparisonOpsType());
        binaryLogicOpTypeEClass.getESuperTypes().add(this.getLogicOpsType());
        binarySpatialOpTypeEClass.getESuperTypes().add(this.getSpatialOpsType());
        binaryTemporalOpTypeEClass.getESuperTypes().add(this.getTemporalOpsType());
        distanceBufferTypeEClass.getESuperTypes().add(this.getSpatialOpsType());
        filterTypeEClass.getESuperTypes().add(this.getAbstractSelectionClauseType());
        propertyIsBetweenTypeEClass.getESuperTypes().add(this.getComparisonOpsType());
        propertyIsLikeTypeEClass.getESuperTypes().add(this.getComparisonOpsType());
        propertyIsNilTypeEClass.getESuperTypes().add(this.getComparisonOpsType());
        propertyIsNullTypeEClass.getESuperTypes().add(this.getComparisonOpsType());
        resourceIdTypeEClass.getESuperTypes().add(this.getAbstractIdType());
        unaryLogicOpTypeEClass.getESuperTypes().add(this.getLogicOpsType());

        // Initialize classes and features; add operations and parameters
        initEClass(abstractAdhocQueryExpressionTypeEClass, AbstractAdhocQueryExpressionType.class, "AbstractAdhocQueryExpressionType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractAdhocQueryExpressionType_AbstractProjectionClause(), ecorePackage.getEJavaObject(), "abstractProjectionClause", null, 0, -1, AbstractAdhocQueryExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractAdhocQueryExpressionType_AbstractSelectionClause(), ecorePackage.getEJavaObject(), "abstractSelectionClause", null, 0, 1, AbstractAdhocQueryExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractAdhocQueryExpressionType_AbstractSortingClause(), ecorePackage.getEJavaObject(), "abstractSortingClause", null, 0, 1, AbstractAdhocQueryExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractAdhocQueryExpressionType_Aliases(), ecorePackage.getEString(), "aliases", null, 0, -1, AbstractAdhocQueryExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractAdhocQueryExpressionType_TypeNames(), ecorePackage.getEJavaObject(), "typeNames", null, 0, -1, AbstractAdhocQueryExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(abstractIdTypeEClass, AbstractIdType.class, "AbstractIdType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(abstractProjectionClauseTypeEClass, AbstractProjectionClauseType.class, "AbstractProjectionClauseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(abstractQueryExpressionTypeEClass, AbstractQueryExpressionType.class, "AbstractQueryExpressionType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractQueryExpressionType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, AbstractQueryExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(abstractSelectionClauseTypeEClass, AbstractSelectionClauseType.class, "AbstractSelectionClauseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(abstractSortingClauseTypeEClass, AbstractSortingClauseType.class, "AbstractSortingClauseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(additionalOperatorsTypeEClass, AdditionalOperatorsType.class, "AdditionalOperatorsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAdditionalOperatorsType_Operator(), this.getExtensionOperatorType(), null, "operator", null, 0, -1, AdditionalOperatorsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(argumentsTypeEClass, ArgumentsType.class, "ArgumentsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getArgumentsType_Argument(), this.getArgumentType(), null, "argument", null, 1, -1, ArgumentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(argumentTypeEClass, ArgumentType.class, "ArgumentType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getArgumentType_Metadata(), theOws11Package.getMetadataType(), null, "metadata", null, 0, 1, ArgumentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArgumentType_Type(), theXMLTypePackage.getQName(), "type", null, 1, 1, ArgumentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArgumentType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ArgumentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(availableFunctionsTypeEClass, AvailableFunctionsType.class, "AvailableFunctionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAvailableFunctionsType_Function(), this.getAvailableFunctionType(), null, "function", null, 1, -1, AvailableFunctionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(availableFunctionTypeEClass, AvailableFunctionType.class, "AvailableFunctionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAvailableFunctionType_Metadata(), theOws11Package.getMetadataType(), null, "metadata", null, 0, 1, AvailableFunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAvailableFunctionType_Returns(), theXMLTypePackage.getQName(), "returns", null, 1, 1, AvailableFunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAvailableFunctionType_Arguments(), this.getArgumentsType(), null, "arguments", null, 0, 1, AvailableFunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAvailableFunctionType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, AvailableFunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(bboxTypeEClass, BBOXType.class, "BBOXType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBBOXType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, 1, BBOXType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBBOXType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, 1, BBOXType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBBOXType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 1, 1, BBOXType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(binaryComparisonOpTypeEClass, BinaryComparisonOpType.class, "BinaryComparisonOpType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBinaryComparisonOpType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 2, 2, BinaryComparisonOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryComparisonOpType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 2, 2, BinaryComparisonOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryComparisonOpType_MatchAction(), this.getMatchActionType(), "matchAction", "Any", 0, 1, BinaryComparisonOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryComparisonOpType_MatchCase(), theXMLTypePackage.getBoolean(), "matchCase", "true", 0, 1, BinaryComparisonOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(binaryLogicOpTypeEClass, BinaryLogicOpType.class, "BinaryLogicOpType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBinaryLogicOpType_FilterPredicates(), theEcorePackage.getEFeatureMapEntry(), "filterPredicates", null, 0, -1, BinaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryLogicOpType_ComparisonOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "comparisonOpsGroup", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryLogicOpType_ComparisonOps(), this.getComparisonOpsType(), null, "comparisonOps", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryLogicOpType_SpatialOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "spatialOpsGroup", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryLogicOpType_SpatialOps(), this.getSpatialOpsType(), null, "spatialOps", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryLogicOpType_TemporalOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "temporalOpsGroup", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryLogicOpType_TemporalOps(), this.getTemporalOpsType(), null, "temporalOps", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryLogicOpType_LogicOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "logicOpsGroup", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryLogicOpType_LogicOps(), this.getLogicOpsType(), null, "logicOps", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryLogicOpType_ExtensionOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "extensionOpsGroup", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryLogicOpType_ExtensionOps(), this.getExtensionOpsType(), null, "extensionOps", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryLogicOpType_Function(), this.getFunctionType(), null, "function", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryLogicOpType_IdGroup(), theEcorePackage.getEFeatureMapEntry(), "idGroup", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryLogicOpType_Id(), this.getAbstractIdType(), null, "id", null, 0, -1, BinaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(binarySpatialOpTypeEClass, BinarySpatialOpType.class, "BinarySpatialOpType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBinarySpatialOpType_ValueReference(), theXMLTypePackage.getString(), "valueReference", null, 1, 1, BinarySpatialOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinarySpatialOpType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, 1, BinarySpatialOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBinarySpatialOpType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, 1, BinarySpatialOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinarySpatialOpType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, 1, BinarySpatialOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(binaryTemporalOpTypeEClass, BinaryTemporalOpType.class, "BinaryTemporalOpType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBinaryTemporalOpType_ValueReference(), theXMLTypePackage.getString(), "valueReference", null, 1, 1, BinaryTemporalOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryTemporalOpType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, 1, BinaryTemporalOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBinaryTemporalOpType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, 1, BinaryTemporalOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryTemporalOpType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, 1, BinaryTemporalOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(comparisonOperatorsTypeEClass, ComparisonOperatorsType.class, "ComparisonOperatorsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComparisonOperatorsType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, ComparisonOperatorsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComparisonOperatorsType_ComparisonOperator(), this.getComparisonOperatorType(), null, "comparisonOperator", null, 1, -1, ComparisonOperatorsType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(comparisonOperatorTypeEClass, ComparisonOperatorType.class, "ComparisonOperatorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComparisonOperatorType_Name(), this.getComparisonOperatorNameType(), "name", null, 1, 1, ComparisonOperatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(comparisonOpsTypeEClass, ComparisonOpsType.class, "ComparisonOpsType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(conformanceTypeEClass, ConformanceType.class, "ConformanceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConformanceType_Constraint(), theOws11Package.getDomainType(), null, "constraint", null, 1, -1, ConformanceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(distanceBufferTypeEClass, DistanceBufferType.class, "DistanceBufferType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDistanceBufferType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, 1, DistanceBufferType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDistanceBufferType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, 1, DistanceBufferType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDistanceBufferType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 1, 1, DistanceBufferType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDistanceBufferType_Distance(), this.getMeasureType(), null, "distance", null, 1, 1, DistanceBufferType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Id(), this.getAbstractIdType(), null, "id", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractAdhocQueryExpression(), this.getAbstractAdhocQueryExpressionType(), null, "abstractAdhocQueryExpression", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractQueryExpression(), this.getAbstractQueryExpressionType(), null, "abstractQueryExpression", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractProjectionClause(), theEcorePackage.getEObject(), null, "abstractProjectionClause", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractSelectionClause(), theEcorePackage.getEObject(), null, "abstractSelectionClause", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractSortingClause(), theEcorePackage.getEObject(), null, "abstractSortingClause", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_After(), this.getBinaryTemporalOpType(), null, "after", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TemporalOps(), this.getTemporalOpsType(), null, "temporalOps", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_And(), this.getBinaryLogicOpType(), null, "and", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_LogicOps(), this.getLogicOpsType(), null, "logicOps", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AnyInteracts(), this.getBinaryTemporalOpType(), null, "anyInteracts", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_BBOX(), this.getBBOXType(), null, "bBOX", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_SpatialOps(), this.getSpatialOpsType(), null, "spatialOps", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Before(), this.getBinaryTemporalOpType(), null, "before", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Begins(), this.getBinaryTemporalOpType(), null, "begins", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_BegunBy(), this.getBinaryTemporalOpType(), null, "begunBy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Beyond(), this.getDistanceBufferType(), null, "beyond", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ComparisonOps(), this.getComparisonOpsType(), null, "comparisonOps", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Contains(), this.getBinarySpatialOpType(), null, "contains", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Crosses(), this.getBinarySpatialOpType(), null, "crosses", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Disjoint(), this.getBinarySpatialOpType(), null, "disjoint", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_During(), this.getBinaryTemporalOpType(), null, "during", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DWithin(), this.getDistanceBufferType(), null, "dWithin", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_EndedBy(), this.getBinaryTemporalOpType(), null, "endedBy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Ends(), this.getBinaryTemporalOpType(), null, "ends", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Equals(), this.getBinarySpatialOpType(), null, "equals", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ExtensionOps(), this.getExtensionOpsType(), null, "extensionOps", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Filter(), this.getFilterType(), null, "filter", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_FilterCapabilities(), this.getFilterCapabilitiesType(), null, "filterCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Function(), this.getFunctionType(), null, "function", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Intersects(), this.getBinarySpatialOpType(), null, "intersects", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Literal(), this.getLiteralType(), null, "literal", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_LogicalOperators(), this.getLogicalOperatorsType(), null, "logicalOperators", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Meets(), this.getBinaryTemporalOpType(), null, "meets", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_MetBy(), this.getBinaryTemporalOpType(), null, "metBy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Not(), this.getUnaryLogicOpType(), null, "not", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Or(), this.getBinaryLogicOpType(), null, "or", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_OverlappedBy(), this.getBinaryTemporalOpType(), null, "overlappedBy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Overlaps(), this.getBinarySpatialOpType(), null, "overlaps", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsBetween(), this.getPropertyIsBetweenType(), null, "propertyIsBetween", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsEqualTo(), this.getBinaryComparisonOpType(), null, "propertyIsEqualTo", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsGreaterThan(), this.getBinaryComparisonOpType(), null, "propertyIsGreaterThan", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsGreaterThanOrEqualTo(), this.getBinaryComparisonOpType(), null, "propertyIsGreaterThanOrEqualTo", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsLessThan(), this.getBinaryComparisonOpType(), null, "propertyIsLessThan", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsLessThanOrEqualTo(), this.getBinaryComparisonOpType(), null, "propertyIsLessThanOrEqualTo", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsLike(), this.getPropertyIsLikeType(), null, "propertyIsLike", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsNil(), this.getPropertyIsNilType(), null, "propertyIsNil", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsNotEqualTo(), this.getBinaryComparisonOpType(), null, "propertyIsNotEqualTo", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyIsNull(), this.getPropertyIsNullType(), null, "propertyIsNull", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ResourceId(), this.getResourceIdType(), null, "resourceId", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_SortBy(), this.getSortByType(), null, "sortBy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TContains(), this.getBinaryTemporalOpType(), null, "tContains", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TEquals(), this.getBinaryTemporalOpType(), null, "tEquals", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Touches(), this.getBinarySpatialOpType(), null, "touches", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TOverlaps(), this.getBinaryTemporalOpType(), null, "tOverlaps", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_ValueReference(), theXMLTypePackage.getString(), "valueReference", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Within(), this.getBinarySpatialOpType(), null, "within", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(extendedCapabilitiesTypeEClass, ExtendedCapabilitiesType.class, "ExtendedCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExtendedCapabilitiesType_AdditionalOperators(), this.getAdditionalOperatorsType(), null, "additionalOperators", null, 0, 1, ExtendedCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(extensionOperatorTypeEClass, ExtensionOperatorType.class, "ExtensionOperatorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getExtensionOperatorType_Name(), theXMLTypePackage.getQName(), "name", null, 1, 1, ExtensionOperatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(extensionOpsTypeEClass, ExtensionOpsType.class, "ExtensionOpsType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(filterCapabilitiesTypeEClass, FilterCapabilitiesType.class, "FilterCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFilterCapabilitiesType_Conformance(), this.getConformanceType(), null, "conformance", null, 1, 1, FilterCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterCapabilitiesType_IdCapabilities(), this.getIdCapabilitiesType(), null, "idCapabilities", null, 0, 1, FilterCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterCapabilitiesType_ScalarCapabilities(), this.getScalarCapabilitiesType(), null, "scalarCapabilities", null, 0, 1, FilterCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterCapabilitiesType_SpatialCapabilities(), this.getSpatialCapabilitiesType(), null, "spatialCapabilities", null, 0, 1, FilterCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterCapabilitiesType_TemporalCapabilities(), this.getTemporalCapabilitiesType(), null, "temporalCapabilities", null, 0, 1, FilterCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterCapabilitiesType_Functions(), this.getAvailableFunctionsType(), null, "functions", null, 0, 1, FilterCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterCapabilitiesType_ExtendedCapabilities(), this.getExtendedCapabilitiesType(), null, "extendedCapabilities", null, 0, 1, FilterCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(filterTypeEClass, FilterType.class, "FilterType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFilterType_ComparisonOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "comparisonOpsGroup", null, 0, 1, FilterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterType_ComparisonOps(), this.getComparisonOpsType(), null, "comparisonOps", null, 0, 1, FilterType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getFilterType_SpatialOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "spatialOpsGroup", null, 0, 1, FilterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterType_SpatialOps(), this.getSpatialOpsType(), null, "spatialOps", null, 0, 1, FilterType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getFilterType_TemporalOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "temporalOpsGroup", null, 0, 1, FilterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterType_TemporalOps(), this.getTemporalOpsType(), null, "temporalOps", null, 0, 1, FilterType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getFilterType_LogicOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "logicOpsGroup", null, 0, 1, FilterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterType_LogicOps(), this.getLogicOpsType(), null, "logicOps", null, 0, 1, FilterType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getFilterType_ExtensionOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "extensionOpsGroup", null, 0, 1, FilterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterType_ExtensionOps(), this.getExtensionOpsType(), null, "extensionOps", null, 0, 1, FilterType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getFilterType_Function(), this.getFunctionType(), null, "function", null, 0, 1, FilterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFilterType_IdGroup(), theEcorePackage.getEFeatureMapEntry(), "idGroup", null, 0, -1, FilterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFilterType_Id(), this.getAbstractIdType(), null, "id", null, 0, -1, FilterType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(functionTypeEClass, FunctionType.class, "FunctionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFunctionType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, -1, FunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFunctionType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, -1, FunctionType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getFunctionType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, FunctionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(geometryOperandsTypeEClass, GeometryOperandsType.class, "GeometryOperandsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGeometryOperandsType_GeometryOperand(), this.getGeometryOperandType(), null, "geometryOperand", null, 1, -1, GeometryOperandsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(geometryOperandTypeEClass, GeometryOperandType.class, "GeometryOperandType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGeometryOperandType_Name(), theXMLTypePackage.getQName(), "name", null, 1, 1, GeometryOperandType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(idCapabilitiesTypeEClass, IdCapabilitiesType.class, "IdCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIdCapabilitiesType_ResourceIdentifier(), this.getResourceIdentifierType(), null, "resourceIdentifier", null, 1, -1, IdCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(literalTypeEClass, LiteralType.class, "LiteralType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLiteralType_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, LiteralType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLiteralType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, 1, LiteralType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getLiteralType_Type(), theXMLTypePackage.getQName(), "type", null, 0, 1, LiteralType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(logicalOperatorsTypeEClass, LogicalOperatorsType.class, "LogicalOperatorsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(logicOpsTypeEClass, LogicOpsType.class, "LogicOpsType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(lowerBoundaryTypeEClass, LowerBoundaryType.class, "LowerBoundaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLowerBoundaryType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, 1, LowerBoundaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLowerBoundaryType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, 1, LowerBoundaryType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(measureTypeEClass, MeasureType.class, "MeasureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMeasureType_Value(), theXMLTypePackage.getDouble(), "value", null, 0, 1, MeasureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMeasureType_Uom(), this.getUomIdentifier(), "uom", null, 1, 1, MeasureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(propertyIsBetweenTypeEClass, PropertyIsBetweenType.class, "PropertyIsBetweenType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPropertyIsBetweenType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 1, 1, PropertyIsBetweenType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPropertyIsBetweenType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 1, 1, PropertyIsBetweenType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getPropertyIsBetweenType_LowerBoundary(), this.getLowerBoundaryType(), null, "lowerBoundary", null, 1, 1, PropertyIsBetweenType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPropertyIsBetweenType_UpperBoundary(), this.getUpperBoundaryType(), null, "upperBoundary", null, 1, 1, PropertyIsBetweenType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(propertyIsLikeTypeEClass, PropertyIsLikeType.class, "PropertyIsLikeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPropertyIsLikeType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 2, 2, PropertyIsLikeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPropertyIsLikeType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 2, 2, PropertyIsLikeType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyIsLikeType_EscapeChar(), theXMLTypePackage.getString(), "escapeChar", null, 1, 1, PropertyIsLikeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyIsLikeType_SingleChar(), theXMLTypePackage.getString(), "singleChar", null, 1, 1, PropertyIsLikeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyIsLikeType_WildCard(), theXMLTypePackage.getString(), "wildCard", null, 1, 1, PropertyIsLikeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(propertyIsNilTypeEClass, PropertyIsNilType.class, "PropertyIsNilType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPropertyIsNilType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, 1, PropertyIsNilType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPropertyIsNilType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, 1, PropertyIsNilType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyIsNilType_NilReason(), theXMLTypePackage.getString(), "nilReason", null, 0, 1, PropertyIsNilType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(propertyIsNullTypeEClass, PropertyIsNullType.class, "PropertyIsNullType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPropertyIsNullType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 0, 1, PropertyIsNullType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPropertyIsNullType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 0, 1, PropertyIsNullType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(resourceIdentifierTypeEClass, ResourceIdentifierType.class, "ResourceIdentifierType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResourceIdentifierType_Metadata(), theOws11Package.getMetadataType(), null, "metadata", null, 0, 1, ResourceIdentifierType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceIdentifierType_Name(), theXMLTypePackage.getQName(), "name", null, 1, 1, ResourceIdentifierType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(resourceIdTypeEClass, ResourceIdType.class, "ResourceIdType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getResourceIdType_EndDate(), theXMLTypePackage.getDateTime(), "endDate", null, 0, 1, ResourceIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceIdType_PreviousRid(), theXMLTypePackage.getString(), "previousRid", null, 0, 1, ResourceIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceIdType_Rid(), theXMLTypePackage.getString(), "rid", null, 1, 1, ResourceIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceIdType_StartDate(), theXMLTypePackage.getDateTime(), "startDate", null, 0, 1, ResourceIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceIdType_Version(), this.getVersionType(), "version", null, 0, 1, ResourceIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(scalarCapabilitiesTypeEClass, ScalarCapabilitiesType.class, "ScalarCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getScalarCapabilitiesType_LogicalOperators(), this.getLogicalOperatorsType(), null, "logicalOperators", null, 0, 1, ScalarCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getScalarCapabilitiesType_ComparisonOperators(), this.getComparisonOperatorsType(), null, "comparisonOperators", null, 0, 1, ScalarCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sortByTypeEClass, SortByType.class, "SortByType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSortByType_SortProperty(), this.getSortPropertyType(), null, "sortProperty", null, 1, -1, SortByType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sortPropertyTypeEClass, SortPropertyType.class, "SortPropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSortPropertyType_ValueReference(), theXMLTypePackage.getString(), "valueReference", null, 1, 1, SortPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSortPropertyType_SortOrder(), this.getSortOrderType(), "sortOrder", null, 0, 1, SortPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(spatialCapabilitiesTypeEClass, SpatialCapabilitiesType.class, "SpatialCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSpatialCapabilitiesType_GeometryOperands(), this.getGeometryOperandsType(), null, "geometryOperands", null, 1, 1, SpatialCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSpatialCapabilitiesType_SpatialOperators(), this.getSpatialOperatorsType(), null, "spatialOperators", null, 1, 1, SpatialCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(spatialOperatorsTypeEClass, SpatialOperatorsType.class, "SpatialOperatorsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSpatialOperatorsType_SpatialOperator(), this.getSpatialOperatorType(), null, "spatialOperator", null, 1, -1, SpatialOperatorsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(spatialOperatorTypeEClass, SpatialOperatorType.class, "SpatialOperatorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSpatialOperatorType_GeometryOperands(), this.getGeometryOperandsType(), null, "geometryOperands", null, 0, 1, SpatialOperatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSpatialOperatorType_Name(), this.getSpatialOperatorNameType(), "name", null, 0, 1, SpatialOperatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(spatialOpsTypeEClass, SpatialOpsType.class, "SpatialOpsType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(temporalCapabilitiesTypeEClass, TemporalCapabilitiesType.class, "TemporalCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTemporalCapabilitiesType_TemporalOperands(), this.getTemporalOperandsType(), null, "temporalOperands", null, 1, 1, TemporalCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemporalCapabilitiesType_TemporalOperators(), this.getTemporalOperatorsType(), null, "temporalOperators", null, 1, 1, TemporalCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(temporalOperandsTypeEClass, TemporalOperandsType.class, "TemporalOperandsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTemporalOperandsType_TemporalOperand(), this.getTemporalOperandType(), null, "temporalOperand", null, 1, -1, TemporalOperandsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(temporalOperandTypeEClass, TemporalOperandType.class, "TemporalOperandType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTemporalOperandType_Name(), theXMLTypePackage.getQName(), "name", null, 1, 1, TemporalOperandType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(temporalOperatorsTypeEClass, TemporalOperatorsType.class, "TemporalOperatorsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTemporalOperatorsType_TemporalOperator(), this.getTemporalOperatorType(), null, "temporalOperator", null, 1, -1, TemporalOperatorsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(temporalOperatorTypeEClass, TemporalOperatorType.class, "TemporalOperatorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTemporalOperatorType_TemporalOperands(), this.getTemporalOperandsType(), null, "temporalOperands", null, 0, 1, TemporalOperatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTemporalOperatorType_Name(), this.getTemporalOperatorNameType(), "name", null, 1, 1, TemporalOperatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(temporalOpsTypeEClass, TemporalOpsType.class, "TemporalOpsType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(unaryLogicOpTypeEClass, UnaryLogicOpType.class, "UnaryLogicOpType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUnaryLogicOpType_ComparisonOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "comparisonOpsGroup", null, 0, 1, UnaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryLogicOpType_ComparisonOps(), this.getComparisonOpsType(), null, "comparisonOps", null, 0, 1, UnaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getUnaryLogicOpType_SpatialOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "spatialOpsGroup", null, 0, 1, UnaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryLogicOpType_SpatialOps(), this.getSpatialOpsType(), null, "spatialOps", null, 0, 1, UnaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getUnaryLogicOpType_TemporalOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "temporalOpsGroup", null, 0, 1, UnaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryLogicOpType_TemporalOps(), this.getTemporalOpsType(), null, "temporalOps", null, 0, 1, UnaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getUnaryLogicOpType_LogicOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "logicOpsGroup", null, 0, 1, UnaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryLogicOpType_LogicOps(), this.getLogicOpsType(), null, "logicOps", null, 0, 1, UnaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getUnaryLogicOpType_ExtensionOpsGroup(), theEcorePackage.getEFeatureMapEntry(), "extensionOpsGroup", null, 0, 1, UnaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryLogicOpType_ExtensionOps(), this.getExtensionOpsType(), null, "extensionOps", null, 0, 1, UnaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryLogicOpType_Function(), this.getFunctionType(), null, "function", null, 0, 1, UnaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUnaryLogicOpType_IdGroup(), theEcorePackage.getEFeatureMapEntry(), "idGroup", null, 0, -1, UnaryLogicOpType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnaryLogicOpType_Id(), this.getAbstractIdType(), null, "id", null, 0, -1, UnaryLogicOpType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(upperBoundaryTypeEClass, UpperBoundaryType.class, "UpperBoundaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUpperBoundaryType_ExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "expressionGroup", null, 1, 1, UpperBoundaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUpperBoundaryType_Expression(), theEcorePackage.getEObject(), null, "expression", null, 1, 1, UpperBoundaryType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.class, "ComparisonOperatorNameTypeMember0");
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_EQUAL_TO);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_NOT_EQUAL_TO);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_LESS_THAN);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_GREATER_THAN);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_LESS_THAN_OR_EQUAL_TO);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_LIKE);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_NULL);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_NIL);
        addEEnumLiteral(comparisonOperatorNameTypeMember0EEnum, ComparisonOperatorNameTypeMember0.PROPERTY_IS_BETWEEN);

        initEEnum(matchActionTypeEEnum, MatchActionType.class, "MatchActionType");
        addEEnumLiteral(matchActionTypeEEnum, MatchActionType.ALL);
        addEEnumLiteral(matchActionTypeEEnum, MatchActionType.ANY);
        addEEnumLiteral(matchActionTypeEEnum, MatchActionType.ONE);

        initEEnum(sortOrderTypeEEnum, SortOrderType.class, "SortOrderType");
        addEEnumLiteral(sortOrderTypeEEnum, SortOrderType.DESC);
        addEEnumLiteral(sortOrderTypeEEnum, SortOrderType.ASC);

        initEEnum(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.class, "SpatialOperatorNameTypeMember0");
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.BBOX);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.EQUALS);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.DISJOINT);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.INTERSECTS);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.TOUCHES);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.CROSSES);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.WITHIN);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.CONTAINS);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.OVERLAPS);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.BEYOND);
        addEEnumLiteral(spatialOperatorNameTypeMember0EEnum, SpatialOperatorNameTypeMember0.DWITHIN);

        initEEnum(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.class, "TemporalOperatorNameTypeMember0");
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.AFTER);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.BEFORE);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.BEGINS);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.BEGUN_BY);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.TCONTAINS);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.DURING);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.TEQUALS);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.TOVERLAPS);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.MEETS);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.OVERLAPPED_BY);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.MET_BY);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.ENDS);
        addEEnumLiteral(temporalOperatorNameTypeMember0EEnum, TemporalOperatorNameTypeMember0.ENDED_BY);

        initEEnum(versionActionTokensEEnum, VersionActionTokens.class, "VersionActionTokens");
        addEEnumLiteral(versionActionTokensEEnum, VersionActionTokens.FIRST);
        addEEnumLiteral(versionActionTokensEEnum, VersionActionTokens.LAST);
        addEEnumLiteral(versionActionTokensEEnum, VersionActionTokens.PREVIOUS);
        addEEnumLiteral(versionActionTokensEEnum, VersionActionTokens.NEXT);
        addEEnumLiteral(versionActionTokensEEnum, VersionActionTokens.ALL);

        // Initialize data types
        initEDataType(aliasesTypeEDataType, List.class, "AliasesType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(comparisonOperatorNameTypeEDataType, Object.class, "ComparisonOperatorNameType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(comparisonOperatorNameTypeMember0ObjectEDataType, ComparisonOperatorNameTypeMember0.class, "ComparisonOperatorNameTypeMember0Object", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(comparisonOperatorNameTypeMember1EDataType, String.class, "ComparisonOperatorNameTypeMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(matchActionTypeObjectEDataType, MatchActionType.class, "MatchActionTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(schemaElementEDataType, String.class, "SchemaElement", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(sortOrderTypeObjectEDataType, SortOrderType.class, "SortOrderTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(spatialOperatorNameTypeEDataType, Object.class, "SpatialOperatorNameType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(spatialOperatorNameTypeMember0ObjectEDataType, SpatialOperatorNameTypeMember0.class, "SpatialOperatorNameTypeMember0Object", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(spatialOperatorNameTypeMember1EDataType, String.class, "SpatialOperatorNameTypeMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(temporalOperatorNameTypeEDataType, Object.class, "TemporalOperatorNameType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(temporalOperatorNameTypeMember0ObjectEDataType, TemporalOperatorNameTypeMember0.class, "TemporalOperatorNameTypeMember0Object", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(temporalOperatorNameTypeMember1EDataType, String.class, "TemporalOperatorNameTypeMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(typeNamesListTypeEDataType, List.class, "TypeNamesListType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(typeNamesTypeEDataType, Object.class, "TypeNamesType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(uomIdentifierEDataType, String.class, "UomIdentifier", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(uomSymbolEDataType, String.class, "UomSymbol", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(uomURIEDataType, String.class, "UomURI", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(versionActionTokensObjectEDataType, VersionActionTokens.class, "VersionActionTokensObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(versionTypeEDataType, Object.class, "VersionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // urn:opengis:specification:gml:schema-xlinks:v3.0c2
        createUrnopengisspecificationgmlschemaxlinksv3Annotations();
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>urn:opengis:specification:gml:schema-xlinks:v3.0c2</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createUrnopengisspecificationgmlschemaxlinksv3Annotations() {
        String source = "urn:opengis:specification:gml:schema-xlinks:v3.0c2";		
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "xlinks.xsd v3.0b2 2001-07"
           });																																																																																																																																																																																																																																																																																									
    }

    /**
     * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createExtendedMetaDataAnnotations() {
        String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";				
        addAnnotation
          (abstractAdhocQueryExpressionTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractAdhocQueryExpressionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (abstractIdTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractIdType",
             "kind", "empty"
           });		
        addAnnotation
          (abstractProjectionClauseTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractProjectionClauseType",
             "kind", "empty"
           });		
        addAnnotation
          (abstractQueryExpressionTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractQueryExpressionType",
             "kind", "empty"
           });		
        addAnnotation
          (getAbstractQueryExpressionType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });		
        addAnnotation
          (abstractSelectionClauseTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractSelectionClauseType",
             "kind", "empty"
           });		
        addAnnotation
          (abstractSortingClauseTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractSortingClauseType",
             "kind", "empty"
           });		
        addAnnotation
          (additionalOperatorsTypeEClass, 
           source, 
           new String[] {
             "name", "AdditionalOperatorsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAdditionalOperatorsType_Operator(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operator",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (argumentsTypeEClass, 
           source, 
           new String[] {
             "name", "ArgumentsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getArgumentsType_Argument(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Argument",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (argumentTypeEClass, 
           source, 
           new String[] {
             "name", "ArgumentType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getArgumentType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getArgumentType_Type(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getArgumentType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (availableFunctionsTypeEClass, 
           source, 
           new String[] {
             "name", "AvailableFunctionsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAvailableFunctionsType_Function(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Function",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (availableFunctionTypeEClass, 
           source, 
           new String[] {
             "name", "AvailableFunctionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAvailableFunctionType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getAvailableFunctionType_Returns(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Returns",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAvailableFunctionType_Arguments(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Arguments",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAvailableFunctionType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (bboxTypeEClass, 
           source, 
           new String[] {
             "name", "BBOXType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getBBOXType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getBBOXType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getBBOXType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":2",
             "processing", "strict"
           });		
        addAnnotation
          (binaryComparisonOpTypeEClass, 
           source, 
           new String[] {
             "name", "BinaryComparisonOpType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getBinaryComparisonOpType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getBinaryComparisonOpType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getBinaryComparisonOpType_MatchAction(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "matchAction"
           });		
        addAnnotation
          (getBinaryComparisonOpType_MatchCase(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "matchCase"
           });		
        addAnnotation
          (binaryLogicOpTypeEClass, 
           source, 
           new String[] {
             "name", "BinaryLogicOpType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getBinaryLogicOpType_FilterPredicates(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_ComparisonOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "comparisonOps:group",
             "namespace", "##targetNamespace",
             "group", "#FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_ComparisonOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "comparisonOps",
             "namespace", "##targetNamespace",
             "group", "comparisonOps:group"
           });		
        addAnnotation
          (getBinaryLogicOpType_SpatialOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "spatialOps:group",
             "namespace", "##targetNamespace",
             "group", "#FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_SpatialOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "spatialOps",
             "namespace", "##targetNamespace",
             "group", "spatialOps:group"
           });		
        addAnnotation
          (getBinaryLogicOpType_TemporalOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "temporalOps:group",
             "namespace", "##targetNamespace",
             "group", "#FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_TemporalOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "temporalOps",
             "namespace", "##targetNamespace",
             "group", "temporalOps:group"
           });		
        addAnnotation
          (getBinaryLogicOpType_LogicOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "logicOps:group",
             "namespace", "##targetNamespace",
             "group", "#FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_LogicOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "logicOps",
             "namespace", "##targetNamespace",
             "group", "logicOps:group"
           });		
        addAnnotation
          (getBinaryLogicOpType_ExtensionOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "extensionOps:group",
             "namespace", "##targetNamespace",
             "group", "#FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_ExtensionOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "extensionOps",
             "namespace", "##targetNamespace",
             "group", "extensionOps:group"
           });		
        addAnnotation
          (getBinaryLogicOpType_Function(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Function",
             "namespace", "##targetNamespace",
             "group", "#FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_IdGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "_Id:group",
             "namespace", "##targetNamespace",
             "group", "#FilterPredicates:0"
           });		
        addAnnotation
          (getBinaryLogicOpType_Id(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "_Id",
             "namespace", "##targetNamespace",
             "group", "_Id:group"
           });		
        addAnnotation
          (binarySpatialOpTypeEClass, 
           source, 
           new String[] {
             "name", "BinarySpatialOpType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getBinarySpatialOpType_ValueReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueReference",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getBinarySpatialOpType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getBinarySpatialOpType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getBinarySpatialOpType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":3",
             "processing", "strict"
           });		
        addAnnotation
          (binaryTemporalOpTypeEClass, 
           source, 
           new String[] {
             "name", "BinaryTemporalOpType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getBinaryTemporalOpType_ValueReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueReference",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getBinaryTemporalOpType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getBinaryTemporalOpType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getBinaryTemporalOpType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":3",
             "processing", "strict"
           });		
        addAnnotation
          (comparisonOperatorsTypeEClass, 
           source, 
           new String[] {
             "name", "ComparisonOperatorsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getComparisonOperatorsType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });		
        addAnnotation
          (getComparisonOperatorsType_ComparisonOperator(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ComparisonOperator",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });		
        addAnnotation
          (comparisonOperatorTypeEClass, 
           source, 
           new String[] {
             "name", "ComparisonOperatorType",
             "kind", "empty"
           });		
        addAnnotation
          (getComparisonOperatorType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (comparisonOpsTypeEClass, 
           source, 
           new String[] {
             "name", "ComparisonOpsType",
             "kind", "empty"
           });		
        addAnnotation
          (conformanceTypeEClass, 
           source, 
           new String[] {
             "name", "ConformanceType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getConformanceType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (distanceBufferTypeEClass, 
           source, 
           new String[] {
             "name", "DistanceBufferType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDistanceBufferType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDistanceBufferType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getDistanceBufferType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":2",
             "processing", "strict"
           });		
        addAnnotation
          (getDistanceBufferType_Distance(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Distance",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (documentRootEClass, 
           source, 
           new String[] {
             "name", "",
             "kind", "mixed"
           });		
        addAnnotation
          (getDocumentRoot_Mixed(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "name", ":mixed"
           });		
        addAnnotation
          (getDocumentRoot_XMLNSPrefixMap(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "xmlns:prefix"
           });		
        addAnnotation
          (getDocumentRoot_XSISchemaLocation(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "xsi:schemaLocation"
           });		
        addAnnotation
          (getDocumentRoot_Id(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "_Id",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AbstractAdhocQueryExpression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractAdhocQueryExpression",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractQueryExpression"
           });		
        addAnnotation
          (getDocumentRoot_AbstractQueryExpression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractQueryExpression",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AbstractProjectionClause(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractProjectionClause",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AbstractSelectionClause(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractSelectionClause",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AbstractSortingClause(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractSortingClause",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_After(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "After",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_TemporalOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "temporalOps",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_And(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "And",
             "namespace", "##targetNamespace",
             "affiliation", "logicOps"
           });		
        addAnnotation
          (getDocumentRoot_LogicOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "logicOps",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AnyInteracts(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AnyInteracts",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_BBOX(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BBOX",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_SpatialOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "spatialOps",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Before(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Before",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_Begins(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Begins",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_BegunBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BegunBy",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_Beyond(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Beyond",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_ComparisonOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "comparisonOps",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Contains(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Contains",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_Crosses(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Crosses",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_Disjoint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Disjoint",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_During(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "During",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_DWithin(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DWithin",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_EndedBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "EndedBy",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_Ends(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Ends",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_Equals(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Equals",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_ExtensionOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "extensionOps",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Filter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractSelectionClause"
           });		
        addAnnotation
          (getDocumentRoot_FilterCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Function(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Function",
             "namespace", "##targetNamespace",
             "affiliation", "expression"
           });		
        addAnnotation
          (getDocumentRoot_Intersects(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Intersects",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_Literal(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Literal",
             "namespace", "##targetNamespace",
             "affiliation", "expression"
           });		
        addAnnotation
          (getDocumentRoot_LogicalOperators(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LogicalOperators",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Meets(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Meets",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_MetBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MetBy",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_Not(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Not",
             "namespace", "##targetNamespace",
             "affiliation", "logicOps"
           });		
        addAnnotation
          (getDocumentRoot_Or(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Or",
             "namespace", "##targetNamespace",
             "affiliation", "logicOps"
           });		
        addAnnotation
          (getDocumentRoot_OverlappedBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OverlappedBy",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_Overlaps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Overlaps",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsBetween(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsBetween",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsEqualTo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsEqualTo",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsGreaterThan(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsGreaterThan",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsGreaterThanOrEqualTo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsGreaterThanOrEqualTo",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsLessThan(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsLessThan",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsLessThanOrEqualTo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsLessThanOrEqualTo",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsLike(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsLike",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsNil(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsNil",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsNotEqualTo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsNotEqualTo",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_PropertyIsNull(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyIsNull",
             "namespace", "##targetNamespace",
             "affiliation", "comparisonOps"
           });		
        addAnnotation
          (getDocumentRoot_ResourceId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResourceId",
             "namespace", "##targetNamespace",
             "affiliation", "_Id"
           });		
        addAnnotation
          (getDocumentRoot_SortBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SortBy",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractSortingClause"
           });		
        addAnnotation
          (getDocumentRoot_TContains(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TContains",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_TEquals(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TEquals",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_Touches(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Touches",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (getDocumentRoot_TOverlaps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TOverlaps",
             "namespace", "##targetNamespace",
             "affiliation", "temporalOps"
           });		
        addAnnotation
          (getDocumentRoot_ValueReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueReference",
             "namespace", "##targetNamespace",
             "affiliation", "expression"
           });		
        addAnnotation
          (getDocumentRoot_Within(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Within",
             "namespace", "##targetNamespace",
             "affiliation", "spatialOps"
           });		
        addAnnotation
          (extendedCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Extended_CapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getExtendedCapabilitiesType_AdditionalOperators(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AdditionalOperators",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (extensionOperatorTypeEClass, 
           source, 
           new String[] {
             "name", "ExtensionOperatorType",
             "kind", "empty"
           });		
        addAnnotation
          (getExtensionOperatorType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (extensionOpsTypeEClass, 
           source, 
           new String[] {
             "name", "ExtensionOpsType",
             "kind", "empty"
           });		
        addAnnotation
          (filterCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Filter_Capabilities_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFilterCapabilitiesType_Conformance(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Conformance",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterCapabilitiesType_IdCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Id_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterCapabilitiesType_ScalarCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Scalar_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterCapabilitiesType_SpatialCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Spatial_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterCapabilitiesType_TemporalCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Temporal_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterCapabilitiesType_Functions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Functions",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterCapabilitiesType_ExtendedCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Extended_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (filterTypeEClass, 
           source, 
           new String[] {
             "name", "FilterType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFilterType_ComparisonOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "comparisonOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterType_ComparisonOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "comparisonOps",
             "namespace", "##targetNamespace",
             "group", "comparisonOps:group"
           });		
        addAnnotation
          (getFilterType_SpatialOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "spatialOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterType_SpatialOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "spatialOps",
             "namespace", "##targetNamespace",
             "group", "spatialOps:group"
           });		
        addAnnotation
          (getFilterType_TemporalOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "temporalOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterType_TemporalOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "temporalOps",
             "namespace", "##targetNamespace",
             "group", "temporalOps:group"
           });		
        addAnnotation
          (getFilterType_LogicOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "logicOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterType_LogicOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "logicOps",
             "namespace", "##targetNamespace",
             "group", "logicOps:group"
           });		
        addAnnotation
          (getFilterType_ExtensionOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "extensionOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterType_ExtensionOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "extensionOps",
             "namespace", "##targetNamespace",
             "group", "extensionOps:group"
           });		
        addAnnotation
          (getFilterType_Function(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Function",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterType_IdGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "_Id:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFilterType_Id(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "_Id",
             "namespace", "##targetNamespace",
             "group", "_Id:group"
           });		
        addAnnotation
          (functionTypeEClass, 
           source, 
           new String[] {
             "name", "FunctionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFunctionType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFunctionType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getFunctionType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (geometryOperandsTypeEClass, 
           source, 
           new String[] {
             "name", "GeometryOperandsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGeometryOperandsType_GeometryOperand(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GeometryOperand",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (geometryOperandTypeEClass, 
           source, 
           new String[] {
             "name", "GeometryOperand_._type",
             "kind", "empty"
           });		
        addAnnotation
          (getGeometryOperandType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (idCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Id_CapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getIdCapabilitiesType_ResourceIdentifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResourceIdentifier",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (literalTypeEClass, 
           source, 
           new String[] {
             "name", "LiteralType",
             "kind", "mixed"
           });		
        addAnnotation
          (getLiteralType_Mixed(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "name", ":mixed"
           });		
        addAnnotation
          (getLiteralType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##any",
             "name", ":1",
             "processing", "strict"
           });		
        addAnnotation
          (getLiteralType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type"
           });		
        addAnnotation
          (logicalOperatorsTypeEClass, 
           source, 
           new String[] {
             "name", "LogicalOperators_._type",
             "kind", "empty"
           });		
        addAnnotation
          (logicOpsTypeEClass, 
           source, 
           new String[] {
             "name", "LogicOpsType",
             "kind", "empty"
           });		
        addAnnotation
          (lowerBoundaryTypeEClass, 
           source, 
           new String[] {
             "name", "LowerBoundaryType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getLowerBoundaryType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getLowerBoundaryType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (measureTypeEClass, 
           source, 
           new String[] {
             "name", "MeasureType",
             "kind", "simple"
           });		
        addAnnotation
          (getMeasureType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getMeasureType_Uom(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "uom"
           });		
        addAnnotation
          (propertyIsBetweenTypeEClass, 
           source, 
           new String[] {
             "name", "PropertyIsBetweenType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getPropertyIsBetweenType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getPropertyIsBetweenType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getPropertyIsBetweenType_LowerBoundary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LowerBoundary",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getPropertyIsBetweenType_UpperBoundary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UpperBoundary",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (propertyIsLikeTypeEClass, 
           source, 
           new String[] {
             "name", "PropertyIsLikeType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getPropertyIsLikeType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getPropertyIsLikeType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getPropertyIsLikeType_EscapeChar(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "escapeChar"
           });		
        addAnnotation
          (getPropertyIsLikeType_SingleChar(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "singleChar"
           });		
        addAnnotation
          (getPropertyIsLikeType_WildCard(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "wildCard"
           });		
        addAnnotation
          (propertyIsNilTypeEClass, 
           source, 
           new String[] {
             "name", "PropertyIsNilType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getPropertyIsNilType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getPropertyIsNilType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (getPropertyIsNilType_NilReason(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "nilReason"
           });		
        addAnnotation
          (propertyIsNullTypeEClass, 
           source, 
           new String[] {
             "name", "PropertyIsNullType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getPropertyIsNullType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getPropertyIsNullType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (resourceIdentifierTypeEClass, 
           source, 
           new String[] {
             "name", "ResourceIdentifierType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getResourceIdentifierType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getResourceIdentifierType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (resourceIdTypeEClass, 
           source, 
           new String[] {
             "name", "ResourceIdType",
             "kind", "empty"
           });		
        addAnnotation
          (getResourceIdType_EndDate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "endDate"
           });		
        addAnnotation
          (getResourceIdType_PreviousRid(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "previousRid"
           });		
        addAnnotation
          (getResourceIdType_Rid(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "rid"
           });		
        addAnnotation
          (getResourceIdType_StartDate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "startDate"
           });		
        addAnnotation
          (getResourceIdType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });		
        addAnnotation
          (scalarCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Scalar_CapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getScalarCapabilitiesType_LogicalOperators(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LogicalOperators",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getScalarCapabilitiesType_ComparisonOperators(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ComparisonOperators",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (sortByTypeEClass, 
           source, 
           new String[] {
             "name", "SortByType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSortByType_SortProperty(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SortProperty",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (sortPropertyTypeEClass, 
           source, 
           new String[] {
             "name", "SortPropertyType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSortPropertyType_ValueReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueReference",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSortPropertyType_SortOrder(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SortOrder",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (spatialCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Spatial_CapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSpatialCapabilitiesType_GeometryOperands(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GeometryOperands",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSpatialCapabilitiesType_SpatialOperators(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SpatialOperators",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (spatialOperatorsTypeEClass, 
           source, 
           new String[] {
             "name", "SpatialOperatorsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSpatialOperatorsType_SpatialOperator(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SpatialOperator",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (spatialOperatorTypeEClass, 
           source, 
           new String[] {
             "name", "SpatialOperatorType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSpatialOperatorType_GeometryOperands(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GeometryOperands",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSpatialOperatorType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (spatialOpsTypeEClass, 
           source, 
           new String[] {
             "name", "SpatialOpsType",
             "kind", "empty"
           });		
        addAnnotation
          (temporalCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Temporal_CapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTemporalCapabilitiesType_TemporalOperands(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalOperands",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTemporalCapabilitiesType_TemporalOperators(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalOperators",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (temporalOperandsTypeEClass, 
           source, 
           new String[] {
             "name", "TemporalOperandsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTemporalOperandsType_TemporalOperand(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalOperand",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (temporalOperandTypeEClass, 
           source, 
           new String[] {
             "name", "TemporalOperand_._type",
             "kind", "empty"
           });		
        addAnnotation
          (getTemporalOperandType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (temporalOperatorsTypeEClass, 
           source, 
           new String[] {
             "name", "TemporalOperatorsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTemporalOperatorsType_TemporalOperator(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalOperator",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (temporalOperatorTypeEClass, 
           source, 
           new String[] {
             "name", "TemporalOperatorType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTemporalOperatorType_TemporalOperands(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalOperands",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTemporalOperatorType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (temporalOpsTypeEClass, 
           source, 
           new String[] {
             "name", "TemporalOpsType",
             "kind", "empty"
           });		
        addAnnotation
          (unaryLogicOpTypeEClass, 
           source, 
           new String[] {
             "name", "UnaryLogicOpType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getUnaryLogicOpType_ComparisonOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "comparisonOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUnaryLogicOpType_ComparisonOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "comparisonOps",
             "namespace", "##targetNamespace",
             "group", "comparisonOps:group"
           });		
        addAnnotation
          (getUnaryLogicOpType_SpatialOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "spatialOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUnaryLogicOpType_SpatialOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "spatialOps",
             "namespace", "##targetNamespace",
             "group", "spatialOps:group"
           });		
        addAnnotation
          (getUnaryLogicOpType_TemporalOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "temporalOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUnaryLogicOpType_TemporalOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "temporalOps",
             "namespace", "##targetNamespace",
             "group", "temporalOps:group"
           });		
        addAnnotation
          (getUnaryLogicOpType_LogicOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "logicOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUnaryLogicOpType_LogicOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "logicOps",
             "namespace", "##targetNamespace",
             "group", "logicOps:group"
           });		
        addAnnotation
          (getUnaryLogicOpType_ExtensionOpsGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "extensionOps:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUnaryLogicOpType_ExtensionOps(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "extensionOps",
             "namespace", "##targetNamespace",
             "group", "extensionOps:group"
           });		
        addAnnotation
          (getUnaryLogicOpType_Function(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Function",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUnaryLogicOpType_IdGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "_Id:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUnaryLogicOpType_Id(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "_Id",
             "namespace", "##targetNamespace",
             "group", "_Id:group"
           });		
        addAnnotation
          (upperBoundaryTypeEClass, 
           source, 
           new String[] {
             "name", "UpperBoundaryType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getUpperBoundaryType_ExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "expression:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUpperBoundaryType_Expression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "expression",
             "namespace", "##targetNamespace",
             "group", "expression:group"
           });		
        addAnnotation
          (comparisonOperatorNameTypeMember0EEnum, 
           source, 
           new String[] {
             "name", "ComparisonOperatorNameType_._member_._0"
           });		
        addAnnotation
          (matchActionTypeEEnum, 
           source, 
           new String[] {
             "name", "MatchActionType"
           });		
        addAnnotation
          (sortOrderTypeEEnum, 
           source, 
           new String[] {
             "name", "SortOrderType"
           });		
        addAnnotation
          (spatialOperatorNameTypeMember0EEnum, 
           source, 
           new String[] {
             "name", "SpatialOperatorNameType_._member_._0"
           });		
        addAnnotation
          (temporalOperatorNameTypeMember0EEnum, 
           source, 
           new String[] {
             "name", "TemporalOperatorNameType_._member_._0"
           });		
        addAnnotation
          (versionActionTokensEEnum, 
           source, 
           new String[] {
             "name", "VersionActionTokens"
           });		
        addAnnotation
          (aliasesTypeEDataType, 
           source, 
           new String[] {
             "name", "AliasesType",
             "itemType", "http://www.eclipse.org/emf/2003/XMLType#NCName"
           });		
        addAnnotation
          (comparisonOperatorNameTypeEDataType, 
           source, 
           new String[] {
             "name", "ComparisonOperatorNameType",
             "memberTypes", "ComparisonOperatorNameType_._member_._0 ComparisonOperatorNameType_._member_._1"
           });		
        addAnnotation
          (comparisonOperatorNameTypeMember0ObjectEDataType, 
           source, 
           new String[] {
             "name", "ComparisonOperatorNameType_._member_._0:Object",
             "baseType", "ComparisonOperatorNameType_._member_._0"
           });		
        addAnnotation
          (comparisonOperatorNameTypeMember1EDataType, 
           source, 
           new String[] {
             "name", "ComparisonOperatorNameType_._member_._1",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "extension:\\w{2,}"
           });		
        addAnnotation
          (matchActionTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "MatchActionType:Object",
             "baseType", "MatchActionType"
           });		
        addAnnotation
          (schemaElementEDataType, 
           source, 
           new String[] {
             "name", "SchemaElement",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "schema\\-element\\(.+\\)"
           });		
        addAnnotation
          (sortOrderTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "SortOrderType:Object",
             "baseType", "SortOrderType"
           });		
        addAnnotation
          (spatialOperatorNameTypeEDataType, 
           source, 
           new String[] {
             "name", "SpatialOperatorNameType",
             "memberTypes", "SpatialOperatorNameType_._member_._0 SpatialOperatorNameType_._member_._1"
           });		
        addAnnotation
          (spatialOperatorNameTypeMember0ObjectEDataType, 
           source, 
           new String[] {
             "name", "SpatialOperatorNameType_._member_._0:Object",
             "baseType", "SpatialOperatorNameType_._member_._0"
           });		
        addAnnotation
          (spatialOperatorNameTypeMember1EDataType, 
           source, 
           new String[] {
             "name", "SpatialOperatorNameType_._member_._1",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "extension:\\w{2,}"
           });		
        addAnnotation
          (temporalOperatorNameTypeEDataType, 
           source, 
           new String[] {
             "name", "TemporalOperatorNameType",
             "memberTypes", "TemporalOperatorNameType_._member_._0 TemporalOperatorNameType_._member_._1"
           });		
        addAnnotation
          (temporalOperatorNameTypeMember0ObjectEDataType, 
           source, 
           new String[] {
             "name", "TemporalOperatorNameType_._member_._0:Object",
             "baseType", "TemporalOperatorNameType_._member_._0"
           });		
        addAnnotation
          (temporalOperatorNameTypeMember1EDataType, 
           source, 
           new String[] {
             "name", "TemporalOperatorNameType_._member_._1",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "extension:\\w{2,}"
           });		
        addAnnotation
          (typeNamesListTypeEDataType, 
           source, 
           new String[] {
             "name", "TypeNamesListType",
             "itemType", "TypeNamesType"
           });		
        addAnnotation
          (typeNamesTypeEDataType, 
           source, 
           new String[] {
             "name", "TypeNamesType",
             "memberTypes", "SchemaElement http://www.eclipse.org/emf/2003/XMLType#QName"
           });		
        addAnnotation
          (uomIdentifierEDataType, 
           source, 
           new String[] {
             "name", "UomIdentifier",
             "memberTypes", "UomSymbol UomURI"
           });		
        addAnnotation
          (uomSymbolEDataType, 
           source, 
           new String[] {
             "name", "UomSymbol",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "[^:%20\\n\\r\\t]+"
           });		
        addAnnotation
          (uomURIEDataType, 
           source, 
           new String[] {
             "name", "UomURI",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#anyURI",
             "pattern", "([a-zA-Z][a-zA-Z0-9\\-\\+\\.]*:|\\.\\./|\\./|#).*"
           });		
        addAnnotation
          (versionActionTokensObjectEDataType, 
           source, 
           new String[] {
             "name", "VersionActionTokens:Object",
             "baseType", "VersionActionTokens"
           });		
        addAnnotation
          (versionTypeEDataType, 
           source, 
           new String[] {
             "name", "VersionType",
             "memberTypes", "VersionActionTokens http://www.eclipse.org/emf/2003/XMLType#positiveInteger http://www.eclipse.org/emf/2003/XMLType#dateTime"
           });
    }

} //Fes20PackageImpl
