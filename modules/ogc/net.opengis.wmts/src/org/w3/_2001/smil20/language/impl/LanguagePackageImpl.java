/**
 */
package org.w3._2001.smil20.language.impl;

import net.opengis.gml311.Gml311Package;

import net.opengis.gml311.impl.Gml311PackageImpl;

import net.opengis.ows11.Ows11Package;

import net.opengis.wmts.v_1.impl.wmtsv_1PackageImpl;

import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.namespace.XMLNamespacePackage;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.w3._2001.smil20.Smil20Package;

import org.w3._2001.smil20.impl.Smil20PackageImpl;

import org.w3._2001.smil20.language.AnimateColorType;
import org.w3._2001.smil20.language.AnimateMotionType;
import org.w3._2001.smil20.language.AnimateType;
import org.w3._2001.smil20.language.DocumentRoot;
import org.w3._2001.smil20.language.LanguageFactory;
import org.w3._2001.smil20.language.LanguagePackage;
import org.w3._2001.smil20.language.SetType;

import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LanguagePackageImpl extends EPackageImpl implements LanguagePackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass animateColorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass animateMotionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass animateTypeEClass = null;

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
    private EClass setTypeEClass = null;

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
     * @see org.w3._2001.smil20.language.LanguagePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private LanguagePackageImpl() {
        super(eNS_URI, LanguageFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link LanguagePackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static LanguagePackage init() {
        if (isInited) return (LanguagePackage)EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI);

        // Obtain or create and register package
        LanguagePackageImpl theLanguagePackage = (LanguagePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LanguagePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new LanguagePackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows11Package.eINSTANCE.eClass();
        XlinkPackage.eINSTANCE.eClass();
        XMLTypePackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Gml311PackageImpl theGml311Package = (Gml311PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI) instanceof Gml311PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI) : Gml311Package.eINSTANCE);
        Smil20PackageImpl theSmil20Package = (Smil20PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI) instanceof Smil20PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI) : Smil20Package.eINSTANCE);
        wmtsv_1PackageImpl thewmtsv_1Package = (wmtsv_1PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(wmtsv_1Package.eNS_URI) instanceof wmtsv_1PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(wmtsv_1Package.eNS_URI) : wmtsv_1Package.eINSTANCE);

        // Load packages
        theGml311Package.loadPackage();

        // Create package meta-data objects
        theLanguagePackage.createPackageContents();
        theSmil20Package.createPackageContents();
        thewmtsv_1Package.createPackageContents();

        // Initialize created meta-data
        theLanguagePackage.initializePackageContents();
        theSmil20Package.initializePackageContents();
        thewmtsv_1Package.initializePackageContents();

        // Fix loaded packages
        theGml311Package.fixPackageContents();

        // Mark meta-data to indicate it can't be changed
        theLanguagePackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(LanguagePackage.eNS_URI, theLanguagePackage);
        return theLanguagePackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnimateColorType() {
        return animateColorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Group() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Any() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Alt() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Begin() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_CalcMode() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Class() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Dur() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_End() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Fill() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_FillDefault() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Id() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Lang() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Longdesc() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Max() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Min() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Repeat() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_RepeatCount() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_RepeatDur() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_Restart() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_RestartDefault() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_SkipContent() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_SyncBehavior() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_SyncBehaviorDefault() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_SyncTolerance() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_SyncToleranceDefault() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_TargetElement() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateColorType_AnyAttribute() {
        return (EAttribute)animateColorTypeEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnimateMotionType() {
        return animateMotionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Group() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Any() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Alt() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Begin() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_CalcMode() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Class() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Dur() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_End() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Fill() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_FillDefault() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Id() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Lang() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Longdesc() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Max() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Min() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Repeat() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_RepeatCount() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_RepeatDur() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_Restart() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_RestartDefault() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_SkipContent() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_SyncBehavior() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_SyncBehaviorDefault() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_SyncTolerance() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_SyncToleranceDefault() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_TargetElement() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateMotionType_AnyAttribute() {
        return (EAttribute)animateMotionTypeEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnimateType() {
        return animateTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Group() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Any() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Alt() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Begin() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_CalcMode() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Class() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Dur() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_End() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Fill() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_FillDefault() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Id() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Lang() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Longdesc() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Max() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Min() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Repeat() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_RepeatCount() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_RepeatDur() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_Restart() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_RestartDefault() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_SkipContent() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_SyncBehavior() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_SyncBehaviorDefault() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_SyncTolerance() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_SyncToleranceDefault() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_TargetElement() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAnimateType_AnyAttribute() {
        return (EAttribute)animateTypeEClass.getEStructuralFeatures().get(26);
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
    public EClass getSetType() {
        return setTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Group() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Any() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Alt() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Begin() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Class() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Dur() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_End() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Fill() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_FillDefault() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Id() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Lang() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Longdesc() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Max() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Min() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Repeat() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_RepeatCount() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_RepeatDur() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_Restart() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_RestartDefault() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_SkipContent() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_SyncBehavior() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_SyncBehaviorDefault() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_SyncTolerance() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_SyncToleranceDefault() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_TargetElement() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSetType_AnyAttribute() {
        return (EAttribute)setTypeEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageFactory getLanguageFactory() {
        return (LanguageFactory)getEFactoryInstance();
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
        animateColorTypeEClass = createEClass(ANIMATE_COLOR_TYPE);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__GROUP);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__ANY);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__ALT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__BEGIN);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__CALC_MODE);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__CLASS);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__DUR);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__END);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__FILL);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__FILL_DEFAULT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__ID);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__LANG);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__LONGDESC);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__MAX);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__MIN);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__REPEAT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__REPEAT_COUNT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__REPEAT_DUR);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__RESTART);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__RESTART_DEFAULT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__SKIP_CONTENT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__SYNC_BEHAVIOR);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__SYNC_BEHAVIOR_DEFAULT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__SYNC_TOLERANCE);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__SYNC_TOLERANCE_DEFAULT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__TARGET_ELEMENT);
        createEAttribute(animateColorTypeEClass, ANIMATE_COLOR_TYPE__ANY_ATTRIBUTE);

        animateMotionTypeEClass = createEClass(ANIMATE_MOTION_TYPE);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__GROUP);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__ANY);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__ALT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__BEGIN);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__CALC_MODE);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__CLASS);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__DUR);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__END);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__FILL);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__FILL_DEFAULT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__ID);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__LANG);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__LONGDESC);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__MAX);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__MIN);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__REPEAT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__REPEAT_COUNT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__REPEAT_DUR);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__RESTART);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__RESTART_DEFAULT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__SKIP_CONTENT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__SYNC_BEHAVIOR);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__SYNC_BEHAVIOR_DEFAULT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__SYNC_TOLERANCE);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__SYNC_TOLERANCE_DEFAULT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__TARGET_ELEMENT);
        createEAttribute(animateMotionTypeEClass, ANIMATE_MOTION_TYPE__ANY_ATTRIBUTE);

        animateTypeEClass = createEClass(ANIMATE_TYPE);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__GROUP);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__ANY);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__ALT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__BEGIN);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__CALC_MODE);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__CLASS);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__DUR);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__END);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__FILL);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__FILL_DEFAULT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__ID);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__LANG);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__LONGDESC);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__MAX);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__MIN);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__REPEAT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__REPEAT_COUNT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__REPEAT_DUR);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__RESTART);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__RESTART_DEFAULT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__SKIP_CONTENT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__SYNC_BEHAVIOR);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__SYNC_BEHAVIOR_DEFAULT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__SYNC_TOLERANCE);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__SYNC_TOLERANCE_DEFAULT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__TARGET_ELEMENT);
        createEAttribute(animateTypeEClass, ANIMATE_TYPE__ANY_ATTRIBUTE);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANIMATE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANIMATE_COLOR);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANIMATE_MOTION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SET);

        setTypeEClass = createEClass(SET_TYPE);
        createEAttribute(setTypeEClass, SET_TYPE__GROUP);
        createEAttribute(setTypeEClass, SET_TYPE__ANY);
        createEAttribute(setTypeEClass, SET_TYPE__ALT);
        createEAttribute(setTypeEClass, SET_TYPE__BEGIN);
        createEAttribute(setTypeEClass, SET_TYPE__CLASS);
        createEAttribute(setTypeEClass, SET_TYPE__DUR);
        createEAttribute(setTypeEClass, SET_TYPE__END);
        createEAttribute(setTypeEClass, SET_TYPE__FILL);
        createEAttribute(setTypeEClass, SET_TYPE__FILL_DEFAULT);
        createEAttribute(setTypeEClass, SET_TYPE__ID);
        createEAttribute(setTypeEClass, SET_TYPE__LANG);
        createEAttribute(setTypeEClass, SET_TYPE__LONGDESC);
        createEAttribute(setTypeEClass, SET_TYPE__MAX);
        createEAttribute(setTypeEClass, SET_TYPE__MIN);
        createEAttribute(setTypeEClass, SET_TYPE__REPEAT);
        createEAttribute(setTypeEClass, SET_TYPE__REPEAT_COUNT);
        createEAttribute(setTypeEClass, SET_TYPE__REPEAT_DUR);
        createEAttribute(setTypeEClass, SET_TYPE__RESTART);
        createEAttribute(setTypeEClass, SET_TYPE__RESTART_DEFAULT);
        createEAttribute(setTypeEClass, SET_TYPE__SKIP_CONTENT);
        createEAttribute(setTypeEClass, SET_TYPE__SYNC_BEHAVIOR);
        createEAttribute(setTypeEClass, SET_TYPE__SYNC_BEHAVIOR_DEFAULT);
        createEAttribute(setTypeEClass, SET_TYPE__SYNC_TOLERANCE);
        createEAttribute(setTypeEClass, SET_TYPE__SYNC_TOLERANCE_DEFAULT);
        createEAttribute(setTypeEClass, SET_TYPE__TARGET_ELEMENT);
        createEAttribute(setTypeEClass, SET_TYPE__ANY_ATTRIBUTE);
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
        Smil20Package theSmil20Package = (Smil20Package)EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI);
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
        XMLNamespacePackage theXMLNamespacePackage = (XMLNamespacePackage)EPackage.Registry.INSTANCE.getEPackage(XMLNamespacePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        animateColorTypeEClass.getESuperTypes().add(theSmil20Package.getAnimateColorPrototype());
        animateMotionTypeEClass.getESuperTypes().add(theSmil20Package.getAnimateMotionPrototype());
        animateTypeEClass.getESuperTypes().add(theSmil20Package.getAnimatePrototype());
        setTypeEClass.getESuperTypes().add(theSmil20Package.getSetPrototype());

        // Initialize classes, features, and operations; add parameters
        initEClass(animateColorTypeEClass, AnimateColorType.class, "AnimateColorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAnimateColorType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Any(), ecorePackage.getEFeatureMapEntry(), "any", null, 0, -1, AnimateColorType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Alt(), theXMLTypePackage.getString(), "alt", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Begin(), theXMLTypePackage.getString(), "begin", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_CalcMode(), theSmil20Package.getCalcModeType(), "calcMode", "linear", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Dur(), theXMLTypePackage.getString(), "dur", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_End(), theXMLTypePackage.getString(), "end", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Fill(), theSmil20Package.getFillTimingAttrsType(), "fill", "default", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_FillDefault(), theSmil20Package.getFillDefaultType(), "fillDefault", "inherit", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Id(), theXMLTypePackage.getID(), "id", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Lang(), theXMLNamespacePackage.getLangType(), "lang", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Longdesc(), theXMLTypePackage.getAnyURI(), "longdesc", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Max(), theXMLTypePackage.getString(), "max", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Min(), theXMLTypePackage.getString(), "min", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Repeat(), theXMLTypePackage.getNonNegativeInteger(), "repeat", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_RepeatCount(), theSmil20Package.getNonNegativeDecimalType(), "repeatCount", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_RepeatDur(), theXMLTypePackage.getString(), "repeatDur", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_Restart(), theSmil20Package.getRestartTimingType(), "restart", "default", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_RestartDefault(), theSmil20Package.getRestartDefaultType(), "restartDefault", "inherit", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_SkipContent(), theXMLTypePackage.getBoolean(), "skipContent", "true", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_SyncBehavior(), theSmil20Package.getSyncBehaviorType(), "syncBehavior", "default", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_SyncBehaviorDefault(), theSmil20Package.getSyncBehaviorDefaultType(), "syncBehaviorDefault", "inherit", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_SyncTolerance(), theXMLTypePackage.getString(), "syncTolerance", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_SyncToleranceDefault(), theXMLTypePackage.getString(), "syncToleranceDefault", "inherit", 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_TargetElement(), theXMLTypePackage.getIDREF(), "targetElement", null, 0, 1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateColorType_AnyAttribute(), ecorePackage.getEFeatureMapEntry(), "anyAttribute", null, 0, -1, AnimateColorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(animateMotionTypeEClass, AnimateMotionType.class, "AnimateMotionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAnimateMotionType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Any(), ecorePackage.getEFeatureMapEntry(), "any", null, 0, -1, AnimateMotionType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Alt(), theXMLTypePackage.getString(), "alt", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Begin(), theXMLTypePackage.getString(), "begin", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_CalcMode(), theSmil20Package.getCalcModeType(), "calcMode", "linear", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Dur(), theXMLTypePackage.getString(), "dur", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_End(), theXMLTypePackage.getString(), "end", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Fill(), theSmil20Package.getFillTimingAttrsType(), "fill", "default", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_FillDefault(), theSmil20Package.getFillDefaultType(), "fillDefault", "inherit", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Id(), theXMLTypePackage.getID(), "id", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Lang(), theXMLNamespacePackage.getLangType(), "lang", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Longdesc(), theXMLTypePackage.getAnyURI(), "longdesc", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Max(), theXMLTypePackage.getString(), "max", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Min(), theXMLTypePackage.getString(), "min", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Repeat(), theXMLTypePackage.getNonNegativeInteger(), "repeat", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_RepeatCount(), theSmil20Package.getNonNegativeDecimalType(), "repeatCount", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_RepeatDur(), theXMLTypePackage.getString(), "repeatDur", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_Restart(), theSmil20Package.getRestartTimingType(), "restart", "default", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_RestartDefault(), theSmil20Package.getRestartDefaultType(), "restartDefault", "inherit", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_SkipContent(), theXMLTypePackage.getBoolean(), "skipContent", "true", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_SyncBehavior(), theSmil20Package.getSyncBehaviorType(), "syncBehavior", "default", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_SyncBehaviorDefault(), theSmil20Package.getSyncBehaviorDefaultType(), "syncBehaviorDefault", "inherit", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_SyncTolerance(), theXMLTypePackage.getString(), "syncTolerance", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_SyncToleranceDefault(), theXMLTypePackage.getString(), "syncToleranceDefault", "inherit", 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_TargetElement(), theXMLTypePackage.getIDREF(), "targetElement", null, 0, 1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateMotionType_AnyAttribute(), ecorePackage.getEFeatureMapEntry(), "anyAttribute", null, 0, -1, AnimateMotionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(animateTypeEClass, AnimateType.class, "AnimateType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAnimateType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Any(), ecorePackage.getEFeatureMapEntry(), "any", null, 0, -1, AnimateType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Alt(), theXMLTypePackage.getString(), "alt", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Begin(), theXMLTypePackage.getString(), "begin", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_CalcMode(), theSmil20Package.getCalcModeType(), "calcMode", "linear", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Dur(), theXMLTypePackage.getString(), "dur", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_End(), theXMLTypePackage.getString(), "end", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Fill(), theSmil20Package.getFillTimingAttrsType(), "fill", "default", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_FillDefault(), theSmil20Package.getFillDefaultType(), "fillDefault", "inherit", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Id(), theXMLTypePackage.getID(), "id", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Lang(), theXMLNamespacePackage.getLangType(), "lang", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Longdesc(), theXMLTypePackage.getAnyURI(), "longdesc", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Max(), theXMLTypePackage.getString(), "max", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Min(), theXMLTypePackage.getString(), "min", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Repeat(), theXMLTypePackage.getNonNegativeInteger(), "repeat", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_RepeatCount(), theSmil20Package.getNonNegativeDecimalType(), "repeatCount", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_RepeatDur(), theXMLTypePackage.getString(), "repeatDur", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_Restart(), theSmil20Package.getRestartTimingType(), "restart", "default", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_RestartDefault(), theSmil20Package.getRestartDefaultType(), "restartDefault", "inherit", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_SkipContent(), theXMLTypePackage.getBoolean(), "skipContent", "true", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_SyncBehavior(), theSmil20Package.getSyncBehaviorType(), "syncBehavior", "default", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_SyncBehaviorDefault(), theSmil20Package.getSyncBehaviorDefaultType(), "syncBehaviorDefault", "inherit", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_SyncTolerance(), theXMLTypePackage.getString(), "syncTolerance", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_SyncToleranceDefault(), theXMLTypePackage.getString(), "syncToleranceDefault", "inherit", 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_TargetElement(), theXMLTypePackage.getIDREF(), "targetElement", null, 0, 1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAnimateType_AnyAttribute(), ecorePackage.getEFeatureMapEntry(), "anyAttribute", null, 0, -1, AnimateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Animate(), this.getAnimateType(), null, "animate", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AnimateColor(), this.getAnimateColorType(), null, "animateColor", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AnimateMotion(), this.getAnimateMotionType(), null, "animateMotion", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Set(), this.getSetType(), null, "set", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(setTypeEClass, SetType.class, "SetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSetType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Any(), ecorePackage.getEFeatureMapEntry(), "any", null, 0, -1, SetType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Alt(), theXMLTypePackage.getString(), "alt", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Begin(), theXMLTypePackage.getString(), "begin", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Dur(), theXMLTypePackage.getString(), "dur", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_End(), theXMLTypePackage.getString(), "end", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Fill(), theSmil20Package.getFillTimingAttrsType(), "fill", "default", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_FillDefault(), theSmil20Package.getFillDefaultType(), "fillDefault", "inherit", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Id(), theXMLTypePackage.getID(), "id", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Lang(), theXMLNamespacePackage.getLangType(), "lang", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Longdesc(), theXMLTypePackage.getAnyURI(), "longdesc", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Max(), theXMLTypePackage.getString(), "max", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Min(), theXMLTypePackage.getString(), "min", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Repeat(), theXMLTypePackage.getNonNegativeInteger(), "repeat", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_RepeatCount(), theSmil20Package.getNonNegativeDecimalType(), "repeatCount", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_RepeatDur(), theXMLTypePackage.getString(), "repeatDur", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_Restart(), theSmil20Package.getRestartTimingType(), "restart", "default", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_RestartDefault(), theSmil20Package.getRestartDefaultType(), "restartDefault", "inherit", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_SkipContent(), theXMLTypePackage.getBoolean(), "skipContent", "true", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_SyncBehavior(), theSmil20Package.getSyncBehaviorType(), "syncBehavior", "default", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_SyncBehaviorDefault(), theSmil20Package.getSyncBehaviorDefaultType(), "syncBehaviorDefault", "inherit", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_SyncTolerance(), theXMLTypePackage.getString(), "syncTolerance", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_SyncToleranceDefault(), theXMLTypePackage.getString(), "syncToleranceDefault", "inherit", 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_TargetElement(), theXMLTypePackage.getIDREF(), "targetElement", null, 0, 1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSetType_AnyAttribute(), ecorePackage.getEFeatureMapEntry(), "anyAttribute", null, 0, -1, SetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
          (animateColorTypeEClass, 
           source, 
           new String[] {
             "name", "animateColorType",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getAnimateColorType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:8"
           });	
        addAnnotation
          (getAnimateColorType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":9",
             "processing", "lax",
             "group", "#group:8"
           });	
        addAnnotation
          (getAnimateColorType_Alt(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "alt"
           });	
        addAnnotation
          (getAnimateColorType_Begin(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "begin"
           });	
        addAnnotation
          (getAnimateColorType_CalcMode(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "calcMode"
           });	
        addAnnotation
          (getAnimateColorType_Class(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "class"
           });	
        addAnnotation
          (getAnimateColorType_Dur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "dur"
           });	
        addAnnotation
          (getAnimateColorType_End(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "end"
           });	
        addAnnotation
          (getAnimateColorType_Fill(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fill"
           });	
        addAnnotation
          (getAnimateColorType_FillDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fillDefault"
           });	
        addAnnotation
          (getAnimateColorType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });	
        addAnnotation
          (getAnimateColorType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });	
        addAnnotation
          (getAnimateColorType_Longdesc(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "longdesc"
           });	
        addAnnotation
          (getAnimateColorType_Max(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "max"
           });	
        addAnnotation
          (getAnimateColorType_Min(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "min"
           });	
        addAnnotation
          (getAnimateColorType_Repeat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeat"
           });	
        addAnnotation
          (getAnimateColorType_RepeatCount(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatCount"
           });	
        addAnnotation
          (getAnimateColorType_RepeatDur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatDur"
           });	
        addAnnotation
          (getAnimateColorType_Restart(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restart"
           });	
        addAnnotation
          (getAnimateColorType_RestartDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restartDefault"
           });	
        addAnnotation
          (getAnimateColorType_SkipContent(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "skip-content"
           });	
        addAnnotation
          (getAnimateColorType_SyncBehavior(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehavior"
           });	
        addAnnotation
          (getAnimateColorType_SyncBehaviorDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehaviorDefault"
           });	
        addAnnotation
          (getAnimateColorType_SyncTolerance(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncTolerance"
           });	
        addAnnotation
          (getAnimateColorType_SyncToleranceDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncToleranceDefault"
           });	
        addAnnotation
          (getAnimateColorType_TargetElement(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "targetElement"
           });	
        addAnnotation
          (getAnimateColorType_AnyAttribute(), 
           source, 
           new String[] {
             "kind", "attributeWildcard",
             "wildcards", "##any",
             "name", ":34",
             "processing", "strict"
           });	
        addAnnotation
          (animateMotionTypeEClass, 
           source, 
           new String[] {
             "name", "animateMotionType",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getAnimateMotionType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:7"
           });	
        addAnnotation
          (getAnimateMotionType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":8",
             "processing", "lax",
             "group", "#group:7"
           });	
        addAnnotation
          (getAnimateMotionType_Alt(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "alt"
           });	
        addAnnotation
          (getAnimateMotionType_Begin(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "begin"
           });	
        addAnnotation
          (getAnimateMotionType_CalcMode(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "calcMode"
           });	
        addAnnotation
          (getAnimateMotionType_Class(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "class"
           });	
        addAnnotation
          (getAnimateMotionType_Dur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "dur"
           });	
        addAnnotation
          (getAnimateMotionType_End(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "end"
           });	
        addAnnotation
          (getAnimateMotionType_Fill(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fill"
           });	
        addAnnotation
          (getAnimateMotionType_FillDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fillDefault"
           });	
        addAnnotation
          (getAnimateMotionType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });	
        addAnnotation
          (getAnimateMotionType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });	
        addAnnotation
          (getAnimateMotionType_Longdesc(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "longdesc"
           });	
        addAnnotation
          (getAnimateMotionType_Max(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "max"
           });	
        addAnnotation
          (getAnimateMotionType_Min(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "min"
           });	
        addAnnotation
          (getAnimateMotionType_Repeat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeat"
           });	
        addAnnotation
          (getAnimateMotionType_RepeatCount(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatCount"
           });	
        addAnnotation
          (getAnimateMotionType_RepeatDur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatDur"
           });	
        addAnnotation
          (getAnimateMotionType_Restart(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restart"
           });	
        addAnnotation
          (getAnimateMotionType_RestartDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restartDefault"
           });	
        addAnnotation
          (getAnimateMotionType_SkipContent(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "skip-content"
           });	
        addAnnotation
          (getAnimateMotionType_SyncBehavior(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehavior"
           });	
        addAnnotation
          (getAnimateMotionType_SyncBehaviorDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehaviorDefault"
           });	
        addAnnotation
          (getAnimateMotionType_SyncTolerance(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncTolerance"
           });	
        addAnnotation
          (getAnimateMotionType_SyncToleranceDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncToleranceDefault"
           });	
        addAnnotation
          (getAnimateMotionType_TargetElement(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "targetElement"
           });	
        addAnnotation
          (getAnimateMotionType_AnyAttribute(), 
           source, 
           new String[] {
             "kind", "attributeWildcard",
             "wildcards", "##any",
             "name", ":33",
             "processing", "strict"
           });	
        addAnnotation
          (animateTypeEClass, 
           source, 
           new String[] {
             "name", "animateType",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getAnimateType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:8"
           });	
        addAnnotation
          (getAnimateType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":9",
             "processing", "lax",
             "group", "#group:8"
           });	
        addAnnotation
          (getAnimateType_Alt(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "alt"
           });	
        addAnnotation
          (getAnimateType_Begin(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "begin"
           });	
        addAnnotation
          (getAnimateType_CalcMode(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "calcMode"
           });	
        addAnnotation
          (getAnimateType_Class(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "class"
           });	
        addAnnotation
          (getAnimateType_Dur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "dur"
           });	
        addAnnotation
          (getAnimateType_End(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "end"
           });	
        addAnnotation
          (getAnimateType_Fill(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fill"
           });	
        addAnnotation
          (getAnimateType_FillDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fillDefault"
           });	
        addAnnotation
          (getAnimateType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });	
        addAnnotation
          (getAnimateType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });	
        addAnnotation
          (getAnimateType_Longdesc(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "longdesc"
           });	
        addAnnotation
          (getAnimateType_Max(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "max"
           });	
        addAnnotation
          (getAnimateType_Min(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "min"
           });	
        addAnnotation
          (getAnimateType_Repeat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeat"
           });	
        addAnnotation
          (getAnimateType_RepeatCount(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatCount"
           });	
        addAnnotation
          (getAnimateType_RepeatDur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatDur"
           });	
        addAnnotation
          (getAnimateType_Restart(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restart"
           });	
        addAnnotation
          (getAnimateType_RestartDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restartDefault"
           });	
        addAnnotation
          (getAnimateType_SkipContent(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "skip-content"
           });	
        addAnnotation
          (getAnimateType_SyncBehavior(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehavior"
           });	
        addAnnotation
          (getAnimateType_SyncBehaviorDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehaviorDefault"
           });	
        addAnnotation
          (getAnimateType_SyncTolerance(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncTolerance"
           });	
        addAnnotation
          (getAnimateType_SyncToleranceDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncToleranceDefault"
           });	
        addAnnotation
          (getAnimateType_TargetElement(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "targetElement"
           });	
        addAnnotation
          (getAnimateType_AnyAttribute(), 
           source, 
           new String[] {
             "kind", "attributeWildcard",
             "wildcards", "##any",
             "name", ":34",
             "processing", "strict"
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
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_AnimateColor(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "animateColor",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_AnimateMotion(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "animateMotion",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_Set(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "set",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (setTypeEClass, 
           source, 
           new String[] {
             "name", "setType",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getSetType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:3"
           });	
        addAnnotation
          (getSetType_Any(), 
           source, 
           new String[] {
             "kind", "elementWildcard",
             "wildcards", "##other",
             "name", ":4",
             "processing", "lax",
             "group", "#group:3"
           });	
        addAnnotation
          (getSetType_Alt(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "alt"
           });	
        addAnnotation
          (getSetType_Begin(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "begin"
           });	
        addAnnotation
          (getSetType_Class(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "class"
           });	
        addAnnotation
          (getSetType_Dur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "dur"
           });	
        addAnnotation
          (getSetType_End(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "end"
           });	
        addAnnotation
          (getSetType_Fill(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fill"
           });	
        addAnnotation
          (getSetType_FillDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "fillDefault"
           });	
        addAnnotation
          (getSetType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id"
           });	
        addAnnotation
          (getSetType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });	
        addAnnotation
          (getSetType_Longdesc(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "longdesc"
           });	
        addAnnotation
          (getSetType_Max(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "max"
           });	
        addAnnotation
          (getSetType_Min(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "min"
           });	
        addAnnotation
          (getSetType_Repeat(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeat"
           });	
        addAnnotation
          (getSetType_RepeatCount(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatCount"
           });	
        addAnnotation
          (getSetType_RepeatDur(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "repeatDur"
           });	
        addAnnotation
          (getSetType_Restart(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restart"
           });	
        addAnnotation
          (getSetType_RestartDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "restartDefault"
           });	
        addAnnotation
          (getSetType_SkipContent(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "skip-content"
           });	
        addAnnotation
          (getSetType_SyncBehavior(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehavior"
           });	
        addAnnotation
          (getSetType_SyncBehaviorDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncBehaviorDefault"
           });	
        addAnnotation
          (getSetType_SyncTolerance(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncTolerance"
           });	
        addAnnotation
          (getSetType_SyncToleranceDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "syncToleranceDefault"
           });	
        addAnnotation
          (getSetType_TargetElement(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "targetElement"
           });	
        addAnnotation
          (getSetType_AnyAttribute(), 
           source, 
           new String[] {
             "kind", "attributeWildcard",
             "wildcards", "##any",
             "name", ":28",
             "processing", "strict"
           });
    }

} //LanguagePackageImpl
