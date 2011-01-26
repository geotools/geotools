/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import net.opengis.wfs.WfsPackage;

import net.opengis.wfs.impl.WfsPackageImpl;

import net.opengis.wfsv.AbstractVersionedFeatureType;
import net.opengis.wfsv.DescribeVersionedFeatureTypeType;
import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.DocumentRoot;
import net.opengis.wfsv.GetDiffType;
import net.opengis.wfsv.GetLogType;
import net.opengis.wfsv.GetVersionedFeatureType;
import net.opengis.wfsv.RollbackType;
import net.opengis.wfsv.VersionedDeleteElementType;
import net.opengis.wfsv.VersionedFeatureCollectionType;
import net.opengis.wfsv.VersionedUpdateElementType;
import net.opengis.wfsv.WfsvFactory;
import net.opengis.wfsv.WfsvPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WfsvPackageImpl extends EPackageImpl implements WfsvPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractVersionedFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass describeVersionedFeatureTypeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass differenceQueryTypeEClass = null;

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
    private EClass getDiffTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getLogTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getVersionedFeatureTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rollbackTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass versionedDeleteElementTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass versionedFeatureCollectionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass versionedUpdateElementTypeEClass = null;

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
     * @see net.opengis.wfsv.WfsvPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private WfsvPackageImpl() {
        super(eNS_URI, WfsvFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this
     * model, and for any others upon which it depends.  Simple
     * dependencies are satisfied by calling this method on all
     * dependent packages before doing anything else.  This method drives
     * initialization for interdependent packages directly, in parallel
     * with this package, itself.
     * <p>Of this package and its interdependencies, all packages which
     * have not yet been registered by their URI values are first created
     * and registered.  The packages are then initialized in two steps:
     * meta-model objects for all of the packages are created before any
     * are initialized, since one package's meta-model objects may refer to
     * those of another.
     * <p>Invocation of this method will not affect any packages that have
     * already been initialized.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static WfsvPackage init() {
        if (isInited) return (WfsvPackage)EPackage.Registry.INSTANCE.getEPackage(WfsvPackage.eNS_URI);

        // Obtain or create and register package
        WfsvPackageImpl theWfsvPackage = (WfsvPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof WfsvPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new WfsvPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        WfsPackageImpl theWfsPackage = (WfsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(WfsPackage.eNS_URI) instanceof WfsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WfsPackage.eNS_URI) : WfsPackage.eINSTANCE);

        // Create package meta-data objects
        theWfsvPackage.createPackageContents();
        theWfsPackage.createPackageContents();

        // Initialize created meta-data
        theWfsvPackage.initializePackageContents();
        theWfsPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theWfsvPackage.freeze();

        return theWfsvPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractVersionedFeatureType() {
        return abstractVersionedFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractVersionedFeatureType_Version() {
        return (EAttribute)abstractVersionedFeatureTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractVersionedFeatureType_Author() {
        return (EAttribute)abstractVersionedFeatureTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractVersionedFeatureType_Date() {
        return (EAttribute)abstractVersionedFeatureTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractVersionedFeatureType_Message() {
        return (EAttribute)abstractVersionedFeatureTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeVersionedFeatureTypeType() {
        return describeVersionedFeatureTypeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeVersionedFeatureTypeType_Versioned() {
        return (EAttribute)describeVersionedFeatureTypeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDifferenceQueryType() {
        return differenceQueryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDifferenceQueryType_Filter() {
        return (EAttribute)differenceQueryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDifferenceQueryType_FromFeatureVersion() {
        return (EAttribute)differenceQueryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDifferenceQueryType_SrsName() {
        return (EAttribute)differenceQueryTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDifferenceQueryType_ToFeatureVersion() {
        return (EAttribute)differenceQueryTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDifferenceQueryType_TypeName() {
        return (EAttribute)differenceQueryTypeEClass.getEStructuralFeatures().get(4);
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
    public EAttribute getDocumentRoot_DescribeVersionedFeatureType() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DifferenceQuery() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetDiff() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetLog() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetVersionedFeature() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Rollback() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VersionedDelete() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VersionedFeatureCollection() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_VersionedUpdate() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetDiffType() {
        return getDiffTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetDiffType_DifferenceQuery() {
        return (EReference)getDiffTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetDiffType_OutputFormat() {
        return (EAttribute)getDiffTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetLogType() {
        return getLogTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetLogType_DifferenceQuery() {
        return (EReference)getLogTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetLogType_MaxFeatures() {
        return (EAttribute)getLogTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetLogType_OutputFormat() {
        return (EAttribute)getLogTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetLogType_ResultType() {
        return (EAttribute)getLogTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetVersionedFeatureType() {
        return getVersionedFeatureTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRollbackType() {
        return rollbackTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRollbackType_Filter() {
        return (EAttribute)rollbackTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRollbackType_Handle() {
        return (EAttribute)rollbackTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRollbackType_ToFeatureVersion() {
        return (EAttribute)rollbackTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRollbackType_TypeName() {
        return (EAttribute)rollbackTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRollbackType_User() {
        return (EAttribute)rollbackTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVersionedDeleteElementType() {
        return versionedDeleteElementTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVersionedDeleteElementType_FeatureVersion() {
        return (EAttribute)versionedDeleteElementTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVersionedFeatureCollectionType() {
        return versionedFeatureCollectionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVersionedFeatureCollectionType_Version() {
        return (EAttribute)versionedFeatureCollectionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getVersionedUpdateElementType() {
        return versionedUpdateElementTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getVersionedUpdateElementType_FeatureVersion() {
        return (EAttribute)versionedUpdateElementTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WfsvFactory getWfsvFactory() {
        return (WfsvFactory)getEFactoryInstance();
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
        abstractVersionedFeatureTypeEClass = createEClass(ABSTRACT_VERSIONED_FEATURE_TYPE);
        createEAttribute(abstractVersionedFeatureTypeEClass, ABSTRACT_VERSIONED_FEATURE_TYPE__VERSION);
        createEAttribute(abstractVersionedFeatureTypeEClass, ABSTRACT_VERSIONED_FEATURE_TYPE__AUTHOR);
        createEAttribute(abstractVersionedFeatureTypeEClass, ABSTRACT_VERSIONED_FEATURE_TYPE__DATE);
        createEAttribute(abstractVersionedFeatureTypeEClass, ABSTRACT_VERSIONED_FEATURE_TYPE__MESSAGE);

        describeVersionedFeatureTypeTypeEClass = createEClass(DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE);
        createEAttribute(describeVersionedFeatureTypeTypeEClass, DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE__VERSIONED);

        differenceQueryTypeEClass = createEClass(DIFFERENCE_QUERY_TYPE);
        createEAttribute(differenceQueryTypeEClass, DIFFERENCE_QUERY_TYPE__FILTER);
        createEAttribute(differenceQueryTypeEClass, DIFFERENCE_QUERY_TYPE__FROM_FEATURE_VERSION);
        createEAttribute(differenceQueryTypeEClass, DIFFERENCE_QUERY_TYPE__SRS_NAME);
        createEAttribute(differenceQueryTypeEClass, DIFFERENCE_QUERY_TYPE__TO_FEATURE_VERSION);
        createEAttribute(differenceQueryTypeEClass, DIFFERENCE_QUERY_TYPE__TYPE_NAME);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_VERSIONED_FEATURE_TYPE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DIFFERENCE_QUERY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_DIFF);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_LOG);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_VERSIONED_FEATURE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ROLLBACK);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VERSIONED_DELETE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VERSIONED_FEATURE_COLLECTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VERSIONED_UPDATE);

        getDiffTypeEClass = createEClass(GET_DIFF_TYPE);
        createEReference(getDiffTypeEClass, GET_DIFF_TYPE__DIFFERENCE_QUERY);
        createEAttribute(getDiffTypeEClass, GET_DIFF_TYPE__OUTPUT_FORMAT);

        getLogTypeEClass = createEClass(GET_LOG_TYPE);
        createEReference(getLogTypeEClass, GET_LOG_TYPE__DIFFERENCE_QUERY);
        createEAttribute(getLogTypeEClass, GET_LOG_TYPE__MAX_FEATURES);
        createEAttribute(getLogTypeEClass, GET_LOG_TYPE__OUTPUT_FORMAT);
        createEAttribute(getLogTypeEClass, GET_LOG_TYPE__RESULT_TYPE);

        getVersionedFeatureTypeEClass = createEClass(GET_VERSIONED_FEATURE_TYPE);

        rollbackTypeEClass = createEClass(ROLLBACK_TYPE);
        createEAttribute(rollbackTypeEClass, ROLLBACK_TYPE__FILTER);
        createEAttribute(rollbackTypeEClass, ROLLBACK_TYPE__HANDLE);
        createEAttribute(rollbackTypeEClass, ROLLBACK_TYPE__TO_FEATURE_VERSION);
        createEAttribute(rollbackTypeEClass, ROLLBACK_TYPE__TYPE_NAME);
        createEAttribute(rollbackTypeEClass, ROLLBACK_TYPE__USER);

        versionedDeleteElementTypeEClass = createEClass(VERSIONED_DELETE_ELEMENT_TYPE);
        createEAttribute(versionedDeleteElementTypeEClass, VERSIONED_DELETE_ELEMENT_TYPE__FEATURE_VERSION);

        versionedFeatureCollectionTypeEClass = createEClass(VERSIONED_FEATURE_COLLECTION_TYPE);
        createEAttribute(versionedFeatureCollectionTypeEClass, VERSIONED_FEATURE_COLLECTION_TYPE__VERSION);

        versionedUpdateElementTypeEClass = createEClass(VERSIONED_UPDATE_ELEMENT_TYPE);
        createEAttribute(versionedUpdateElementTypeEClass, VERSIONED_UPDATE_ELEMENT_TYPE__FEATURE_VERSION);
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
        WfsPackage theWfsPackage = (WfsPackage)EPackage.Registry.INSTANCE.getEPackage(WfsPackage.eNS_URI);

        // Add supertypes to classes
        describeVersionedFeatureTypeTypeEClass.getESuperTypes().add(theWfsPackage.getDescribeFeatureTypeType());
        getDiffTypeEClass.getESuperTypes().add(theWfsPackage.getBaseRequestType());
        getLogTypeEClass.getESuperTypes().add(theWfsPackage.getBaseRequestType());
        getVersionedFeatureTypeEClass.getESuperTypes().add(theWfsPackage.getGetFeatureType());
        rollbackTypeEClass.getESuperTypes().add(theWfsPackage.getNativeType());
        versionedDeleteElementTypeEClass.getESuperTypes().add(theWfsPackage.getDeleteElementType());
        versionedFeatureCollectionTypeEClass.getESuperTypes().add(theWfsPackage.getFeatureCollectionType());
        versionedUpdateElementTypeEClass.getESuperTypes().add(theWfsPackage.getUpdateElementType());

        // Initialize classes and features; add operations and parameters
        initEClass(abstractVersionedFeatureTypeEClass, AbstractVersionedFeatureType.class, "AbstractVersionedFeatureType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractVersionedFeatureType_Version(), theXMLTypePackage.getString(), "version", null, 1, 1, AbstractVersionedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractVersionedFeatureType_Author(), theXMLTypePackage.getString(), "author", null, 0, 1, AbstractVersionedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractVersionedFeatureType_Date(), theXMLTypePackage.getDateTime(), "date", null, 1, 1, AbstractVersionedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractVersionedFeatureType_Message(), theXMLTypePackage.getString(), "message", null, 0, 1, AbstractVersionedFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeVersionedFeatureTypeTypeEClass, DescribeVersionedFeatureTypeType.class, "DescribeVersionedFeatureTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescribeVersionedFeatureTypeType_Versioned(), theXMLTypePackage.getBoolean(), "versioned", "true", 0, 1, DescribeVersionedFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(differenceQueryTypeEClass, DifferenceQueryType.class, "DifferenceQueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDifferenceQueryType_Filter(), theXMLTypePackage.getAnySimpleType(), "filter", null, 0, 1, DifferenceQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDifferenceQueryType_FromFeatureVersion(), theXMLTypePackage.getString(), "fromFeatureVersion", "FIRST", 0, 1, DifferenceQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDifferenceQueryType_SrsName(), theXMLTypePackage.getAnyURI(), "srsName", null, 0, 1, DifferenceQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDifferenceQueryType_ToFeatureVersion(), theXMLTypePackage.getString(), "toFeatureVersion", "LAST", 0, 1, DifferenceQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDifferenceQueryType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 1, 1, DifferenceQueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_DescribeVersionedFeatureType(), theXMLTypePackage.getAnySimpleType(), "describeVersionedFeatureType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DifferenceQuery(), this.getDifferenceQueryType(), null, "differenceQuery", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetDiff(), this.getGetDiffType(), null, "getDiff", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetLog(), this.getGetLogType(), null, "getLog", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetVersionedFeature(), this.getGetVersionedFeatureType(), null, "getVersionedFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Rollback(), this.getRollbackType(), null, "rollback", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_VersionedDelete(), this.getVersionedDeleteElementType(), null, "versionedDelete", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_VersionedFeatureCollection(), this.getVersionedFeatureCollectionType(), null, "versionedFeatureCollection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_VersionedUpdate(), this.getVersionedUpdateElementType(), null, "versionedUpdate", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(getDiffTypeEClass, GetDiffType.class, "GetDiffType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetDiffType_DifferenceQuery(), this.getDifferenceQueryType(), null, "differenceQuery", null, 1, -1, GetDiffType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetDiffType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "text/xml; subtype=wfs-transaction/1.1.0", 0, 1, GetDiffType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getLogTypeEClass, GetLogType.class, "GetLogType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetLogType_DifferenceQuery(), this.getDifferenceQueryType(), null, "differenceQuery", null, 1, -1, GetLogType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetLogType_MaxFeatures(), theXMLTypePackage.getPositiveInteger(), "maxFeatures", null, 0, 1, GetLogType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetLogType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "text/xml; subtype=gml/3.1.1", 0, 1, GetLogType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetLogType_ResultType(), theWfsPackage.getResultTypeType(), "resultType", "results", 0, 1, GetLogType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getVersionedFeatureTypeEClass, GetVersionedFeatureType.class, "GetVersionedFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(rollbackTypeEClass, RollbackType.class, "RollbackType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRollbackType_Filter(), theXMLTypePackage.getAnySimpleType(), "filter", null, 0, 1, RollbackType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRollbackType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, RollbackType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRollbackType_ToFeatureVersion(), theXMLTypePackage.getString(), "toFeatureVersion", "FIRST", 0, 1, RollbackType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRollbackType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 1, 1, RollbackType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRollbackType_User(), theXMLTypePackage.getString(), "user", "", 0, 1, RollbackType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(versionedDeleteElementTypeEClass, VersionedDeleteElementType.class, "VersionedDeleteElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getVersionedDeleteElementType_FeatureVersion(), theXMLTypePackage.getString(), "featureVersion", null, 1, 1, VersionedDeleteElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(versionedFeatureCollectionTypeEClass, VersionedFeatureCollectionType.class, "VersionedFeatureCollectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getVersionedFeatureCollectionType_Version(), theXMLTypePackage.getString(), "version", null, 1, 1, VersionedFeatureCollectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(versionedUpdateElementTypeEClass, VersionedUpdateElementType.class, "VersionedUpdateElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getVersionedUpdateElementType_FeatureVersion(), theXMLTypePackage.getString(), "featureVersion", null, 1, 1, VersionedUpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
          (abstractVersionedFeatureTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractVersionedFeatureType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAbstractVersionedFeatureType_Version(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "version",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAbstractVersionedFeatureType_Author(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "author",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAbstractVersionedFeatureType_Date(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "date",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getAbstractVersionedFeatureType_Message(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "message",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (describeVersionedFeatureTypeTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeVersionedFeatureTypeType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDescribeVersionedFeatureTypeType_Versioned(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "versioned"
           });		
        addAnnotation
          (differenceQueryTypeEClass, 
           source, 
           new String[] {
             "name", "DifferenceQueryType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDifferenceQueryType_Filter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter",
             "namespace", "http://www.opengis.net/ogc"
           });			
        addAnnotation
          (getDifferenceQueryType_FromFeatureVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fromFeatureVersion"
           });			
        addAnnotation
          (getDifferenceQueryType_SrsName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "srsName"
           });			
        addAnnotation
          (getDifferenceQueryType_ToFeatureVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "toFeatureVersion"
           });			
        addAnnotation
          (getDifferenceQueryType_TypeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeName"
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
          (getDocumentRoot_DescribeVersionedFeatureType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DescribeVersionedFeatureType",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/wfs#DescribeFeatureType"
           });			
        addAnnotation
          (getDocumentRoot_DifferenceQuery(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DifferenceQuery",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GetDiff(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetDiff",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GetLog(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetLog",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GetVersionedFeature(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetVersionedFeature",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/wfs#GetFeature"
           });			
        addAnnotation
          (getDocumentRoot_Rollback(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Rollback",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/wfs#Native"
           });		
        addAnnotation
          (getDocumentRoot_VersionedDelete(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "VersionedDelete",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/wfs#Delete"
           });		
        addAnnotation
          (getDocumentRoot_VersionedFeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "VersionedFeatureCollection",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/wfs#FeatureCollection"
           });		
        addAnnotation
          (getDocumentRoot_VersionedUpdate(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "VersionedUpdate",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/wfs#Update"
           });			
        addAnnotation
          (getDiffTypeEClass, 
           source, 
           new String[] {
             "name", "GetDiffType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetDiffType_DifferenceQuery(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DifferenceQuery",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetDiffType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });			
        addAnnotation
          (getLogTypeEClass, 
           source, 
           new String[] {
             "name", "GetLogType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetLogType_DifferenceQuery(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DifferenceQuery",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetLogType_MaxFeatures(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "maxFeatures"
           });			
        addAnnotation
          (getGetLogType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "outputFormat"
           });			
        addAnnotation
          (getGetLogType_ResultType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resultType"
           });			
        addAnnotation
          (getVersionedFeatureTypeEClass, 
           source, 
           new String[] {
             "name", "GetVersionedFeatureType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (rollbackTypeEClass, 
           source, 
           new String[] {
             "name", "RollbackType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRollbackType_Filter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Filter",
             "namespace", "http://www.opengis.net/ogc"
           });			
        addAnnotation
          (getRollbackType_Handle(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "handle"
           });			
        addAnnotation
          (getRollbackType_ToFeatureVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "toFeatureVersion"
           });			
        addAnnotation
          (getRollbackType_TypeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "typeName"
           });			
        addAnnotation
          (getRollbackType_User(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "user"
           });		
        addAnnotation
          (versionedDeleteElementTypeEClass, 
           source, 
           new String[] {
             "name", "VersionedDeleteElementType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getVersionedDeleteElementType_FeatureVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "featureVersion"
           });			
        addAnnotation
          (versionedFeatureCollectionTypeEClass, 
           source, 
           new String[] {
             "name", "VersionedFeatureCollectionType",
             "kind", "empty"
           });		
        addAnnotation
          (getVersionedFeatureCollectionType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });		
        addAnnotation
          (versionedUpdateElementTypeEClass, 
           source, 
           new String[] {
             "name", "VersionedUpdateElementType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getVersionedUpdateElementType_FeatureVersion(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "featureVersion"
           });
    }

} //WfsvPackageImpl
