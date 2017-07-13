/**
 */
package org.w3._2001.smil20.impl;

import java.math.BigDecimal;

import net.opengis.gml311.Gml311Package;

import net.opengis.gml311.impl.Gml311PackageImpl;

import net.opengis.ows11.Ows11Package;

import net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl;

import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.w3._2001.smil20.AccumulateType;
import org.w3._2001.smil20.AdditiveType;
import org.w3._2001.smil20.AnimateColorPrototype;
import org.w3._2001.smil20.AnimateMotionPrototype;
import org.w3._2001.smil20.AnimatePrototype;
import org.w3._2001.smil20.AttributeTypeType;
import org.w3._2001.smil20.CalcModeType;
import org.w3._2001.smil20.DocumentRoot;
import org.w3._2001.smil20.FillDefaultType;
import org.w3._2001.smil20.FillTimingAttrsType;
import org.w3._2001.smil20.RestartDefaultType;
import org.w3._2001.smil20.RestartTimingType;
import org.w3._2001.smil20.SetPrototype;
import org.w3._2001.smil20.Smil20Factory;
import org.w3._2001.smil20.Smil20Package;
import org.w3._2001.smil20.SyncBehaviorDefaultType;
import org.w3._2001.smil20.SyncBehaviorType;

import org.w3._2001.smil20.language.LanguagePackage;

import org.w3._2001.smil20.language.impl.LanguagePackageImpl;

import org.w3._2001.smil20.util.Smil20Validator;

import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Smil20PackageImpl extends EPackageImpl implements Smil20Package {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass animateColorPrototypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass animateMotionPrototypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass animatePrototypeEClass = null;

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
    private EClass setPrototypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum accumulateTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum additiveTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum attributeTypeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum calcModeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum fillDefaultTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum fillTimingAttrsTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum restartDefaultTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum restartTimingTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum syncBehaviorDefaultTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum syncBehaviorTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType accumulateTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType additiveTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType attributeTypeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType calcModeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType fillDefaultTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType fillTimingAttrsTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType nonNegativeDecimalTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType restartDefaultTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType restartTimingTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType syncBehaviorDefaultTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType syncBehaviorTypeObjectEDataType = null;

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
     * @see org.w3._2001.smil20.Smil20Package#eNS_URI
     * @see #init()
     * @generated
     */
    private Smil20PackageImpl() {
        super(eNS_URI, Smil20Factory.eINSTANCE);
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
     * <p>This method is used to initialize {@link Smil20Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static Smil20Package init() {
        if (isInited) return (Smil20Package)EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI);

        // Obtain or create and register package
        Smil20PackageImpl theSmil20Package = (Smil20PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Smil20PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Smil20PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows11Package.eINSTANCE.eClass();
        XlinkPackage.eINSTANCE.eClass();
        XMLTypePackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Gml311PackageImpl theGml311Package = (Gml311PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI) instanceof Gml311PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI) : Gml311Package.eINSTANCE);
        LanguagePackageImpl theLanguagePackage = (LanguagePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI) instanceof LanguagePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI) : LanguagePackage.eINSTANCE);
        wmtsv_1PackageImpl thewmtsv_1Package = (wmtsv_1PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(wmtsv_1Package.eNS_URI) instanceof wmtsv_1PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(wmtsv_1Package.eNS_URI) : wmtsv_1Package.eINSTANCE);

        // Load packages
        theGml311Package.loadPackage();

        // Create package meta-data objects
        theSmil20Package.createPackageContents();
        theLanguagePackage.createPackageContents();
        thewmtsv_1Package.createPackageContents();

        // Initialize created meta-data
        theSmil20Package.initializePackageContents();
        theLanguagePackage.initializePackageContents();
        thewmtsv_1Package.initializePackageContents();

        // Fix loaded packages
        theGml311Package.fixPackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theSmil20Package, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return Smil20Validator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theSmil20Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(Smil20Package.eNS_URI, theSmil20Package);
        return theSmil20Package;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnimateColorPrototype() {
        return animateColorPrototypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_Accumulate() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_Additive() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_AttributeName() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_AttributeType() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_By() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_From() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_To() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorPrototype_Values() {
        return (EAttribute)animateColorPrototypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnimateMotionPrototype() {
        return animateMotionPrototypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionPrototype_Accumulate() {
        return (EAttribute)animateMotionPrototypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionPrototype_Additive() {
        return (EAttribute)animateMotionPrototypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionPrototype_By() {
        return (EAttribute)animateMotionPrototypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionPrototype_From() {
        return (EAttribute)animateMotionPrototypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionPrototype_Origin() {
        return (EAttribute)animateMotionPrototypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionPrototype_To() {
        return (EAttribute)animateMotionPrototypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionPrototype_Values() {
        return (EAttribute)animateMotionPrototypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnimatePrototype() {
        return animatePrototypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_Accumulate() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_Additive() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_AttributeName() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_AttributeType() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_By() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_From() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_To() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimatePrototype_Values() {
        return (EAttribute)animatePrototypeEClass.getEStructuralFeatures().get(7);
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
    public EReference getDocumentRoot_Animate() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AnimateColor() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AnimateMotion() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Set() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSetPrototype() {
        return setPrototypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetPrototype_AttributeName() {
        return (EAttribute)setPrototypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetPrototype_AttributeType() {
        return (EAttribute)setPrototypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetPrototype_To() {
        return (EAttribute)setPrototypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getAccumulateType() {
        return accumulateTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getAdditiveType() {
        return additiveTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getAttributeTypeType() {
        return attributeTypeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getCalcModeType() {
        return calcModeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFillDefaultType() {
        return fillDefaultTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFillTimingAttrsType() {
        return fillTimingAttrsTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getRestartDefaultType() {
        return restartDefaultTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getRestartTimingType() {
        return restartTimingTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSyncBehaviorDefaultType() {
        return syncBehaviorDefaultTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getSyncBehaviorType() {
        return syncBehaviorTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAccumulateTypeObject() {
        return accumulateTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAdditiveTypeObject() {
        return additiveTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAttributeTypeTypeObject() {
        return attributeTypeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getCalcModeTypeObject() {
        return calcModeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFillDefaultTypeObject() {
        return fillDefaultTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFillTimingAttrsTypeObject() {
        return fillTimingAttrsTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getNonNegativeDecimalType() {
        return nonNegativeDecimalTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getRestartDefaultTypeObject() {
        return restartDefaultTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getRestartTimingTypeObject() {
        return restartTimingTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSyncBehaviorDefaultTypeObject() {
        return syncBehaviorDefaultTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSyncBehaviorTypeObject() {
        return syncBehaviorTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Smil20Factory getSmil20Factory() {
        return (Smil20Factory)getEFactoryInstance();
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
        animateColorPrototypeEClass = createEClass(ANIMATE_COLOR_PROTOTYPE);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__ACCUMULATE);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__ADDITIVE);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_NAME);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_TYPE);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__BY);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__FROM);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__TO);
        createEAttribute(animateColorPrototypeEClass, ANIMATE_COLOR_PROTOTYPE__VALUES);

        animateMotionPrototypeEClass = createEClass(ANIMATE_MOTION_PROTOTYPE);
        createEAttribute(animateMotionPrototypeEClass, ANIMATE_MOTION_PROTOTYPE__ACCUMULATE);
        createEAttribute(animateMotionPrototypeEClass, ANIMATE_MOTION_PROTOTYPE__ADDITIVE);
        createEAttribute(animateMotionPrototypeEClass, ANIMATE_MOTION_PROTOTYPE__BY);
        createEAttribute(animateMotionPrototypeEClass, ANIMATE_MOTION_PROTOTYPE__FROM);
        createEAttribute(animateMotionPrototypeEClass, ANIMATE_MOTION_PROTOTYPE__ORIGIN);
        createEAttribute(animateMotionPrototypeEClass, ANIMATE_MOTION_PROTOTYPE__TO);
        createEAttribute(animateMotionPrototypeEClass, ANIMATE_MOTION_PROTOTYPE__VALUES);

        animatePrototypeEClass = createEClass(ANIMATE_PROTOTYPE);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__ACCUMULATE);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__ADDITIVE);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__ATTRIBUTE_NAME);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__ATTRIBUTE_TYPE);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__BY);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__FROM);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__TO);
        createEAttribute(animatePrototypeEClass, ANIMATE_PROTOTYPE__VALUES);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANIMATE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANIMATE_COLOR);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANIMATE_MOTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SET);

        setPrototypeEClass = createEClass(SET_PROTOTYPE);
        createEAttribute(setPrototypeEClass, SET_PROTOTYPE__ATTRIBUTE_NAME);
        createEAttribute(setPrototypeEClass, SET_PROTOTYPE__ATTRIBUTE_TYPE);
        createEAttribute(setPrototypeEClass, SET_PROTOTYPE__TO);

        // Create enums
        accumulateTypeEEnum = createEEnum(ACCUMULATE_TYPE);
        additiveTypeEEnum = createEEnum(ADDITIVE_TYPE);
        attributeTypeTypeEEnum = createEEnum(ATTRIBUTE_TYPE_TYPE);
        calcModeTypeEEnum = createEEnum(CALC_MODE_TYPE);
        fillDefaultTypeEEnum = createEEnum(FILL_DEFAULT_TYPE);
        fillTimingAttrsTypeEEnum = createEEnum(FILL_TIMING_ATTRS_TYPE);
        restartDefaultTypeEEnum = createEEnum(RESTART_DEFAULT_TYPE);
        restartTimingTypeEEnum = createEEnum(RESTART_TIMING_TYPE);
        syncBehaviorDefaultTypeEEnum = createEEnum(SYNC_BEHAVIOR_DEFAULT_TYPE);
        syncBehaviorTypeEEnum = createEEnum(SYNC_BEHAVIOR_TYPE);

        // Create data types
        accumulateTypeObjectEDataType = createEDataType(ACCUMULATE_TYPE_OBJECT);
        additiveTypeObjectEDataType = createEDataType(ADDITIVE_TYPE_OBJECT);
        attributeTypeTypeObjectEDataType = createEDataType(ATTRIBUTE_TYPE_TYPE_OBJECT);
        calcModeTypeObjectEDataType = createEDataType(CALC_MODE_TYPE_OBJECT);
        fillDefaultTypeObjectEDataType = createEDataType(FILL_DEFAULT_TYPE_OBJECT);
        fillTimingAttrsTypeObjectEDataType = createEDataType(FILL_TIMING_ATTRS_TYPE_OBJECT);
        nonNegativeDecimalTypeEDataType = createEDataType(NON_NEGATIVE_DECIMAL_TYPE);
        restartDefaultTypeObjectEDataType = createEDataType(RESTART_DEFAULT_TYPE_OBJECT);
        restartTimingTypeObjectEDataType = createEDataType(RESTART_TIMING_TYPE_OBJECT);
        syncBehaviorDefaultTypeObjectEDataType = createEDataType(SYNC_BEHAVIOR_DEFAULT_TYPE_OBJECT);
        syncBehaviorTypeObjectEDataType = createEDataType(SYNC_BEHAVIOR_TYPE_OBJECT);
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
        LanguagePackage theLanguagePackage = (LanguagePackage)EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes

        // Initialize classes, features, and operations; add parameters
        initEClass(animateColorPrototypeEClass, AnimateColorPrototype.class, "AnimateColorPrototype", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAnimateColorPrototype_Accumulate(), this.getAccumulateType(), "accumulate", "none", 0, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorPrototype_Additive(), this.getAdditiveType(), "additive", "replace", 0, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorPrototype_AttributeName(), theXMLTypePackage.getString(), "attributeName", null, 1, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorPrototype_AttributeType(), this.getAttributeTypeType(), "attributeType", "auto", 0, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorPrototype_By(), theXMLTypePackage.getString(), "by", null, 0, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorPrototype_From(), theXMLTypePackage.getString(), "from", null, 0, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorPrototype_To(), theXMLTypePackage.getString(), "to", null, 0, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorPrototype_Values(), theXMLTypePackage.getString(), "values", null, 0, 1, AnimateColorPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(animateMotionPrototypeEClass, AnimateMotionPrototype.class, "AnimateMotionPrototype", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAnimateMotionPrototype_Accumulate(), this.getAccumulateType(), "accumulate", "none", 0, 1, AnimateMotionPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionPrototype_Additive(), this.getAdditiveType(), "additive", "replace", 0, 1, AnimateMotionPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionPrototype_By(), theXMLTypePackage.getString(), "by", null, 0, 1, AnimateMotionPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionPrototype_From(), theXMLTypePackage.getString(), "from", null, 0, 1, AnimateMotionPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionPrototype_Origin(), theXMLTypePackage.getString(), "origin", null, 0, 1, AnimateMotionPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionPrototype_To(), theXMLTypePackage.getString(), "to", null, 0, 1, AnimateMotionPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionPrototype_Values(), theXMLTypePackage.getString(), "values", null, 0, 1, AnimateMotionPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(animatePrototypeEClass, AnimatePrototype.class, "AnimatePrototype", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAnimatePrototype_Accumulate(), this.getAccumulateType(), "accumulate", "none", 0, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimatePrototype_Additive(), this.getAdditiveType(), "additive", "replace", 0, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimatePrototype_AttributeName(), theXMLTypePackage.getString(), "attributeName", null, 1, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimatePrototype_AttributeType(), this.getAttributeTypeType(), "attributeType", "auto", 0, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimatePrototype_By(), theXMLTypePackage.getString(), "by", null, 0, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimatePrototype_From(), theXMLTypePackage.getString(), "from", null, 0, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimatePrototype_To(), theXMLTypePackage.getString(), "to", null, 0, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimatePrototype_Values(), theXMLTypePackage.getString(), "values", null, 0, 1, AnimatePrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Animate(), theLanguagePackage.getAnimateType(), null, "animate", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AnimateColor(), theLanguagePackage.getAnimateColorType(), null, "animateColor", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AnimateMotion(), theLanguagePackage.getAnimateMotionType(), null, "animateMotion", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Set(), theLanguagePackage.getSetType(), null, "set", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(setPrototypeEClass, SetPrototype.class, "SetPrototype", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSetPrototype_AttributeName(), theXMLTypePackage.getString(), "attributeName", null, 1, 1, SetPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetPrototype_AttributeType(), this.getAttributeTypeType(), "attributeType", "auto", 0, 1, SetPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetPrototype_To(), theXMLTypePackage.getString(), "to", null, 0, 1, SetPrototype.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(accumulateTypeEEnum, AccumulateType.class, "AccumulateType");
        addEEnumLiteral(accumulateTypeEEnum, AccumulateType.NONE);
        addEEnumLiteral(accumulateTypeEEnum, AccumulateType.SUM);

        initEEnum(additiveTypeEEnum, AdditiveType.class, "AdditiveType");
        addEEnumLiteral(additiveTypeEEnum, AdditiveType.REPLACE);
        addEEnumLiteral(additiveTypeEEnum, AdditiveType.SUM);

        initEEnum(attributeTypeTypeEEnum, AttributeTypeType.class, "AttributeTypeType");
        addEEnumLiteral(attributeTypeTypeEEnum, AttributeTypeType.XML);
        addEEnumLiteral(attributeTypeTypeEEnum, AttributeTypeType.CSS);
        addEEnumLiteral(attributeTypeTypeEEnum, AttributeTypeType.AUTO);

        initEEnum(calcModeTypeEEnum, CalcModeType.class, "CalcModeType");
        addEEnumLiteral(calcModeTypeEEnum, CalcModeType.DISCRETE);
        addEEnumLiteral(calcModeTypeEEnum, CalcModeType.LINEAR);
        addEEnumLiteral(calcModeTypeEEnum, CalcModeType.PACED);

        initEEnum(fillDefaultTypeEEnum, FillDefaultType.class, "FillDefaultType");
        addEEnumLiteral(fillDefaultTypeEEnum, FillDefaultType.REMOVE);
        addEEnumLiteral(fillDefaultTypeEEnum, FillDefaultType.FREEZE);
        addEEnumLiteral(fillDefaultTypeEEnum, FillDefaultType.HOLD);
        addEEnumLiteral(fillDefaultTypeEEnum, FillDefaultType.AUTO);
        addEEnumLiteral(fillDefaultTypeEEnum, FillDefaultType.INHERIT);
        addEEnumLiteral(fillDefaultTypeEEnum, FillDefaultType.TRANSITION);

        initEEnum(fillTimingAttrsTypeEEnum, FillTimingAttrsType.class, "FillTimingAttrsType");
        addEEnumLiteral(fillTimingAttrsTypeEEnum, FillTimingAttrsType.REMOVE);
        addEEnumLiteral(fillTimingAttrsTypeEEnum, FillTimingAttrsType.FREEZE);
        addEEnumLiteral(fillTimingAttrsTypeEEnum, FillTimingAttrsType.HOLD);
        addEEnumLiteral(fillTimingAttrsTypeEEnum, FillTimingAttrsType.AUTO);
        addEEnumLiteral(fillTimingAttrsTypeEEnum, FillTimingAttrsType.DEFAULT);
        addEEnumLiteral(fillTimingAttrsTypeEEnum, FillTimingAttrsType.TRANSITION);

        initEEnum(restartDefaultTypeEEnum, RestartDefaultType.class, "RestartDefaultType");
        addEEnumLiteral(restartDefaultTypeEEnum, RestartDefaultType.NEVER);
        addEEnumLiteral(restartDefaultTypeEEnum, RestartDefaultType.ALWAYS);
        addEEnumLiteral(restartDefaultTypeEEnum, RestartDefaultType.WHEN_NOT_ACTIVE);
        addEEnumLiteral(restartDefaultTypeEEnum, RestartDefaultType.INHERIT);

        initEEnum(restartTimingTypeEEnum, RestartTimingType.class, "RestartTimingType");
        addEEnumLiteral(restartTimingTypeEEnum, RestartTimingType.NEVER);
        addEEnumLiteral(restartTimingTypeEEnum, RestartTimingType.ALWAYS);
        addEEnumLiteral(restartTimingTypeEEnum, RestartTimingType.WHEN_NOT_ACTIVE);
        addEEnumLiteral(restartTimingTypeEEnum, RestartTimingType.DEFAULT);

        initEEnum(syncBehaviorDefaultTypeEEnum, SyncBehaviorDefaultType.class, "SyncBehaviorDefaultType");
        addEEnumLiteral(syncBehaviorDefaultTypeEEnum, SyncBehaviorDefaultType.CAN_SLIP);
        addEEnumLiteral(syncBehaviorDefaultTypeEEnum, SyncBehaviorDefaultType.LOCKED);
        addEEnumLiteral(syncBehaviorDefaultTypeEEnum, SyncBehaviorDefaultType.INDEPENDENT);
        addEEnumLiteral(syncBehaviorDefaultTypeEEnum, SyncBehaviorDefaultType.INHERIT);

        initEEnum(syncBehaviorTypeEEnum, SyncBehaviorType.class, "SyncBehaviorType");
        addEEnumLiteral(syncBehaviorTypeEEnum, SyncBehaviorType.CAN_SLIP);
        addEEnumLiteral(syncBehaviorTypeEEnum, SyncBehaviorType.LOCKED);
        addEEnumLiteral(syncBehaviorTypeEEnum, SyncBehaviorType.INDEPENDENT);
        addEEnumLiteral(syncBehaviorTypeEEnum, SyncBehaviorType.DEFAULT);

        // Initialize data types
        initEDataType(accumulateTypeObjectEDataType, AccumulateType.class, "AccumulateTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(additiveTypeObjectEDataType, AdditiveType.class, "AdditiveTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(attributeTypeTypeObjectEDataType, AttributeTypeType.class, "AttributeTypeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(calcModeTypeObjectEDataType, CalcModeType.class, "CalcModeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(fillDefaultTypeObjectEDataType, FillDefaultType.class, "FillDefaultTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(fillTimingAttrsTypeObjectEDataType, FillTimingAttrsType.class, "FillTimingAttrsTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(nonNegativeDecimalTypeEDataType, BigDecimal.class, "NonNegativeDecimalType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(restartDefaultTypeObjectEDataType, RestartDefaultType.class, "RestartDefaultTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(restartTimingTypeObjectEDataType, RestartTimingType.class, "RestartTimingTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(syncBehaviorDefaultTypeObjectEDataType, SyncBehaviorDefaultType.class, "SyncBehaviorDefaultTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(syncBehaviorTypeObjectEDataType, SyncBehaviorType.class, "SyncBehaviorTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

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
          (accumulateTypeEEnum, 
           source, 
           new String[] {
             "name", "accumulate_._type"
           });	
        addAnnotation
          (accumulateTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "accumulate_._type:Object",
             "baseType", "accumulate_._type"
           });	
        addAnnotation
          (additiveTypeEEnum, 
           source, 
           new String[] {
             "name", "additive_._type"
           });	
        addAnnotation
          (additiveTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "additive_._type:Object",
             "baseType", "additive_._type"
           });	
        addAnnotation
          (animateColorPrototypeEClass, 
           source, 
           new String[] {
             "name", "animateColorPrototype",
             "kind", "empty"
           });	
        addAnnotation
          (getAnimateColorPrototype_Accumulate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "accumulate"
           });	
        addAnnotation
          (getAnimateColorPrototype_Additive(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "additive"
           });	
        addAnnotation
          (getAnimateColorPrototype_AttributeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "attributeName"
           });	
        addAnnotation
          (getAnimateColorPrototype_AttributeType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "attributeType"
           });	
        addAnnotation
          (getAnimateColorPrototype_By(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "by"
           });	
        addAnnotation
          (getAnimateColorPrototype_From(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "from"
           });	
        addAnnotation
          (getAnimateColorPrototype_To(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "to"
           });	
        addAnnotation
          (getAnimateColorPrototype_Values(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "values"
           });	
        addAnnotation
          (animateMotionPrototypeEClass, 
           source, 
           new String[] {
             "name", "animateMotionPrototype",
             "kind", "empty"
           });	
        addAnnotation
          (getAnimateMotionPrototype_Accumulate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "accumulate"
           });	
        addAnnotation
          (getAnimateMotionPrototype_Additive(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "additive"
           });	
        addAnnotation
          (getAnimateMotionPrototype_By(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "by"
           });	
        addAnnotation
          (getAnimateMotionPrototype_From(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "from"
           });	
        addAnnotation
          (getAnimateMotionPrototype_Origin(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "origin"
           });	
        addAnnotation
          (getAnimateMotionPrototype_To(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "to"
           });	
        addAnnotation
          (getAnimateMotionPrototype_Values(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "values"
           });	
        addAnnotation
          (animatePrototypeEClass, 
           source, 
           new String[] {
             "name", "animatePrototype",
             "kind", "empty"
           });	
        addAnnotation
          (getAnimatePrototype_Accumulate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "accumulate"
           });	
        addAnnotation
          (getAnimatePrototype_Additive(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "additive"
           });	
        addAnnotation
          (getAnimatePrototype_AttributeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "attributeName"
           });	
        addAnnotation
          (getAnimatePrototype_AttributeType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "attributeType"
           });	
        addAnnotation
          (getAnimatePrototype_By(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "by"
           });	
        addAnnotation
          (getAnimatePrototype_From(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "from"
           });	
        addAnnotation
          (getAnimatePrototype_To(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "to"
           });	
        addAnnotation
          (getAnimatePrototype_Values(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "values"
           });	
        addAnnotation
          (attributeTypeTypeEEnum, 
           source, 
           new String[] {
             "name", "attributeType_._type"
           });	
        addAnnotation
          (attributeTypeTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "attributeType_._type:Object",
             "baseType", "attributeType_._type"
           });	
        addAnnotation
          (calcModeTypeEEnum, 
           source, 
           new String[] {
             "name", "calcMode_._type"
           });	
        addAnnotation
          (calcModeTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "calcMode_._type:Object",
             "baseType", "calcMode_._type"
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
          (getDocumentRoot_Animate(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "animate",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.w3.org/2001/SMIL20/Language#animate"
           });	
        addAnnotation
          (getDocumentRoot_AnimateColor(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "animateColor",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.w3.org/2001/SMIL20/Language#animateColor"
           });	
        addAnnotation
          (getDocumentRoot_AnimateMotion(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "animateMotion",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.w3.org/2001/SMIL20/Language#animateMotion"
           });	
        addAnnotation
          (getDocumentRoot_Set(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "set",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.w3.org/2001/SMIL20/Language#set"
           });	
        addAnnotation
          (fillDefaultTypeEEnum, 
           source, 
           new String[] {
             "name", "fillDefaultType"
           });	
        addAnnotation
          (fillDefaultTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "fillDefaultType:Object",
             "baseType", "fillDefaultType"
           });	
        addAnnotation
          (fillTimingAttrsTypeEEnum, 
           source, 
           new String[] {
             "name", "fillTimingAttrsType"
           });	
        addAnnotation
          (fillTimingAttrsTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "fillTimingAttrsType:Object",
             "baseType", "fillTimingAttrsType"
           });	
        addAnnotation
          (nonNegativeDecimalTypeEDataType, 
           source, 
           new String[] {
             "name", "nonNegativeDecimalType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#decimal",
             "minInclusive", "0.0"
           });	
        addAnnotation
          (restartDefaultTypeEEnum, 
           source, 
           new String[] {
             "name", "restartDefaultType"
           });	
        addAnnotation
          (restartDefaultTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "restartDefaultType:Object",
             "baseType", "restartDefaultType"
           });	
        addAnnotation
          (restartTimingTypeEEnum, 
           source, 
           new String[] {
             "name", "restartTimingType"
           });	
        addAnnotation
          (restartTimingTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "restartTimingType:Object",
             "baseType", "restartTimingType"
           });	
        addAnnotation
          (setPrototypeEClass, 
           source, 
           new String[] {
             "name", "setPrototype",
             "kind", "empty"
           });	
        addAnnotation
          (getSetPrototype_AttributeName(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "attributeName"
           });	
        addAnnotation
          (getSetPrototype_AttributeType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "attributeType"
           });	
        addAnnotation
          (getSetPrototype_To(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "to"
           });	
        addAnnotation
          (syncBehaviorDefaultTypeEEnum, 
           source, 
           new String[] {
             "name", "syncBehaviorDefaultType"
           });	
        addAnnotation
          (syncBehaviorDefaultTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "syncBehaviorDefaultType:Object",
             "baseType", "syncBehaviorDefaultType"
           });	
        addAnnotation
          (syncBehaviorTypeEEnum, 
           source, 
           new String[] {
             "name", "syncBehaviorType"
           });	
        addAnnotation
          (syncBehaviorTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "syncBehaviorType:Object",
             "baseType", "syncBehaviorType"
           });
    }

} //Smil20PackageImpl
