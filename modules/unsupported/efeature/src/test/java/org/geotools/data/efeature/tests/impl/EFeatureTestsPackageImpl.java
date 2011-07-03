/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.geotools.data.efeature.EFeaturePackage;

import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.EFeatureTestsFactory;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.NonGeoEObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EFeatureTestsPackageImpl extends EPackageImpl implements EFeatureTestsPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass eFeatureDataEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nonGeoEObjectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass eFeatureCompatibleDataEClass = null;

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
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private EFeatureTestsPackageImpl() {
        super(eNS_URI, EFeatureTestsFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link EFeatureTestsPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static EFeatureTestsPackage init() {
        if (isInited) return (EFeatureTestsPackage)EPackage.Registry.INSTANCE.getEPackage(EFeatureTestsPackage.eNS_URI);

        // Obtain or create and register package
        EFeatureTestsPackageImpl theEFeatureTestsPackage = (EFeatureTestsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EFeatureTestsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EFeatureTestsPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        EFeaturePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theEFeatureTestsPackage.createPackageContents();

        // Initialize created meta-data
        theEFeatureTestsPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theEFeatureTestsPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(EFeatureTestsPackage.eNS_URI, theEFeatureTestsPackage);
        return theEFeatureTestsPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEFeatureData() {
        return eFeatureDataEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeatureData_Attribute() {
        return (EAttribute)eFeatureDataEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeatureData_Geometry() {
        return (EAttribute)eFeatureDataEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNonGeoEObject() {
        return nonGeoEObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getNonGeoEObject_NonGeoAttribute() {
        return (EAttribute)nonGeoEObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEFeatureCompatibleData() {
        return eFeatureCompatibleDataEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeatureCompatibleData_Attribute() {
        return (EAttribute)eFeatureCompatibleDataEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeatureCompatibleData_Geometry() {
        return (EAttribute)eFeatureCompatibleDataEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeatureCompatibleData_SRID() {
        return (EAttribute)eFeatureCompatibleDataEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeatureCompatibleData_Default() {
        return (EAttribute)eFeatureCompatibleDataEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeatureCompatibleData_ID() {
        return (EAttribute)eFeatureCompatibleDataEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EFeatureTestsFactory getEFeatureTestsFactory() {
        return (EFeatureTestsFactory)getEFactoryInstance();
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
        eFeatureDataEClass = createEClass(EFEATURE_DATA);
        createEAttribute(eFeatureDataEClass, EFEATURE_DATA__ATTRIBUTE);
        createEAttribute(eFeatureDataEClass, EFEATURE_DATA__GEOMETRY);

        nonGeoEObjectEClass = createEClass(NON_GEO_EOBJECT);
        createEAttribute(nonGeoEObjectEClass, NON_GEO_EOBJECT__NON_GEO_ATTRIBUTE);

        eFeatureCompatibleDataEClass = createEClass(EFEATURE_COMPATIBLE_DATA);
        createEAttribute(eFeatureCompatibleDataEClass, EFEATURE_COMPATIBLE_DATA__ID);
        createEAttribute(eFeatureCompatibleDataEClass, EFEATURE_COMPATIBLE_DATA__ATTRIBUTE);
        createEAttribute(eFeatureCompatibleDataEClass, EFEATURE_COMPATIBLE_DATA__GEOMETRY);
        createEAttribute(eFeatureCompatibleDataEClass, EFEATURE_COMPATIBLE_DATA__SRID);
        createEAttribute(eFeatureCompatibleDataEClass, EFEATURE_COMPATIBLE_DATA__DEFAULT);
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
        EFeaturePackage theEFeaturePackage = (EFeaturePackage)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI);

        // Create type parameters
        ETypeParameter eFeatureDataEClass_A = addETypeParameter(eFeatureDataEClass, "A"); //$NON-NLS-1$
        ETypeParameter eFeatureDataEClass_G = addETypeParameter(eFeatureDataEClass, "G"); //$NON-NLS-1$
        ETypeParameter eFeatureCompatibleDataEClass_A = addETypeParameter(eFeatureCompatibleDataEClass, "A"); //$NON-NLS-1$
        ETypeParameter eFeatureCompatibleDataEClass_G = addETypeParameter(eFeatureCompatibleDataEClass, "G"); //$NON-NLS-1$

        // Set bounds for type parameters
        EGenericType g1 = createEGenericType(theEFeaturePackage.getGeometry());
        eFeatureDataEClass_G.getEBounds().add(g1);
        g1 = createEGenericType(theEFeaturePackage.getGeometry());
        eFeatureCompatibleDataEClass_G.getEBounds().add(g1);

        // Add supertypes to classes
        eFeatureDataEClass.getESuperTypes().add(theEFeaturePackage.getEFeature());

        // Initialize classes and features; add operations and parameters
        initEClass(eFeatureDataEClass, EFeatureData.class, "EFeatureData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        g1 = createEGenericType(eFeatureDataEClass_A);
        initEAttribute(getEFeatureData_Attribute(), g1, "attribute", null, 1, 1, EFeatureData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        g1 = createEGenericType(eFeatureDataEClass_G);
        initEAttribute(getEFeatureData_Geometry(), g1, "geometry", null, 1, 1, EFeatureData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(nonGeoEObjectEClass, NonGeoEObject.class, "NonGeoEObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getNonGeoEObject_NonGeoAttribute(), ecorePackage.getEInt(), "nonGeoAttribute", null, 0, 1, NonGeoEObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        initEClass(eFeatureCompatibleDataEClass, EFeatureCompatibleData.class, "EFeatureCompatibleData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
        initEAttribute(getEFeatureCompatibleData_ID(), ecorePackage.getEString(), "ID", null, 1, 1, EFeatureCompatibleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        g1 = createEGenericType(eFeatureCompatibleDataEClass_A);
        initEAttribute(getEFeatureCompatibleData_Attribute(), g1, "attribute", null, 1, 1, EFeatureCompatibleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        g1 = createEGenericType(eFeatureCompatibleDataEClass_G);
        initEAttribute(getEFeatureCompatibleData_Geometry(), g1, "geometry", null, 1, 1, EFeatureCompatibleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getEFeatureCompatibleData_SRID(), ecorePackage.getEString(), "SRID", null, 1, 1, EFeatureCompatibleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
        initEAttribute(getEFeatureCompatibleData_Default(), ecorePackage.getEString(), "default", null, 1, 1, EFeatureCompatibleData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

        // Create resource
        createResource(eNS_URI);
    }

} //EFeatureTestsPackageImpl
