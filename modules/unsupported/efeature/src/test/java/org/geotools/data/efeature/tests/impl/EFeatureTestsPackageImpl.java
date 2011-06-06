/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.impl;

import java.io.IOException;

import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import org.geotools.data.efeature.EFeaturePackage;

import org.geotools.data.efeature.tests.EFeatureTestsFactory;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;

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
    protected String packageFilename = "efeature.ecore"; //$NON-NLS-1$

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
     * @generated
     */
    public static EFeatureTestsPackage init() {
        if (isInited) return (EFeatureTestsPackage)EPackage.Registry.INSTANCE.getEPackage(EFeatureTestsPackage.eNS_URI);

        // Obtain or create and register package
        EFeatureTestsPackageImpl theEFeatureTestsPackage = (EFeatureTestsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EFeatureTestsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EFeatureTestsPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        EFeaturePackage.eINSTANCE.eClass();

        // Load packages
        theEFeatureTestsPackage.loadPackage();

        // Fix loaded packages
        theEFeatureTestsPackage.fixPackageContents();

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
    public EClass getEFeatureData() {
        if (eFeatureDataEClass == null) {
            eFeatureDataEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(EFeatureTestsPackage.eNS_URI).getEClassifiers().get(0);
        }
        return eFeatureDataEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeatureData_Attribute() {
        return (EAttribute)getEFeatureData().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeatureData_Geometry() {
        return (EAttribute)getEFeatureData().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNonGeoEObject() {
        if (nonGeoEObjectEClass == null) {
            nonGeoEObjectEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(EFeatureTestsPackage.eNS_URI).getEClassifiers().get(1);
        }
        return nonGeoEObjectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getNonGeoEObject_NonGeoAttribute() {
        return (EAttribute)getNonGeoEObject().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEFeatureCompatibleData() {
        if (eFeatureCompatibleDataEClass == null) {
            eFeatureCompatibleDataEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(EFeatureTestsPackage.eNS_URI).getEClassifiers().get(2);
        }
        return eFeatureCompatibleDataEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeatureCompatibleData_Attribute() {
        return (EAttribute)getEFeatureCompatibleData().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeatureCompatibleData_Geometry() {
        return (EAttribute)getEFeatureCompatibleData().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeatureCompatibleData_SRID() {
        return (EAttribute)getEFeatureCompatibleData().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeatureCompatibleData_Default() {
        return (EAttribute)getEFeatureCompatibleData().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeatureCompatibleData_ID() {
        return (EAttribute)getEFeatureCompatibleData().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EFeatureTestsFactory getEFeatureTestsFactory() {
        return (EFeatureTestsFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isLoaded = false;

    /**
     * Laods the package and any sub-packages from their serialized form.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void loadPackage() {
        if (isLoaded) return;
        isLoaded = true;

        URL url = getClass().getResource(packageFilename);
        if (url == null) {
            throw new RuntimeException("Missing serialized package: " + packageFilename); //$NON-NLS-1$
        }
        URI uri = URI.createURI(url.toString());
        Resource resource = new EcoreResourceFactoryImpl().createResource(uri);
        try {
            resource.load(null);
        }
        catch (IOException exception) {
            throw new WrappedException(exception);
        }
        initializeFromLoadedEPackage(this, (EPackage)resource.getContents().get(0));
        createResource(eNS_URI);
    }


    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isFixed = false;

    /**
     * Fixes up the loaded package, to make it appear as if it had been programmatically built.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void fixPackageContents() {
        if (isFixed) return;
        isFixed = true;
        fixEClassifiers();
    }

    /**
     * Sets the instance class on the given classifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void fixInstanceClass(EClassifier eClassifier) {
        if (eClassifier.getInstanceClassName() == null) {
            eClassifier.setInstanceClassName("org.geotools.data.efeature.tests." + eClassifier.getName()); //$NON-NLS-1$
            setGeneratedClassName(eClassifier);
        }
    }

} //EFeatureTestsPackageImpl
