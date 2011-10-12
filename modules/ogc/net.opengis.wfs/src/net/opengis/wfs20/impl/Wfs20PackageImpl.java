/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import java.util.Calendar;
import java.util.List;

import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.fes20.Fes20Package;

import net.opengis.ows10.Ows10Package;
import net.opengis.ows11.Ows11Package;

import net.opengis.wfs20.AbstractTransactionActionType;
import net.opengis.wfs20.AbstractType;
import net.opengis.wfs20.ActionResultsType;
import net.opengis.wfs20.AdditionalObjectsType;
import net.opengis.wfs20.AdditionalValuesType;
import net.opengis.wfs20.AllSomeType;
import net.opengis.wfs20.BaseRequestType;
import net.opengis.wfs20.CreateStoredQueryResponseType;
import net.opengis.wfs20.CreateStoredQueryType;
import net.opengis.wfs20.CreatedOrModifiedFeatureType;
import net.opengis.wfs20.DeleteType;
import net.opengis.wfs20.DescribeFeatureTypeType;
import net.opengis.wfs20.DescribeStoredQueriesResponseType;
import net.opengis.wfs20.DescribeStoredQueriesType;
import net.opengis.wfs20.DocumentRoot;
import net.opengis.wfs20.DropStoredQueryType;
import net.opengis.wfs20.ElementType;
import net.opengis.wfs20.EmptyType;
import net.opengis.wfs20.EnvelopePropertyType;
import net.opengis.wfs20.ExecutionStatusType;
import net.opengis.wfs20.ExtendedDescriptionType;
import net.opengis.wfs20.FeatureCollectionType;
import net.opengis.wfs20.FeatureTypeListType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.FeaturesLockedType;
import net.opengis.wfs20.FeaturesNotLockedType;
import net.opengis.wfs20.GetCapabilitiesType;
import net.opengis.wfs20.GetFeatureType;
import net.opengis.wfs20.GetFeatureWithLockType;
import net.opengis.wfs20.GetPropertyValueType;
import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.ListStoredQueriesResponseType;
import net.opengis.wfs20.ListStoredQueriesType;
import net.opengis.wfs20.LockFeatureResponseType;
import net.opengis.wfs20.LockFeatureType;
import net.opengis.wfs20.MemberPropertyType;
import net.opengis.wfs20.MetadataURLType;
import net.opengis.wfs20.NativeType;
import net.opengis.wfs20.NoCRSType;
import net.opengis.wfs20.NonNegativeIntegerOrUnknownMember0;
import net.opengis.wfs20.OutputFormatListType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.PropertyNameType;
import net.opengis.wfs20.PropertyType;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.ReplaceType;
import net.opengis.wfs20.ResolveValueType;
import net.opengis.wfs20.ResultTypeType;
import net.opengis.wfs20.SimpleFeatureCollectionType;
import net.opengis.wfs20.StarStringType;
import net.opengis.wfs20.StateValueTypeMember0;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.StoredQueryListItemType;
import net.opengis.wfs20.StoredQueryType;
import net.opengis.wfs20.TitleType;
import net.opengis.wfs20.TransactionResponseType;
import net.opengis.wfs20.TransactionSummaryType;
import net.opengis.wfs20.TransactionType;
import net.opengis.wfs20.TruncatedResponseType;
import net.opengis.wfs20.TupleType;
import net.opengis.wfs20.UpdateActionType;
import net.opengis.wfs20.UpdateType;
import net.opengis.wfs20.ValueCollectionType;
import net.opengis.wfs20.ValueListType;
import net.opengis.wfs20.ValueReferenceType;
import net.opengis.wfs20.WFSCapabilitiesType;
import net.opengis.wfs20.WSDLType;
import net.opengis.wfs20.Wfs20Factory;
import net.opengis.wfs20.Wfs20Package;

import net.opengis.wfs20.util.Wfs20Validator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.namespace.XMLNamespacePackage;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.geotools.feature.FeatureCollection;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wfs20PackageImpl extends EPackageImpl implements Wfs20Package {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTransactionActionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass actionResultsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass additionalObjectsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass additionalValuesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass baseRequestTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass createdOrModifiedFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass createStoredQueryResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass createStoredQueryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass deleteTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass describeFeatureTypeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass describeStoredQueriesResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass describeStoredQueriesTypeEClass = null;

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
    private EClass dropStoredQueryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elementTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass emptyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass envelopePropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass executionStatusTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass extendedDescriptionTypeEClass = null;

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
    private EClass featuresLockedTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featuresNotLockedTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureTypeListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureTypeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getFeatureWithLockTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getPropertyValueTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass insertTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass listStoredQueriesResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass listStoredQueriesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lockFeatureResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lockFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass memberPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataURLTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nativeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass noCRSTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass outputFormatListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parameterExpressionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parameterTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass propertyNameTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass propertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass queryExpressionTextTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass queryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass replaceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass simpleFeatureCollectionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass storedQueryDescriptionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass storedQueryListItemTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass storedQueryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass titleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass transactionResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass transactionSummaryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass transactionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass truncatedResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tupleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass updateTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valueCollectionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valueListTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valueReferenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass wfsCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass wsdlTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum allSomeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum nonNegativeIntegerOrUnknownMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum resolveValueTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum resultTypeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum starStringTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum stateValueTypeMember0EEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum updateActionTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType allSomeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nonNegativeIntegerOrUnknownEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nonNegativeIntegerOrUnknownMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nonNegativeIntegerOrUnknownMember1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType positiveIntegerWithStarEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType resolveValueTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType resultTypeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType returnFeatureTypesListTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType starStringTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType stateValueTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType stateValueTypeMember0ObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType stateValueTypeMember1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType updateActionTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType uriEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType mapEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType filterEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType qNameEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType sortByEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType calendarEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType featureCollectionEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType propertyNameEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType featureIdEDataType = null;

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
     * @see net.opengis.wfs20.Wfs20Package#eNS_URI
     * @see #init()
     * @generated
     */
    private Wfs20PackageImpl() {
        super(eNS_URI, Wfs20Factory.eINSTANCE);
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
     * <p>This method is used to initialize {@link Wfs20Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static Wfs20Package init() {
        if (isInited) return (Wfs20Package)EPackage.Registry.INSTANCE.getEPackage(Wfs20Package.eNS_URI);

        // Obtain or create and register package
        Wfs20PackageImpl theWfs20Package = (Wfs20PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Wfs20PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Wfs20PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows10Package.eINSTANCE.eClass();
        Fes20Package.eINSTANCE.eClass();
        XlinkPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theWfs20Package.createPackageContents();

        // Initialize created meta-data
        theWfs20Package.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theWfs20Package, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return Wfs20Validator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theWfs20Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Wfs20Package.eNS_URI, theWfs20Package);
        return theWfs20Package;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractTransactionActionType() {
        return abstractTransactionActionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractTransactionActionType_Handle() {
        return (EAttribute)abstractTransactionActionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractType() {
        return abstractTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractType_Value() {
        return (EAttribute)abstractTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractType_Lang() {
        return (EAttribute)abstractTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getActionResultsType() {
        return actionResultsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getActionResultsType_Feature() {
        return (EReference)actionResultsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAdditionalObjectsType() {
        return additionalObjectsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAdditionalObjectsType_ValueCollection() {
        return (EReference)additionalObjectsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalObjectsType_SimpleFeatureCollectionGroup() {
        return (EAttribute)additionalObjectsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAdditionalObjectsType_SimpleFeatureCollection() {
        return (EReference)additionalObjectsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAdditionalValuesType() {
        return additionalValuesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAdditionalValuesType_ValueCollection() {
        return (EReference)additionalValuesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAdditionalValuesType_SimpleFeatureCollectionGroup() {
        return (EAttribute)additionalValuesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAdditionalValuesType_SimpleFeatureCollection() {
        return (EReference)additionalValuesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBaseRequestType() {
        return baseRequestTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBaseRequestType_Handle() {
        return (EAttribute)baseRequestTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBaseRequestType_Service() {
        return (EAttribute)baseRequestTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBaseRequestType_Version() {
        return (EAttribute)baseRequestTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBaseRequestType_BaseUrl() {
        return (EAttribute)baseRequestTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBaseRequestType_ExtendedProperties() {
        return (EAttribute)baseRequestTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCreatedOrModifiedFeatureType() {
        return createdOrModifiedFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCreatedOrModifiedFeatureType_ResourceId() {
        return (EAttribute)createdOrModifiedFeatureTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCreatedOrModifiedFeatureType_Handle() {
        return (EAttribute)createdOrModifiedFeatureTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCreateStoredQueryResponseType() {
        return createStoredQueryResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCreateStoredQueryType() {
        return createStoredQueryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCreateStoredQueryType_StoredQueryDefinition() {
        return (EReference)createStoredQueryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDeleteType() {
        return deleteTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDeleteType_Filter() {
        return (EAttribute)deleteTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDeleteType_TypeName() {
        return (EAttribute)deleteTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeFeatureTypeType() {
        return describeFeatureTypeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeFeatureTypeType_TypeName() {
        return (EAttribute)describeFeatureTypeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeFeatureTypeType_OutputFormat() {
        return (EAttribute)describeFeatureTypeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeStoredQueriesResponseType() {
        return describeStoredQueriesResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDescribeStoredQueriesResponseType_StoredQueryDescription() {
        return (EReference)describeStoredQueriesResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeStoredQueriesType() {
        return describeStoredQueriesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeStoredQueriesType_StoredQueryId() {
        return (EAttribute)describeStoredQueriesTypeEClass.getEStructuralFeatures().get(0);
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
    public EReference getDocumentRoot_Abstract() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AbstractTransactionAction() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AdditionalObjects() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AdditionalValues() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_BoundedBy() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CreateStoredQuery() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CreateStoredQueryResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Delete() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DescribeFeatureType() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DescribeStoredQueries() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DescribeStoredQueriesResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DropStoredQuery() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DropStoredQueryResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Element() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureCollection() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_SimpleFeatureCollection() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureTypeList() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetFeature() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetFeatureWithLock() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetPropertyValue() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Insert() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ListStoredQueries() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ListStoredQueriesResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LockFeature() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LockFeatureResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Member() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Native() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Property() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_PropertyName() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Query() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Replace() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_StoredQuery() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Title() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(36);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Transaction() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(37);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TransactionResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(38);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TruncatedResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(39);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Tuple() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(40);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Update() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(41);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Value() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(42);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueCollection() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(43);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValueList() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(44);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_WFSCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(45);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDropStoredQueryType() {
        return dropStoredQueryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDropStoredQueryType_Id() {
        return (EAttribute)dropStoredQueryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getElementType() {
        return elementTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getElementType_Metadata() {
        return (EReference)elementTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getElementType_ValueList() {
        return (EReference)elementTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementType_Name() {
        return (EAttribute)elementTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementType_Type() {
        return (EAttribute)elementTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEmptyType() {
        return emptyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEnvelopePropertyType() {
        return envelopePropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEnvelopePropertyType_Any() {
        return (EAttribute)envelopePropertyTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExecutionStatusType() {
        return executionStatusTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExecutionStatusType_Status() {
        return (EAttribute)executionStatusTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExtendedDescriptionType() {
        return extendedDescriptionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtendedDescriptionType_Element() {
        return (EReference)extendedDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureCollectionType() {
        return featureCollectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureCollectionType_AdditionalObjects() {
        return (EReference)featureCollectionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureCollectionType_TruncatedResponse() {
        return (EReference)featureCollectionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureCollectionType_LockId() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureCollectionType_Next() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureCollectionType_NumberMatched() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureCollectionType_NumberReturned() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureCollectionType_Previous() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureCollectionType_TimeStamp() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeaturesLockedType() {
        return featuresLockedTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturesLockedType_Group() {
        return (EAttribute)featuresLockedTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturesLockedType_ResourceId() {
        return (EAttribute)featuresLockedTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeaturesNotLockedType() {
        return featuresNotLockedTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturesNotLockedType_Group() {
        return (EAttribute)featuresNotLockedTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeaturesNotLockedType_ResourceId() {
        return (EAttribute)featuresNotLockedTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureTypeListType() {
        return featureTypeListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeListType_FeatureType() {
        return (EReference)featureTypeListTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureTypeType() {
        return featureTypeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureTypeType_Name() {
        return (EAttribute)featureTypeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_Title() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_Abstract() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_Keywords() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureTypeType_DefaultCRS() {
        return (EAttribute)featureTypeTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureTypeType_OtherCRS() {
        return (EAttribute)featureTypeTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_NoCRS() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_OutputFormats() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_WGS84BoundingBox() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_MetadataURL() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureTypeType_ExtendedDescription() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetCapabilitiesType() {
        return getCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCapabilitiesType_Service() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetFeatureType() {
        return getFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_AbstractQueryExpressionGroup() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetFeatureType_AbstractQueryExpression() {
        return (EReference)getFeatureTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_Count() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_OutputFormat() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_Resolve() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_ResolveDepth() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_ResolveTimeout() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_ResultType() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_StartIndex() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_Metadata() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_FormatOptions() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetFeatureWithLockType() {
        return getFeatureWithLockTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureWithLockType_Expiry() {
        return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureWithLockType_LockAction() {
        return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetPropertyValueType() {
        return getPropertyValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetPropertyValueType_AbstractQueryExpression() {
        return (EReference)getPropertyValueTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_Count() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_OutputFormat() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_Resolve() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_ResolveDepth() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_ResolvePath() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_ResolveTimeout() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_ResultType() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_StartIndex() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetPropertyValueType_ValueReference() {
        return (EAttribute)getPropertyValueTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInsertType() {
        return insertTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInsertType_Any() {
        return (EAttribute)insertTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInsertType_InputFormat() {
        return (EAttribute)insertTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInsertType_SrsName() {
        return (EAttribute)insertTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getListStoredQueriesResponseType() {
        return listStoredQueriesResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getListStoredQueriesResponseType_StoredQuery() {
        return (EReference)listStoredQueriesResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getListStoredQueriesType() {
        return listStoredQueriesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLockFeatureResponseType() {
        return lockFeatureResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLockFeatureResponseType_FeaturesLocked() {
        return (EReference)lockFeatureResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLockFeatureResponseType_FeaturesNotLocked() {
        return (EReference)lockFeatureResponseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLockFeatureResponseType_LockId() {
        return (EAttribute)lockFeatureResponseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLockFeatureType() {
        return lockFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLockFeatureType_AbstractQueryExpressionGroup() {
        return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLockFeatureType_AbstractQueryExpression() {
        return (EReference)lockFeatureTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLockFeatureType_Expiry() {
        return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLockFeatureType_LockAction() {
        return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLockFeatureType_LockId() {
        return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMemberPropertyType() {
        return memberPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Mixed() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Any() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMemberPropertyType_Tuple() {
        return (EReference)memberPropertyTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_SimpleFeatureCollectionGroup() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMemberPropertyType_SimpleFeatureCollection() {
        return (EReference)memberPropertyTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Actuate() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Arcrole() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Href() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Role() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Show() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_State() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Title() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMemberPropertyType_Type() {
        return (EAttribute)memberPropertyTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMetadataURLType() {
        return metadataURLTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_About() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_Actuate() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_Arcrole() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_Href() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_Role() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_Show() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_Title() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMetadataURLType_Type() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNativeType() {
        return nativeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNativeType_Mixed() {
        return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNativeType_Any() {
        return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNativeType_SafeToIgnore() {
        return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNativeType_VendorId() {
        return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNoCRSType() {
        return noCRSTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOutputFormatListType() {
        return outputFormatListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputFormatListType_Group() {
        return (EAttribute)outputFormatListTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputFormatListType_Format() {
        return (EAttribute)outputFormatListTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getParameterExpressionType() {
        return parameterExpressionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterExpressionType_Title() {
        return (EReference)parameterExpressionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterExpressionType_Abstract() {
        return (EReference)parameterExpressionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterExpressionType_Metadata() {
        return (EReference)parameterExpressionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterExpressionType_Name() {
        return (EAttribute)parameterExpressionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterExpressionType_Type() {
        return (EAttribute)parameterExpressionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getParameterType() {
        return parameterTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterType_Name() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterType_Value() {
        return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPropertyNameType() {
        return propertyNameTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyNameType_Value() {
        return (EAttribute)propertyNameTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyNameType_Resolve() {
        return (EAttribute)propertyNameTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyNameType_ResolveDepth() {
        return (EAttribute)propertyNameTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyNameType_ResolvePath() {
        return (EAttribute)propertyNameTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyNameType_ResolveTimeout() {
        return (EAttribute)propertyNameTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPropertyType() {
        return propertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPropertyType_ValueReference() {
        return (EReference)propertyTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getPropertyType_Value() {
        return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getQueryExpressionTextType() {
        return queryExpressionTextTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryExpressionTextType_IsPrivate() {
        return (EAttribute)queryExpressionTextTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryExpressionTextType_Language() {
        return (EAttribute)queryExpressionTextTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryExpressionTextType_ReturnFeatureTypes() {
        return (EAttribute)queryExpressionTextTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryExpressionTextType_Value() {
        return (EAttribute)queryExpressionTextTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getQueryType() {
        return queryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryType_FeatureVersion() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryType_SrsName() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryType_Filter() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryType_PropertyNames() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryType_SortBy() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getReplaceType() {
        return replaceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReplaceType_Any() {
        return (EAttribute)replaceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReplaceType_Filter() {
        return (EAttribute)replaceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReplaceType_InputFormat() {
        return (EAttribute)replaceTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReplaceType_SrsName() {
        return (EAttribute)replaceTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSimpleFeatureCollectionType() {
        return simpleFeatureCollectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSimpleFeatureCollectionType_BoundedBy() {
        return (EReference)simpleFeatureCollectionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimpleFeatureCollectionType_Member() {
        return (EAttribute)simpleFeatureCollectionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStoredQueryDescriptionType() {
        return storedQueryDescriptionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStoredQueryDescriptionType_Title() {
        return (EReference)storedQueryDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStoredQueryDescriptionType_Abstract() {
        return (EReference)storedQueryDescriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStoredQueryDescriptionType_Metadata() {
        return (EReference)storedQueryDescriptionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStoredQueryDescriptionType_Parameter() {
        return (EReference)storedQueryDescriptionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStoredQueryDescriptionType_QueryExpressionText() {
        return (EReference)storedQueryDescriptionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStoredQueryDescriptionType_Id() {
        return (EAttribute)storedQueryDescriptionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStoredQueryListItemType() {
        return storedQueryListItemTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStoredQueryListItemType_Title() {
        return (EReference)storedQueryListItemTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStoredQueryListItemType_ReturnFeatureType() {
        return (EAttribute)storedQueryListItemTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStoredQueryListItemType_Id() {
        return (EAttribute)storedQueryListItemTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStoredQueryType() {
        return storedQueryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStoredQueryType_Parameter() {
        return (EReference)storedQueryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStoredQueryType_Id() {
        return (EAttribute)storedQueryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTitleType() {
        return titleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTitleType_Value() {
        return (EAttribute)titleTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTitleType_Lang() {
        return (EAttribute)titleTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTransactionResponseType() {
        return transactionResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransactionResponseType_TransactionSummary() {
        return (EReference)transactionResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransactionResponseType_InsertResults() {
        return (EReference)transactionResponseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransactionResponseType_UpdateResults() {
        return (EReference)transactionResponseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransactionResponseType_ReplaceResults() {
        return (EReference)transactionResponseTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionResponseType_Version() {
        return (EAttribute)transactionResponseTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTransactionSummaryType() {
        return transactionSummaryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionSummaryType_TotalInserted() {
        return (EAttribute)transactionSummaryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionSummaryType_TotalUpdated() {
        return (EAttribute)transactionSummaryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionSummaryType_TotalReplaced() {
        return (EAttribute)transactionSummaryTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionSummaryType_TotalDeleted() {
        return (EAttribute)transactionSummaryTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTransactionType() {
        return transactionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionType_Group() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionType_AbstractTransactionActionGroup() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransactionType_AbstractTransactionAction() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionType_LockId() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionType_ReleaseAction() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionType_SrsName() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTruncatedResponseType() {
        return truncatedResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTruncatedResponseType_ExceptionReport() {
        return (EReference)truncatedResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTupleType() {
        return tupleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTupleType_Member() {
        return (EReference)tupleTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUpdateType() {
        return updateTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUpdateType_Property() {
        return (EReference)updateTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUpdateType_Filter() {
        return (EAttribute)updateTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUpdateType_InputFormat() {
        return (EAttribute)updateTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUpdateType_SrsName() {
        return (EAttribute)updateTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUpdateType_TypeName() {
        return (EAttribute)updateTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValueCollectionType() {
        return valueCollectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueCollectionType_Member() {
        return (EAttribute)valueCollectionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueCollectionType_AdditionalValues() {
        return (EReference)valueCollectionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueCollectionType_TruncatedResponse() {
        return (EReference)valueCollectionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueCollectionType_Next() {
        return (EAttribute)valueCollectionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueCollectionType_NumberMatched() {
        return (EAttribute)valueCollectionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueCollectionType_NumberReturned() {
        return (EAttribute)valueCollectionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueCollectionType_Previous() {
        return (EAttribute)valueCollectionTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueCollectionType_TimeStamp() {
        return (EAttribute)valueCollectionTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValueListType() {
        return valueListTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueListType_Group() {
        return (EAttribute)valueListTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getValueListType_Value() {
        return (EReference)valueListTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValueReferenceType() {
        return valueReferenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueReferenceType_Value() {
        return (EAttribute)valueReferenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueReferenceType_Action() {
        return (EAttribute)valueReferenceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getWFSCapabilitiesType() {
        return wfsCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getWFSCapabilitiesType_WSDL() {
        return (EReference)wfsCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getWFSCapabilitiesType_FeatureTypeList() {
        return (EReference)wfsCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getWFSCapabilitiesType_FilterCapabilities() {
        return (EReference)wfsCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getWSDLType() {
        return wsdlTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Actuate() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Arcrole() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Href() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Role() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Show() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Title() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDLType_Type() {
        return (EAttribute)wsdlTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getAllSomeType() {
        return allSomeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getNonNegativeIntegerOrUnknownMember0() {
        return nonNegativeIntegerOrUnknownMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getResolveValueType() {
        return resolveValueTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getResultTypeType() {
        return resultTypeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getStarStringType() {
        return starStringTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getStateValueTypeMember0() {
        return stateValueTypeMember0EEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getUpdateActionType() {
        return updateActionTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAllSomeTypeObject() {
        return allSomeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNonNegativeIntegerOrUnknown() {
        return nonNegativeIntegerOrUnknownEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNonNegativeIntegerOrUnknownMember0Object() {
        return nonNegativeIntegerOrUnknownMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNonNegativeIntegerOrUnknownMember1() {
        return nonNegativeIntegerOrUnknownMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getPositiveIntegerWithStar() {
        return positiveIntegerWithStarEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getResolveValueTypeObject() {
        return resolveValueTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getResultTypeTypeObject() {
        return resultTypeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getReturnFeatureTypesListType() {
        return returnFeatureTypesListTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getStarStringTypeObject() {
        return starStringTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getStateValueType() {
        return stateValueTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getStateValueTypeMember0Object() {
        return stateValueTypeMember0ObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getStateValueTypeMember1() {
        return stateValueTypeMember1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getUpdateActionTypeObject() {
        return updateActionTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getURI() {
        return uriEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getMap() {
        return mapEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFilter() {
        return filterEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getQName() {
        return qNameEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSortBy() {
        return sortByEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getCalendar() {
        return calendarEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFeatureCollection() {
        return featureCollectionEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getPropertyName() {
        return propertyNameEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFeatureId() {
        return featureIdEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wfs20Factory getWfs20Factory() {
        return (Wfs20Factory)getEFactoryInstance();
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
        abstractTransactionActionTypeEClass = createEClass(ABSTRACT_TRANSACTION_ACTION_TYPE);
        createEAttribute(abstractTransactionActionTypeEClass, ABSTRACT_TRANSACTION_ACTION_TYPE__HANDLE);

        abstractTypeEClass = createEClass(ABSTRACT_TYPE);
        createEAttribute(abstractTypeEClass, ABSTRACT_TYPE__VALUE);
        createEAttribute(abstractTypeEClass, ABSTRACT_TYPE__LANG);

        actionResultsTypeEClass = createEClass(ACTION_RESULTS_TYPE);
        createEReference(actionResultsTypeEClass, ACTION_RESULTS_TYPE__FEATURE);

        additionalObjectsTypeEClass = createEClass(ADDITIONAL_OBJECTS_TYPE);
        createEReference(additionalObjectsTypeEClass, ADDITIONAL_OBJECTS_TYPE__VALUE_COLLECTION);
        createEAttribute(additionalObjectsTypeEClass, ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP);
        createEReference(additionalObjectsTypeEClass, ADDITIONAL_OBJECTS_TYPE__SIMPLE_FEATURE_COLLECTION);

        additionalValuesTypeEClass = createEClass(ADDITIONAL_VALUES_TYPE);
        createEReference(additionalValuesTypeEClass, ADDITIONAL_VALUES_TYPE__VALUE_COLLECTION);
        createEAttribute(additionalValuesTypeEClass, ADDITIONAL_VALUES_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP);
        createEReference(additionalValuesTypeEClass, ADDITIONAL_VALUES_TYPE__SIMPLE_FEATURE_COLLECTION);

        baseRequestTypeEClass = createEClass(BASE_REQUEST_TYPE);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__HANDLE);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__SERVICE);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__VERSION);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__BASE_URL);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__EXTENDED_PROPERTIES);

        createdOrModifiedFeatureTypeEClass = createEClass(CREATED_OR_MODIFIED_FEATURE_TYPE);
        createEAttribute(createdOrModifiedFeatureTypeEClass, CREATED_OR_MODIFIED_FEATURE_TYPE__RESOURCE_ID);
        createEAttribute(createdOrModifiedFeatureTypeEClass, CREATED_OR_MODIFIED_FEATURE_TYPE__HANDLE);

        createStoredQueryResponseTypeEClass = createEClass(CREATE_STORED_QUERY_RESPONSE_TYPE);

        createStoredQueryTypeEClass = createEClass(CREATE_STORED_QUERY_TYPE);
        createEReference(createStoredQueryTypeEClass, CREATE_STORED_QUERY_TYPE__STORED_QUERY_DEFINITION);

        deleteTypeEClass = createEClass(DELETE_TYPE);
        createEAttribute(deleteTypeEClass, DELETE_TYPE__FILTER);
        createEAttribute(deleteTypeEClass, DELETE_TYPE__TYPE_NAME);

        describeFeatureTypeTypeEClass = createEClass(DESCRIBE_FEATURE_TYPE_TYPE);
        createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME);
        createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT);

        describeStoredQueriesResponseTypeEClass = createEClass(DESCRIBE_STORED_QUERIES_RESPONSE_TYPE);
        createEReference(describeStoredQueriesResponseTypeEClass, DESCRIBE_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY_DESCRIPTION);

        describeStoredQueriesTypeEClass = createEClass(DESCRIBE_STORED_QUERIES_TYPE);
        createEAttribute(describeStoredQueriesTypeEClass, DESCRIBE_STORED_QUERIES_TYPE__STORED_QUERY_ID);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_TRANSACTION_ACTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ADDITIONAL_OBJECTS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ADDITIONAL_VALUES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BOUNDED_BY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CREATE_STORED_QUERY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CREATE_STORED_QUERY_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DELETE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_STORED_QUERIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_STORED_QUERIES_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DROP_STORED_QUERY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DROP_STORED_QUERY_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ELEMENT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FEATURE_COLLECTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SIMPLE_FEATURE_COLLECTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FEATURE_TYPE_LIST);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_FEATURE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_PROPERTY_VALUE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__INSERT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LIST_STORED_QUERIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LIST_STORED_QUERIES_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LOCK_FEATURE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__MEMBER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__NATIVE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY_NAME);
        createEReference(documentRootEClass, DOCUMENT_ROOT__QUERY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__REPLACE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__STORED_QUERY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TITLE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TRANSACTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TRANSACTION_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TRUNCATED_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TUPLE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__UPDATE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VALUE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VALUE_COLLECTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VALUE_LIST);
        createEReference(documentRootEClass, DOCUMENT_ROOT__WFS_CAPABILITIES);

        dropStoredQueryTypeEClass = createEClass(DROP_STORED_QUERY_TYPE);
        createEAttribute(dropStoredQueryTypeEClass, DROP_STORED_QUERY_TYPE__ID);

        elementTypeEClass = createEClass(ELEMENT_TYPE);
        createEReference(elementTypeEClass, ELEMENT_TYPE__METADATA);
        createEReference(elementTypeEClass, ELEMENT_TYPE__VALUE_LIST);
        createEAttribute(elementTypeEClass, ELEMENT_TYPE__NAME);
        createEAttribute(elementTypeEClass, ELEMENT_TYPE__TYPE);

        emptyTypeEClass = createEClass(EMPTY_TYPE);

        envelopePropertyTypeEClass = createEClass(ENVELOPE_PROPERTY_TYPE);
        createEAttribute(envelopePropertyTypeEClass, ENVELOPE_PROPERTY_TYPE__ANY);

        executionStatusTypeEClass = createEClass(EXECUTION_STATUS_TYPE);
        createEAttribute(executionStatusTypeEClass, EXECUTION_STATUS_TYPE__STATUS);

        extendedDescriptionTypeEClass = createEClass(EXTENDED_DESCRIPTION_TYPE);
        createEReference(extendedDescriptionTypeEClass, EXTENDED_DESCRIPTION_TYPE__ELEMENT);

        featureCollectionTypeEClass = createEClass(FEATURE_COLLECTION_TYPE);
        createEReference(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS);
        createEReference(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__LOCK_ID);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__NEXT);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__NUMBER_MATCHED);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__NUMBER_RETURNED);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__PREVIOUS);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__TIME_STAMP);

        featuresLockedTypeEClass = createEClass(FEATURES_LOCKED_TYPE);
        createEAttribute(featuresLockedTypeEClass, FEATURES_LOCKED_TYPE__GROUP);
        createEAttribute(featuresLockedTypeEClass, FEATURES_LOCKED_TYPE__RESOURCE_ID);

        featuresNotLockedTypeEClass = createEClass(FEATURES_NOT_LOCKED_TYPE);
        createEAttribute(featuresNotLockedTypeEClass, FEATURES_NOT_LOCKED_TYPE__GROUP);
        createEAttribute(featuresNotLockedTypeEClass, FEATURES_NOT_LOCKED_TYPE__RESOURCE_ID);

        featureTypeListTypeEClass = createEClass(FEATURE_TYPE_LIST_TYPE);
        createEReference(featureTypeListTypeEClass, FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE);

        featureTypeTypeEClass = createEClass(FEATURE_TYPE_TYPE);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__NAME);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__TITLE);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__ABSTRACT);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__KEYWORDS);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__DEFAULT_CRS);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__OTHER_CRS);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__NO_CRS);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__OUTPUT_FORMATS);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__METADATA_URL);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__EXTENDED_DESCRIPTION);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);

        getFeatureTypeEClass = createEClass(GET_FEATURE_TYPE);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP);
        createEReference(getFeatureTypeEClass, GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__COUNT);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__OUTPUT_FORMAT);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__RESOLVE);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__RESOLVE_DEPTH);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__RESOLVE_TIMEOUT);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__RESULT_TYPE);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__START_INDEX);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__METADATA);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__FORMAT_OPTIONS);

        getFeatureWithLockTypeEClass = createEClass(GET_FEATURE_WITH_LOCK_TYPE);
        createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__EXPIRY);
        createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__LOCK_ACTION);

        getPropertyValueTypeEClass = createEClass(GET_PROPERTY_VALUE_TYPE);
        createEReference(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__ABSTRACT_QUERY_EXPRESSION);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__COUNT);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__OUTPUT_FORMAT);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__RESOLVE);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__RESOLVE_DEPTH);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__RESOLVE_PATH);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__RESOLVE_TIMEOUT);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__RESULT_TYPE);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__START_INDEX);
        createEAttribute(getPropertyValueTypeEClass, GET_PROPERTY_VALUE_TYPE__VALUE_REFERENCE);

        insertTypeEClass = createEClass(INSERT_TYPE);
        createEAttribute(insertTypeEClass, INSERT_TYPE__ANY);
        createEAttribute(insertTypeEClass, INSERT_TYPE__INPUT_FORMAT);
        createEAttribute(insertTypeEClass, INSERT_TYPE__SRS_NAME);

        listStoredQueriesResponseTypeEClass = createEClass(LIST_STORED_QUERIES_RESPONSE_TYPE);
        createEReference(listStoredQueriesResponseTypeEClass, LIST_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY);

        listStoredQueriesTypeEClass = createEClass(LIST_STORED_QUERIES_TYPE);

        lockFeatureResponseTypeEClass = createEClass(LOCK_FEATURE_RESPONSE_TYPE);
        createEReference(lockFeatureResponseTypeEClass, LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED);
        createEReference(lockFeatureResponseTypeEClass, LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED);
        createEAttribute(lockFeatureResponseTypeEClass, LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID);

        lockFeatureTypeEClass = createEClass(LOCK_FEATURE_TYPE);
        createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP);
        createEReference(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION);
        createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__EXPIRY);
        createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__LOCK_ACTION);
        createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__LOCK_ID);

        memberPropertyTypeEClass = createEClass(MEMBER_PROPERTY_TYPE);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__MIXED);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__ANY);
        createEReference(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__TUPLE);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__SIMPLE_FEATURE_COLLECTION_GROUP);
        createEReference(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__SIMPLE_FEATURE_COLLECTION);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__ACTUATE);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__ARCROLE);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__HREF);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__ROLE);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__SHOW);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__STATE);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__TITLE);
        createEAttribute(memberPropertyTypeEClass, MEMBER_PROPERTY_TYPE__TYPE);

        metadataURLTypeEClass = createEClass(METADATA_URL_TYPE);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__ABOUT);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__ACTUATE);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__ARCROLE);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__HREF);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__ROLE);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__SHOW);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__TITLE);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__TYPE);

        nativeTypeEClass = createEClass(NATIVE_TYPE);
        createEAttribute(nativeTypeEClass, NATIVE_TYPE__MIXED);
        createEAttribute(nativeTypeEClass, NATIVE_TYPE__ANY);
        createEAttribute(nativeTypeEClass, NATIVE_TYPE__SAFE_TO_IGNORE);
        createEAttribute(nativeTypeEClass, NATIVE_TYPE__VENDOR_ID);

        noCRSTypeEClass = createEClass(NO_CRS_TYPE);

        outputFormatListTypeEClass = createEClass(OUTPUT_FORMAT_LIST_TYPE);
        createEAttribute(outputFormatListTypeEClass, OUTPUT_FORMAT_LIST_TYPE__GROUP);
        createEAttribute(outputFormatListTypeEClass, OUTPUT_FORMAT_LIST_TYPE__FORMAT);

        parameterExpressionTypeEClass = createEClass(PARAMETER_EXPRESSION_TYPE);
        createEReference(parameterExpressionTypeEClass, PARAMETER_EXPRESSION_TYPE__TITLE);
        createEReference(parameterExpressionTypeEClass, PARAMETER_EXPRESSION_TYPE__ABSTRACT);
        createEReference(parameterExpressionTypeEClass, PARAMETER_EXPRESSION_TYPE__METADATA);
        createEAttribute(parameterExpressionTypeEClass, PARAMETER_EXPRESSION_TYPE__NAME);
        createEAttribute(parameterExpressionTypeEClass, PARAMETER_EXPRESSION_TYPE__TYPE);

        parameterTypeEClass = createEClass(PARAMETER_TYPE);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NAME);
        createEAttribute(parameterTypeEClass, PARAMETER_TYPE__VALUE);

        propertyNameTypeEClass = createEClass(PROPERTY_NAME_TYPE);
        createEAttribute(propertyNameTypeEClass, PROPERTY_NAME_TYPE__VALUE);
        createEAttribute(propertyNameTypeEClass, PROPERTY_NAME_TYPE__RESOLVE);
        createEAttribute(propertyNameTypeEClass, PROPERTY_NAME_TYPE__RESOLVE_DEPTH);
        createEAttribute(propertyNameTypeEClass, PROPERTY_NAME_TYPE__RESOLVE_PATH);
        createEAttribute(propertyNameTypeEClass, PROPERTY_NAME_TYPE__RESOLVE_TIMEOUT);

        propertyTypeEClass = createEClass(PROPERTY_TYPE);
        createEReference(propertyTypeEClass, PROPERTY_TYPE__VALUE_REFERENCE);
        createEAttribute(propertyTypeEClass, PROPERTY_TYPE__VALUE);

        queryExpressionTextTypeEClass = createEClass(QUERY_EXPRESSION_TEXT_TYPE);
        createEAttribute(queryExpressionTextTypeEClass, QUERY_EXPRESSION_TEXT_TYPE__IS_PRIVATE);
        createEAttribute(queryExpressionTextTypeEClass, QUERY_EXPRESSION_TEXT_TYPE__LANGUAGE);
        createEAttribute(queryExpressionTextTypeEClass, QUERY_EXPRESSION_TEXT_TYPE__RETURN_FEATURE_TYPES);
        createEAttribute(queryExpressionTextTypeEClass, QUERY_EXPRESSION_TEXT_TYPE__VALUE);

        queryTypeEClass = createEClass(QUERY_TYPE);
        createEAttribute(queryTypeEClass, QUERY_TYPE__FEATURE_VERSION);
        createEAttribute(queryTypeEClass, QUERY_TYPE__SRS_NAME);
        createEAttribute(queryTypeEClass, QUERY_TYPE__FILTER);
        createEAttribute(queryTypeEClass, QUERY_TYPE__PROPERTY_NAMES);
        createEAttribute(queryTypeEClass, QUERY_TYPE__SORT_BY);

        replaceTypeEClass = createEClass(REPLACE_TYPE);
        createEAttribute(replaceTypeEClass, REPLACE_TYPE__ANY);
        createEAttribute(replaceTypeEClass, REPLACE_TYPE__FILTER);
        createEAttribute(replaceTypeEClass, REPLACE_TYPE__INPUT_FORMAT);
        createEAttribute(replaceTypeEClass, REPLACE_TYPE__SRS_NAME);

        simpleFeatureCollectionTypeEClass = createEClass(SIMPLE_FEATURE_COLLECTION_TYPE);
        createEReference(simpleFeatureCollectionTypeEClass, SIMPLE_FEATURE_COLLECTION_TYPE__BOUNDED_BY);
        createEAttribute(simpleFeatureCollectionTypeEClass, SIMPLE_FEATURE_COLLECTION_TYPE__MEMBER);

        storedQueryDescriptionTypeEClass = createEClass(STORED_QUERY_DESCRIPTION_TYPE);
        createEReference(storedQueryDescriptionTypeEClass, STORED_QUERY_DESCRIPTION_TYPE__TITLE);
        createEReference(storedQueryDescriptionTypeEClass, STORED_QUERY_DESCRIPTION_TYPE__ABSTRACT);
        createEReference(storedQueryDescriptionTypeEClass, STORED_QUERY_DESCRIPTION_TYPE__METADATA);
        createEReference(storedQueryDescriptionTypeEClass, STORED_QUERY_DESCRIPTION_TYPE__PARAMETER);
        createEReference(storedQueryDescriptionTypeEClass, STORED_QUERY_DESCRIPTION_TYPE__QUERY_EXPRESSION_TEXT);
        createEAttribute(storedQueryDescriptionTypeEClass, STORED_QUERY_DESCRIPTION_TYPE__ID);

        storedQueryListItemTypeEClass = createEClass(STORED_QUERY_LIST_ITEM_TYPE);
        createEReference(storedQueryListItemTypeEClass, STORED_QUERY_LIST_ITEM_TYPE__TITLE);
        createEAttribute(storedQueryListItemTypeEClass, STORED_QUERY_LIST_ITEM_TYPE__RETURN_FEATURE_TYPE);
        createEAttribute(storedQueryListItemTypeEClass, STORED_QUERY_LIST_ITEM_TYPE__ID);

        storedQueryTypeEClass = createEClass(STORED_QUERY_TYPE);
        createEReference(storedQueryTypeEClass, STORED_QUERY_TYPE__PARAMETER);
        createEAttribute(storedQueryTypeEClass, STORED_QUERY_TYPE__ID);

        titleTypeEClass = createEClass(TITLE_TYPE);
        createEAttribute(titleTypeEClass, TITLE_TYPE__VALUE);
        createEAttribute(titleTypeEClass, TITLE_TYPE__LANG);

        transactionResponseTypeEClass = createEClass(TRANSACTION_RESPONSE_TYPE);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__UPDATE_RESULTS);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__REPLACE_RESULTS);
        createEAttribute(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__VERSION);

        transactionSummaryTypeEClass = createEClass(TRANSACTION_SUMMARY_TYPE);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_REPLACED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED);

        transactionTypeEClass = createEClass(TRANSACTION_TYPE);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__GROUP);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION_GROUP);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__LOCK_ID);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__RELEASE_ACTION);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__SRS_NAME);

        truncatedResponseTypeEClass = createEClass(TRUNCATED_RESPONSE_TYPE);
        createEReference(truncatedResponseTypeEClass, TRUNCATED_RESPONSE_TYPE__EXCEPTION_REPORT);

        tupleTypeEClass = createEClass(TUPLE_TYPE);
        createEReference(tupleTypeEClass, TUPLE_TYPE__MEMBER);

        updateTypeEClass = createEClass(UPDATE_TYPE);
        createEReference(updateTypeEClass, UPDATE_TYPE__PROPERTY);
        createEAttribute(updateTypeEClass, UPDATE_TYPE__FILTER);
        createEAttribute(updateTypeEClass, UPDATE_TYPE__INPUT_FORMAT);
        createEAttribute(updateTypeEClass, UPDATE_TYPE__SRS_NAME);
        createEAttribute(updateTypeEClass, UPDATE_TYPE__TYPE_NAME);

        valueCollectionTypeEClass = createEClass(VALUE_COLLECTION_TYPE);
        createEAttribute(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__MEMBER);
        createEReference(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__ADDITIONAL_VALUES);
        createEReference(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__TRUNCATED_RESPONSE);
        createEAttribute(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__NEXT);
        createEAttribute(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__NUMBER_MATCHED);
        createEAttribute(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__NUMBER_RETURNED);
        createEAttribute(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__PREVIOUS);
        createEAttribute(valueCollectionTypeEClass, VALUE_COLLECTION_TYPE__TIME_STAMP);

        valueListTypeEClass = createEClass(VALUE_LIST_TYPE);
        createEAttribute(valueListTypeEClass, VALUE_LIST_TYPE__GROUP);
        createEReference(valueListTypeEClass, VALUE_LIST_TYPE__VALUE);

        valueReferenceTypeEClass = createEClass(VALUE_REFERENCE_TYPE);
        createEAttribute(valueReferenceTypeEClass, VALUE_REFERENCE_TYPE__VALUE);
        createEAttribute(valueReferenceTypeEClass, VALUE_REFERENCE_TYPE__ACTION);

        wfsCapabilitiesTypeEClass = createEClass(WFS_CAPABILITIES_TYPE);
        createEReference(wfsCapabilitiesTypeEClass, WFS_CAPABILITIES_TYPE__WSDL);
        createEReference(wfsCapabilitiesTypeEClass, WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST);
        createEReference(wfsCapabilitiesTypeEClass, WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES);

        wsdlTypeEClass = createEClass(WSDL_TYPE);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__ACTUATE);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__ARCROLE);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__HREF);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__ROLE);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__SHOW);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__TITLE);
        createEAttribute(wsdlTypeEClass, WSDL_TYPE__TYPE);

        // Create enums
        allSomeTypeEEnum = createEEnum(ALL_SOME_TYPE);
        nonNegativeIntegerOrUnknownMember0EEnum = createEEnum(NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0);
        resolveValueTypeEEnum = createEEnum(RESOLVE_VALUE_TYPE);
        resultTypeTypeEEnum = createEEnum(RESULT_TYPE_TYPE);
        starStringTypeEEnum = createEEnum(STAR_STRING_TYPE);
        stateValueTypeMember0EEnum = createEEnum(STATE_VALUE_TYPE_MEMBER0);
        updateActionTypeEEnum = createEEnum(UPDATE_ACTION_TYPE);

        // Create data types
        allSomeTypeObjectEDataType = createEDataType(ALL_SOME_TYPE_OBJECT);
        nonNegativeIntegerOrUnknownEDataType = createEDataType(NON_NEGATIVE_INTEGER_OR_UNKNOWN);
        nonNegativeIntegerOrUnknownMember0ObjectEDataType = createEDataType(NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0_OBJECT);
        nonNegativeIntegerOrUnknownMember1EDataType = createEDataType(NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1);
        positiveIntegerWithStarEDataType = createEDataType(POSITIVE_INTEGER_WITH_STAR);
        resolveValueTypeObjectEDataType = createEDataType(RESOLVE_VALUE_TYPE_OBJECT);
        resultTypeTypeObjectEDataType = createEDataType(RESULT_TYPE_TYPE_OBJECT);
        returnFeatureTypesListTypeEDataType = createEDataType(RETURN_FEATURE_TYPES_LIST_TYPE);
        starStringTypeObjectEDataType = createEDataType(STAR_STRING_TYPE_OBJECT);
        stateValueTypeEDataType = createEDataType(STATE_VALUE_TYPE);
        stateValueTypeMember0ObjectEDataType = createEDataType(STATE_VALUE_TYPE_MEMBER0_OBJECT);
        stateValueTypeMember1EDataType = createEDataType(STATE_VALUE_TYPE_MEMBER1);
        updateActionTypeObjectEDataType = createEDataType(UPDATE_ACTION_TYPE_OBJECT);
        uriEDataType = createEDataType(URI);
        mapEDataType = createEDataType(MAP);
        filterEDataType = createEDataType(FILTER);
        qNameEDataType = createEDataType(QNAME);
        sortByEDataType = createEDataType(SORT_BY);
        calendarEDataType = createEDataType(CALENDAR);
        featureCollectionEDataType = createEDataType(FEATURE_COLLECTION);
        propertyNameEDataType = createEDataType(PROPERTY_NAME);
        featureIdEDataType = createEDataType(FEATURE_ID);
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
        XMLNamespacePackage theXMLNamespacePackage = (XMLNamespacePackage)EPackage.Registry.INSTANCE.getEPackage(XMLNamespacePackage.eNS_URI);
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
        Ows11Package theOws11Package = (Ows11Package)EPackage.Registry.INSTANCE.getEPackage(Ows11Package.eNS_URI);
        Fes20Package theFes20Package = (Fes20Package)EPackage.Registry.INSTANCE.getEPackage(Fes20Package.eNS_URI);
        XlinkPackage theXlinkPackage = (XlinkPackage)EPackage.Registry.INSTANCE.getEPackage(XlinkPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        createStoredQueryResponseTypeEClass.getESuperTypes().add(this.getExecutionStatusType());
        createStoredQueryTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        deleteTypeEClass.getESuperTypes().add(this.getAbstractTransactionActionType());
        describeFeatureTypeTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        describeStoredQueriesTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        dropStoredQueryTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        featureCollectionTypeEClass.getESuperTypes().add(this.getSimpleFeatureCollectionType());
        getCapabilitiesTypeEClass.getESuperTypes().add(theOws11Package.getGetCapabilitiesType());
        getFeatureTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        getFeatureWithLockTypeEClass.getESuperTypes().add(this.getGetFeatureType());
        getPropertyValueTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        insertTypeEClass.getESuperTypes().add(this.getAbstractTransactionActionType());
        listStoredQueriesTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        lockFeatureTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        nativeTypeEClass.getESuperTypes().add(this.getAbstractTransactionActionType());
        queryTypeEClass.getESuperTypes().add(theFes20Package.getAbstractAdhocQueryExpressionType());
        replaceTypeEClass.getESuperTypes().add(this.getAbstractTransactionActionType());
        storedQueryTypeEClass.getESuperTypes().add(theFes20Package.getAbstractQueryExpressionType());
        transactionTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        updateTypeEClass.getESuperTypes().add(this.getAbstractTransactionActionType());
        wfsCapabilitiesTypeEClass.getESuperTypes().add(theOws11Package.getCapabilitiesBaseType());

        // Initialize classes and features; add operations and parameters
        initEClass(abstractTransactionActionTypeEClass, AbstractTransactionActionType.class, "AbstractTransactionActionType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractTransactionActionType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, AbstractTransactionActionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(abstractTypeEClass, AbstractType.class, "AbstractType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, AbstractType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractType_Lang(), theXMLNamespacePackage.getLangType(), "lang", "en", 0, 1, AbstractType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(actionResultsTypeEClass, ActionResultsType.class, "ActionResultsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getActionResultsType_Feature(), this.getCreatedOrModifiedFeatureType(), null, "feature", null, 1, -1, ActionResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(additionalObjectsTypeEClass, AdditionalObjectsType.class, "AdditionalObjectsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAdditionalObjectsType_ValueCollection(), this.getValueCollectionType(), null, "valueCollection", null, 0, 1, AdditionalObjectsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAdditionalObjectsType_SimpleFeatureCollectionGroup(), theEcorePackage.getEFeatureMapEntry(), "simpleFeatureCollectionGroup", null, 0, 1, AdditionalObjectsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAdditionalObjectsType_SimpleFeatureCollection(), this.getSimpleFeatureCollectionType(), null, "simpleFeatureCollection", null, 0, 1, AdditionalObjectsType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(additionalValuesTypeEClass, AdditionalValuesType.class, "AdditionalValuesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAdditionalValuesType_ValueCollection(), this.getValueCollectionType(), null, "valueCollection", null, 0, 1, AdditionalValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAdditionalValuesType_SimpleFeatureCollectionGroup(), theEcorePackage.getEFeatureMapEntry(), "simpleFeatureCollectionGroup", null, 0, 1, AdditionalValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAdditionalValuesType_SimpleFeatureCollection(), this.getSimpleFeatureCollectionType(), null, "simpleFeatureCollection", null, 0, 1, AdditionalValuesType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(baseRequestTypeEClass, BaseRequestType.class, "BaseRequestType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBaseRequestType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_Version(), theXMLTypePackage.getString(), "version", "2.0.0", 1, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_BaseUrl(), ecorePackage.getEString(), "baseUrl", null, 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(createdOrModifiedFeatureTypeEClass, CreatedOrModifiedFeatureType.class, "CreatedOrModifiedFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCreatedOrModifiedFeatureType_ResourceId(), this.getFeatureId(), "resourceId", null, 0, -1, CreatedOrModifiedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCreatedOrModifiedFeatureType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, CreatedOrModifiedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(createStoredQueryResponseTypeEClass, CreateStoredQueryResponseType.class, "CreateStoredQueryResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(createStoredQueryTypeEClass, CreateStoredQueryType.class, "CreateStoredQueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCreateStoredQueryType_StoredQueryDefinition(), this.getStoredQueryDescriptionType(), null, "storedQueryDefinition", null, 0, -1, CreateStoredQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(deleteTypeEClass, DeleteType.class, "DeleteType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDeleteType_Filter(), this.getFilter(), "filter", null, 0, 1, DeleteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDeleteType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 1, 1, DeleteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeFeatureTypeTypeEClass, DescribeFeatureTypeType.class, "DescribeFeatureTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescribeFeatureTypeType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 0, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDescribeFeatureTypeType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "application/gml+xml; version=3.2", 0, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeStoredQueriesResponseTypeEClass, DescribeStoredQueriesResponseType.class, "DescribeStoredQueriesResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDescribeStoredQueriesResponseType_StoredQueryDescription(), this.getStoredQueryDescriptionType(), null, "storedQueryDescription", null, 0, -1, DescribeStoredQueriesResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeStoredQueriesTypeEClass, DescribeStoredQueriesType.class, "DescribeStoredQueriesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescribeStoredQueriesType_StoredQueryId(), this.getURI(), "storedQueryId", null, 0, -1, DescribeStoredQueriesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Abstract(), this.getAbstractType(), null, "abstract", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractTransactionAction(), this.getAbstractTransactionActionType(), null, "abstractTransactionAction", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AdditionalObjects(), this.getAdditionalObjectsType(), null, "additionalObjects", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AdditionalValues(), this.getAdditionalValuesType(), null, "additionalValues", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_BoundedBy(), this.getEnvelopePropertyType(), null, "boundedBy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CreateStoredQuery(), this.getCreateStoredQueryType(), null, "createStoredQuery", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CreateStoredQueryResponse(), this.getCreateStoredQueryResponseType(), null, "createStoredQueryResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Delete(), this.getDeleteType(), null, "delete", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DescribeFeatureType(), this.getDescribeFeatureTypeType(), null, "describeFeatureType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DescribeStoredQueries(), this.getDescribeStoredQueriesType(), null, "describeStoredQueries", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DescribeStoredQueriesResponse(), this.getDescribeStoredQueriesResponseType(), null, "describeStoredQueriesResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DropStoredQuery(), this.getDropStoredQueryType(), null, "dropStoredQuery", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DropStoredQueryResponse(), this.getExecutionStatusType(), null, "dropStoredQueryResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Element(), this.getElementType(), null, "element", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_FeatureCollection(), this.getFeatureCollectionType(), null, "featureCollection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_SimpleFeatureCollection(), this.getSimpleFeatureCollectionType(), null, "simpleFeatureCollection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_FeatureTypeList(), this.getFeatureTypeListType(), null, "featureTypeList", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetFeature(), this.getGetFeatureType(), null, "getFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetFeatureWithLock(), this.getGetFeatureWithLockType(), null, "getFeatureWithLock", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetPropertyValue(), this.getGetPropertyValueType(), null, "getPropertyValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Insert(), this.getInsertType(), null, "insert", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ListStoredQueries(), this.getListStoredQueriesType(), null, "listStoredQueries", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ListStoredQueriesResponse(), this.getListStoredQueriesResponseType(), null, "listStoredQueriesResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_LockFeature(), this.getLockFeatureType(), null, "lockFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_LockFeatureResponse(), this.getLockFeatureResponseType(), null, "lockFeatureResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Member(), this.getMemberPropertyType(), null, "member", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Native(), this.getNativeType(), null, "native", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Property(), this.getPropertyType(), null, "property", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_PropertyName(), this.getPropertyNameType(), null, "propertyName", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Query(), this.getQueryType(), null, "query", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Replace(), this.getReplaceType(), null, "replace", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_StoredQuery(), this.getStoredQueryType(), null, "storedQuery", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Title(), this.getTitleType(), null, "title", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Transaction(), this.getTransactionType(), null, "transaction", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TransactionResponse(), this.getTransactionResponseType(), null, "transactionResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TruncatedResponse(), this.getTruncatedResponseType(), null, "truncatedResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Tuple(), this.getTupleType(), null, "tuple", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Update(), this.getUpdateType(), null, "update", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Value(), theEcorePackage.getEObject(), null, "value", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ValueCollection(), this.getValueCollectionType(), null, "valueCollection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ValueList(), this.getValueListType(), null, "valueList", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_WFSCapabilities(), this.getWFSCapabilitiesType(), null, "wFSCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(dropStoredQueryTypeEClass, DropStoredQueryType.class, "DropStoredQueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDropStoredQueryType_Id(), theXMLTypePackage.getAnyURI(), "id", null, 1, 1, DropStoredQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(elementTypeEClass, ElementType.class, "ElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getElementType_Metadata(), theOws11Package.getMetadataType(), null, "metadata", null, 1, 1, ElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getElementType_ValueList(), this.getValueListType(), null, "valueList", null, 1, 1, ElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementType_Type(), theXMLTypePackage.getQName(), "type", null, 1, 1, ElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(emptyTypeEClass, EmptyType.class, "EmptyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(envelopePropertyTypeEClass, EnvelopePropertyType.class, "EnvelopePropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEnvelopePropertyType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 1, 1, EnvelopePropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(executionStatusTypeEClass, ExecutionStatusType.class, "ExecutionStatusType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getExecutionStatusType_Status(), theXMLTypePackage.getString(), "status", "OK", 0, 1, ExecutionStatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(extendedDescriptionTypeEClass, ExtendedDescriptionType.class, "ExtendedDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExtendedDescriptionType_Element(), this.getElementType(), null, "element", null, 1, -1, ExtendedDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featureCollectionTypeEClass, FeatureCollectionType.class, "FeatureCollectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureCollectionType_AdditionalObjects(), this.getAdditionalObjectsType(), null, "additionalObjects", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureCollectionType_TruncatedResponse(), this.getTruncatedResponseType(), null, "truncatedResponse", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_Next(), theXMLTypePackage.getAnyURI(), "next", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_NumberMatched(), this.getNonNegativeIntegerOrUnknown(), "numberMatched", null, 1, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_NumberReturned(), theXMLTypePackage.getNonNegativeInteger(), "numberReturned", null, 1, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_Previous(), theXMLTypePackage.getAnyURI(), "previous", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_TimeStamp(), this.getCalendar(), "timeStamp", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featuresLockedTypeEClass, FeaturesLockedType.class, "FeaturesLockedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeaturesLockedType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, FeaturesLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeaturesLockedType_ResourceId(), this.getFeatureId(), "resourceId", null, 0, -1, FeaturesLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featuresNotLockedTypeEClass, FeaturesNotLockedType.class, "FeaturesNotLockedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeaturesNotLockedType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, FeaturesNotLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeaturesNotLockedType_ResourceId(), this.getFeatureId(), "resourceId", null, 0, -1, FeaturesNotLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featureTypeListTypeEClass, FeatureTypeListType.class, "FeatureTypeListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureTypeListType_FeatureType(), this.getFeatureTypeType(), null, "featureType", null, 1, -1, FeatureTypeListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featureTypeTypeEClass, FeatureTypeType.class, "FeatureTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeatureTypeType_Name(), theXMLTypePackage.getQName(), "name", null, 1, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_Title(), this.getTitleType(), null, "title", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_Abstract(), this.getAbstractType(), null, "abstract", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_Keywords(), theOws11Package.getKeywordsType(), null, "keywords", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureTypeType_DefaultCRS(), theXMLTypePackage.getAnyURI(), "defaultCRS", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureTypeType_OtherCRS(), theXMLTypePackage.getAnyURI(), "otherCRS", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_NoCRS(), this.getNoCRSType(), null, "noCRS", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_OutputFormats(), this.getOutputFormatListType(), null, "outputFormats", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_WGS84BoundingBox(), theOws11Package.getWGS84BoundingBoxType(), null, "wGS84BoundingBox", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_MetadataURL(), this.getMetadataURLType(), null, "metadataURL", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_ExtendedDescription(), this.getExtendedDescriptionType(), null, "extendedDescription", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCapabilitiesType_Service(), theOws11Package.getServiceType(), "service", "WFS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getFeatureTypeEClass, GetFeatureType.class, "GetFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetFeatureType_AbstractQueryExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "abstractQueryExpressionGroup", null, 1, -1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetFeatureType_AbstractQueryExpression(), theFes20Package.getAbstractQueryExpressionType(), null, "abstractQueryExpression", null, 1, -1, GetFeatureType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_Count(), theXMLTypePackage.getNonNegativeInteger(), "count", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "application/gml+xml; version=3.2", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_Resolve(), this.getResolveValueType(), "resolve", "none", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_ResolveDepth(), this.getPositiveIntegerWithStar(), "resolveDepth", "*", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_ResolveTimeout(), theXMLTypePackage.getPositiveInteger(), "resolveTimeout", "300", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_ResultType(), this.getResultTypeType(), "resultType", "results", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_StartIndex(), theXMLTypePackage.getNonNegativeInteger(), "startIndex", "0", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_Metadata(), this.getMap(), "metadata", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_FormatOptions(), this.getMap(), "formatOptions", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getFeatureWithLockTypeEClass, GetFeatureWithLockType.class, "GetFeatureWithLockType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetFeatureWithLockType_Expiry(), theXMLTypePackage.getPositiveInteger(), "expiry", "300", 0, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureWithLockType_LockAction(), this.getAllSomeType(), "lockAction", "ALL", 0, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getPropertyValueTypeEClass, GetPropertyValueType.class, "GetPropertyValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetPropertyValueType_AbstractQueryExpression(), theFes20Package.getAbstractQueryExpressionType(), null, "abstractQueryExpression", null, 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_Count(), theXMLTypePackage.getNonNegativeInteger(), "count", null, 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "application/gml+xml; version=3.2", 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_Resolve(), this.getResolveValueType(), "resolve", "none", 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_ResolveDepth(), ecorePackage.getEIntegerObject(), "resolveDepth", null, 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_ResolvePath(), theXMLTypePackage.getString(), "resolvePath", null, 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_ResolveTimeout(), theXMLTypePackage.getPositiveInteger(), "resolveTimeout", "300", 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_ResultType(), this.getResultTypeType(), "resultType", "results", 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_StartIndex(), theXMLTypePackage.getNonNegativeInteger(), "startIndex", "0", 0, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetPropertyValueType_ValueReference(), theXMLTypePackage.getString(), "valueReference", null, 1, 1, GetPropertyValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(insertTypeEClass, InsertType.class, "InsertType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInsertType_Any(), ecorePackage.getEJavaObject(), "any", null, 0, -1, InsertType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertType_InputFormat(), theXMLTypePackage.getString(), "inputFormat", "application/gml+xml; version=3.2", 0, 1, InsertType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertType_SrsName(), theXMLTypePackage.getAnyURI(), "srsName", null, 0, 1, InsertType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(listStoredQueriesResponseTypeEClass, ListStoredQueriesResponseType.class, "ListStoredQueriesResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getListStoredQueriesResponseType_StoredQuery(), this.getStoredQueryListItemType(), null, "storedQuery", null, 0, -1, ListStoredQueriesResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(listStoredQueriesTypeEClass, ListStoredQueriesType.class, "ListStoredQueriesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(lockFeatureResponseTypeEClass, LockFeatureResponseType.class, "LockFeatureResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLockFeatureResponseType_FeaturesLocked(), this.getFeaturesLockedType(), null, "featuresLocked", null, 0, 1, LockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLockFeatureResponseType_FeaturesNotLocked(), this.getFeaturesNotLockedType(), null, "featuresNotLocked", null, 0, 1, LockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockFeatureResponseType_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, 1, LockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(lockFeatureTypeEClass, LockFeatureType.class, "LockFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLockFeatureType_AbstractQueryExpressionGroup(), theEcorePackage.getEFeatureMapEntry(), "abstractQueryExpressionGroup", null, 1, -1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLockFeatureType_AbstractQueryExpression(), theFes20Package.getAbstractQueryExpressionType(), null, "abstractQueryExpression", null, 1, -1, LockFeatureType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockFeatureType_Expiry(), theXMLTypePackage.getPositiveInteger(), "expiry", "300", 0, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockFeatureType_LockAction(), this.getAllSomeType(), "lockAction", "ALL", 0, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockFeatureType_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(memberPropertyTypeEClass, MemberPropertyType.class, "MemberPropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMemberPropertyType_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, 1, MemberPropertyType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getMemberPropertyType_Tuple(), this.getTupleType(), null, "tuple", null, 0, 1, MemberPropertyType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_SimpleFeatureCollectionGroup(), theEcorePackage.getEFeatureMapEntry(), "simpleFeatureCollectionGroup", null, 0, 1, MemberPropertyType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getMemberPropertyType_SimpleFeatureCollection(), this.getSimpleFeatureCollectionType(), null, "simpleFeatureCollection", null, 0, 1, MemberPropertyType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Actuate(), theXlinkPackage.getActuateType(), "actuate", null, 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Show(), theXlinkPackage.getShowType(), "show", null, 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_State(), this.getStateValueType(), "state", null, 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMemberPropertyType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, MemberPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(metadataURLTypeEClass, MetadataURLType.class, "MetadataURLType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMetadataURLType_About(), theXMLTypePackage.getAnyURI(), "about", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Actuate(), theXlinkPackage.getActuateType(), "actuate", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Show(), theXlinkPackage.getShowType(), "show", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(nativeTypeEClass, NativeType.class, "NativeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getNativeType_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, NativeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNativeType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, 1, NativeType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getNativeType_SafeToIgnore(), theXMLTypePackage.getBoolean(), "safeToIgnore", null, 1, 1, NativeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNativeType_VendorId(), theXMLTypePackage.getString(), "vendorId", null, 1, 1, NativeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(noCRSTypeEClass, NoCRSType.class, "NoCRSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(outputFormatListTypeEClass, OutputFormatListType.class, "OutputFormatListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOutputFormatListType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, OutputFormatListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputFormatListType_Format(), theXMLTypePackage.getString(), "format", null, 1, 1, OutputFormatListType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(parameterExpressionTypeEClass, ParameterExpressionType.class, "ParameterExpressionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getParameterExpressionType_Title(), this.getTitleType(), null, "title", null, 0, -1, ParameterExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getParameterExpressionType_Abstract(), this.getAbstractType(), null, "abstract", null, 0, -1, ParameterExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getParameterExpressionType_Metadata(), theOws11Package.getMetadataType(), null, "metadata", null, 0, -1, ParameterExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getParameterExpressionType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ParameterExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getParameterExpressionType_Type(), theXMLTypePackage.getQName(), "type", null, 1, 1, ParameterExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(parameterTypeEClass, ParameterType.class, "ParameterType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getParameterType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getParameterType_Value(), ecorePackage.getEString(), "value", null, 0, 1, ParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(propertyNameTypeEClass, PropertyNameType.class, "PropertyNameType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPropertyNameType_Value(), theXMLTypePackage.getQName(), "value", null, 0, 1, PropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyNameType_Resolve(), this.getResolveValueType(), "resolve", "none", 0, 1, PropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyNameType_ResolveDepth(), this.getPositiveIntegerWithStar(), "resolveDepth", "*", 0, 1, PropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyNameType_ResolvePath(), theXMLTypePackage.getString(), "resolvePath", null, 0, 1, PropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyNameType_ResolveTimeout(), theXMLTypePackage.getPositiveInteger(), "resolveTimeout", "300", 0, 1, PropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(propertyTypeEClass, PropertyType.class, "PropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPropertyType_ValueReference(), this.getValueReferenceType(), null, "valueReference", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyType_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(queryExpressionTextTypeEClass, QueryExpressionTextType.class, "QueryExpressionTextType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getQueryExpressionTextType_IsPrivate(), theXMLTypePackage.getBoolean(), "isPrivate", "false", 0, 1, QueryExpressionTextType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryExpressionTextType_Language(), theXMLTypePackage.getAnyURI(), "language", null, 1, 1, QueryExpressionTextType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryExpressionTextType_ReturnFeatureTypes(), this.getReturnFeatureTypesListType(), "returnFeatureTypes", null, 1, 1, QueryExpressionTextType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryExpressionTextType_Value(), ecorePackage.getEString(), "value", null, 0, 1, QueryExpressionTextType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(queryTypeEClass, QueryType.class, "QueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getQueryType_FeatureVersion(), theXMLTypePackage.getString(), "featureVersion", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_SrsName(), this.getURI(), "srsName", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_Filter(), this.getFilter(), "filter", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_PropertyNames(), this.getQName(), "propertyNames", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_SortBy(), this.getSortBy(), "sortBy", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(replaceTypeEClass, ReplaceType.class, "ReplaceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getReplaceType_Any(), ecorePackage.getEJavaObject(), "any", null, 0, -1, ReplaceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getReplaceType_Filter(), this.getFilter(), "filter", null, 0, 1, ReplaceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getReplaceType_InputFormat(), theXMLTypePackage.getString(), "inputFormat", "application/gml+xml; version=3.2", 0, 1, ReplaceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getReplaceType_SrsName(), theXMLTypePackage.getAnyURI(), "srsName", null, 0, 1, ReplaceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(simpleFeatureCollectionTypeEClass, SimpleFeatureCollectionType.class, "SimpleFeatureCollectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSimpleFeatureCollectionType_BoundedBy(), this.getEnvelopePropertyType(), null, "boundedBy", null, 0, 1, SimpleFeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimpleFeatureCollectionType_Member(), this.getFeatureCollection(), "member", null, 0, -1, SimpleFeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(storedQueryDescriptionTypeEClass, StoredQueryDescriptionType.class, "StoredQueryDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStoredQueryDescriptionType_Title(), this.getTitleType(), null, "title", null, 0, -1, StoredQueryDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStoredQueryDescriptionType_Abstract(), this.getAbstractType(), null, "abstract", null, 0, -1, StoredQueryDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStoredQueryDescriptionType_Metadata(), theOws11Package.getMetadataType(), null, "metadata", null, 0, -1, StoredQueryDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStoredQueryDescriptionType_Parameter(), this.getParameterExpressionType(), null, "parameter", null, 0, -1, StoredQueryDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStoredQueryDescriptionType_QueryExpressionText(), this.getQueryExpressionTextType(), null, "queryExpressionText", null, 1, -1, StoredQueryDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStoredQueryDescriptionType_Id(), theXMLTypePackage.getAnyURI(), "id", null, 1, 1, StoredQueryDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(storedQueryListItemTypeEClass, StoredQueryListItemType.class, "StoredQueryListItemType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStoredQueryListItemType_Title(), this.getTitleType(), null, "title", null, 0, -1, StoredQueryListItemType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStoredQueryListItemType_ReturnFeatureType(), theXMLTypePackage.getQName(), "returnFeatureType", null, 1, 1, StoredQueryListItemType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStoredQueryListItemType_Id(), theXMLTypePackage.getAnyURI(), "id", null, 1, 1, StoredQueryListItemType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(storedQueryTypeEClass, StoredQueryType.class, "StoredQueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStoredQueryType_Parameter(), this.getParameterType(), null, "parameter", null, 0, -1, StoredQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStoredQueryType_Id(), theXMLTypePackage.getAnyURI(), "id", null, 1, 1, StoredQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(titleTypeEClass, TitleType.class, "TitleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTitleType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, TitleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTitleType_Lang(), theXMLNamespacePackage.getLangType(), "lang", "en", 0, 1, TitleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionResponseTypeEClass, TransactionResponseType.class, "TransactionResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTransactionResponseType_TransactionSummary(), this.getTransactionSummaryType(), null, "transactionSummary", null, 1, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionResponseType_InsertResults(), this.getActionResultsType(), null, "insertResults", null, 0, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionResponseType_UpdateResults(), this.getActionResultsType(), null, "updateResults", null, 0, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionResponseType_ReplaceResults(), this.getActionResultsType(), null, "replaceResults", null, 0, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionResponseType_Version(), theXMLTypePackage.getString(), "version", "2.0.0", 1, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionSummaryTypeEClass, TransactionSummaryType.class, "TransactionSummaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransactionSummaryType_TotalInserted(), theXMLTypePackage.getNonNegativeInteger(), "totalInserted", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_TotalUpdated(), theXMLTypePackage.getNonNegativeInteger(), "totalUpdated", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_TotalReplaced(), theXMLTypePackage.getNonNegativeInteger(), "totalReplaced", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_TotalDeleted(), theXMLTypePackage.getNonNegativeInteger(), "totalDeleted", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionTypeEClass, TransactionType.class, "TransactionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransactionType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_AbstractTransactionActionGroup(), theEcorePackage.getEFeatureMapEntry(), "abstractTransactionActionGroup", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_AbstractTransactionAction(), this.getAbstractTransactionActionType(), null, "abstractTransactionAction", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_ReleaseAction(), this.getAllSomeType(), "releaseAction", "ALL", 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_SrsName(), theXMLTypePackage.getAnyURI(), "srsName", null, 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(truncatedResponseTypeEClass, TruncatedResponseType.class, "TruncatedResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTruncatedResponseType_ExceptionReport(), theOws11Package.getExceptionReportType(), null, "exceptionReport", null, 1, 1, TruncatedResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tupleTypeEClass, TupleType.class, "TupleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTupleType_Member(), this.getMemberPropertyType(), null, "member", null, 2, -1, TupleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(updateTypeEClass, UpdateType.class, "UpdateType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUpdateType_Property(), this.getPropertyType(), null, "property", null, 1, -1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateType_Filter(), this.getFilter(), "filter", null, 0, 1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateType_InputFormat(), theXMLTypePackage.getString(), "inputFormat", "application/gml+xml; version=3.2", 0, 1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateType_SrsName(), theXMLTypePackage.getAnyURI(), "srsName", null, 0, 1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 1, 1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(valueCollectionTypeEClass, ValueCollectionType.class, "ValueCollectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getValueCollectionType_Member(), this.getFeatureCollection(), "member", null, 0, -1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getValueCollectionType_AdditionalValues(), this.getAdditionalValuesType(), null, "additionalValues", null, 0, 1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getValueCollectionType_TruncatedResponse(), this.getTruncatedResponseType(), null, "truncatedResponse", null, 0, 1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValueCollectionType_Next(), theXMLTypePackage.getAnyURI(), "next", null, 0, 1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValueCollectionType_NumberMatched(), this.getNonNegativeIntegerOrUnknown(), "numberMatched", null, 1, 1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValueCollectionType_NumberReturned(), theXMLTypePackage.getNonNegativeInteger(), "numberReturned", null, 1, 1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValueCollectionType_Previous(), theXMLTypePackage.getAnyURI(), "previous", null, 0, 1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValueCollectionType_TimeStamp(), this.getCalendar(), "timeStamp", null, 0, 1, ValueCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(valueListTypeEClass, ValueListType.class, "ValueListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getValueListType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, ValueListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getValueListType_Value(), theEcorePackage.getEObject(), null, "value", null, 1, -1, ValueListType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(valueReferenceTypeEClass, ValueReferenceType.class, "ValueReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getValueReferenceType_Value(), this.getQName(), "value", null, 0, 1, ValueReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValueReferenceType_Action(), this.getUpdateActionType(), "action", "replace", 0, 1, ValueReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wfsCapabilitiesTypeEClass, WFSCapabilitiesType.class, "WFSCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWFSCapabilitiesType_WSDL(), this.getWSDLType(), null, "wSDL", null, 0, 1, WFSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWFSCapabilitiesType_FeatureTypeList(), this.getFeatureTypeListType(), null, "featureTypeList", null, 0, 1, WFSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWFSCapabilitiesType_FilterCapabilities(), theFes20Package.getFilterCapabilitiesType(), null, "filterCapabilities", null, 0, 1, WFSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wsdlTypeEClass, WSDLType.class, "WSDLType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getWSDLType_Actuate(), theXlinkPackage.getActuateType(), "actuate", null, 0, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLType_Show(), theXlinkPackage.getShowType(), "show", null, 0, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDLType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, WSDLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(allSomeTypeEEnum, AllSomeType.class, "AllSomeType");
        addEEnumLiteral(allSomeTypeEEnum, AllSomeType.ALL);
        addEEnumLiteral(allSomeTypeEEnum, AllSomeType.SOME);

        initEEnum(nonNegativeIntegerOrUnknownMember0EEnum, NonNegativeIntegerOrUnknownMember0.class, "NonNegativeIntegerOrUnknownMember0");
        addEEnumLiteral(nonNegativeIntegerOrUnknownMember0EEnum, NonNegativeIntegerOrUnknownMember0.UNKNOWN);

        initEEnum(resolveValueTypeEEnum, ResolveValueType.class, "ResolveValueType");
        addEEnumLiteral(resolveValueTypeEEnum, ResolveValueType.LOCAL);
        addEEnumLiteral(resolveValueTypeEEnum, ResolveValueType.REMOTE);
        addEEnumLiteral(resolveValueTypeEEnum, ResolveValueType.ALL);
        addEEnumLiteral(resolveValueTypeEEnum, ResolveValueType.NONE);

        initEEnum(resultTypeTypeEEnum, ResultTypeType.class, "ResultTypeType");
        addEEnumLiteral(resultTypeTypeEEnum, ResultTypeType.RESULTS);
        addEEnumLiteral(resultTypeTypeEEnum, ResultTypeType.HITS);

        initEEnum(starStringTypeEEnum, StarStringType.class, "StarStringType");
        addEEnumLiteral(starStringTypeEEnum, StarStringType._);

        initEEnum(stateValueTypeMember0EEnum, StateValueTypeMember0.class, "StateValueTypeMember0");
        addEEnumLiteral(stateValueTypeMember0EEnum, StateValueTypeMember0.VALID);
        addEEnumLiteral(stateValueTypeMember0EEnum, StateValueTypeMember0.SUPERSEDED);
        addEEnumLiteral(stateValueTypeMember0EEnum, StateValueTypeMember0.RETIRED);
        addEEnumLiteral(stateValueTypeMember0EEnum, StateValueTypeMember0.FUTURE);

        initEEnum(updateActionTypeEEnum, UpdateActionType.class, "UpdateActionType");
        addEEnumLiteral(updateActionTypeEEnum, UpdateActionType.REPLACE);
        addEEnumLiteral(updateActionTypeEEnum, UpdateActionType.INSERT_BEFORE);
        addEEnumLiteral(updateActionTypeEEnum, UpdateActionType.INSERT_AFTER);
        addEEnumLiteral(updateActionTypeEEnum, UpdateActionType.REMOVE);

        // Initialize data types
        initEDataType(allSomeTypeObjectEDataType, AllSomeType.class, "AllSomeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(nonNegativeIntegerOrUnknownEDataType, Object.class, "NonNegativeIntegerOrUnknown", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(nonNegativeIntegerOrUnknownMember0ObjectEDataType, NonNegativeIntegerOrUnknownMember0.class, "NonNegativeIntegerOrUnknownMember0Object", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(nonNegativeIntegerOrUnknownMember1EDataType, BigInteger.class, "NonNegativeIntegerOrUnknownMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(positiveIntegerWithStarEDataType, Object.class, "PositiveIntegerWithStar", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(resolveValueTypeObjectEDataType, ResolveValueType.class, "ResolveValueTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(resultTypeTypeObjectEDataType, ResultTypeType.class, "ResultTypeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(returnFeatureTypesListTypeEDataType, List.class, "ReturnFeatureTypesListType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(starStringTypeObjectEDataType, StarStringType.class, "StarStringTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(stateValueTypeEDataType, Object.class, "StateValueType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(stateValueTypeMember0ObjectEDataType, StateValueTypeMember0.class, "StateValueTypeMember0Object", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(stateValueTypeMember1EDataType, String.class, "StateValueTypeMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(updateActionTypeObjectEDataType, UpdateActionType.class, "UpdateActionTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(uriEDataType, java.net.URI.class, "URI", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(mapEDataType, Map.class, "Map", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(filterEDataType, Filter.class, "Filter", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(qNameEDataType, QName.class, "QName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(sortByEDataType, SortBy.class, "SortBy", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(calendarEDataType, Calendar.class, "Calendar", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(featureCollectionEDataType, FeatureCollection.class, "FeatureCollection", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(propertyNameEDataType, PropertyName.class, "PropertyName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(featureIdEDataType, FeatureId.class, "FeatureId", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

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
          (abstractTransactionActionTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractTransactionActionType",
             "kind", "empty"
           });		
        addAnnotation
          (getAbstractTransactionActionType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });		
        addAnnotation
          (abstractTypeEClass, 
           source, 
           new String[] {
             "name", "Abstract_._type",
             "kind", "simple"
           });		
        addAnnotation
          (getAbstractType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getAbstractType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });			
        addAnnotation
          (actionResultsTypeEClass, 
           source, 
           new String[] {
             "name", "ActionResultsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getActionResultsType_Feature(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Feature",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (additionalObjectsTypeEClass, 
           source, 
           new String[] {
             "name", "additionalObjects_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAdditionalObjectsType_ValueCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueCollection",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAdditionalObjectsType_SimpleFeatureCollectionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "SimpleFeatureCollection:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAdditionalObjectsType_SimpleFeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SimpleFeatureCollection",
             "namespace", "##targetNamespace",
             "group", "SimpleFeatureCollection:group"
           });		
        addAnnotation
          (additionalValuesTypeEClass, 
           source, 
           new String[] {
             "name", "additionalValues_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAdditionalValuesType_ValueCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueCollection",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAdditionalValuesType_SimpleFeatureCollectionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "SimpleFeatureCollection:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAdditionalValuesType_SimpleFeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SimpleFeatureCollection",
             "namespace", "##targetNamespace",
             "group", "SimpleFeatureCollection:group"
           });		
        addAnnotation
          (baseRequestTypeEClass, 
           source, 
           new String[] {
             "name", "BaseRequestType",
             "kind", "empty"
           });		
        addAnnotation
          (getBaseRequestType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });		
        addAnnotation
          (getBaseRequestType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });		
        addAnnotation
          (getBaseRequestType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });		
        addAnnotation
          (createdOrModifiedFeatureTypeEClass, 
           source, 
           new String[] {
             "name", "CreatedOrModifiedFeatureType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCreatedOrModifiedFeatureType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });		
        addAnnotation
          (createStoredQueryResponseTypeEClass, 
           source, 
           new String[] {
             "name", "CreateStoredQueryResponseType",
             "kind", "empty"
           });		
        addAnnotation
          (createStoredQueryTypeEClass, 
           source, 
           new String[] {
             "name", "CreateStoredQueryType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCreateStoredQueryType_StoredQueryDefinition(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "StoredQueryDefinition",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (deleteTypeEClass, 
           source, 
           new String[] {
             "name", "DeleteType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDeleteType_TypeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeName"
           });		
        addAnnotation
          (describeFeatureTypeTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeFeatureTypeType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDescribeFeatureTypeType_TypeName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TypeName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDescribeFeatureTypeType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });		
        addAnnotation
          (describeStoredQueriesResponseTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeStoredQueriesResponseType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDescribeStoredQueriesResponseType_StoredQueryDescription(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "StoredQueryDescription",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (describeStoredQueriesTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeStoredQueriesType",
             "kind", "elementOnly"
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
          (getDocumentRoot_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AbstractTransactionAction(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractTransactionAction",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AdditionalObjects(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "additionalObjects",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AdditionalValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "additionalValues",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_BoundedBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "boundedBy",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_CreateStoredQuery(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CreateStoredQuery",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_CreateStoredQueryResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CreateStoredQueryResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Delete(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Delete",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractTransactionAction"
           });		
        addAnnotation
          (getDocumentRoot_DescribeFeatureType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DescribeFeatureType",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_DescribeStoredQueries(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DescribeStoredQueries",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_DescribeStoredQueriesResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DescribeStoredQueriesResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_DropStoredQuery(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DropStoredQuery",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_DropStoredQueryResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DropStoredQueryResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Element(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Element",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_FeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeatureCollection",
             "namespace", "##targetNamespace",
             "affiliation", "SimpleFeatureCollection"
           });		
        addAnnotation
          (getDocumentRoot_SimpleFeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SimpleFeatureCollection",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_FeatureTypeList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeatureTypeList",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_GetCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetCapabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_GetFeature(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetFeature",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_GetFeatureWithLock(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetFeatureWithLock",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_GetPropertyValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetPropertyValue",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Insert(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Insert",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractTransactionAction"
           });		
        addAnnotation
          (getDocumentRoot_ListStoredQueries(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ListStoredQueries",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_ListStoredQueriesResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ListStoredQueriesResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_LockFeature(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LockFeature",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_LockFeatureResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LockFeatureResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Member(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "member",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Native(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Native",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractTransactionAction"
           });		
        addAnnotation
          (getDocumentRoot_Property(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Property",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_PropertyName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyName",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/fes/2.0#AbstractProjectionClause"
           });		
        addAnnotation
          (getDocumentRoot_Query(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Query",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/fes/2.0#AbstractAdhocQueryExpression"
           });		
        addAnnotation
          (getDocumentRoot_Replace(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Replace",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractTransactionAction"
           });		
        addAnnotation
          (getDocumentRoot_StoredQuery(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "StoredQuery",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/fes/2.0#AbstractQueryExpression"
           });		
        addAnnotation
          (getDocumentRoot_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Transaction(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Transaction",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_TransactionResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TransactionResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_TruncatedResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "truncatedResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Tuple(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Tuple",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Update(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Update",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractTransactionAction"
           });		
        addAnnotation
          (getDocumentRoot_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_ValueCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueCollection",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_ValueList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueList",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_WFSCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WFS_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (dropStoredQueryTypeEClass, 
           source, 
           new String[] {
             "name", "DropStoredQuery_._type",
             "kind", "empty"
           });		
        addAnnotation
          (getDropStoredQueryType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });		
        addAnnotation
          (elementTypeEClass, 
           source, 
           new String[] {
             "name", "ElementType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getElementType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getElementType_ValueList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValueList",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getElementType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (getElementType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type"
           });		
        addAnnotation
          (emptyTypeEClass, 
           source, 
           new String[] {
             "name", "EmptyType",
             "kind", "empty"
           });		
        addAnnotation
          (envelopePropertyTypeEClass, 
           source, 
           new String[] {
             "name", "EnvelopePropertyType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getEnvelopePropertyType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":0",
             "processing", "strict"
           });		
        addAnnotation
          (executionStatusTypeEClass, 
           source, 
           new String[] {
             "name", "ExecutionStatusType",
             "kind", "empty"
           });		
        addAnnotation
          (getExecutionStatusType_Status(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "status"
           });		
        addAnnotation
          (extendedDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "ExtendedDescriptionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getExtendedDescriptionType_Element(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Element",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (featureCollectionTypeEClass, 
           source, 
           new String[] {
             "name", "FeatureCollectionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFeatureCollectionType_AdditionalObjects(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "additionalObjects",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureCollectionType_TruncatedResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "truncatedResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureCollectionType_LockId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lockId"
           });		
        addAnnotation
          (getFeatureCollectionType_Next(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "next"
           });		
        addAnnotation
          (getFeatureCollectionType_NumberMatched(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "numberMatched"
           });		
        addAnnotation
          (getFeatureCollectionType_NumberReturned(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "numberReturned"
           });		
        addAnnotation
          (getFeatureCollectionType_Previous(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "previous"
           });		
        addAnnotation
          (featuresLockedTypeEClass, 
           source, 
           new String[] {
             "name", "FeaturesLockedType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFeaturesLockedType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });		
        addAnnotation
          (featuresNotLockedTypeEClass, 
           source, 
           new String[] {
             "name", "FeaturesNotLockedType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFeaturesNotLockedType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });		
        addAnnotation
          (featureTypeListTypeEClass, 
           source, 
           new String[] {
             "name", "FeatureTypeListType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFeatureTypeListType_FeatureType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeatureType",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (featureTypeTypeEClass, 
           source, 
           new String[] {
             "name", "FeatureTypeType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getFeatureTypeType_Name(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Name",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_Keywords(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Keywords",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getFeatureTypeType_DefaultCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DefaultCRS",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_OtherCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OtherCRS",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_NoCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "NoCRS",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_OutputFormats(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputFormats",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_WGS84BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WGS84BoundingBox",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getFeatureTypeType_MetadataURL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MetadataURL",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_ExtendedDescription(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExtendedDescription",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "GetCapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGetCapabilitiesType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });		
        addAnnotation
          (getFeatureTypeEClass, 
           source, 
           new String[] {
             "name", "GetFeatureType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGetFeatureType_AbstractQueryExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AbstractQueryExpression:group",
             "namespace", "http://www.opengis.net/fes/2.0"
           });		
        addAnnotation
          (getGetFeatureType_AbstractQueryExpression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractQueryExpression",
             "namespace", "http://www.opengis.net/fes/2.0",
             "group", "http://www.opengis.net/fes/2.0#AbstractQueryExpression:group"
           });		
        addAnnotation
          (getGetFeatureType_Count(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "count"
           });		
        addAnnotation
          (getGetFeatureType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });		
        addAnnotation
          (getGetFeatureType_Resolve(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolve"
           });		
        addAnnotation
          (getGetFeatureType_ResolveDepth(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolveDepth"
           });		
        addAnnotation
          (getGetFeatureType_ResolveTimeout(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolveTimeout"
           });		
        addAnnotation
          (getGetFeatureType_ResultType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resultType"
           });		
        addAnnotation
          (getGetFeatureType_StartIndex(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "startIndex"
           });		
        addAnnotation
          (getFeatureWithLockTypeEClass, 
           source, 
           new String[] {
             "name", "GetFeatureWithLockType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGetFeatureWithLockType_Expiry(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "expiry"
           });		
        addAnnotation
          (getGetFeatureWithLockType_LockAction(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lockAction"
           });		
        addAnnotation
          (getPropertyValueTypeEClass, 
           source, 
           new String[] {
             "name", "GetPropertyValueType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGetPropertyValueType_Count(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "count"
           });		
        addAnnotation
          (getGetPropertyValueType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });		
        addAnnotation
          (getGetPropertyValueType_Resolve(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolve"
           });		
        addAnnotation
          (getGetPropertyValueType_ResolvePath(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolvePath"
           });		
        addAnnotation
          (getGetPropertyValueType_ResolveTimeout(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolveTimeout"
           });		
        addAnnotation
          (getGetPropertyValueType_ResultType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resultType"
           });		
        addAnnotation
          (getGetPropertyValueType_StartIndex(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "startIndex"
           });		
        addAnnotation
          (getGetPropertyValueType_ValueReference(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "valueReference"
           });		
        addAnnotation
          (insertTypeEClass, 
           source, 
           new String[] {
             "name", "InsertType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getInsertType_InputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "inputFormat"
           });		
        addAnnotation
          (getInsertType_SrsName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "srsName"
           });		
        addAnnotation
          (listStoredQueriesResponseTypeEClass, 
           source, 
           new String[] {
             "name", "ListStoredQueriesResponseType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getListStoredQueriesResponseType_StoredQuery(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "StoredQuery",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (listStoredQueriesTypeEClass, 
           source, 
           new String[] {
             "name", "ListStoredQueriesType",
             "kind", "empty"
           });		
        addAnnotation
          (lockFeatureResponseTypeEClass, 
           source, 
           new String[] {
             "name", "LockFeatureResponseType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getLockFeatureResponseType_FeaturesLocked(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeaturesLocked",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getLockFeatureResponseType_FeaturesNotLocked(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeaturesNotLocked",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getLockFeatureResponseType_LockId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lockId"
           });		
        addAnnotation
          (lockFeatureTypeEClass, 
           source, 
           new String[] {
             "name", "LockFeatureType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getLockFeatureType_AbstractQueryExpressionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AbstractQueryExpression:group",
             "namespace", "http://www.opengis.net/fes/2.0"
           });		
        addAnnotation
          (getLockFeatureType_AbstractQueryExpression(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractQueryExpression",
             "namespace", "http://www.opengis.net/fes/2.0",
             "group", "http://www.opengis.net/fes/2.0#AbstractQueryExpression:group"
           });		
        addAnnotation
          (getLockFeatureType_Expiry(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "expiry"
           });		
        addAnnotation
          (getLockFeatureType_LockAction(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lockAction"
           });		
        addAnnotation
          (getLockFeatureType_LockId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lockId"
           });		
        addAnnotation
          (memberPropertyTypeEClass, 
           source, 
           new String[] {
             "name", "MemberPropertyType",
             "kind", "mixed"
           });		
        addAnnotation
          (getMemberPropertyType_Mixed(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "name", ":mixed"
           });		
        addAnnotation
          (getMemberPropertyType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":1",
             "processing", "lax"
           });		
        addAnnotation
          (getMemberPropertyType_Tuple(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Tuple",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getMemberPropertyType_SimpleFeatureCollectionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "SimpleFeatureCollection:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getMemberPropertyType_SimpleFeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SimpleFeatureCollection",
             "namespace", "##targetNamespace",
             "group", "SimpleFeatureCollection:group"
           });		
        addAnnotation
          (getMemberPropertyType_Actuate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "actuate",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getMemberPropertyType_Arcrole(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "arcrole",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMemberPropertyType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMemberPropertyType_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMemberPropertyType_Show(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "show",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getMemberPropertyType_State(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "state"
           });		
        addAnnotation
          (getMemberPropertyType_Title(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMemberPropertyType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (metadataURLTypeEClass, 
           source, 
           new String[] {
             "name", "MetadataURLType",
             "kind", "empty"
           });		
        addAnnotation
          (getMetadataURLType_About(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "about"
           });		
        addAnnotation
          (getMetadataURLType_Actuate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "actuate",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getMetadataURLType_Arcrole(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "arcrole",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMetadataURLType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMetadataURLType_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMetadataURLType_Show(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "show",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getMetadataURLType_Title(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getMetadataURLType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (nativeTypeEClass, 
           source, 
           new String[] {
             "name", "NativeType",
             "kind", "mixed"
           });		
        addAnnotation
          (getNativeType_Mixed(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "name", ":mixed"
           });		
        addAnnotation
          (getNativeType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":2",
             "processing", "lax"
           });		
        addAnnotation
          (getNativeType_SafeToIgnore(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "safeToIgnore"
           });		
        addAnnotation
          (getNativeType_VendorId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "vendorId"
           });		
        addAnnotation
          (noCRSTypeEClass, 
           source, 
           new String[] {
             "name", "NoCRS_._type",
             "kind", "empty"
           });		
        addAnnotation
          (outputFormatListTypeEClass, 
           source, 
           new String[] {
             "name", "OutputFormatListType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getOutputFormatListType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });		
        addAnnotation
          (getOutputFormatListType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });		
        addAnnotation
          (parameterExpressionTypeEClass, 
           source, 
           new String[] {
             "name", "ParameterExpressionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getParameterExpressionType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getParameterExpressionType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getParameterExpressionType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getParameterExpressionType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (getParameterExpressionType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type"
           });		
        addAnnotation
          (parameterTypeEClass, 
           source, 
           new String[] {
             "name", "ParameterType",
             "kind", "mixed"
           });		
        addAnnotation
          (getParameterType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (propertyNameTypeEClass, 
           source, 
           new String[] {
             "name", "PropertyName_._type",
             "kind", "simple"
           });		
        addAnnotation
          (getPropertyNameType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getPropertyNameType_Resolve(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolve"
           });		
        addAnnotation
          (getPropertyNameType_ResolveDepth(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolveDepth"
           });		
        addAnnotation
          (getPropertyNameType_ResolvePath(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolvePath"
           });		
        addAnnotation
          (getPropertyNameType_ResolveTimeout(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resolveTimeout"
           });		
        addAnnotation
          (propertyTypeEClass, 
           source, 
           new String[] {
             "name", "PropertyType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (queryExpressionTextTypeEClass, 
           source, 
           new String[] {
             "name", "QueryExpressionTextType",
             "kind", "mixed"
           });		
        addAnnotation
          (getQueryExpressionTextType_IsPrivate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "isPrivate"
           });		
        addAnnotation
          (getQueryExpressionTextType_Language(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "language"
           });		
        addAnnotation
          (getQueryExpressionTextType_ReturnFeatureTypes(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "returnFeatureTypes"
           });		
        addAnnotation
          (queryTypeEClass, 
           source, 
           new String[] {
             "name", "QueryType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getQueryType_FeatureVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "featureVersion"
           });		
        addAnnotation
          (replaceTypeEClass, 
           source, 
           new String[] {
             "name", "ReplaceType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getReplaceType_InputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "inputFormat"
           });		
        addAnnotation
          (getReplaceType_SrsName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "srsName"
           });		
        addAnnotation
          (simpleFeatureCollectionTypeEClass, 
           source, 
           new String[] {
             "name", "SimpleFeatureCollectionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSimpleFeatureCollectionType_BoundedBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "boundedBy",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (storedQueryDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "StoredQueryDescriptionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getStoredQueryDescriptionType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getStoredQueryDescriptionType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getStoredQueryDescriptionType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows/1.1"
           });		
        addAnnotation
          (getStoredQueryDescriptionType_Parameter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Parameter",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getStoredQueryDescriptionType_QueryExpressionText(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "QueryExpressionText",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getStoredQueryDescriptionType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });		
        addAnnotation
          (storedQueryListItemTypeEClass, 
           source, 
           new String[] {
             "name", "StoredQueryListItemType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getStoredQueryListItemType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getStoredQueryListItemType_ReturnFeatureType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ReturnFeatureType",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getStoredQueryListItemType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });		
        addAnnotation
          (storedQueryTypeEClass, 
           source, 
           new String[] {
             "name", "StoredQueryType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getStoredQueryType_Parameter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Parameter",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getStoredQueryType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });		
        addAnnotation
          (titleTypeEClass, 
           source, 
           new String[] {
             "name", "Title_._type",
             "kind", "simple"
           });		
        addAnnotation
          (getTitleType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getTitleType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });			
        addAnnotation
          (transactionResponseTypeEClass, 
           source, 
           new String[] {
             "name", "TransactionResponseType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTransactionResponseType_TransactionSummary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TransactionSummary",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionResponseType_InsertResults(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "InsertResults",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionResponseType_UpdateResults(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UpdateResults",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionResponseType_ReplaceResults(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ReplaceResults",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionResponseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });		
        addAnnotation
          (transactionSummaryTypeEClass, 
           source, 
           new String[] {
             "name", "TransactionSummaryType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTransactionSummaryType_TotalInserted(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "totalInserted",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionSummaryType_TotalUpdated(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "totalUpdated",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionSummaryType_TotalReplaced(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "totalReplaced",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionSummaryType_TotalDeleted(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "totalDeleted",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (transactionTypeEClass, 
           source, 
           new String[] {
             "name", "TransactionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTransactionType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:3"
           });		
        addAnnotation
          (getTransactionType_AbstractTransactionActionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AbstractTransactionAction:group",
             "namespace", "##targetNamespace",
             "group", "#group:3"
           });		
        addAnnotation
          (getTransactionType_AbstractTransactionAction(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractTransactionAction",
             "namespace", "##targetNamespace",
             "group", "AbstractTransactionAction:group"
           });		
        addAnnotation
          (getTransactionType_LockId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lockId"
           });		
        addAnnotation
          (getTransactionType_ReleaseAction(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "releaseAction"
           });		
        addAnnotation
          (getTransactionType_SrsName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "srsName"
           });		
        addAnnotation
          (truncatedResponseTypeEClass, 
           source, 
           new String[] {
             "name", "truncatedResponse_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTruncatedResponseType_ExceptionReport(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExceptionReport",
             "namespace", "http://www.opengis.net/ows/1.1"
           });			
        addAnnotation
          (tupleTypeEClass, 
           source, 
           new String[] {
             "name", "TupleType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTupleType_Member(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "member",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (updateTypeEClass, 
           source, 
           new String[] {
             "name", "UpdateType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getUpdateType_Property(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Property",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUpdateType_InputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "inputFormat"
           });		
        addAnnotation
          (getUpdateType_SrsName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "srsName"
           });		
        addAnnotation
          (getUpdateType_TypeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeName"
           });		
        addAnnotation
          (valueCollectionTypeEClass, 
           source, 
           new String[] {
             "name", "ValueCollectionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getValueCollectionType_AdditionalValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "additionalValues",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getValueCollectionType_TruncatedResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "truncatedResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getValueCollectionType_Next(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "next"
           });		
        addAnnotation
          (getValueCollectionType_NumberMatched(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "numberMatched"
           });		
        addAnnotation
          (getValueCollectionType_NumberReturned(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "numberReturned"
           });		
        addAnnotation
          (getValueCollectionType_Previous(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "previous"
           });		
        addAnnotation
          (valueListTypeEClass, 
           source, 
           new String[] {
             "name", "ValueListType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getValueListType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });		
        addAnnotation
          (getValueListType_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });		
        addAnnotation
          (valueReferenceTypeEClass, 
           source, 
           new String[] {
             "name", "ValueReference_._type",
             "kind", "simple"
           });		
        addAnnotation
          (getValueReferenceType_Action(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "action"
           });		
        addAnnotation
          (wfsCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "WFS_CapabilitiesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getWFSCapabilitiesType_WSDL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WSDL",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getWFSCapabilitiesType_FeatureTypeList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeatureTypeList",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getWFSCapabilitiesType_FilterCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter_Capabilities",
             "namespace", "http://www.opengis.net/fes/2.0"
           });		
        addAnnotation
          (wsdlTypeEClass, 
           source, 
           new String[] {
             "name", "WSDL_._type",
             "kind", "empty"
           });		
        addAnnotation
          (getWSDLType_Actuate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "actuate",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getWSDLType_Arcrole(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "arcrole",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getWSDLType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getWSDLType_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getWSDLType_Show(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "show",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getWSDLType_Title(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getWSDLType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (allSomeTypeEEnum, 
           source, 
           new String[] {
             "name", "AllSomeType"
           });		
        addAnnotation
          (nonNegativeIntegerOrUnknownMember0EEnum, 
           source, 
           new String[] {
             "name", "nonNegativeIntegerOrUnknown_._member_._0"
           });		
        addAnnotation
          (resolveValueTypeEEnum, 
           source, 
           new String[] {
             "name", "ResolveValueType"
           });		
        addAnnotation
          (resultTypeTypeEEnum, 
           source, 
           new String[] {
             "name", "ResultTypeType"
           });		
        addAnnotation
          (starStringTypeEEnum, 
           source, 
           new String[] {
             "name", "StarStringType"
           });		
        addAnnotation
          (stateValueTypeMember0EEnum, 
           source, 
           new String[] {
             "name", "StateValueType_._member_._0"
           });		
        addAnnotation
          (updateActionTypeEEnum, 
           source, 
           new String[] {
             "name", "UpdateActionType"
           });		
        addAnnotation
          (allSomeTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "AllSomeType:Object",
             "baseType", "AllSomeType"
           });		
        addAnnotation
          (nonNegativeIntegerOrUnknownEDataType, 
           source, 
           new String[] {
             "name", "nonNegativeIntegerOrUnknown",
             "memberTypes", "nonNegativeIntegerOrUnknown_._member_._0 nonNegativeIntegerOrUnknown_._member_._1"
           });		
        addAnnotation
          (nonNegativeIntegerOrUnknownMember0ObjectEDataType, 
           source, 
           new String[] {
             "name", "nonNegativeIntegerOrUnknown_._member_._0:Object",
             "baseType", "nonNegativeIntegerOrUnknown_._member_._0"
           });		
        addAnnotation
          (nonNegativeIntegerOrUnknownMember1EDataType, 
           source, 
           new String[] {
             "name", "nonNegativeIntegerOrUnknown_._member_._1",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#nonNegativeInteger"
           });		
        addAnnotation
          (positiveIntegerWithStarEDataType, 
           source, 
           new String[] {
             "name", "positiveIntegerWithStar",
             "memberTypes", "http://www.eclipse.org/emf/2003/XMLType#positiveInteger StarStringType"
           });		
        addAnnotation
          (resolveValueTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "ResolveValueType:Object",
             "baseType", "ResolveValueType"
           });		
        addAnnotation
          (resultTypeTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "ResultTypeType:Object",
             "baseType", "ResultTypeType"
           });		
        addAnnotation
          (returnFeatureTypesListTypeEDataType, 
           source, 
           new String[] {
             "name", "ReturnFeatureTypesListType",
             "itemType", "http://www.eclipse.org/emf/2003/XMLType#QName"
           });		
        addAnnotation
          (starStringTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "StarStringType:Object",
             "baseType", "StarStringType"
           });		
        addAnnotation
          (stateValueTypeEDataType, 
           source, 
           new String[] {
             "name", "StateValueType",
             "memberTypes", "StateValueType_._member_._0 StateValueType_._member_._1"
           });		
        addAnnotation
          (stateValueTypeMember0ObjectEDataType, 
           source, 
           new String[] {
             "name", "StateValueType_._member_._0:Object",
             "baseType", "StateValueType_._member_._0"
           });		
        addAnnotation
          (stateValueTypeMember1EDataType, 
           source, 
           new String[] {
             "name", "StateValueType_._member_._1",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "other:\\w{2,}"
           });		
        addAnnotation
          (updateActionTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "UpdateActionType:Object",
             "baseType", "UpdateActionType"
           });
    }

} //Wfs20PackageImpl
