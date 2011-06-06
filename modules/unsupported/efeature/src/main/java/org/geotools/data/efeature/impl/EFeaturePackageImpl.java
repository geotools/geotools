/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.impl;

import java.io.IOException;
import java.net.URL;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.geotools.data.efeature.EFeatureFactory;
import org.geotools.data.efeature.EFeaturePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class EFeaturePackageImpl extends EPackageImpl implements EFeaturePackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected String packageFilename = "efeature.ecore";

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass eFeatureEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType featureEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType propertyEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType attributeEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType geometryAttributeEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType geometryEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eStructureInfoEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eFeatureInfoEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eFeatureAttributeInfoEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eFeatureGeometryInfoEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eStructuralFeatureEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType listEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eFeaturePropertyEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eFeatureAttributeEDataType = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EDataType eFeatureGeometryEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.geotools.data.efeature.EFeaturePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private EFeaturePackageImpl() {
        super(eNS_URI, EFeatureFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link EFeaturePackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #eNS_URI
     * @generated
     */
    public static EFeaturePackage init() {
        if (isInited) return (EFeaturePackage)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI);

        // Obtain or create and register package
        EFeaturePackageImpl theEFeaturePackage = (EFeaturePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EFeaturePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EFeaturePackageImpl());

        isInited = true;

        // Load packages
        theEFeaturePackage.loadPackage();

        // Fix loaded packages
        theEFeaturePackage.fixPackageContents();

        // Mark meta-data to indicate it can't be changed
        theEFeaturePackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(EFeaturePackage.eNS_URI, theEFeaturePackage);
        return theEFeaturePackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getEFeature() {
        if (eFeatureEClass == null) {
            eFeatureEClass = (EClass)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(0);
        }
        return eFeatureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeature_ID() {
        return (EAttribute)getEFeature().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeature_SRID() {
        return (EAttribute)getEFeature().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeature_Data() {
        return (EAttribute)getEFeature().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeature_Simple() {
        return (EAttribute)getEFeature().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeature_Default() {
        return (EAttribute)getEFeature().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEFeature_Structure() {
        return (EAttribute)getEFeature().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEFeatureProperty() {
        if (eFeaturePropertyEDataType == null) {
            eFeaturePropertyEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(12);
        }
        return eFeaturePropertyEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEFeatureAttribute() {
        if (eFeatureAttributeEDataType == null) {
            eFeatureAttributeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(13);
        }
        return eFeatureAttributeEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEFeatureGeometry() {
        if (eFeatureGeometryEDataType == null) {
            eFeatureGeometryEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(14);
        }
        return eFeatureGeometryEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEStructuralFeature() {
        if (eStructuralFeatureEDataType == null) {
            eStructuralFeatureEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(10);
        }
        return eStructuralFeatureEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFeature() {
        if (featureEDataType == null) {
            featureEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(1);
        }
        return featureEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getProperty() {
        if (propertyEDataType == null) {
            propertyEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(2);
        }
        return propertyEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAttribute() {
        if (attributeEDataType == null) {
            attributeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(3);
        }
        return attributeEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getGeometryAttribute() {
        if (geometryAttributeEDataType == null) {
            geometryAttributeEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(4);
        }
        return geometryAttributeEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getGeometry() {
        if (geometryEDataType == null) {
            geometryEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(5);
        }
        return geometryEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEStructureInfo() {
        if (eStructureInfoEDataType == null) {
            eStructureInfoEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(6);
        }
        return eStructureInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEFeatureInfo() {
        if (eFeatureInfoEDataType == null) {
            eFeatureInfoEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(7);
        }
        return eFeatureInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEFeatureAttributeInfo() {
        if (eFeatureAttributeInfoEDataType == null) {
            eFeatureAttributeInfoEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(8);
        }
        return eFeatureAttributeInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getEFeatureGeometryInfo() {
        if (eFeatureGeometryInfoEDataType == null) {
            eFeatureGeometryInfoEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(9);
        }
        return eFeatureGeometryInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EDataType getList() {
        if (listEDataType == null) {
            listEDataType = (EDataType)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI).getEClassifiers().get(11);
        }
        return listEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EFeatureFactory getEFeatureFactory() {
        return (EFeatureFactory)getEFactoryInstance();
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
            throw new RuntimeException("Missing serialized package: " + packageFilename);
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
            eClassifier.setInstanceClassName("org.geotools.data.efeature." + eClassifier.getName());
            setGeneratedClassName(eClassifier);
        }
    }

} // EFeaturePackageImpl
