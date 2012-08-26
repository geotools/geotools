/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.util.Calendar;
import java.util.List;

import net.opengis.cat.csw20.AbstractQueryType;
import net.opengis.cat.csw20.AbstractRecordType;
import net.opengis.cat.csw20.AcknowledgementType;
import net.opengis.cat.csw20.BriefRecordType;
import net.opengis.cat.csw20.CapabilitiesType;
import net.opengis.cat.csw20.ConceptualSchemeType;
import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DCMIRecordType;
import net.opengis.cat.csw20.DeleteType;
import net.opengis.cat.csw20.DescribeRecordResponseType;
import net.opengis.cat.csw20.DescribeRecordType;
import net.opengis.cat.csw20.DistributedSearchType;
import net.opengis.cat.csw20.DomainValuesType;
import net.opengis.cat.csw20.EchoedRequestType;
import net.opengis.cat.csw20.ElementSetNameType;
import net.opengis.cat.csw20.ElementSetType;
import net.opengis.cat.csw20.EmptyType;
import net.opengis.cat.csw20.GetCapabilitiesType;
import net.opengis.cat.csw20.GetDomainResponseType;
import net.opengis.cat.csw20.GetDomainType;
import net.opengis.cat.csw20.GetRecordByIdType;
import net.opengis.cat.csw20.GetRecordsResponseType;
import net.opengis.cat.csw20.GetRecordsType;
import net.opengis.cat.csw20.HarvestResponseType;
import net.opengis.cat.csw20.HarvestType;
import net.opengis.cat.csw20.InsertResultType;
import net.opengis.cat.csw20.InsertType;
import net.opengis.cat.csw20.ListOfValuesType;
import net.opengis.cat.csw20.QueryConstraintType;
import net.opengis.cat.csw20.QueryType;
import net.opengis.cat.csw20.RangeOfValuesType;
import net.opengis.cat.csw20.RecordPropertyType;
import net.opengis.cat.csw20.RecordType;
import net.opengis.cat.csw20.RequestBaseType;
import net.opengis.cat.csw20.RequestStatusType;
import net.opengis.cat.csw20.ResultType;
import net.opengis.cat.csw20.SchemaComponentType;
import net.opengis.cat.csw20.SearchResultsType;
import net.opengis.cat.csw20.SimpleLiteral;
import net.opengis.cat.csw20.SummaryRecordType;
import net.opengis.cat.csw20.TransactionResponseType;
import net.opengis.cat.csw20.TransactionSummaryType;
import net.opengis.cat.csw20.TransactionType;
import net.opengis.cat.csw20.UpdateType;

import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.opengis.filter.Filter;

import org.opengis.filter.capability.FilterCapabilities;

import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Csw20PackageImpl extends EPackageImpl implements Csw20Package {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractQueryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractRecordTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass acknowledgementTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass briefRecordTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass capabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conceptualSchemeTypeEClass = null;

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
    private EClass describeRecordResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass describeRecordTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass distributedSearchTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass domainValuesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass echoedRequestTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elementSetNameTypeEClass = null;

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
    private EClass getCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getDomainResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getRecordByIdTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getRecordsResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getRecordsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass harvestResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass harvestTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass insertResultTypeEClass = null;

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
    private EClass listOfValuesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass queryConstraintTypeEClass = null;

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
    private EClass rangeOfValuesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass recordPropertyTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requestBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requestStatusTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass schemaComponentTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass searchResultsTypeEClass = null;

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
    private EClass updateTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stringEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass filterCapabilitiesEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass filterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sortByEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dcmiRecordTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass recordTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass simpleLiteralEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass summaryRecordTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum elementSetTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum resultTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType typeNameListTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType serviceTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType typeNameListType_1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType serviceType_1EDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType versionTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType calendarEDataType = null;

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
     * @see net.opengis.cat.csw20.Csw20Package#eNS_URI
     * @see #init()
     * @generated
     */
    private Csw20PackageImpl() {
        super(eNS_URI, Csw20Factory.eINSTANCE);
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
     * <p>This method is used to initialize {@link Csw20Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static Csw20Package init() {
        if (isInited) return (Csw20Package)EPackage.Registry.INSTANCE.getEPackage(Csw20Package.eNS_URI);

        // Obtain or create and register package
        Csw20PackageImpl theCsw20Package = (Csw20PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Csw20PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Csw20PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows10Package.eINSTANCE.eClass();

        // Create package meta-data objects
        theCsw20Package.createPackageContents();

        // Initialize created meta-data
        theCsw20Package.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theCsw20Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Csw20Package.eNS_URI, theCsw20Package);
        return theCsw20Package;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractQueryType() {
        return abstractQueryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractRecordType() {
        return abstractRecordTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAcknowledgementType() {
        return acknowledgementTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAcknowledgementType_EchoedRequest() {
        return (EReference)acknowledgementTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAcknowledgementType_RequestId() {
        return (EAttribute)acknowledgementTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAcknowledgementType_TimeStamp() {
        return (EAttribute)acknowledgementTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBriefRecordType() {
        return briefRecordTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBriefRecordType_Identifier() {
        return (EReference)briefRecordTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBriefRecordType_Title() {
        return (EReference)briefRecordTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBriefRecordType_Type() {
        return (EAttribute)briefRecordTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBriefRecordType_BoundingBox() {
        return (EReference)briefRecordTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCapabilitiesType() {
        return capabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_FilterCapabilities() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConceptualSchemeType() {
        return conceptualSchemeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConceptualSchemeType_Name() {
        return (EAttribute)conceptualSchemeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConceptualSchemeType_Document() {
        return (EAttribute)conceptualSchemeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getConceptualSchemeType_Authority() {
        return (EAttribute)conceptualSchemeTypeEClass.getEStructuralFeatures().get(2);
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
    public EReference getDeleteType_Constraint() {
        return (EReference)deleteTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDeleteType_Handle() {
        return (EAttribute)deleteTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDeleteType_TypeName() {
        return (EAttribute)deleteTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeRecordResponseType() {
        return describeRecordResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDescribeRecordResponseType_SchemaComponent() {
        return (EReference)describeRecordResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeRecordType() {
        return describeRecordTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeRecordType_TypeName() {
        return (EAttribute)describeRecordTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeRecordType_OutputFormat() {
        return (EAttribute)describeRecordTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeRecordType_SchemaLanguage() {
        return (EAttribute)describeRecordTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDistributedSearchType() {
        return distributedSearchTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDistributedSearchType_HopCount() {
        return (EAttribute)distributedSearchTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDomainValuesType() {
        return domainValuesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainValuesType_PropertyName() {
        return (EAttribute)domainValuesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainValuesType_ParameterName() {
        return (EAttribute)domainValuesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDomainValuesType_ListOfValues() {
        return (EReference)domainValuesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDomainValuesType_ConceptualScheme() {
        return (EReference)domainValuesTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDomainValuesType_RangeOfValues() {
        return (EReference)domainValuesTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainValuesType_Type() {
        return (EAttribute)domainValuesTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainValuesType_Uom() {
        return (EAttribute)domainValuesTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEchoedRequestType() {
        return echoedRequestTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEchoedRequestType_Any() {
        return (EAttribute)echoedRequestTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getElementSetNameType() {
        return elementSetNameTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementSetNameType_Value() {
        return (EAttribute)elementSetNameTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getElementSetNameType_TypeNames() {
        return (EAttribute)elementSetNameTypeEClass.getEStructuralFeatures().get(1);
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
    public EClass getGetDomainResponseType() {
        return getDomainResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetDomainResponseType_DomainValues() {
        return (EReference)getDomainResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetDomainType() {
        return getDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetDomainType_PropertyName() {
        return (EAttribute)getDomainTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetDomainType_ParameterName() {
        return (EAttribute)getDomainTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetRecordByIdType() {
        return getRecordByIdTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordByIdType_Id() {
        return (EAttribute)getRecordByIdTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetRecordByIdType_ElementSetName() {
        return (EReference)getRecordByIdTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordByIdType_OutputFormat() {
        return (EAttribute)getRecordByIdTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordByIdType_OutputSchema() {
        return (EAttribute)getRecordByIdTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetRecordsResponseType() {
        return getRecordsResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsResponseType_RequestId() {
        return (EAttribute)getRecordsResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetRecordsResponseType_SearchStatus() {
        return (EReference)getRecordsResponseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetRecordsResponseType_SearchResults() {
        return (EReference)getRecordsResponseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsResponseType_Version() {
        return (EAttribute)getRecordsResponseTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetRecordsType() {
        return getRecordsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetRecordsType_DistributedSearch() {
        return (EReference)getRecordsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_ResponseHandler() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_Any() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_MaxRecords() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_OutputFormat() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_OutputSchema() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_RequestId() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_ResultType() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_StartPosition() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetRecordsType_Query() {
        return (EAttribute)getRecordsTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getHarvestResponseType() {
        return harvestResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHarvestResponseType_Acknowledgement() {
        return (EReference)harvestResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHarvestResponseType_TransactionResponse() {
        return (EReference)harvestResponseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getHarvestType() {
        return harvestTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHarvestType_Source() {
        return (EAttribute)harvestTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHarvestType_ResourceType() {
        return (EAttribute)harvestTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHarvestType_ResourceFormat() {
        return (EAttribute)harvestTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHarvestType_HarvestInterval() {
        return (EAttribute)harvestTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHarvestType_ResponseHandler() {
        return (EAttribute)harvestTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInsertResultType() {
        return insertResultTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInsertResultType_BriefRecord() {
        return (EReference)insertResultTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInsertResultType_HandleRef() {
        return (EAttribute)insertResultTypeEClass.getEStructuralFeatures().get(1);
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
    public EAttribute getInsertType_Handle() {
        return (EAttribute)insertTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInsertType_TypeName() {
        return (EAttribute)insertTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getListOfValuesType() {
        return listOfValuesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getListOfValuesType_Value() {
        return (EReference)listOfValuesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getQueryConstraintType() {
        return queryConstraintTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getQueryConstraintType_Filter() {
        return (EReference)queryConstraintTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryConstraintType_CqlText() {
        return (EAttribute)queryConstraintTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryConstraintType_Version() {
        return (EAttribute)queryConstraintTypeEClass.getEStructuralFeatures().get(2);
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
    public EReference getQueryType_ElementSetName() {
        return (EReference)queryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryType_ElementName() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getQueryType_Constraint() {
        return (EReference)queryTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getQueryType_SortBy() {
        return (EReference)queryTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getQueryType_TypeNames() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRangeOfValuesType() {
        return rangeOfValuesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeOfValuesType_MinValue() {
        return (EReference)rangeOfValuesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeOfValuesType_MaxValue() {
        return (EReference)rangeOfValuesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRecordPropertyType() {
        return recordPropertyTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRecordPropertyType_Name() {
        return (EAttribute)recordPropertyTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRecordPropertyType_Value() {
        return (EReference)recordPropertyTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequestBaseType() {
        return requestBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequestBaseType_Service() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequestBaseType_Version() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequestStatusType() {
        return requestStatusTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequestStatusType_Timestamp() {
        return (EAttribute)requestStatusTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSchemaComponentType() {
        return schemaComponentTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSchemaComponentType_Mixed() {
        return (EAttribute)schemaComponentTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSchemaComponentType_Any() {
        return (EAttribute)schemaComponentTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSchemaComponentType_ParentSchema() {
        return (EAttribute)schemaComponentTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSchemaComponentType_SchemaLanguage() {
        return (EAttribute)schemaComponentTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSchemaComponentType_TargetNamespace() {
        return (EAttribute)schemaComponentTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSearchResultsType() {
        return searchResultsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_AbstractRecordGroup() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSearchResultsType_AbstractRecord() {
        return (EReference)searchResultsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_Any() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_ElementSet() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_Expires() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_NextRecord() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_NumberOfRecordsMatched() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_NumberOfRecordsReturned() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_RecordSchema() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSearchResultsType_ResultSetId() {
        return (EAttribute)searchResultsTypeEClass.getEStructuralFeatures().get(9);
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
    public EReference getTransactionResponseType_InsertResult() {
        return (EReference)transactionResponseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionResponseType_Version() {
        return (EAttribute)transactionResponseTypeEClass.getEStructuralFeatures().get(2);
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
    public EAttribute getTransactionSummaryType_TotalDeleted() {
        return (EAttribute)transactionSummaryTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionSummaryType_RequestId() {
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
    public EReference getTransactionType_Insert() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransactionType_Update() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTransactionType_Delete() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionType_RequestId() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTransactionType_VerboseResponse() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(5);
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
    public EAttribute getUpdateType_Any() {
        return (EAttribute)updateTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUpdateType_RecordProperty() {
        return (EReference)updateTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUpdateType_Constraint() {
        return (EReference)updateTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUpdateType_Handle() {
        return (EAttribute)updateTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getString() {
        return stringEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFilterCapabilities() {
        return filterCapabilitiesEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFilter() {
        return filterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSortBy() {
        return sortByEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDCMIRecordType() {
        return dcmiRecordTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDCMIRecordType_DCElement() {
        return (EReference)dcmiRecordTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRecordType() {
        return recordTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRecordType_AnyText() {
        return (EReference)recordTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRecordType_BoundingBox() {
        return (EReference)recordTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSimpleLiteral() {
        return simpleLiteralEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimpleLiteral_Value() {
        return (EAttribute)simpleLiteralEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimpleLiteral_Scheme() {
        return (EAttribute)simpleLiteralEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSummaryRecordType() {
        return summaryRecordTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Identifier() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Title() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Type() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Subject() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Format() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Relation() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Modified() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Abstract() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_Spatial() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSummaryRecordType_BoundingBox() {
        return (EReference)summaryRecordTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getElementSetType() {
        return elementSetTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getResultType() {
        return resultTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTypeNameListType() {
        return typeNameListTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getServiceType() {
        return serviceTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTypeNameListType_1() {
        return typeNameListType_1EDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getServiceType_1() {
        return serviceType_1EDataType;
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
    public EDataType getCalendar() {
        return calendarEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Csw20Factory getCsw20Factory() {
        return (Csw20Factory)getEFactoryInstance();
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
        abstractQueryTypeEClass = createEClass(ABSTRACT_QUERY_TYPE);

        abstractRecordTypeEClass = createEClass(ABSTRACT_RECORD_TYPE);

        acknowledgementTypeEClass = createEClass(ACKNOWLEDGEMENT_TYPE);
        createEReference(acknowledgementTypeEClass, ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST);
        createEAttribute(acknowledgementTypeEClass, ACKNOWLEDGEMENT_TYPE__REQUEST_ID);
        createEAttribute(acknowledgementTypeEClass, ACKNOWLEDGEMENT_TYPE__TIME_STAMP);

        briefRecordTypeEClass = createEClass(BRIEF_RECORD_TYPE);
        createEReference(briefRecordTypeEClass, BRIEF_RECORD_TYPE__IDENTIFIER);
        createEReference(briefRecordTypeEClass, BRIEF_RECORD_TYPE__TITLE);
        createEAttribute(briefRecordTypeEClass, BRIEF_RECORD_TYPE__TYPE);
        createEReference(briefRecordTypeEClass, BRIEF_RECORD_TYPE__BOUNDING_BOX);

        capabilitiesTypeEClass = createEClass(CAPABILITIES_TYPE);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__FILTER_CAPABILITIES);

        conceptualSchemeTypeEClass = createEClass(CONCEPTUAL_SCHEME_TYPE);
        createEAttribute(conceptualSchemeTypeEClass, CONCEPTUAL_SCHEME_TYPE__NAME);
        createEAttribute(conceptualSchemeTypeEClass, CONCEPTUAL_SCHEME_TYPE__DOCUMENT);
        createEAttribute(conceptualSchemeTypeEClass, CONCEPTUAL_SCHEME_TYPE__AUTHORITY);

        deleteTypeEClass = createEClass(DELETE_TYPE);
        createEReference(deleteTypeEClass, DELETE_TYPE__CONSTRAINT);
        createEAttribute(deleteTypeEClass, DELETE_TYPE__HANDLE);
        createEAttribute(deleteTypeEClass, DELETE_TYPE__TYPE_NAME);

        describeRecordResponseTypeEClass = createEClass(DESCRIBE_RECORD_RESPONSE_TYPE);
        createEReference(describeRecordResponseTypeEClass, DESCRIBE_RECORD_RESPONSE_TYPE__SCHEMA_COMPONENT);

        describeRecordTypeEClass = createEClass(DESCRIBE_RECORD_TYPE);
        createEAttribute(describeRecordTypeEClass, DESCRIBE_RECORD_TYPE__TYPE_NAME);
        createEAttribute(describeRecordTypeEClass, DESCRIBE_RECORD_TYPE__OUTPUT_FORMAT);
        createEAttribute(describeRecordTypeEClass, DESCRIBE_RECORD_TYPE__SCHEMA_LANGUAGE);

        distributedSearchTypeEClass = createEClass(DISTRIBUTED_SEARCH_TYPE);
        createEAttribute(distributedSearchTypeEClass, DISTRIBUTED_SEARCH_TYPE__HOP_COUNT);

        domainValuesTypeEClass = createEClass(DOMAIN_VALUES_TYPE);
        createEAttribute(domainValuesTypeEClass, DOMAIN_VALUES_TYPE__PROPERTY_NAME);
        createEAttribute(domainValuesTypeEClass, DOMAIN_VALUES_TYPE__PARAMETER_NAME);
        createEReference(domainValuesTypeEClass, DOMAIN_VALUES_TYPE__LIST_OF_VALUES);
        createEReference(domainValuesTypeEClass, DOMAIN_VALUES_TYPE__CONCEPTUAL_SCHEME);
        createEReference(domainValuesTypeEClass, DOMAIN_VALUES_TYPE__RANGE_OF_VALUES);
        createEAttribute(domainValuesTypeEClass, DOMAIN_VALUES_TYPE__TYPE);
        createEAttribute(domainValuesTypeEClass, DOMAIN_VALUES_TYPE__UOM);

        echoedRequestTypeEClass = createEClass(ECHOED_REQUEST_TYPE);
        createEAttribute(echoedRequestTypeEClass, ECHOED_REQUEST_TYPE__ANY);

        elementSetNameTypeEClass = createEClass(ELEMENT_SET_NAME_TYPE);
        createEAttribute(elementSetNameTypeEClass, ELEMENT_SET_NAME_TYPE__VALUE);
        createEAttribute(elementSetNameTypeEClass, ELEMENT_SET_NAME_TYPE__TYPE_NAMES);

        emptyTypeEClass = createEClass(EMPTY_TYPE);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);

        getDomainResponseTypeEClass = createEClass(GET_DOMAIN_RESPONSE_TYPE);
        createEReference(getDomainResponseTypeEClass, GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES);

        getDomainTypeEClass = createEClass(GET_DOMAIN_TYPE);
        createEAttribute(getDomainTypeEClass, GET_DOMAIN_TYPE__PROPERTY_NAME);
        createEAttribute(getDomainTypeEClass, GET_DOMAIN_TYPE__PARAMETER_NAME);

        getRecordByIdTypeEClass = createEClass(GET_RECORD_BY_ID_TYPE);
        createEAttribute(getRecordByIdTypeEClass, GET_RECORD_BY_ID_TYPE__ID);
        createEReference(getRecordByIdTypeEClass, GET_RECORD_BY_ID_TYPE__ELEMENT_SET_NAME);
        createEAttribute(getRecordByIdTypeEClass, GET_RECORD_BY_ID_TYPE__OUTPUT_FORMAT);
        createEAttribute(getRecordByIdTypeEClass, GET_RECORD_BY_ID_TYPE__OUTPUT_SCHEMA);

        getRecordsResponseTypeEClass = createEClass(GET_RECORDS_RESPONSE_TYPE);
        createEAttribute(getRecordsResponseTypeEClass, GET_RECORDS_RESPONSE_TYPE__REQUEST_ID);
        createEReference(getRecordsResponseTypeEClass, GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS);
        createEReference(getRecordsResponseTypeEClass, GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS);
        createEAttribute(getRecordsResponseTypeEClass, GET_RECORDS_RESPONSE_TYPE__VERSION);

        getRecordsTypeEClass = createEClass(GET_RECORDS_TYPE);
        createEReference(getRecordsTypeEClass, GET_RECORDS_TYPE__DISTRIBUTED_SEARCH);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__RESPONSE_HANDLER);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__ANY);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__MAX_RECORDS);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__OUTPUT_FORMAT);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__OUTPUT_SCHEMA);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__REQUEST_ID);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__RESULT_TYPE);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__START_POSITION);
        createEAttribute(getRecordsTypeEClass, GET_RECORDS_TYPE__QUERY);

        harvestResponseTypeEClass = createEClass(HARVEST_RESPONSE_TYPE);
        createEReference(harvestResponseTypeEClass, HARVEST_RESPONSE_TYPE__ACKNOWLEDGEMENT);
        createEReference(harvestResponseTypeEClass, HARVEST_RESPONSE_TYPE__TRANSACTION_RESPONSE);

        harvestTypeEClass = createEClass(HARVEST_TYPE);
        createEAttribute(harvestTypeEClass, HARVEST_TYPE__SOURCE);
        createEAttribute(harvestTypeEClass, HARVEST_TYPE__RESOURCE_TYPE);
        createEAttribute(harvestTypeEClass, HARVEST_TYPE__RESOURCE_FORMAT);
        createEAttribute(harvestTypeEClass, HARVEST_TYPE__HARVEST_INTERVAL);
        createEAttribute(harvestTypeEClass, HARVEST_TYPE__RESPONSE_HANDLER);

        insertResultTypeEClass = createEClass(INSERT_RESULT_TYPE);
        createEReference(insertResultTypeEClass, INSERT_RESULT_TYPE__BRIEF_RECORD);
        createEAttribute(insertResultTypeEClass, INSERT_RESULT_TYPE__HANDLE_REF);

        insertTypeEClass = createEClass(INSERT_TYPE);
        createEAttribute(insertTypeEClass, INSERT_TYPE__ANY);
        createEAttribute(insertTypeEClass, INSERT_TYPE__HANDLE);
        createEAttribute(insertTypeEClass, INSERT_TYPE__TYPE_NAME);

        listOfValuesTypeEClass = createEClass(LIST_OF_VALUES_TYPE);
        createEReference(listOfValuesTypeEClass, LIST_OF_VALUES_TYPE__VALUE);

        queryConstraintTypeEClass = createEClass(QUERY_CONSTRAINT_TYPE);
        createEReference(queryConstraintTypeEClass, QUERY_CONSTRAINT_TYPE__FILTER);
        createEAttribute(queryConstraintTypeEClass, QUERY_CONSTRAINT_TYPE__CQL_TEXT);
        createEAttribute(queryConstraintTypeEClass, QUERY_CONSTRAINT_TYPE__VERSION);

        queryTypeEClass = createEClass(QUERY_TYPE);
        createEReference(queryTypeEClass, QUERY_TYPE__ELEMENT_SET_NAME);
        createEAttribute(queryTypeEClass, QUERY_TYPE__ELEMENT_NAME);
        createEReference(queryTypeEClass, QUERY_TYPE__CONSTRAINT);
        createEReference(queryTypeEClass, QUERY_TYPE__SORT_BY);
        createEAttribute(queryTypeEClass, QUERY_TYPE__TYPE_NAMES);

        rangeOfValuesTypeEClass = createEClass(RANGE_OF_VALUES_TYPE);
        createEReference(rangeOfValuesTypeEClass, RANGE_OF_VALUES_TYPE__MIN_VALUE);
        createEReference(rangeOfValuesTypeEClass, RANGE_OF_VALUES_TYPE__MAX_VALUE);

        recordPropertyTypeEClass = createEClass(RECORD_PROPERTY_TYPE);
        createEAttribute(recordPropertyTypeEClass, RECORD_PROPERTY_TYPE__NAME);
        createEReference(recordPropertyTypeEClass, RECORD_PROPERTY_TYPE__VALUE);

        requestBaseTypeEClass = createEClass(REQUEST_BASE_TYPE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__SERVICE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__VERSION);

        requestStatusTypeEClass = createEClass(REQUEST_STATUS_TYPE);
        createEAttribute(requestStatusTypeEClass, REQUEST_STATUS_TYPE__TIMESTAMP);

        schemaComponentTypeEClass = createEClass(SCHEMA_COMPONENT_TYPE);
        createEAttribute(schemaComponentTypeEClass, SCHEMA_COMPONENT_TYPE__MIXED);
        createEAttribute(schemaComponentTypeEClass, SCHEMA_COMPONENT_TYPE__ANY);
        createEAttribute(schemaComponentTypeEClass, SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA);
        createEAttribute(schemaComponentTypeEClass, SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE);
        createEAttribute(schemaComponentTypeEClass, SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE);

        searchResultsTypeEClass = createEClass(SEARCH_RESULTS_TYPE);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP);
        createEReference(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__ABSTRACT_RECORD);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__ANY);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__ELEMENT_SET);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__EXPIRES);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__NEXT_RECORD);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__RECORD_SCHEMA);
        createEAttribute(searchResultsTypeEClass, SEARCH_RESULTS_TYPE__RESULT_SET_ID);

        transactionResponseTypeEClass = createEClass(TRANSACTION_RESPONSE_TYPE);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__INSERT_RESULT);
        createEAttribute(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__VERSION);

        transactionSummaryTypeEClass = createEClass(TRANSACTION_SUMMARY_TYPE);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__REQUEST_ID);

        transactionTypeEClass = createEClass(TRANSACTION_TYPE);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__GROUP);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__INSERT);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__UPDATE);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__DELETE);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__REQUEST_ID);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__VERBOSE_RESPONSE);

        updateTypeEClass = createEClass(UPDATE_TYPE);
        createEAttribute(updateTypeEClass, UPDATE_TYPE__ANY);
        createEReference(updateTypeEClass, UPDATE_TYPE__RECORD_PROPERTY);
        createEReference(updateTypeEClass, UPDATE_TYPE__CONSTRAINT);
        createEAttribute(updateTypeEClass, UPDATE_TYPE__HANDLE);

        stringEClass = createEClass(STRING);

        filterCapabilitiesEClass = createEClass(FILTER_CAPABILITIES);

        filterEClass = createEClass(FILTER);

        sortByEClass = createEClass(SORT_BY);

        dcmiRecordTypeEClass = createEClass(DCMI_RECORD_TYPE);
        createEReference(dcmiRecordTypeEClass, DCMI_RECORD_TYPE__DC_ELEMENT);

        recordTypeEClass = createEClass(RECORD_TYPE);
        createEReference(recordTypeEClass, RECORD_TYPE__ANY_TEXT);
        createEReference(recordTypeEClass, RECORD_TYPE__BOUNDING_BOX);

        simpleLiteralEClass = createEClass(SIMPLE_LITERAL);
        createEAttribute(simpleLiteralEClass, SIMPLE_LITERAL__VALUE);
        createEAttribute(simpleLiteralEClass, SIMPLE_LITERAL__SCHEME);

        summaryRecordTypeEClass = createEClass(SUMMARY_RECORD_TYPE);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__IDENTIFIER);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__TITLE);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__TYPE);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__SUBJECT);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__FORMAT);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__RELATION);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__MODIFIED);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__ABSTRACT);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__SPATIAL);
        createEReference(summaryRecordTypeEClass, SUMMARY_RECORD_TYPE__BOUNDING_BOX);

        // Create enums
        elementSetTypeEEnum = createEEnum(ELEMENT_SET_TYPE);
        resultTypeEEnum = createEEnum(RESULT_TYPE);

        // Create data types
        typeNameListTypeEDataType = createEDataType(TYPE_NAME_LIST_TYPE);
        serviceTypeEDataType = createEDataType(SERVICE_TYPE);
        typeNameListType_1EDataType = createEDataType(TYPE_NAME_LIST_TYPE_1);
        serviceType_1EDataType = createEDataType(SERVICE_TYPE_1);
        versionTypeEDataType = createEDataType(VERSION_TYPE);
        calendarEDataType = createEDataType(CALENDAR);
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
        Ows10Package theOws10Package = (Ows10Package)EPackage.Registry.INSTANCE.getEPackage(Ows10Package.eNS_URI);
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        briefRecordTypeEClass.getESuperTypes().add(this.getAbstractRecordType());
        capabilitiesTypeEClass.getESuperTypes().add(theOws10Package.getCapabilitiesBaseType());
        describeRecordTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        getCapabilitiesTypeEClass.getESuperTypes().add(theOws10Package.getGetCapabilitiesType());
        getDomainTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        getRecordByIdTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        getRecordsTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        harvestTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        queryTypeEClass.getESuperTypes().add(this.getAbstractQueryType());
        transactionTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        dcmiRecordTypeEClass.getESuperTypes().add(this.getAbstractRecordType());
        recordTypeEClass.getESuperTypes().add(this.getDCMIRecordType());
        summaryRecordTypeEClass.getESuperTypes().add(this.getAbstractRecordType());

        // Initialize classes and features; add operations and parameters
        initEClass(abstractQueryTypeEClass, AbstractQueryType.class, "AbstractQueryType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(abstractRecordTypeEClass, AbstractRecordType.class, "AbstractRecordType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(acknowledgementTypeEClass, AcknowledgementType.class, "AcknowledgementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAcknowledgementType_EchoedRequest(), this.getEchoedRequestType(), null, "echoedRequest", null, 1, 1, AcknowledgementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAcknowledgementType_RequestId(), theXMLTypePackage.getAnyURI(), "requestId", null, 0, 1, AcknowledgementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAcknowledgementType_TimeStamp(), this.getCalendar(), "timeStamp", null, 0, 1, AcknowledgementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(briefRecordTypeEClass, BriefRecordType.class, "BriefRecordType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBriefRecordType_Identifier(), this.getSimpleLiteral(), null, "identifier", null, 0, -1, BriefRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBriefRecordType_Title(), this.getSimpleLiteral(), null, "title", null, 0, -1, BriefRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBriefRecordType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, BriefRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBriefRecordType_BoundingBox(), theOws10Package.getBoundingBoxType(), null, "boundingBox", null, 0, -1, BriefRecordType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(capabilitiesTypeEClass, CapabilitiesType.class, "CapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCapabilitiesType_FilterCapabilities(), this.getFilterCapabilities(), null, "filterCapabilities", null, 1, 1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(conceptualSchemeTypeEClass, ConceptualSchemeType.class, "ConceptualSchemeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getConceptualSchemeType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ConceptualSchemeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConceptualSchemeType_Document(), theXMLTypePackage.getAnyURI(), "document", null, 1, 1, ConceptualSchemeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConceptualSchemeType_Authority(), theXMLTypePackage.getAnyURI(), "authority", null, 1, 1, ConceptualSchemeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(deleteTypeEClass, DeleteType.class, "DeleteType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDeleteType_Constraint(), this.getQueryConstraintType(), null, "constraint", null, 1, 1, DeleteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDeleteType_Handle(), theXMLTypePackage.getID(), "handle", null, 0, 1, DeleteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDeleteType_TypeName(), theXMLTypePackage.getAnyURI(), "typeName", null, 0, 1, DeleteType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeRecordResponseTypeEClass, DescribeRecordResponseType.class, "DescribeRecordResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDescribeRecordResponseType_SchemaComponent(), this.getSchemaComponentType(), null, "schemaComponent", null, 0, -1, DescribeRecordResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeRecordTypeEClass, DescribeRecordType.class, "DescribeRecordType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescribeRecordType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 0, 1, DescribeRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDescribeRecordType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "application/xml", 0, 1, DescribeRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDescribeRecordType_SchemaLanguage(), theXMLTypePackage.getAnyURI(), "schemaLanguage", "http://www.w3.org/XML/Schema", 0, 1, DescribeRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(distributedSearchTypeEClass, DistributedSearchType.class, "DistributedSearchType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDistributedSearchType_HopCount(), theXMLTypePackage.getPositiveInteger(), "hopCount", "2", 0, 1, DistributedSearchType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(domainValuesTypeEClass, DomainValuesType.class, "DomainValuesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDomainValuesType_PropertyName(), theXMLTypePackage.getAnyURI(), "propertyName", null, 0, 1, DomainValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDomainValuesType_ParameterName(), theXMLTypePackage.getAnyURI(), "parameterName", null, 0, 1, DomainValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDomainValuesType_ListOfValues(), this.getListOfValuesType(), null, "listOfValues", null, 0, 1, DomainValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDomainValuesType_ConceptualScheme(), this.getConceptualSchemeType(), null, "conceptualScheme", null, 0, 1, DomainValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDomainValuesType_RangeOfValues(), this.getRangeOfValuesType(), null, "rangeOfValues", null, 0, 1, DomainValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDomainValuesType_Type(), theXMLTypePackage.getQName(), "type", null, 1, 1, DomainValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDomainValuesType_Uom(), theXMLTypePackage.getAnyURI(), "uom", null, 0, 1, DomainValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(echoedRequestTypeEClass, EchoedRequestType.class, "EchoedRequestType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEchoedRequestType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 1, 1, EchoedRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(elementSetNameTypeEClass, ElementSetNameType.class, "ElementSetNameType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getElementSetNameType_Value(), this.getElementSetType(), "value", null, 0, 1, ElementSetNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElementSetNameType_TypeNames(), this.getTypeNameListType(), "typeNames", null, 0, 1, ElementSetNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(emptyTypeEClass, EmptyType.class, "EmptyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCapabilitiesType_Service(), this.getServiceType(), "service", "http://www.opengis.net/cat/csw", 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getDomainResponseTypeEClass, GetDomainResponseType.class, "GetDomainResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetDomainResponseType_DomainValues(), this.getDomainValuesType(), null, "domainValues", null, 1, -1, GetDomainResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getDomainTypeEClass, GetDomainType.class, "GetDomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetDomainType_PropertyName(), theXMLTypePackage.getAnyURI(), "propertyName", null, 0, 1, GetDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetDomainType_ParameterName(), theXMLTypePackage.getAnyURI(), "parameterName", null, 0, 1, GetDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getRecordByIdTypeEClass, GetRecordByIdType.class, "GetRecordByIdType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetRecordByIdType_Id(), theXMLTypePackage.getAnyURI(), "id", null, 1, 1, GetRecordByIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetRecordByIdType_ElementSetName(), this.getElementSetNameType(), null, "elementSetName", null, 0, 1, GetRecordByIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordByIdType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "application/xml", 0, 1, GetRecordByIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordByIdType_OutputSchema(), theXMLTypePackage.getAnyURI(), "outputSchema", null, 0, 1, GetRecordByIdType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getRecordsResponseTypeEClass, GetRecordsResponseType.class, "GetRecordsResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetRecordsResponseType_RequestId(), theXMLTypePackage.getAnyURI(), "requestId", null, 0, 1, GetRecordsResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetRecordsResponseType_SearchStatus(), this.getRequestStatusType(), null, "searchStatus", null, 1, 1, GetRecordsResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetRecordsResponseType_SearchResults(), this.getSearchResultsType(), null, "searchResults", null, 1, 1, GetRecordsResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsResponseType_Version(), theXMLTypePackage.getString(), "version", null, 0, 1, GetRecordsResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getRecordsTypeEClass, GetRecordsType.class, "GetRecordsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetRecordsType_DistributedSearch(), this.getDistributedSearchType(), null, "distributedSearch", null, 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_ResponseHandler(), theXMLTypePackage.getAnyURI(), "responseHandler", null, 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_MaxRecords(), theXMLTypePackage.getNonNegativeInteger(), "maxRecords", "10", 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "application/xml", 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_OutputSchema(), theXMLTypePackage.getAnyURI(), "outputSchema", null, 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_RequestId(), theXMLTypePackage.getAnyURI(), "requestId", null, 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_ResultType(), this.getResultType(), "resultType", "hits", 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_StartPosition(), theXMLTypePackage.getPositiveInteger(), "startPosition", "1", 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetRecordsType_Query(), ecorePackage.getEJavaObject(), "query", null, 0, 1, GetRecordsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(harvestResponseTypeEClass, HarvestResponseType.class, "HarvestResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getHarvestResponseType_Acknowledgement(), this.getAcknowledgementType(), null, "acknowledgement", null, 0, 1, HarvestResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHarvestResponseType_TransactionResponse(), this.getTransactionResponseType(), null, "transactionResponse", null, 0, 1, HarvestResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(harvestTypeEClass, HarvestType.class, "HarvestType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHarvestType_Source(), theXMLTypePackage.getAnyURI(), "source", null, 1, 1, HarvestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHarvestType_ResourceType(), theXMLTypePackage.getString(), "resourceType", null, 1, 1, HarvestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHarvestType_ResourceFormat(), theXMLTypePackage.getString(), "resourceFormat", "application/xml", 0, 1, HarvestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHarvestType_HarvestInterval(), theXMLTypePackage.getDuration(), "harvestInterval", null, 0, 1, HarvestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getHarvestType_ResponseHandler(), theXMLTypePackage.getAnyURI(), "responseHandler", null, 0, 1, HarvestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(insertResultTypeEClass, InsertResultType.class, "InsertResultType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInsertResultType_BriefRecord(), this.getBriefRecordType(), null, "briefRecord", null, 1, -1, InsertResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertResultType_HandleRef(), theXMLTypePackage.getAnyURI(), "handleRef", null, 0, 1, InsertResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(insertTypeEClass, InsertType.class, "InsertType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInsertType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 1, -1, InsertType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertType_Handle(), theXMLTypePackage.getID(), "handle", null, 0, 1, InsertType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertType_TypeName(), theXMLTypePackage.getAnyURI(), "typeName", null, 0, 1, InsertType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(listOfValuesTypeEClass, ListOfValuesType.class, "ListOfValuesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getListOfValuesType_Value(), theEcorePackage.getEObject(), null, "value", null, 1, -1, ListOfValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(queryConstraintTypeEClass, QueryConstraintType.class, "QueryConstraintType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getQueryConstraintType_Filter(), this.getFilter(), null, "filter", null, 0, 1, QueryConstraintType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryConstraintType_CqlText(), theXMLTypePackage.getString(), "cqlText", null, 0, 1, QueryConstraintType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryConstraintType_Version(), theXMLTypePackage.getString(), "version", null, 1, 1, QueryConstraintType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(queryTypeEClass, QueryType.class, "QueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getQueryType_ElementSetName(), this.getElementSetNameType(), null, "elementSetName", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_ElementName(), theXMLTypePackage.getQName(), "elementName", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getQueryType_Constraint(), this.getQueryConstraintType(), null, "constraint", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getQueryType_SortBy(), this.getSortBy(), null, "sortBy", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_TypeNames(), this.getTypeNameListType_1(), "typeNames", null, 1, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(rangeOfValuesTypeEClass, RangeOfValuesType.class, "RangeOfValuesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRangeOfValuesType_MinValue(), theEcorePackage.getEObject(), null, "minValue", null, 1, 1, RangeOfValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRangeOfValuesType_MaxValue(), theEcorePackage.getEObject(), null, "maxValue", null, 1, 1, RangeOfValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(recordPropertyTypeEClass, RecordPropertyType.class, "RecordPropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRecordPropertyType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, RecordPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRecordPropertyType_Value(), theEcorePackage.getEObject(), null, "value", null, 0, 1, RecordPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requestBaseTypeEClass, RequestBaseType.class, "RequestBaseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRequestBaseType_Service(), this.getServiceType_1(), "service", "CSW", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_Version(), this.getVersionType(), "version", "2.0.2", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requestStatusTypeEClass, RequestStatusType.class, "RequestStatusType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRequestStatusType_Timestamp(), theXMLTypePackage.getDateTime(), "timestamp", null, 0, 1, RequestStatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(schemaComponentTypeEClass, SchemaComponentType.class, "SchemaComponentType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSchemaComponentType_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, SchemaComponentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSchemaComponentType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 1, 1, SchemaComponentType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getSchemaComponentType_ParentSchema(), theXMLTypePackage.getAnyURI(), "parentSchema", null, 0, 1, SchemaComponentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSchemaComponentType_SchemaLanguage(), theXMLTypePackage.getAnyURI(), "schemaLanguage", null, 1, 1, SchemaComponentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSchemaComponentType_TargetNamespace(), theXMLTypePackage.getAnyURI(), "targetNamespace", null, 1, 1, SchemaComponentType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(searchResultsTypeEClass, SearchResultsType.class, "SearchResultsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSearchResultsType_AbstractRecordGroup(), theEcorePackage.getEFeatureMapEntry(), "abstractRecordGroup", null, 0, -1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSearchResultsType_AbstractRecord(), this.getAbstractRecordType(), null, "abstractRecord", null, 0, -1, SearchResultsType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, -1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_ElementSet(), this.getElementSetType(), "elementSet", null, 0, 1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_Expires(), theXMLTypePackage.getDateTime(), "expires", null, 0, 1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_NextRecord(), theXMLTypePackage.getNonNegativeInteger(), "nextRecord", null, 0, 1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_NumberOfRecordsMatched(), theXMLTypePackage.getNonNegativeInteger(), "numberOfRecordsMatched", null, 1, 1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_NumberOfRecordsReturned(), theXMLTypePackage.getNonNegativeInteger(), "numberOfRecordsReturned", null, 1, 1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_RecordSchema(), theXMLTypePackage.getAnyURI(), "recordSchema", null, 0, 1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSearchResultsType_ResultSetId(), theXMLTypePackage.getAnyURI(), "resultSetId", null, 0, 1, SearchResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionResponseTypeEClass, TransactionResponseType.class, "TransactionResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTransactionResponseType_TransactionSummary(), this.getTransactionSummaryType(), null, "transactionSummary", null, 1, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionResponseType_InsertResult(), this.getInsertResultType(), null, "insertResult", null, 0, -1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionResponseType_Version(), theXMLTypePackage.getString(), "version", null, 0, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionSummaryTypeEClass, TransactionSummaryType.class, "TransactionSummaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransactionSummaryType_TotalInserted(), theXMLTypePackage.getNonNegativeInteger(), "totalInserted", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_TotalUpdated(), theXMLTypePackage.getNonNegativeInteger(), "totalUpdated", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_TotalDeleted(), theXMLTypePackage.getNonNegativeInteger(), "totalDeleted", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_RequestId(), theXMLTypePackage.getAnyURI(), "requestId", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionTypeEClass, TransactionType.class, "TransactionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransactionType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_Insert(), this.getInsertType(), null, "insert", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_Update(), this.getUpdateType(), null, "update", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_Delete(), this.getDeleteType(), null, "delete", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_RequestId(), theXMLTypePackage.getAnyURI(), "requestId", null, 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_VerboseResponse(), theXMLTypePackage.getBoolean(), "verboseResponse", "false", 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(updateTypeEClass, UpdateType.class, "UpdateType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUpdateType_Any(), theEcorePackage.getEFeatureMapEntry(), "any", null, 0, 1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUpdateType_RecordProperty(), this.getRecordPropertyType(), null, "recordProperty", null, 0, -1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUpdateType_Constraint(), this.getQueryConstraintType(), null, "constraint", null, 0, 1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateType_Handle(), theXMLTypePackage.getID(), "handle", null, 0, 1, UpdateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(stringEClass, String.class, "String", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        initEClass(filterCapabilitiesEClass, FilterCapabilities.class, "FilterCapabilities", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        initEClass(filterEClass, Filter.class, "Filter", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        initEClass(sortByEClass, SortBy.class, "SortBy", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        initEClass(dcmiRecordTypeEClass, DCMIRecordType.class, "DCMIRecordType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDCMIRecordType_DCElement(), this.getSimpleLiteral(), null, "dCElement", null, 0, -1, DCMIRecordType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(recordTypeEClass, RecordType.class, "RecordType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRecordType_AnyText(), this.getString(), null, "anyText", null, 0, -1, RecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRecordType_BoundingBox(), theOws10Package.getBoundingBoxType(), null, "boundingBox", null, 0, -1, RecordType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(simpleLiteralEClass, SimpleLiteral.class, "SimpleLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSimpleLiteral_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, SimpleLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimpleLiteral_Scheme(), theXMLTypePackage.getAnyURI(), "scheme", null, 0, 1, SimpleLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(summaryRecordTypeEClass, SummaryRecordType.class, "SummaryRecordType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSummaryRecordType_Identifier(), this.getSimpleLiteral(), null, "identifier", null, 1, -1, SummaryRecordType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Title(), this.getSimpleLiteral(), null, "title", null, 1, -1, SummaryRecordType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Type(), this.getSimpleLiteral(), null, "type", null, 0, 1, SummaryRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Subject(), this.getSimpleLiteral(), null, "subject", null, 0, -1, SummaryRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Format(), this.getSimpleLiteral(), null, "format", null, 0, -1, SummaryRecordType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Relation(), this.getSimpleLiteral(), null, "relation", null, 0, -1, SummaryRecordType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Modified(), this.getSimpleLiteral(), null, "modified", null, 0, -1, SummaryRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Abstract(), this.getSimpleLiteral(), null, "abstract", null, 0, -1, SummaryRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_Spatial(), this.getSimpleLiteral(), null, "spatial", null, 0, -1, SummaryRecordType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSummaryRecordType_BoundingBox(), theOws10Package.getBoundingBoxType(), null, "boundingBox", null, 0, -1, SummaryRecordType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(elementSetTypeEEnum, ElementSetType.class, "ElementSetType");
        addEEnumLiteral(elementSetTypeEEnum, ElementSetType.BRIEF);
        addEEnumLiteral(elementSetTypeEEnum, ElementSetType.SUMMARY);
        addEEnumLiteral(elementSetTypeEEnum, ElementSetType.FULL);

        initEEnum(resultTypeEEnum, ResultType.class, "ResultType");
        addEEnumLiteral(resultTypeEEnum, ResultType.RESULTS);
        addEEnumLiteral(resultTypeEEnum, ResultType.HITS);
        addEEnumLiteral(resultTypeEEnum, ResultType.VALIDATE);

        // Initialize data types
        initEDataType(typeNameListTypeEDataType, List.class, "TypeNameListType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "java.util.List<javax.xml.namespace.QName>");
        initEDataType(serviceTypeEDataType, String.class, "ServiceType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(typeNameListType_1EDataType, List.class, "TypeNameListType_1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "java.util.List<javax.xml.namespace.QName>");
        initEDataType(serviceType_1EDataType, String.class, "ServiceType_1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(versionTypeEDataType, String.class, "VersionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(calendarEDataType, Calendar.class, "Calendar", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
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
          (abstractQueryTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractQueryType",
             "kind", "empty"
           });		
        addAnnotation
          (abstractRecordTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractRecordType",
             "kind", "empty"
           });		
        addAnnotation
          (acknowledgementTypeEClass, 
           source, 
           new String[] {
             "name", "AcknowledgementType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getAcknowledgementType_EchoedRequest(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "EchoedRequest",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAcknowledgementType_RequestId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RequestId",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (briefRecordTypeEClass, 
           source, 
           new String[] {
             "name", "BriefRecordType",
             "kind", "elementOnly"
           });						
        addAnnotation
          (getBriefRecordType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows",
             "group", "http://www.opengis.net/ows#BoundingBox:group"
           });		
        addAnnotation
          (capabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "CapabilitiesType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getCapabilitiesType_FilterCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter_Capabilities",
             "namespace", "http://www.opengis.net/ogc"
           });		
        addAnnotation
          (conceptualSchemeTypeEClass, 
           source, 
           new String[] {
             "name", "ConceptualSchemeType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getConceptualSchemeType_Name(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Name",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getConceptualSchemeType_Document(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Document",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getConceptualSchemeType_Authority(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Authority",
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
          (getDeleteType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDeleteType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });		
        addAnnotation
          (getDeleteType_TypeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeName"
           });		
        addAnnotation
          (describeRecordResponseTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeRecordResponseType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDescribeRecordResponseType_SchemaComponent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SchemaComponent",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (describeRecordTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeRecordType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDescribeRecordType_TypeName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TypeName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDescribeRecordType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });		
        addAnnotation
          (getDescribeRecordType_SchemaLanguage(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "schemaLanguage"
           });		
        addAnnotation
          (distributedSearchTypeEClass, 
           source, 
           new String[] {
             "name", "DistributedSearchType",
             "kind", "empty"
           });			
        addAnnotation
          (getDistributedSearchType_HopCount(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "hopCount"
           });		
        addAnnotation
          (domainValuesTypeEClass, 
           source, 
           new String[] {
             "name", "DomainValuesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDomainValuesType_PropertyName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDomainValuesType_ParameterName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ParameterName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDomainValuesType_ListOfValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ListOfValues",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDomainValuesType_ConceptualScheme(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ConceptualScheme",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDomainValuesType_RangeOfValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RangeOfValues",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDomainValuesType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type"
           });		
        addAnnotation
          (getDomainValuesType_Uom(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "uom"
           });		
        addAnnotation
          (echoedRequestTypeEClass, 
           source, 
           new String[] {
             "name", "EchoedRequestType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getEchoedRequestType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##any",
             "name", ":0",
             "processing", "lax"
           });		
        addAnnotation
          (elementSetNameTypeEClass, 
           source, 
           new String[] {
             "name", "ElementSetNameType",
             "kind", "simple"
           });		
        addAnnotation
          (getElementSetNameType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getElementSetNameType_TypeNames(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeNames"
           });		
        addAnnotation
          (emptyTypeEClass, 
           source, 
           new String[] {
             "name", "EmptyType",
             "kind", "empty"
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
          (getDomainResponseTypeEClass, 
           source, 
           new String[] {
             "name", "GetDomainResponseType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetDomainResponseType_DomainValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DomainValues",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDomainTypeEClass, 
           source, 
           new String[] {
             "name", "GetDomainType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetDomainType_PropertyName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "PropertyName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetDomainType_ParameterName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ParameterName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getRecordByIdTypeEClass, 
           source, 
           new String[] {
             "name", "GetRecordByIdType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetRecordByIdType_Id(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Id",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetRecordByIdType_ElementSetName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ElementSetName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetRecordByIdType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });		
        addAnnotation
          (getGetRecordByIdType_OutputSchema(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputSchema"
           });		
        addAnnotation
          (getRecordsResponseTypeEClass, 
           source, 
           new String[] {
             "name", "GetRecordsResponseType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetRecordsResponseType_RequestId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RequestId",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetRecordsResponseType_SearchStatus(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SearchStatus",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetRecordsResponseType_SearchResults(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SearchResults",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetRecordsResponseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });		
        addAnnotation
          (getRecordsTypeEClass, 
           source, 
           new String[] {
             "name", "GetRecordsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetRecordsType_DistributedSearch(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DistributedSearch",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetRecordsType_ResponseHandler(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResponseHandler",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetRecordsType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":6",
             "processing", "strict"
           });		
        addAnnotation
          (getGetRecordsType_MaxRecords(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "maxRecords"
           });		
        addAnnotation
          (getGetRecordsType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });		
        addAnnotation
          (getGetRecordsType_OutputSchema(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputSchema"
           });		
        addAnnotation
          (getGetRecordsType_RequestId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "requestId"
           });		
        addAnnotation
          (getGetRecordsType_ResultType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resultType"
           });		
        addAnnotation
          (getGetRecordsType_StartPosition(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "startPosition"
           });		
        addAnnotation
          (harvestResponseTypeEClass, 
           source, 
           new String[] {
             "name", "HarvestResponseType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getHarvestResponseType_Acknowledgement(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Acknowledgement",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getHarvestResponseType_TransactionResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TransactionResponse",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (harvestTypeEClass, 
           source, 
           new String[] {
             "name", "HarvestType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getHarvestType_Source(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Source",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getHarvestType_ResourceType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResourceType",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getHarvestType_ResourceFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResourceFormat",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getHarvestType_HarvestInterval(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HarvestInterval",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getHarvestType_ResponseHandler(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResponseHandler",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (insertResultTypeEClass, 
           source, 
           new String[] {
             "name", "InsertResultType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInsertResultType_BriefRecord(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BriefRecord",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getInsertResultType_HandleRef(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handleRef"
           });		
        addAnnotation
          (insertTypeEClass, 
           source, 
           new String[] {
             "name", "InsertType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInsertType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":0",
             "processing", "strict"
           });		
        addAnnotation
          (getInsertType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });		
        addAnnotation
          (getInsertType_TypeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeName"
           });		
        addAnnotation
          (listOfValuesTypeEClass, 
           source, 
           new String[] {
             "name", "ListOfValuesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getListOfValuesType_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (queryConstraintTypeEClass, 
           source, 
           new String[] {
             "name", "QueryConstraintType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getQueryConstraintType_Filter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter",
             "namespace", "http://www.opengis.net/ogc"
           });		
        addAnnotation
          (getQueryConstraintType_CqlText(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CqlText",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getQueryConstraintType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });			
        addAnnotation
          (queryTypeEClass, 
           source, 
           new String[] {
             "name", "QueryType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getQueryType_ElementSetName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ElementSetName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getQueryType_ElementName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ElementName",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getQueryType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getQueryType_SortBy(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SortBy",
             "namespace", "http://www.opengis.net/ogc"
           });		
        addAnnotation
          (getQueryType_TypeNames(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeNames"
           });		
        addAnnotation
          (rangeOfValuesTypeEClass, 
           source, 
           new String[] {
             "name", "RangeOfValuesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getRangeOfValuesType_MinValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MinValue",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getRangeOfValuesType_MaxValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MaxValue",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (recordPropertyTypeEClass, 
           source, 
           new String[] {
             "name", "RecordPropertyType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getRecordPropertyType_Name(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Name",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getRecordPropertyType_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (requestBaseTypeEClass, 
           source, 
           new String[] {
             "name", "RequestBaseType",
             "kind", "empty"
           });			
        addAnnotation
          (getRequestBaseType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });		
        addAnnotation
          (getRequestBaseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });		
        addAnnotation
          (requestStatusTypeEClass, 
           source, 
           new String[] {
             "name", "RequestStatusType",
             "kind", "empty"
           });			
        addAnnotation
          (getRequestStatusType_Timestamp(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "timestamp"
           });		
        addAnnotation
          (schemaComponentTypeEClass, 
           source, 
           new String[] {
             "name", "SchemaComponentType",
             "kind", "mixed"
           });			
        addAnnotation
          (getSchemaComponentType_Mixed(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "name", ":mixed"
           });		
        addAnnotation
          (getSchemaComponentType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##any",
             "name", ":1",
             "processing", "lax"
           });		
        addAnnotation
          (getSchemaComponentType_ParentSchema(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "parentSchema"
           });		
        addAnnotation
          (getSchemaComponentType_SchemaLanguage(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "schemaLanguage"
           });		
        addAnnotation
          (getSchemaComponentType_TargetNamespace(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "targetNamespace"
           });		
        addAnnotation
          (searchResultsTypeEClass, 
           source, 
           new String[] {
             "name", "SearchResultsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getSearchResultsType_AbstractRecordGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AbstractRecord:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSearchResultsType_AbstractRecord(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractRecord",
             "namespace", "##targetNamespace",
             "group", "AbstractRecord:group"
           });		
        addAnnotation
          (getSearchResultsType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":2",
             "processing", "strict"
           });		
        addAnnotation
          (getSearchResultsType_ElementSet(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "elementSet"
           });		
        addAnnotation
          (getSearchResultsType_Expires(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "expires"
           });		
        addAnnotation
          (getSearchResultsType_NextRecord(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "nextRecord"
           });		
        addAnnotation
          (getSearchResultsType_NumberOfRecordsMatched(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "numberOfRecordsMatched"
           });		
        addAnnotation
          (getSearchResultsType_NumberOfRecordsReturned(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "numberOfRecordsReturned"
           });		
        addAnnotation
          (getSearchResultsType_RecordSchema(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "recordSchema"
           });		
        addAnnotation
          (getSearchResultsType_ResultSetId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resultSetId"
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
          (getTransactionResponseType_InsertResult(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "InsertResult",
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
          (getTransactionSummaryType_TotalDeleted(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "totalDeleted",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTransactionSummaryType_RequestId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "requestId"
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
             "name", "group:2"
           });		
        addAnnotation
          (getTransactionType_Insert(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Insert",
             "namespace", "##targetNamespace",
             "group", "#group:2"
           });		
        addAnnotation
          (getTransactionType_Update(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Update",
             "namespace", "##targetNamespace",
             "group", "#group:2"
           });		
        addAnnotation
          (getTransactionType_Delete(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Delete",
             "namespace", "##targetNamespace",
             "group", "#group:2"
           });		
        addAnnotation
          (getTransactionType_RequestId(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "requestId"
           });		
        addAnnotation
          (getTransactionType_VerboseResponse(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "verboseResponse"
           });		
        addAnnotation
          (updateTypeEClass, 
           source, 
           new String[] {
             "name", "UpdateType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getUpdateType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":0",
             "processing", "strict"
           });		
        addAnnotation
          (getUpdateType_RecordProperty(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RecordProperty",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUpdateType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getUpdateType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });		
        addAnnotation
          (dcmiRecordTypeEClass, 
           source, 
           new String[] {
             "name", "DCMIRecordType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDCMIRecordType_DCElement(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DC-element",
             "namespace", "http://purl.org/dc/elements/1.1/",
             "group", "http://purl.org/dc/elements/1.1/#DC-element:group"
           });		
        addAnnotation
          (recordTypeEClass, 
           source, 
           new String[] {
             "name", "RecordType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRecordType_AnyText(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AnyText",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getRecordType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows",
             "group", "http://www.opengis.net/ows#BoundingBox:group"
           });		
        addAnnotation
          (simpleLiteralEClass, 
           source, 
           new String[] {
             "name", "SimpleLiteral",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSimpleLiteral_Scheme(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "scheme"
           });		
        addAnnotation
          (summaryRecordTypeEClass, 
           source, 
           new String[] {
             "name", "SummaryRecordType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getSummaryRecordType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "identifier",
             "namespace", "http://purl.org/dc/elements/1.1/",
             "group", "http://purl.org/dc/elements/1.1/#identifier:group"
           });			
        addAnnotation
          (getSummaryRecordType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "title",
             "namespace", "http://purl.org/dc/elements/1.1/",
             "group", "http://purl.org/dc/elements/1.1/#title:group"
           });			
        addAnnotation
          (getSummaryRecordType_Type(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "type",
             "namespace", "http://purl.org/dc/elements/1.1/"
           });			
        addAnnotation
          (getSummaryRecordType_Subject(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "subject",
             "namespace", "http://purl.org/dc/elements/1.1/"
           });			
        addAnnotation
          (getSummaryRecordType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "format",
             "namespace", "http://purl.org/dc/elements/1.1/",
             "group", "http://purl.org/dc/elements/1.1/#format:group"
           });			
        addAnnotation
          (getSummaryRecordType_Relation(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "relation",
             "namespace", "http://purl.org/dc/elements/1.1/",
             "group", "http://purl.org/dc/elements/1.1/#relation:group"
           });			
        addAnnotation
          (getSummaryRecordType_Modified(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "modified",
             "namespace", "http://purl.org/dc/terms/"
           });		
        addAnnotation
          (getSummaryRecordType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "abstract",
             "namespace", "http://purl.org/dc/terms/"
           });		
        addAnnotation
          (getSummaryRecordType_Spatial(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "spatial",
             "namespace", "http://purl.org/dc/terms/"
           });		
        addAnnotation
          (getSummaryRecordType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows",
             "group", "http://www.opengis.net/ows#BoundingBox:group"
           });		
        addAnnotation
          (elementSetTypeEEnum, 
           source, 
           new String[] {
             "name", "ElementSetType"
           });			
        addAnnotation
          (resultTypeEEnum, 
           source, 
           new String[] {
             "name", "ResultType"
           });			
    }

} //Csw20PackageImpl
