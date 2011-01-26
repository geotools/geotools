/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Calendar;
import java.util.List;

import java.util.Map;
import javax.xml.namespace.QName;

import net.opengis.ows10.Ows10Package;

import net.opengis.wfs.ActionType;
import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.BaseRequestType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.DocumentRoot;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.FeaturesLockedType;
import net.opengis.wfs.FeaturesNotLockedType;
import net.opengis.wfs.GMLObjectTypeListType;
import net.opengis.wfs.GMLObjectTypeType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.GetGmlObjectType;
import net.opengis.wfs.IdentifierGenerationOptionType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.LockType;
import net.opengis.wfs.MetadataURLType;
import net.opengis.wfs.NativeType;
import net.opengis.wfs.NoSRSType;
import net.opengis.wfs.OperationType;
import net.opengis.wfs.OperationsType;
import net.opengis.wfs.OutputFormatListType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionResultsType;
import net.opengis.wfs.TransactionSummaryType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WFSCapabilitiesType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wfs.WfsPackage;
import net.opengis.wfs.XlinkPropertyNameType;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.geotools.feature.FeatureCollection;

import org.opengis.filter.Filter;

import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.expression.Function;

import org.opengis.filter.identity.FeatureId;

import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WfsPackageImpl extends EPackageImpl implements WfsPackage {
	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass actionTypeEClass = null;

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
	private EClass deleteElementTypeEClass = null;

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
	private EClass documentRootEClass = null;

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
	private EClass gmlObjectTypeListTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass gmlObjectTypeTypeEClass = null;

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
	private EClass getGmlObjectTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass insertElementTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass insertResultsTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass insertedFeatureTypeEClass = null;

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
	private EClass lockTypeEClass = null;

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
	private EClass noSRSTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass operationsTypeEClass = null;

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
	private EClass propertyTypeEClass = null;

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
	private EClass transactionResponseTypeEClass = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EClass transactionResultsTypeEClass = null;

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
	private EClass updateElementTypeEClass = null;

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
	private EClass xlinkPropertyNameTypeEClass = null;

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
	private EEnum identifierGenerationOptionTypeEEnum = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EEnum operationTypeEEnum = null;

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
	private EDataType serviceTypeEDataType = null;

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
    private EDataType filterCapabilitiesEDataType = null;

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
	private EDataType featureIdEDataType = null;

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
	private EDataType uriEDataType = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EDataType formatTypeEDataType = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EDataType typeTypeEDataType = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private EDataType functionEDataType = null;

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
	private EDataType typeNameListTypeEDataType = null;

	/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType mapEDataType = null;

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
     * @see net.opengis.wfs.WfsPackage#eNS_URI
     * @see #init()
     * @generated
     */
	private WfsPackageImpl() {
        super(eNS_URI, WfsFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link WfsPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
	public static WfsPackage init() {
        if (isInited) return (WfsPackage)EPackage.Registry.INSTANCE.getEPackage(WfsPackage.eNS_URI);

        // Obtain or create and register package
        WfsPackageImpl theWfsPackage = (WfsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof WfsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new WfsPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows10Package.eINSTANCE.eClass();

        // Create package meta-data objects
        theWfsPackage.createPackageContents();

        // Initialize created meta-data
        theWfsPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theWfsPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(WfsPackage.eNS_URI, theWfsPackage);
        return theWfsPackage;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getActionType() {
        return actionTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getActionType_Message() {
        return (EAttribute)actionTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getActionType_Code() {
        return (EAttribute)actionTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getActionType_Locator() {
        return (EAttribute)actionTypeEClass.getEStructuralFeatures().get(2);
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
    public EAttribute getBaseRequestType_ProvidedVersion() {
        return (EAttribute)baseRequestTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getBaseRequestType_ExtendedProperties() {
        return (EAttribute)baseRequestTypeEClass.getEStructuralFeatures().get(5);
    }

				/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getDeleteElementType() {
        return deleteElementTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDeleteElementType_Filter() {
        return (EAttribute)deleteElementTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDeleteElementType_Handle() {
        return (EAttribute)deleteElementTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDeleteElementType_TypeName() {
        return (EAttribute)deleteElementTypeEClass.getEStructuralFeatures().get(2);
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
	public EReference getDocumentRoot_Delete() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_DescribeFeatureType() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_FeatureCollection() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_FeatureTypeList() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_GetFeature() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_GetFeatureWithLock() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_GetGmlObject() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Insert() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_LockFeature() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_LockFeatureResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_LockId() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(14);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Native() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Property() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getDocumentRoot_PropertyName() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(17);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Query() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_ServesGMLObjectTypeList() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_SupportsGMLObjectTypeList() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Transaction() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_TransactionResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_Update() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_WfsCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(24);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getDocumentRoot_XlinkPropertyName() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
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
	public EAttribute getFeatureCollectionType_LockId() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getFeatureCollectionType_TimeStamp() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getFeatureCollectionType_NumberOfFeatures() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getFeatureCollectionType_Feature() {
        return (EAttribute)featureCollectionTypeEClass.getEStructuralFeatures().get(3);
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
	public EReference getFeatureTypeListType_Operations() {
        return (EReference)featureTypeListTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getFeatureTypeListType_FeatureType() {
        return (EReference)featureTypeListTypeEClass.getEStructuralFeatures().get(1);
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
	public EAttribute getFeatureTypeType_Title() {
        return (EAttribute)featureTypeTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getFeatureTypeType_Abstract() {
        return (EAttribute)featureTypeTypeEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getFeatureTypeType_DefaultSRS() {
        return (EAttribute)featureTypeTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getFeatureTypeType_OtherSRS() {
        return (EAttribute)featureTypeTypeEClass.getEStructuralFeatures().get(5);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getFeatureTypeType_NoSRS() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(6);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getFeatureTypeType_Operations() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(7);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getFeatureTypeType_OutputFormats() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(8);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getFeatureTypeType_WGS84BoundingBox() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(9);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getFeatureTypeType_MetadataURL() {
        return (EReference)featureTypeTypeEClass.getEStructuralFeatures().get(10);
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
	public EAttribute getFeaturesLockedType_FeatureId() {
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
	public EAttribute getFeaturesNotLockedType_FeatureId() {
        return (EAttribute)featuresNotLockedTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getGMLObjectTypeListType() {
        return gmlObjectTypeListTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getGMLObjectTypeListType_GMLObjectType() {
        return (EReference)gmlObjectTypeListTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getGMLObjectTypeType() {
        return gmlObjectTypeTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGMLObjectTypeType_Name() {
        return (EAttribute)gmlObjectTypeTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGMLObjectTypeType_Title() {
        return (EAttribute)gmlObjectTypeTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGMLObjectTypeType_Abstract() {
        return (EAttribute)gmlObjectTypeTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getGMLObjectTypeType_Keywords() {
        return (EReference)gmlObjectTypeTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getGMLObjectTypeType_OutputFormats() {
        return (EReference)gmlObjectTypeTypeEClass.getEStructuralFeatures().get(4);
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
	public EReference getGetFeatureType_Query() {
        return (EReference)getFeatureTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetFeatureType_MaxFeatures() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetFeatureType_OutputFormat() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetFeatureType_ResultType() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetFeatureType_TraverseXlinkDepth() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetFeatureType_TraverseXlinkExpiry() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(5);
    }

	/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_FormatOptions() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureType_Metadata() {
        return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(7);
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
	public EClass getGetGmlObjectType() {
        return getGmlObjectTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetGmlObjectType_GmlObjectId() {
        return (EAttribute)getGmlObjectTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetGmlObjectType_OutputFormat() {
        return (EAttribute)getGmlObjectTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetGmlObjectType_TraverseXlinkDepth() {
        return (EAttribute)getGmlObjectTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getGetGmlObjectType_TraverseXlinkExpiry() {
        return (EAttribute)getGmlObjectTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getInsertElementType() {
        return insertElementTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getInsertElementType_Feature() {
        return (EAttribute)insertElementTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getInsertElementType_Handle() {
        return (EAttribute)insertElementTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getInsertElementType_Idgen() {
        return (EAttribute)insertElementTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getInsertElementType_InputFormat() {
        return (EAttribute)insertElementTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getInsertElementType_SrsName() {
        return (EAttribute)insertElementTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getInsertResultsType() {
        return insertResultsTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getInsertResultsType_Feature() {
        return (EReference)insertResultsTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getInsertedFeatureType() {
        return insertedFeatureTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getInsertedFeatureType_FeatureId() {
        return (EAttribute)insertedFeatureTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getInsertedFeatureType_Handle() {
        return (EAttribute)insertedFeatureTypeEClass.getEStructuralFeatures().get(1);
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
	public EAttribute getLockFeatureResponseType_LockId() {
        return (EAttribute)lockFeatureResponseTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getLockFeatureResponseType_FeaturesLocked() {
        return (EReference)lockFeatureResponseTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getLockFeatureResponseType_FeaturesNotLocked() {
        return (EReference)lockFeatureResponseTypeEClass.getEStructuralFeatures().get(2);
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
	public EReference getLockFeatureType_Lock() {
        return (EReference)lockFeatureTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getLockFeatureType_Expiry() {
        return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getLockFeatureType_LockAction() {
        return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getLockType() {
        return lockTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getLockType_Filter() {
        return (EAttribute)lockTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getLockType_Handle() {
        return (EAttribute)lockTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getLockType_TypeName() {
        return (EAttribute)lockTypeEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getMetadataURLType_Value() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getMetadataURLType_Format() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getMetadataURLType_Type() {
        return (EAttribute)metadataURLTypeEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getNativeType_SafeToIgnore() {
        return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getNativeType_VendorId() {
        return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getNoSRSType() {
        return noSRSTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getOperationsType() {
        return operationsTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getOperationsType_Operation() {
        return (EAttribute)operationsTypeEClass.getEStructuralFeatures().get(0);
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
	public EClass getPropertyType() {
        return propertyTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getPropertyType_Name() {
        return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(0);
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
	public EClass getQueryType() {
        return queryTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_Group() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_PropertyName() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getQueryType_XlinkPropertyName() {
        return (EReference)queryTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_Function() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_Filter() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_SortBy() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(5);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_FeatureVersion() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(6);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_Handle() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(7);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_SrsName() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(8);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getQueryType_TypeName() {
        return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(9);
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
	public EReference getTransactionResponseType_TransactionResults() {
        return (EReference)transactionResponseTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getTransactionResponseType_InsertResults() {
        return (EReference)transactionResponseTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getTransactionResponseType_Version() {
        return (EAttribute)transactionResponseTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getTransactionResultsType() {
        return transactionResultsTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getTransactionResultsType_Handle() {
        return (EAttribute)transactionResultsTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getTransactionResultsType_Action() {
        return (EReference)transactionResultsTypeEClass.getEStructuralFeatures().get(1);
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
	public EClass getTransactionType() {
        return transactionTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getTransactionType_LockId() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getTransactionType_Group() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getTransactionType_Insert() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getTransactionType_Update() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getTransactionType_Delete() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getTransactionType_Native() {
        return (EReference)transactionTypeEClass.getEStructuralFeatures().get(5);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getTransactionType_ReleaseAction() {
        return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(6);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getUpdateElementType() {
        return updateElementTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getUpdateElementType_Property() {
        return (EReference)updateElementTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getUpdateElementType_Filter() {
        return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getUpdateElementType_Handle() {
        return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getUpdateElementType_InputFormat() {
        return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getUpdateElementType_SrsName() {
        return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(4);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getUpdateElementType_TypeName() {
        return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(5);
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
	public EReference getWFSCapabilitiesType_FeatureTypeList() {
        return (EReference)wfsCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getWFSCapabilitiesType_ServesGMLObjectTypeList() {
        return (EReference)wfsCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EReference getWFSCapabilitiesType_SupportsGMLObjectTypeList() {
        return (EReference)wfsCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getWFSCapabilitiesType_FilterCapabilities() {
        return (EAttribute)wfsCapabilitiesTypeEClass.getEStructuralFeatures().get(3);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EClass getXlinkPropertyNameType() {
        return xlinkPropertyNameTypeEClass;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getXlinkPropertyNameType_Value() {
        return (EAttribute)xlinkPropertyNameTypeEClass.getEStructuralFeatures().get(0);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getXlinkPropertyNameType_TraverseXlinkDepth() {
        return (EAttribute)xlinkPropertyNameTypeEClass.getEStructuralFeatures().get(1);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EAttribute getXlinkPropertyNameType_TraverseXlinkExpiry() {
        return (EAttribute)xlinkPropertyNameTypeEClass.getEStructuralFeatures().get(2);
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
	public EEnum getIdentifierGenerationOptionType() {
        return identifierGenerationOptionTypeEEnum;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EEnum getOperationType() {
        return operationTypeEEnum;
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
	public EDataType getServiceType() {
        return serviceTypeEDataType;
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
    public EDataType getFilterCapabilities() {
        return filterCapabilitiesEDataType;
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
	public EDataType getFeatureId() {
        return featureIdEDataType;
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
	public EDataType getURI() {
        return uriEDataType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EDataType getFormatType() {
        return formatTypeEDataType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EDataType getTypeType() {
        return typeTypeEDataType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EDataType getFunction() {
        return functionEDataType;
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
	public EDataType getTypeNameListType() {
        return typeNameListTypeEDataType;
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
	public WfsFactory getWfsFactory() {
        return (WfsFactory)getEFactoryInstance();
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
        actionTypeEClass = createEClass(ACTION_TYPE);
        createEAttribute(actionTypeEClass, ACTION_TYPE__MESSAGE);
        createEAttribute(actionTypeEClass, ACTION_TYPE__CODE);
        createEAttribute(actionTypeEClass, ACTION_TYPE__LOCATOR);

        baseRequestTypeEClass = createEClass(BASE_REQUEST_TYPE);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__HANDLE);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__SERVICE);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__VERSION);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__BASE_URL);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__PROVIDED_VERSION);
        createEAttribute(baseRequestTypeEClass, BASE_REQUEST_TYPE__EXTENDED_PROPERTIES);

        deleteElementTypeEClass = createEClass(DELETE_ELEMENT_TYPE);
        createEAttribute(deleteElementTypeEClass, DELETE_ELEMENT_TYPE__FILTER);
        createEAttribute(deleteElementTypeEClass, DELETE_ELEMENT_TYPE__HANDLE);
        createEAttribute(deleteElementTypeEClass, DELETE_ELEMENT_TYPE__TYPE_NAME);

        describeFeatureTypeTypeEClass = createEClass(DESCRIBE_FEATURE_TYPE_TYPE);
        createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME);
        createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DELETE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FEATURE_COLLECTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FEATURE_TYPE_LIST);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_FEATURE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_GML_OBJECT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__INSERT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LOCK_FEATURE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__LOCK_ID);
        createEReference(documentRootEClass, DOCUMENT_ROOT__NATIVE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__PROPERTY_NAME);
        createEReference(documentRootEClass, DOCUMENT_ROOT__QUERY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TRANSACTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TRANSACTION_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__UPDATE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__WFS_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XLINK_PROPERTY_NAME);

        featureCollectionTypeEClass = createEClass(FEATURE_COLLECTION_TYPE);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__LOCK_ID);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__TIME_STAMP);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__NUMBER_OF_FEATURES);
        createEAttribute(featureCollectionTypeEClass, FEATURE_COLLECTION_TYPE__FEATURE);

        featureTypeListTypeEClass = createEClass(FEATURE_TYPE_LIST_TYPE);
        createEReference(featureTypeListTypeEClass, FEATURE_TYPE_LIST_TYPE__OPERATIONS);
        createEReference(featureTypeListTypeEClass, FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE);

        featureTypeTypeEClass = createEClass(FEATURE_TYPE_TYPE);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__NAME);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__TITLE);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__ABSTRACT);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__KEYWORDS);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__DEFAULT_SRS);
        createEAttribute(featureTypeTypeEClass, FEATURE_TYPE_TYPE__OTHER_SRS);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__NO_SRS);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__OPERATIONS);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__OUTPUT_FORMATS);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX);
        createEReference(featureTypeTypeEClass, FEATURE_TYPE_TYPE__METADATA_URL);

        featuresLockedTypeEClass = createEClass(FEATURES_LOCKED_TYPE);
        createEAttribute(featuresLockedTypeEClass, FEATURES_LOCKED_TYPE__GROUP);
        createEAttribute(featuresLockedTypeEClass, FEATURES_LOCKED_TYPE__FEATURE_ID);

        featuresNotLockedTypeEClass = createEClass(FEATURES_NOT_LOCKED_TYPE);
        createEAttribute(featuresNotLockedTypeEClass, FEATURES_NOT_LOCKED_TYPE__GROUP);
        createEAttribute(featuresNotLockedTypeEClass, FEATURES_NOT_LOCKED_TYPE__FEATURE_ID);

        gmlObjectTypeListTypeEClass = createEClass(GML_OBJECT_TYPE_LIST_TYPE);
        createEReference(gmlObjectTypeListTypeEClass, GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE);

        gmlObjectTypeTypeEClass = createEClass(GML_OBJECT_TYPE_TYPE);
        createEAttribute(gmlObjectTypeTypeEClass, GML_OBJECT_TYPE_TYPE__NAME);
        createEAttribute(gmlObjectTypeTypeEClass, GML_OBJECT_TYPE_TYPE__TITLE);
        createEAttribute(gmlObjectTypeTypeEClass, GML_OBJECT_TYPE_TYPE__ABSTRACT);
        createEReference(gmlObjectTypeTypeEClass, GML_OBJECT_TYPE_TYPE__KEYWORDS);
        createEReference(gmlObjectTypeTypeEClass, GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);

        getFeatureTypeEClass = createEClass(GET_FEATURE_TYPE);
        createEReference(getFeatureTypeEClass, GET_FEATURE_TYPE__QUERY);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__MAX_FEATURES);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__OUTPUT_FORMAT);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__RESULT_TYPE);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__FORMAT_OPTIONS);
        createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__METADATA);

        getFeatureWithLockTypeEClass = createEClass(GET_FEATURE_WITH_LOCK_TYPE);
        createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__EXPIRY);

        getGmlObjectTypeEClass = createEClass(GET_GML_OBJECT_TYPE);
        createEAttribute(getGmlObjectTypeEClass, GET_GML_OBJECT_TYPE__GML_OBJECT_ID);
        createEAttribute(getGmlObjectTypeEClass, GET_GML_OBJECT_TYPE__OUTPUT_FORMAT);
        createEAttribute(getGmlObjectTypeEClass, GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_DEPTH);
        createEAttribute(getGmlObjectTypeEClass, GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_EXPIRY);

        insertElementTypeEClass = createEClass(INSERT_ELEMENT_TYPE);
        createEAttribute(insertElementTypeEClass, INSERT_ELEMENT_TYPE__FEATURE);
        createEAttribute(insertElementTypeEClass, INSERT_ELEMENT_TYPE__HANDLE);
        createEAttribute(insertElementTypeEClass, INSERT_ELEMENT_TYPE__IDGEN);
        createEAttribute(insertElementTypeEClass, INSERT_ELEMENT_TYPE__INPUT_FORMAT);
        createEAttribute(insertElementTypeEClass, INSERT_ELEMENT_TYPE__SRS_NAME);

        insertResultsTypeEClass = createEClass(INSERT_RESULTS_TYPE);
        createEReference(insertResultsTypeEClass, INSERT_RESULTS_TYPE__FEATURE);

        insertedFeatureTypeEClass = createEClass(INSERTED_FEATURE_TYPE);
        createEAttribute(insertedFeatureTypeEClass, INSERTED_FEATURE_TYPE__FEATURE_ID);
        createEAttribute(insertedFeatureTypeEClass, INSERTED_FEATURE_TYPE__HANDLE);

        lockFeatureResponseTypeEClass = createEClass(LOCK_FEATURE_RESPONSE_TYPE);
        createEAttribute(lockFeatureResponseTypeEClass, LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID);
        createEReference(lockFeatureResponseTypeEClass, LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED);
        createEReference(lockFeatureResponseTypeEClass, LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED);

        lockFeatureTypeEClass = createEClass(LOCK_FEATURE_TYPE);
        createEReference(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__LOCK);
        createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__EXPIRY);
        createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__LOCK_ACTION);

        lockTypeEClass = createEClass(LOCK_TYPE);
        createEAttribute(lockTypeEClass, LOCK_TYPE__FILTER);
        createEAttribute(lockTypeEClass, LOCK_TYPE__HANDLE);
        createEAttribute(lockTypeEClass, LOCK_TYPE__TYPE_NAME);

        metadataURLTypeEClass = createEClass(METADATA_URL_TYPE);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__VALUE);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__FORMAT);
        createEAttribute(metadataURLTypeEClass, METADATA_URL_TYPE__TYPE);

        nativeTypeEClass = createEClass(NATIVE_TYPE);
        createEAttribute(nativeTypeEClass, NATIVE_TYPE__SAFE_TO_IGNORE);
        createEAttribute(nativeTypeEClass, NATIVE_TYPE__VENDOR_ID);

        noSRSTypeEClass = createEClass(NO_SRS_TYPE);

        operationsTypeEClass = createEClass(OPERATIONS_TYPE);
        createEAttribute(operationsTypeEClass, OPERATIONS_TYPE__OPERATION);

        outputFormatListTypeEClass = createEClass(OUTPUT_FORMAT_LIST_TYPE);
        createEAttribute(outputFormatListTypeEClass, OUTPUT_FORMAT_LIST_TYPE__GROUP);
        createEAttribute(outputFormatListTypeEClass, OUTPUT_FORMAT_LIST_TYPE__FORMAT);

        propertyTypeEClass = createEClass(PROPERTY_TYPE);
        createEAttribute(propertyTypeEClass, PROPERTY_TYPE__NAME);
        createEAttribute(propertyTypeEClass, PROPERTY_TYPE__VALUE);

        queryTypeEClass = createEClass(QUERY_TYPE);
        createEAttribute(queryTypeEClass, QUERY_TYPE__GROUP);
        createEAttribute(queryTypeEClass, QUERY_TYPE__PROPERTY_NAME);
        createEReference(queryTypeEClass, QUERY_TYPE__XLINK_PROPERTY_NAME);
        createEAttribute(queryTypeEClass, QUERY_TYPE__FUNCTION);
        createEAttribute(queryTypeEClass, QUERY_TYPE__FILTER);
        createEAttribute(queryTypeEClass, QUERY_TYPE__SORT_BY);
        createEAttribute(queryTypeEClass, QUERY_TYPE__FEATURE_VERSION);
        createEAttribute(queryTypeEClass, QUERY_TYPE__HANDLE);
        createEAttribute(queryTypeEClass, QUERY_TYPE__SRS_NAME);
        createEAttribute(queryTypeEClass, QUERY_TYPE__TYPE_NAME);

        transactionResponseTypeEClass = createEClass(TRANSACTION_RESPONSE_TYPE);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULTS);
        createEReference(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS);
        createEAttribute(transactionResponseTypeEClass, TRANSACTION_RESPONSE_TYPE__VERSION);

        transactionResultsTypeEClass = createEClass(TRANSACTION_RESULTS_TYPE);
        createEAttribute(transactionResultsTypeEClass, TRANSACTION_RESULTS_TYPE__HANDLE);
        createEReference(transactionResultsTypeEClass, TRANSACTION_RESULTS_TYPE__ACTION);

        transactionSummaryTypeEClass = createEClass(TRANSACTION_SUMMARY_TYPE);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED);
        createEAttribute(transactionSummaryTypeEClass, TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED);

        transactionTypeEClass = createEClass(TRANSACTION_TYPE);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__LOCK_ID);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__GROUP);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__INSERT);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__UPDATE);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__DELETE);
        createEReference(transactionTypeEClass, TRANSACTION_TYPE__NATIVE);
        createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__RELEASE_ACTION);

        updateElementTypeEClass = createEClass(UPDATE_ELEMENT_TYPE);
        createEReference(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__PROPERTY);
        createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__FILTER);
        createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__HANDLE);
        createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__INPUT_FORMAT);
        createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__SRS_NAME);
        createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__TYPE_NAME);

        wfsCapabilitiesTypeEClass = createEClass(WFS_CAPABILITIES_TYPE);
        createEReference(wfsCapabilitiesTypeEClass, WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST);
        createEReference(wfsCapabilitiesTypeEClass, WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST);
        createEReference(wfsCapabilitiesTypeEClass, WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST);
        createEAttribute(wfsCapabilitiesTypeEClass, WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES);

        xlinkPropertyNameTypeEClass = createEClass(XLINK_PROPERTY_NAME_TYPE);
        createEAttribute(xlinkPropertyNameTypeEClass, XLINK_PROPERTY_NAME_TYPE__VALUE);
        createEAttribute(xlinkPropertyNameTypeEClass, XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_DEPTH);
        createEAttribute(xlinkPropertyNameTypeEClass, XLINK_PROPERTY_NAME_TYPE__TRAVERSE_XLINK_EXPIRY);

        // Create enums
        allSomeTypeEEnum = createEEnum(ALL_SOME_TYPE);
        identifierGenerationOptionTypeEEnum = createEEnum(IDENTIFIER_GENERATION_OPTION_TYPE);
        operationTypeEEnum = createEEnum(OPERATION_TYPE);
        resultTypeTypeEEnum = createEEnum(RESULT_TYPE_TYPE);

        // Create data types
        serviceTypeEDataType = createEDataType(SERVICE_TYPE);
        filterEDataType = createEDataType(FILTER);
        filterCapabilitiesEDataType = createEDataType(FILTER_CAPABILITIES);
        qNameEDataType = createEDataType(QNAME);
        calendarEDataType = createEDataType(CALENDAR);
        featureCollectionEDataType = createEDataType(FEATURE_COLLECTION);
        featureIdEDataType = createEDataType(FEATURE_ID);
        serviceType_1EDataType = createEDataType(SERVICE_TYPE_1);
        uriEDataType = createEDataType(URI);
        formatTypeEDataType = createEDataType(FORMAT_TYPE);
        typeTypeEDataType = createEDataType(TYPE_TYPE);
        functionEDataType = createEDataType(FUNCTION);
        sortByEDataType = createEDataType(SORT_BY);
        typeNameListTypeEDataType = createEDataType(TYPE_NAME_LIST_TYPE);
        mapEDataType = createEDataType(MAP);
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
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
        Ows10Package theOws10Package = (Ows10Package)EPackage.Registry.INSTANCE.getEPackage(Ows10Package.eNS_URI);

        // Add supertypes to classes
        describeFeatureTypeTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        getCapabilitiesTypeEClass.getESuperTypes().add(theOws10Package.getGetCapabilitiesType());
        getFeatureTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        getFeatureWithLockTypeEClass.getESuperTypes().add(this.getGetFeatureType());
        getGmlObjectTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        lockFeatureTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        transactionTypeEClass.getESuperTypes().add(this.getBaseRequestType());
        wfsCapabilitiesTypeEClass.getESuperTypes().add(theOws10Package.getCapabilitiesBaseType());

        // Initialize classes and features; add operations and parameters
        initEClass(actionTypeEClass, ActionType.class, "ActionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getActionType_Message(), theXMLTypePackage.getString(), "message", null, 0, 1, ActionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getActionType_Code(), theXMLTypePackage.getString(), "code", null, 0, 1, ActionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getActionType_Locator(), theXMLTypePackage.getString(), "locator", null, 1, 1, ActionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(baseRequestTypeEClass, BaseRequestType.class, "BaseRequestType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBaseRequestType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_Service(), this.getServiceType(), "service", "WFS", 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_Version(), theXMLTypePackage.getString(), "version", "1.1.0", 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_BaseUrl(), theXMLTypePackage.getString(), "baseUrl", null, 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_ProvidedVersion(), ecorePackage.getEString(), "providedVersion", null, 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBaseRequestType_ExtendedProperties(), this.getMap(), "extendedProperties", null, 0, 1, BaseRequestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(deleteElementTypeEClass, DeleteElementType.class, "DeleteElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDeleteElementType_Filter(), this.getFilter(), "filter", null, 0, 1, DeleteElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDeleteElementType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, DeleteElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDeleteElementType_TypeName(), this.getQName(), "typeName", null, 0, 1, DeleteElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeFeatureTypeTypeEClass, DescribeFeatureTypeType.class, "DescribeFeatureTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescribeFeatureTypeType_TypeName(), this.getQName(), "typeName", null, 0, -1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDescribeFeatureTypeType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "text/xml; subtype=gml/3.1.1", 0, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Delete(), this.getDeleteElementType(), null, "delete", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DescribeFeatureType(), this.getDescribeFeatureTypeType(), null, "describeFeatureType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_FeatureCollection(), this.getFeatureCollectionType(), null, "featureCollection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_FeatureTypeList(), this.getFeatureTypeListType(), null, "featureTypeList", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetFeature(), this.getGetFeatureType(), null, "getFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetFeatureWithLock(), this.getGetFeatureWithLockType(), null, "getFeatureWithLock", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetGmlObject(), this.getGetGmlObjectType(), null, "getGmlObject", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Insert(), this.getInsertElementType(), null, "insert", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_LockFeature(), this.getLockFeatureType(), null, "lockFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_LockFeatureResponse(), this.getLockFeatureResponseType(), null, "lockFeatureResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Native(), this.getNativeType(), null, "native", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Property(), this.getPropertyType(), null, "property", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_PropertyName(), theXMLTypePackage.getString(), "propertyName", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Query(), this.getQueryType(), null, "query", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ServesGMLObjectTypeList(), this.getGMLObjectTypeListType(), null, "servesGMLObjectTypeList", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_SupportsGMLObjectTypeList(), this.getGMLObjectTypeListType(), null, "supportsGMLObjectTypeList", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Transaction(), this.getTransactionType(), null, "transaction", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TransactionResponse(), this.getTransactionResponseType(), null, "transactionResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Update(), this.getUpdateElementType(), null, "update", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_WfsCapabilities(), this.getWFSCapabilitiesType(), null, "wfsCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XlinkPropertyName(), this.getXlinkPropertyNameType(), null, "xlinkPropertyName", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(featureCollectionTypeEClass, FeatureCollectionType.class, "FeatureCollectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeatureCollectionType_LockId(), ecorePackage.getEString(), "lockId", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_TimeStamp(), this.getCalendar(), "timeStamp", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_NumberOfFeatures(), ecorePackage.getEBigInteger(), "numberOfFeatures", null, 0, 1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCollectionType_Feature(), this.getFeatureCollection(), "feature", null, 0, -1, FeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featureTypeListTypeEClass, FeatureTypeListType.class, "FeatureTypeListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureTypeListType_Operations(), this.getOperationsType(), null, "operations", null, 0, 1, FeatureTypeListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeListType_FeatureType(), this.getFeatureTypeType(), null, "featureType", null, 1, -1, FeatureTypeListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featureTypeTypeEClass, FeatureTypeType.class, "FeatureTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeatureTypeType_Name(), this.getQName(), "name", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureTypeType_Title(), theXMLTypePackage.getString(), "title", null, 1, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureTypeType_Abstract(), theXMLTypePackage.getString(), "abstract", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_Keywords(), theOws10Package.getKeywordsType(), null, "keywords", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureTypeType_DefaultSRS(), theXMLTypePackage.getAnyURI(), "defaultSRS", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureTypeType_OtherSRS(), theXMLTypePackage.getAnyURI(), "otherSRS", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_NoSRS(), this.getNoSRSType(), null, "noSRS", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_Operations(), this.getOperationsType(), null, "operations", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_OutputFormats(), this.getOutputFormatListType(), null, "outputFormats", null, 0, 1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_WGS84BoundingBox(), theOws10Package.getWGS84BoundingBoxType(), null, "wGS84BoundingBox", null, 1, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureTypeType_MetadataURL(), this.getMetadataURLType(), null, "metadataURL", null, 0, -1, FeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featuresLockedTypeEClass, FeaturesLockedType.class, "FeaturesLockedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeaturesLockedType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, FeaturesLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeaturesLockedType_FeatureId(), this.getFeatureId(), "featureId", null, 0, -1, FeaturesLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(featuresNotLockedTypeEClass, FeaturesNotLockedType.class, "FeaturesNotLockedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeaturesNotLockedType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, FeaturesNotLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeaturesNotLockedType_FeatureId(), this.getFeatureId(), "featureId", null, 0, -1, FeaturesNotLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(gmlObjectTypeListTypeEClass, GMLObjectTypeListType.class, "GMLObjectTypeListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGMLObjectTypeListType_GMLObjectType(), this.getGMLObjectTypeType(), null, "gMLObjectType", null, 1, -1, GMLObjectTypeListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(gmlObjectTypeTypeEClass, GMLObjectTypeType.class, "GMLObjectTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGMLObjectTypeType_Name(), this.getQName(), "name", null, 0, 1, GMLObjectTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGMLObjectTypeType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, GMLObjectTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGMLObjectTypeType_Abstract(), theXMLTypePackage.getString(), "abstract", null, 0, 1, GMLObjectTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGMLObjectTypeType_Keywords(), theOws10Package.getKeywordsType(), null, "keywords", null, 0, -1, GMLObjectTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGMLObjectTypeType_OutputFormats(), this.getOutputFormatListType(), null, "outputFormats", null, 0, 1, GMLObjectTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCapabilitiesType_Service(), this.getServiceType_1(), "service", "WFS", 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getFeatureTypeEClass, GetFeatureType.class, "GetFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetFeatureType_Query(), this.getQueryType(), null, "query", null, 1, -1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_MaxFeatures(), theXMLTypePackage.getPositiveInteger(), "maxFeatures", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "text/xml; subtype=gml/3.1.1", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_ResultType(), this.getResultTypeType(), "resultType", "results", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_TraverseXlinkDepth(), theXMLTypePackage.getString(), "traverseXlinkDepth", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_TraverseXlinkExpiry(), theXMLTypePackage.getPositiveInteger(), "traverseXlinkExpiry", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_FormatOptions(), this.getMap(), "formatOptions", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureType_Metadata(), this.getMap(), "metadata", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getFeatureWithLockTypeEClass, GetFeatureWithLockType.class, "GetFeatureWithLockType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetFeatureWithLockType_Expiry(), theXMLTypePackage.getPositiveInteger(), "expiry", "5", 0, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getGmlObjectTypeEClass, GetGmlObjectType.class, "GetGmlObjectType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetGmlObjectType_GmlObjectId(), theXMLTypePackage.getAnySimpleType(), "gmlObjectId", null, 1, 1, GetGmlObjectType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetGmlObjectType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "GML3", 0, 1, GetGmlObjectType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetGmlObjectType_TraverseXlinkDepth(), theXMLTypePackage.getString(), "traverseXlinkDepth", null, 1, 1, GetGmlObjectType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetGmlObjectType_TraverseXlinkExpiry(), theXMLTypePackage.getPositiveInteger(), "traverseXlinkExpiry", null, 0, 1, GetGmlObjectType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(insertElementTypeEClass, InsertElementType.class, "InsertElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInsertElementType_Feature(), ecorePackage.getEJavaObject(), "feature", null, 0, -1, InsertElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertElementType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, InsertElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertElementType_Idgen(), this.getIdentifierGenerationOptionType(), "idgen", "GenerateNew", 0, 1, InsertElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertElementType_InputFormat(), theXMLTypePackage.getString(), "inputFormat", "text/xml; subtype=gml/3.1.1", 0, 1, InsertElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertElementType_SrsName(), this.getURI(), "srsName", null, 0, 1, InsertElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(insertResultsTypeEClass, InsertResultsType.class, "InsertResultsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInsertResultsType_Feature(), this.getInsertedFeatureType(), null, "feature", null, 1, -1, InsertResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(insertedFeatureTypeEClass, InsertedFeatureType.class, "InsertedFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInsertedFeatureType_FeatureId(), this.getFeatureId(), "featureId", null, 0, -1, InsertedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getInsertedFeatureType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, InsertedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(lockFeatureResponseTypeEClass, LockFeatureResponseType.class, "LockFeatureResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLockFeatureResponseType_LockId(), theXMLTypePackage.getString(), "lockId", null, 1, 1, LockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLockFeatureResponseType_FeaturesLocked(), this.getFeaturesLockedType(), null, "featuresLocked", null, 0, 1, LockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLockFeatureResponseType_FeaturesNotLocked(), this.getFeaturesNotLockedType(), null, "featuresNotLocked", null, 0, 1, LockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(lockFeatureTypeEClass, LockFeatureType.class, "LockFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLockFeatureType_Lock(), this.getLockType(), null, "lock", null, 1, -1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockFeatureType_Expiry(), theXMLTypePackage.getPositiveInteger(), "expiry", "5", 0, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockFeatureType_LockAction(), this.getAllSomeType(), "lockAction", "ALL", 0, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(lockTypeEClass, LockType.class, "LockType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLockType_Filter(), this.getFilter(), "filter", null, 0, 1, LockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, LockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLockType_TypeName(), this.getQName(), "typeName", null, 0, 1, LockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(metadataURLTypeEClass, MetadataURLType.class, "MetadataURLType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMetadataURLType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Format(), this.getFormatType(), "format", null, 1, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMetadataURLType_Type(), this.getTypeType(), "type", null, 1, 1, MetadataURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(nativeTypeEClass, NativeType.class, "NativeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getNativeType_SafeToIgnore(), theXMLTypePackage.getBoolean(), "safeToIgnore", null, 1, 1, NativeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNativeType_VendorId(), theXMLTypePackage.getString(), "vendorId", null, 1, 1, NativeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(noSRSTypeEClass, NoSRSType.class, "NoSRSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(operationsTypeEClass, OperationsType.class, "OperationsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOperationsType_Operation(), this.getOperationType(), "operation", "Insert", 1, 1, OperationsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(outputFormatListTypeEClass, OutputFormatListType.class, "OutputFormatListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOutputFormatListType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, OutputFormatListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputFormatListType_Format(), theXMLTypePackage.getString(), "format", null, 1, -1, OutputFormatListType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(propertyTypeEClass, PropertyType.class, "PropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPropertyType_Name(), this.getQName(), "name", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPropertyType_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(queryTypeEClass, QueryType.class, "QueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getQueryType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_PropertyName(), this.getServiceType(), "propertyName", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getQueryType_XlinkPropertyName(), this.getXlinkPropertyNameType(), null, "xlinkPropertyName", null, 0, -1, QueryType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_Function(), this.getFunction(), "function", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_Filter(), this.getFilter(), "filter", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_SortBy(), this.getSortBy(), "sortBy", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_FeatureVersion(), theXMLTypePackage.getString(), "featureVersion", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_SrsName(), this.getURI(), "srsName", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getQueryType_TypeName(), this.getTypeNameListType(), "typeName", null, 1, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionResponseTypeEClass, TransactionResponseType.class, "TransactionResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTransactionResponseType_TransactionSummary(), this.getTransactionSummaryType(), null, "transactionSummary", null, 1, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionResponseType_TransactionResults(), this.getTransactionResultsType(), null, "transactionResults", null, 0, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionResponseType_InsertResults(), this.getInsertResultsType(), null, "insertResults", null, 1, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionResponseType_Version(), theXMLTypePackage.getString(), "version", "1.1.0", 1, 1, TransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionResultsTypeEClass, TransactionResultsType.class, "TransactionResultsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransactionResultsType_Handle(), ecorePackage.getEString(), "handle", null, 0, 1, TransactionResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionResultsType_Action(), this.getActionType(), null, "action", null, 0, -1, TransactionResultsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionSummaryTypeEClass, TransactionSummaryType.class, "TransactionSummaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransactionSummaryType_TotalInserted(), theXMLTypePackage.getNonNegativeInteger(), "totalInserted", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_TotalUpdated(), theXMLTypePackage.getNonNegativeInteger(), "totalUpdated", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionSummaryType_TotalDeleted(), theXMLTypePackage.getNonNegativeInteger(), "totalDeleted", null, 0, 1, TransactionSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(transactionTypeEClass, TransactionType.class, "TransactionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransactionType_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_Group(), theEcorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_Insert(), this.getInsertElementType(), null, "insert", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_Update(), this.getUpdateElementType(), null, "update", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_Delete(), this.getDeleteElementType(), null, "delete", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getTransactionType_Native(), this.getNativeType(), null, "native", null, 0, -1, TransactionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getTransactionType_ReleaseAction(), this.getAllSomeType(), "releaseAction", "ALL", 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(updateElementTypeEClass, UpdateElementType.class, "UpdateElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUpdateElementType_Property(), this.getPropertyType(), null, "property", null, 1, -1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateElementType_Filter(), this.getFilter(), "filter", null, 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateElementType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateElementType_InputFormat(), theXMLTypePackage.getString(), "inputFormat", "x-application/gml:3", 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateElementType_SrsName(), this.getURI(), "srsName", null, 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUpdateElementType_TypeName(), this.getQName(), "typeName", null, 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wfsCapabilitiesTypeEClass, WFSCapabilitiesType.class, "WFSCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWFSCapabilitiesType_FeatureTypeList(), this.getFeatureTypeListType(), null, "featureTypeList", null, 0, 1, WFSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWFSCapabilitiesType_ServesGMLObjectTypeList(), this.getGMLObjectTypeListType(), null, "servesGMLObjectTypeList", null, 0, 1, WFSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWFSCapabilitiesType_SupportsGMLObjectTypeList(), this.getGMLObjectTypeListType(), null, "supportsGMLObjectTypeList", null, 0, 1, WFSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWFSCapabilitiesType_FilterCapabilities(), this.getFilterCapabilities(), "filterCapabilities", null, 1, 1, WFSCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(xlinkPropertyNameTypeEClass, XlinkPropertyNameType.class, "XlinkPropertyNameType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getXlinkPropertyNameType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, XlinkPropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXlinkPropertyNameType_TraverseXlinkDepth(), theXMLTypePackage.getString(), "traverseXlinkDepth", null, 1, 1, XlinkPropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getXlinkPropertyNameType_TraverseXlinkExpiry(), theXMLTypePackage.getPositiveInteger(), "traverseXlinkExpiry", null, 0, 1, XlinkPropertyNameType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(allSomeTypeEEnum, AllSomeType.class, "AllSomeType");
        addEEnumLiteral(allSomeTypeEEnum, AllSomeType.ALL_LITERAL);
        addEEnumLiteral(allSomeTypeEEnum, AllSomeType.SOME_LITERAL);

        initEEnum(identifierGenerationOptionTypeEEnum, IdentifierGenerationOptionType.class, "IdentifierGenerationOptionType");
        addEEnumLiteral(identifierGenerationOptionTypeEEnum, IdentifierGenerationOptionType.USE_EXISTING_LITERAL);
        addEEnumLiteral(identifierGenerationOptionTypeEEnum, IdentifierGenerationOptionType.REPLACE_DUPLICATE_LITERAL);
        addEEnumLiteral(identifierGenerationOptionTypeEEnum, IdentifierGenerationOptionType.GENERATE_NEW_LITERAL);

        initEEnum(operationTypeEEnum, OperationType.class, "OperationType");
        addEEnumLiteral(operationTypeEEnum, OperationType.INSERT_LITERAL);
        addEEnumLiteral(operationTypeEEnum, OperationType.UPDATE_LITERAL);
        addEEnumLiteral(operationTypeEEnum, OperationType.DELETE_LITERAL);
        addEEnumLiteral(operationTypeEEnum, OperationType.QUERY_LITERAL);
        addEEnumLiteral(operationTypeEEnum, OperationType.LOCK_LITERAL);
        addEEnumLiteral(operationTypeEEnum, OperationType.GET_GML_OBJECT_LITERAL);

        initEEnum(resultTypeTypeEEnum, ResultTypeType.class, "ResultTypeType");
        addEEnumLiteral(resultTypeTypeEEnum, ResultTypeType.RESULTS_LITERAL);
        addEEnumLiteral(resultTypeTypeEEnum, ResultTypeType.HITS_LITERAL);

        // Initialize data types
        initEDataType(serviceTypeEDataType, String.class, "ServiceType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(filterEDataType, Filter.class, "Filter", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(filterCapabilitiesEDataType, FilterCapabilities.class, "FilterCapabilities", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(qNameEDataType, QName.class, "QName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(calendarEDataType, Calendar.class, "Calendar", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(featureCollectionEDataType, FeatureCollection.class, "FeatureCollection", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(featureIdEDataType, FeatureId.class, "FeatureId", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(serviceType_1EDataType, String.class, "ServiceType_1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(uriEDataType, java.net.URI.class, "URI", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(formatTypeEDataType, String.class, "FormatType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(typeTypeEDataType, String.class, "TypeType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(functionEDataType, Function.class, "Function", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(sortByEDataType, SortBy.class, "SortBy", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(typeNameListTypeEDataType, List.class, "TypeNameListType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(mapEDataType, Map.class, "Map", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

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
          (actionTypeEClass, 
           source, 
           new String[] {
             "name", "ActionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getActionType_Message(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Message",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getActionType_Code(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "code"
           });			
        addAnnotation
          (getActionType_Locator(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "locator"
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
          (deleteElementTypeEClass, 
           source, 
           new String[] {
             "name", "DeleteElementType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDeleteElementType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });				
        addAnnotation
          (describeFeatureTypeTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeFeatureTypeType",
             "kind", "elementOnly"
           });				
        addAnnotation
          (getDescribeFeatureTypeType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
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
          (getDocumentRoot_Delete(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Delete",
             "namespace", "##targetNamespace"
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
          (getDocumentRoot_FeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeatureCollection",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/gml#_FeatureCollection"
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
          (getDocumentRoot_GetGmlObject(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetGmlObject",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Insert(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Insert",
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
          (getDocumentRoot_LockId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LockId",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Native(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Native",
             "namespace", "##targetNamespace"
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
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Query(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Query",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ServesGMLObjectTypeList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServesGMLObjectTypeList",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_SupportsGMLObjectTypeList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportsGMLObjectTypeList",
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
          (getDocumentRoot_Update(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Update",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_WfsCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WFS_Capabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_XlinkPropertyName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "XlinkPropertyName",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (featureCollectionTypeEClass, 
           source, 
           new String[] {
             "name", "FeatureCollectionType",
             "kind", "empty"
           });			
        addAnnotation
          (featureTypeListTypeEClass, 
           source, 
           new String[] {
             "name", "FeatureTypeListType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getFeatureTypeListType_Operations(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operations",
             "namespace", "##targetNamespace"
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
             "namespace", "http://www.opengis.net/ows"
           });		
        addAnnotation
          (getFeatureTypeType_DefaultSRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DefaultSRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getFeatureTypeType_OtherSRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OtherSRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getFeatureTypeType_NoSRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "NoSRS",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getFeatureTypeType_Operations(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operations",
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
             "namespace", "http://www.opengis.net/ows"
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
          (gmlObjectTypeListTypeEClass, 
           source, 
           new String[] {
             "name", "GMLObjectTypeListType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGMLObjectTypeListType_GMLObjectType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GMLObjectType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (gmlObjectTypeTypeEClass, 
           source, 
           new String[] {
             "name", "GMLObjectTypeType",
             "kind", "elementOnly"
           });				
        addAnnotation
          (getGMLObjectTypeType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGMLObjectTypeType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGMLObjectTypeType_Keywords(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Keywords",
             "namespace", "http://www.opengis.net/ows"
           });		
        addAnnotation
          (getGMLObjectTypeType_OutputFormats(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputFormats",
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
          (getGetFeatureType_Query(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Query",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetFeatureType_MaxFeatures(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "maxFeatures"
           });			
        addAnnotation
          (getGetFeatureType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });			
        addAnnotation
          (getGetFeatureType_ResultType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resultType"
           });			
        addAnnotation
          (getGetFeatureType_TraverseXlinkDepth(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "traverseXlinkDepth"
           });			
        addAnnotation
          (getGetFeatureType_TraverseXlinkExpiry(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "traverseXlinkExpiry"
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
          (getGmlObjectTypeEClass, 
           source, 
           new String[] {
             "name", "GetGmlObjectType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetGmlObjectType_GmlObjectId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GmlObjectId",
             "namespace", "http://www.opengis.net/ogc"
           });		
        addAnnotation
          (getGetGmlObjectType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });		
        addAnnotation
          (getGetGmlObjectType_TraverseXlinkDepth(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "traverseXlinkDepth"
           });			
        addAnnotation
          (getGetGmlObjectType_TraverseXlinkExpiry(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "traverseXlinkExpiry"
           });			
        addAnnotation
          (insertElementTypeEClass, 
           source, 
           new String[] {
             "name", "InsertElementType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInsertElementType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });			
        addAnnotation
          (getInsertElementType_Idgen(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "idgen"
           });			
        addAnnotation
          (getInsertElementType_InputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "inputFormat"
           });				
        addAnnotation
          (insertResultsTypeEClass, 
           source, 
           new String[] {
             "name", "InsertResultsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInsertResultsType_Feature(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Feature",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (insertedFeatureTypeEClass, 
           source, 
           new String[] {
             "name", "InsertedFeatureType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInsertedFeatureType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });			
        addAnnotation
          (lockFeatureResponseTypeEClass, 
           source, 
           new String[] {
             "name", "LockFeatureResponseType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getLockFeatureResponseType_LockId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LockId",
             "namespace", "##targetNamespace"
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
          (lockFeatureTypeEClass, 
           source, 
           new String[] {
             "name", "LockFeatureType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getLockFeatureType_Lock(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Lock",
             "namespace", "##targetNamespace"
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
          (lockTypeEClass, 
           source, 
           new String[] {
             "name", "LockType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getLockType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });				
        addAnnotation
          (metadataURLTypeEClass, 
           source, 
           new String[] {
             "name", "MetadataURLType",
             "kind", "simple"
           });			
        addAnnotation
          (getMetadataURLType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getMetadataURLType_Format(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "format"
           });		
        addAnnotation
          (getMetadataURLType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type"
           });		
        addAnnotation
          (nativeTypeEClass, 
           source, 
           new String[] {
             "name", "NativeType",
             "kind", "empty"
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
          (noSRSTypeEClass, 
           source, 
           new String[] {
             "name", "NoSRS_._type",
             "kind", "empty"
           });		
        addAnnotation
          (operationsTypeEClass, 
           source, 
           new String[] {
             "name", "OperationsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getOperationsType_Operation(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operation",
             "namespace", "##targetNamespace"
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
          (propertyTypeEClass, 
           source, 
           new String[] {
             "name", "PropertyType",
             "kind", "elementOnly"
           });				
        addAnnotation
          (queryTypeEClass, 
           source, 
           new String[] {
             "name", "QueryType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getQueryType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });			
        addAnnotation
          (getQueryType_XlinkPropertyName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "XlinkPropertyName",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });					
        addAnnotation
          (getQueryType_FeatureVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "featureVersion"
           });			
        addAnnotation
          (getQueryType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });				
        addAnnotation
          (getQueryType_TypeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeName"
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
          (getTransactionResponseType_TransactionResults(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TransactionResults",
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
          (getTransactionResponseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });			
        addAnnotation
          (transactionResultsTypeEClass, 
           source, 
           new String[] {
             "name", "TransactionResultsType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getTransactionResultsType_Action(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Action",
             "namespace", "##targetNamespace"
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
          (transactionTypeEClass, 
           source, 
           new String[] {
             "name", "TransactionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getTransactionType_LockId(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LockId",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getTransactionType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:4"
           });		
        addAnnotation
          (getTransactionType_Insert(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Insert",
             "namespace", "##targetNamespace",
             "group", "#group:4"
           });		
        addAnnotation
          (getTransactionType_Update(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Update",
             "namespace", "##targetNamespace",
             "group", "#group:4"
           });		
        addAnnotation
          (getTransactionType_Delete(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Delete",
             "namespace", "##targetNamespace",
             "group", "#group:4"
           });		
        addAnnotation
          (getTransactionType_Native(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Native",
             "namespace", "##targetNamespace",
             "group", "#group:4"
           });		
        addAnnotation
          (getTransactionType_ReleaseAction(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "releaseAction"
           });			
        addAnnotation
          (updateElementTypeEClass, 
           source, 
           new String[] {
             "name", "UpdateElementType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getUpdateElementType_Property(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Property",
             "namespace", "##targetNamespace"
           });				
        addAnnotation
          (getUpdateElementType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });			
        addAnnotation
          (getUpdateElementType_InputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "inputFormat"
           });					
        addAnnotation
          (wfsCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "WFS_CapabilitiesType",
             "kind", "elementOnly"
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
          (getWFSCapabilitiesType_ServesGMLObjectTypeList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServesGMLObjectTypeList",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getWFSCapabilitiesType_SupportsGMLObjectTypeList(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportsGMLObjectTypeList",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getWFSCapabilitiesType_FilterCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter_Capabilities",
             "namespace", "http://www.opengis.net/ogc"
           });		
        addAnnotation
          (xlinkPropertyNameTypeEClass, 
           source, 
           new String[] {
             "name", "XlinkPropertyName_._type",
             "kind", "simple"
           });		
        addAnnotation
          (getXlinkPropertyNameType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (getXlinkPropertyNameType_TraverseXlinkDepth(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "traverseXlinkDepth"
           });			
        addAnnotation
          (getXlinkPropertyNameType_TraverseXlinkExpiry(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "traverseXlinkExpiry"
           });						
    }

} //WfsPackageImpl
