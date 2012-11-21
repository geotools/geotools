/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3.xlink.impl;

import java.lang.Object;
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

import org.w3.xlink.ActuateType;
import org.w3.xlink.ArcType;
import org.w3.xlink.DocumentRoot;
import org.w3.xlink.Extended;
import org.w3.xlink.LocatorType;
import org.w3.xlink.ResourceType;
import org.w3.xlink.ShowType;
import org.w3.xlink.Simple;
import org.w3.xlink.TitleEltType;
import org.w3.xlink.TypeType;
import org.w3.xlink.XlinkFactory;
import org.w3.xlink.XlinkPackage;
import org.w3.xlink.util.XlinkValidator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XlinkPackageImpl extends EPackageImpl implements XlinkPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass arcTypeEClass = null;

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
    private EClass extendedEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass locatorTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass resourceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass simpleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass titleEltTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass objectEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum actuateTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum showTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum typeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType actuateTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType arcroleTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType fromTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType hrefTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType labelTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType roleTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType showTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType titleAttrTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType toTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType typeTypeObjectEDataType = null;

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
     * @see org.w3.xlink.XlinkPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private XlinkPackageImpl() {
        super(eNS_URI, XlinkFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link XlinkPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static XlinkPackage init() {
        if (isInited) return (XlinkPackage)EPackage.Registry.INSTANCE.getEPackage(XlinkPackage.eNS_URI);

        // Obtain or create and register package
        XlinkPackageImpl theXlinkPackage = (XlinkPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof XlinkPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new XlinkPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        EcorePackage.eINSTANCE.eClass();
        XMLNamespacePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theXlinkPackage.createPackageContents();

        // Initialize created meta-data
        theXlinkPackage.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (theXlinkPackage, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return XlinkValidator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        theXlinkPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(XlinkPackage.eNS_URI, theXlinkPackage);
        return theXlinkPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getArcType() {
        return arcTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_TitleGroup() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getArcType_Title() {
        return (EReference)arcTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_Actuate() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_Arcrole() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_From() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_Show() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_Title1() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_To() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getArcType_Type() {
        return (EAttribute)arcTypeEClass.getEStructuralFeatures().get(8);
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
    public EReference getDocumentRoot_Arc() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Locator() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Resource() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Actuate() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Arcrole() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_From() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Href() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Label() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Role() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Show() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Title1() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Title() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_To() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Type() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExtended() {
        return extendedEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtended_Title() {
        return (EReference)extendedEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtended_Resource() {
        return (EReference)extendedEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtended_Locator() {
        return (EReference)extendedEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExtended_Arc() {
        return (EReference)extendedEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExtended_Role() {
        return (EAttribute)extendedEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExtended_Type() {
        return (EAttribute)extendedEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getExtended_TitleAttribute() {
        return (EAttribute)extendedEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLocatorType() {
        return locatorTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocatorType_TitleGroup() {
        return (EAttribute)locatorTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLocatorType_Title() {
        return (EReference)locatorTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocatorType_Href() {
        return (EAttribute)locatorTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocatorType_Label() {
        return (EAttribute)locatorTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocatorType_Role() {
        return (EAttribute)locatorTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocatorType_Title1() {
        return (EAttribute)locatorTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLocatorType_Type() {
        return (EAttribute)locatorTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResourceType() {
        return resourceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceType_Label() {
        return (EAttribute)resourceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceType_Role() {
        return (EAttribute)resourceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceType_Title() {
        return (EAttribute)resourceTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getResourceType_Type() {
        return (EAttribute)resourceTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResourceType_Contents() {
        return (EReference)resourceTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSimple() {
        return simpleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimple_Actuate() {
        return (EAttribute)simpleEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimple_Arcrole() {
        return (EAttribute)simpleEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimple_Href() {
        return (EAttribute)simpleEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimple_Role() {
        return (EAttribute)simpleEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimple_Show() {
        return (EAttribute)simpleEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimple_Title() {
        return (EAttribute)simpleEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSimple_Type() {
        return (EAttribute)simpleEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSimple_Contents() {
        return (EReference)simpleEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTitleEltType() {
        return titleEltTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTitleEltType_Lang() {
        return (EAttribute)titleEltTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTitleEltType_Type() {
        return (EAttribute)titleEltTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTitleEltType_Contents() {
        return (EReference)titleEltTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getObject() {
        return objectEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getActuateType() {
        return actuateTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getShowType() {
        return showTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getTypeType() {
        return typeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getActuateTypeObject() {
        return actuateTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getArcroleType() {
        return arcroleTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getFromType() {
        return fromTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getHrefType() {
        return hrefTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getLabelType() {
        return labelTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getRoleType() {
        return roleTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getShowTypeObject() {
        return showTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTitleAttrType() {
        return titleAttrTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getToType() {
        return toTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTypeTypeObject() {
        return typeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XlinkFactory getXlinkFactory() {
        return (XlinkFactory)getEFactoryInstance();
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
        arcTypeEClass = createEClass(ARC_TYPE);
        createEAttribute(arcTypeEClass, ARC_TYPE__TITLE_GROUP);
        createEReference(arcTypeEClass, ARC_TYPE__TITLE);
        createEAttribute(arcTypeEClass, ARC_TYPE__ACTUATE);
        createEAttribute(arcTypeEClass, ARC_TYPE__ARCROLE);
        createEAttribute(arcTypeEClass, ARC_TYPE__FROM);
        createEAttribute(arcTypeEClass, ARC_TYPE__SHOW);
        createEAttribute(arcTypeEClass, ARC_TYPE__TITLE1);
        createEAttribute(arcTypeEClass, ARC_TYPE__TO);
        createEAttribute(arcTypeEClass, ARC_TYPE__TYPE);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ARC);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LOCATOR);
        createEReference(documentRootEClass, DOCUMENT_ROOT__RESOURCE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TITLE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__ACTUATE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__ARCROLE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__FROM);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__HREF);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__LABEL);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__ROLE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__SHOW);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__TITLE1);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__TO);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__TYPE);

        extendedEClass = createEClass(EXTENDED);
        createEReference(extendedEClass, EXTENDED__TITLE);
        createEReference(extendedEClass, EXTENDED__RESOURCE);
        createEReference(extendedEClass, EXTENDED__LOCATOR);
        createEReference(extendedEClass, EXTENDED__ARC);
        createEAttribute(extendedEClass, EXTENDED__ROLE);
        createEAttribute(extendedEClass, EXTENDED__TYPE);
        createEAttribute(extendedEClass, EXTENDED__TITLE_ATTRIBUTE);

        locatorTypeEClass = createEClass(LOCATOR_TYPE);
        createEAttribute(locatorTypeEClass, LOCATOR_TYPE__TITLE_GROUP);
        createEReference(locatorTypeEClass, LOCATOR_TYPE__TITLE);
        createEAttribute(locatorTypeEClass, LOCATOR_TYPE__HREF);
        createEAttribute(locatorTypeEClass, LOCATOR_TYPE__LABEL);
        createEAttribute(locatorTypeEClass, LOCATOR_TYPE__ROLE);
        createEAttribute(locatorTypeEClass, LOCATOR_TYPE__TITLE1);
        createEAttribute(locatorTypeEClass, LOCATOR_TYPE__TYPE);

        resourceTypeEClass = createEClass(RESOURCE_TYPE);
        createEAttribute(resourceTypeEClass, RESOURCE_TYPE__LABEL);
        createEAttribute(resourceTypeEClass, RESOURCE_TYPE__ROLE);
        createEAttribute(resourceTypeEClass, RESOURCE_TYPE__TITLE);
        createEAttribute(resourceTypeEClass, RESOURCE_TYPE__TYPE);
        createEReference(resourceTypeEClass, RESOURCE_TYPE__CONTENTS);

        simpleEClass = createEClass(SIMPLE);
        createEAttribute(simpleEClass, SIMPLE__ACTUATE);
        createEAttribute(simpleEClass, SIMPLE__ARCROLE);
        createEAttribute(simpleEClass, SIMPLE__HREF);
        createEAttribute(simpleEClass, SIMPLE__ROLE);
        createEAttribute(simpleEClass, SIMPLE__SHOW);
        createEAttribute(simpleEClass, SIMPLE__TITLE);
        createEAttribute(simpleEClass, SIMPLE__TYPE);
        createEReference(simpleEClass, SIMPLE__CONTENTS);

        titleEltTypeEClass = createEClass(TITLE_ELT_TYPE);
        createEAttribute(titleEltTypeEClass, TITLE_ELT_TYPE__LANG);
        createEAttribute(titleEltTypeEClass, TITLE_ELT_TYPE__TYPE);
        createEReference(titleEltTypeEClass, TITLE_ELT_TYPE__CONTENTS);

        objectEClass = createEClass(OBJECT);

        // Create enums
        actuateTypeEEnum = createEEnum(ACTUATE_TYPE);
        showTypeEEnum = createEEnum(SHOW_TYPE);
        typeTypeEEnum = createEEnum(TYPE_TYPE);

        // Create data types
        actuateTypeObjectEDataType = createEDataType(ACTUATE_TYPE_OBJECT);
        arcroleTypeEDataType = createEDataType(ARCROLE_TYPE);
        fromTypeEDataType = createEDataType(FROM_TYPE);
        hrefTypeEDataType = createEDataType(HREF_TYPE);
        labelTypeEDataType = createEDataType(LABEL_TYPE);
        roleTypeEDataType = createEDataType(ROLE_TYPE);
        showTypeObjectEDataType = createEDataType(SHOW_TYPE_OBJECT);
        titleAttrTypeEDataType = createEDataType(TITLE_ATTR_TYPE);
        toTypeEDataType = createEDataType(TO_TYPE);
        typeTypeObjectEDataType = createEDataType(TYPE_TYPE_OBJECT);
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
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
        XMLNamespacePackage theXMLNamespacePackage = (XMLNamespacePackage)EPackage.Registry.INSTANCE.getEPackage(XMLNamespacePackage.eNS_URI);

        // Add supertypes to classes

        // Initialize classes and features; add operations and parameters
        initEClass(arcTypeEClass, ArcType.class, "ArcType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getArcType_TitleGroup(), theEcorePackage.getEFeatureMapEntry(), "titleGroup", null, 0, -1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getArcType_Title(), this.getTitleEltType(), null, "title", null, 0, -1, ArcType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getArcType_Actuate(), this.getActuateType(), "actuate", null, 0, 1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArcType_Arcrole(), this.getArcroleType(), "arcrole", null, 0, 1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArcType_From(), this.getFromType(), "from", null, 0, 1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArcType_Show(), this.getShowType(), "show", null, 0, 1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArcType_Title1(), this.getTitleAttrType(), "title1", null, 0, 1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArcType_To(), this.getToType(), "to", null, 0, 1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArcType_Type(), this.getTypeType(), "type", "arc", 1, 1, ArcType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Arc(), this.getArcType(), null, "arc", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Locator(), this.getLocatorType(), null, "locator", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Resource(), this.getResourceType(), null, "resource", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Title(), this.getTitleEltType(), null, "title", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Actuate(), this.getActuateType(), "actuate", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Arcrole(), this.getArcroleType(), "arcrole", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_From(), this.getFromType(), "from", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Href(), this.getHrefType(), "href", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Label(), this.getLabelType(), "label", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Role(), this.getRoleType(), "role", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Show(), this.getShowType(), "show", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Title1(), this.getTitleAttrType(), "title1", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_To(), this.getToType(), "to", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Type(), this.getTypeType(), "type", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(extendedEClass, Extended.class, "Extended", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExtended_Title(), this.getTitleEltType(), null, "title", null, 0, -1, Extended.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getExtended_Resource(), this.getResourceType(), null, "resource", null, 0, -1, Extended.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getExtended_Locator(), this.getLocatorType(), null, "locator", null, 0, -1, Extended.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getExtended_Arc(), this.getArcType(), null, "arc", null, 0, -1, Extended.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getExtended_Role(), this.getRoleType(), "role", null, 0, 1, Extended.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExtended_Type(), this.getTypeType(), "type", "extended", 1, 1, Extended.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getExtended_TitleAttribute(), this.getTitleAttrType(), "titleAttribute", null, 0, 1, Extended.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(locatorTypeEClass, LocatorType.class, "LocatorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLocatorType_TitleGroup(), theEcorePackage.getEFeatureMapEntry(), "titleGroup", null, 0, -1, LocatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLocatorType_Title(), this.getTitleEltType(), null, "title", null, 0, -1, LocatorType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getLocatorType_Href(), this.getHrefType(), "href", null, 1, 1, LocatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLocatorType_Label(), this.getLabelType(), "label", null, 0, 1, LocatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLocatorType_Role(), this.getRoleType(), "role", null, 0, 1, LocatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLocatorType_Title1(), this.getTitleAttrType(), "title1", null, 0, 1, LocatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLocatorType_Type(), this.getTypeType(), "type", "locator", 1, 1, LocatorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(resourceTypeEClass, ResourceType.class, "ResourceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getResourceType_Label(), this.getLabelType(), "label", null, 0, 1, ResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceType_Role(), this.getRoleType(), "role", null, 0, 1, ResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceType_Title(), this.getTitleAttrType(), "title", null, 0, 1, ResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getResourceType_Type(), this.getTypeType(), "type", "resource", 1, 1, ResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getResourceType_Contents(), this.getObject(), null, "contents", null, 0, -1, ResourceType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(simpleEClass, Simple.class, "Simple", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSimple_Actuate(), this.getActuateType(), "actuate", null, 0, 1, Simple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimple_Arcrole(), this.getArcroleType(), "arcrole", null, 0, 1, Simple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimple_Href(), this.getHrefType(), "href", null, 0, 1, Simple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimple_Role(), this.getRoleType(), "role", null, 0, 1, Simple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimple_Show(), this.getShowType(), "show", null, 0, 1, Simple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimple_Title(), this.getTitleAttrType(), "title", null, 0, 1, Simple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSimple_Type(), this.getTypeType(), "type", "simple", 0, 1, Simple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSimple_Contents(), this.getObject(), null, "contents", null, 0, -1, Simple.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(titleEltTypeEClass, TitleEltType.class, "TitleEltType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTitleEltType_Lang(), theXMLNamespacePackage.getLangType(), "lang", null, 0, 1, TitleEltType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTitleEltType_Type(), this.getTypeType(), "type", "title", 1, 1, TitleEltType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTitleEltType_Contents(), this.getObject(), null, "contents", null, 0, -1, TitleEltType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(objectEClass, Object.class, "Object", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

        // Initialize enums and add enum literals
        initEEnum(actuateTypeEEnum, ActuateType.class, "ActuateType");
        addEEnumLiteral(actuateTypeEEnum, ActuateType.ON_LOAD_LITERAL);
        addEEnumLiteral(actuateTypeEEnum, ActuateType.ON_REQUEST_LITERAL);
        addEEnumLiteral(actuateTypeEEnum, ActuateType.OTHER_LITERAL);
        addEEnumLiteral(actuateTypeEEnum, ActuateType.NONE_LITERAL);

        initEEnum(showTypeEEnum, ShowType.class, "ShowType");
        addEEnumLiteral(showTypeEEnum, ShowType.NEW_LITERAL);
        addEEnumLiteral(showTypeEEnum, ShowType.REPLACE_LITERAL);
        addEEnumLiteral(showTypeEEnum, ShowType.EMBED_LITERAL);
        addEEnumLiteral(showTypeEEnum, ShowType.OTHER_LITERAL);
        addEEnumLiteral(showTypeEEnum, ShowType.NONE_LITERAL);

        initEEnum(typeTypeEEnum, TypeType.class, "TypeType");
        addEEnumLiteral(typeTypeEEnum, TypeType.SIMPLE_LITERAL);
        addEEnumLiteral(typeTypeEEnum, TypeType.EXTENDED_LITERAL);
        addEEnumLiteral(typeTypeEEnum, TypeType.TITLE_LITERAL);
        addEEnumLiteral(typeTypeEEnum, TypeType.RESOURCE_LITERAL);
        addEEnumLiteral(typeTypeEEnum, TypeType.LOCATOR_LITERAL);
        addEEnumLiteral(typeTypeEEnum, TypeType.ARC_LITERAL);

        // Initialize data types
        initEDataType(actuateTypeObjectEDataType, ActuateType.class, "ActuateTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(arcroleTypeEDataType, String.class, "ArcroleType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(fromTypeEDataType, String.class, "FromType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(hrefTypeEDataType, String.class, "HrefType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(labelTypeEDataType, String.class, "LabelType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(roleTypeEDataType, String.class, "RoleType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(showTypeObjectEDataType, ShowType.class, "ShowTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(titleAttrTypeEDataType, String.class, "TitleAttrType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(toTypeEDataType, String.class, "ToType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(typeTypeObjectEDataType, TypeType.class, "TypeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

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
          (arcTypeEClass, 
           source, 
           new String[] {
             "name", "arcType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getArcType_TitleGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "title:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getArcType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "title",
             "namespace", "##targetNamespace",
             "group", "title:group"
           });		
        addAnnotation
          (getArcType_Actuate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "actuate",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getArcType_Arcrole(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "arcrole",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getArcType_From(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "from",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getArcType_Show(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "show",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getArcType_Title1(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getArcType_To(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "to",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getArcType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
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
          (getDocumentRoot_Arc(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "arc",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Locator(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "locator",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Resource(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "resource",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Actuate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "actuate",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Arcrole(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "arcrole",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_From(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "from",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Label(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "label",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Show(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "show",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Title1(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_To(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "to",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (extendedEClass, 
           source, 
           new String[] {
             "name", "extended",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getExtended_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "title",
             "namespace", "##targetNamespace",
             "group", "title:group"
           });		
        addAnnotation
          (getExtended_Resource(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "resource",
             "namespace", "##targetNamespace",
             "group", "resource:group"
           });		
        addAnnotation
          (getExtended_Locator(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "locator",
             "namespace", "##targetNamespace",
             "group", "locator:group"
           });		
        addAnnotation
          (getExtended_Arc(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "arc",
             "namespace", "##targetNamespace",
             "group", "arc:group"
           });		
        addAnnotation
          (getExtended_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getExtended_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getExtended_TitleAttribute(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (locatorTypeEClass, 
           source, 
           new String[] {
             "name", "locatorType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getLocatorType_TitleGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "title:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getLocatorType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "title",
             "namespace", "##targetNamespace",
             "group", "title:group"
           });		
        addAnnotation
          (getLocatorType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getLocatorType_Label(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "label",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getLocatorType_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getLocatorType_Title1(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getLocatorType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (resourceTypeEClass, 
           source, 
           new String[] {
             "name", "resourceType",
             "kind", "mixed"
           });		
        addAnnotation
          (getResourceType_Label(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "label",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResourceType_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResourceType_Title(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getResourceType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (simpleEClass, 
           source, 
           new String[] {
             "name", "simple",
             "kind", "mixed"
           });			
        addAnnotation
          (getSimple_Actuate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "actuate",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSimple_Arcrole(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "arcrole",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSimple_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSimple_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSimple_Show(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "show",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSimple_Title(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getSimple_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (titleEltTypeEClass, 
           source, 
           new String[] {
             "name", "titleEltType",
             "kind", "mixed"
           });		
        addAnnotation
          (getTitleEltType_Lang(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "lang",
             "namespace", "http://www.w3.org/XML/1998/namespace"
           });			
        addAnnotation
          (getTitleEltType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (actuateTypeEEnum, 
           source, 
           new String[] {
             "name", "actuateType"
           });		
        addAnnotation
          (showTypeEEnum, 
           source, 
           new String[] {
             "name", "showType"
           });		
        addAnnotation
          (typeTypeEEnum, 
           source, 
           new String[] {
             "name", "typeType"
           });		
        addAnnotation
          (actuateTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "actuateType:Object",
             "baseType", "actuateType"
           });		
        addAnnotation
          (arcroleTypeEDataType, 
           source, 
           new String[] {
             "name", "arcroleType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#anyURI",
             "minLength", "1"
           });		
        addAnnotation
          (fromTypeEDataType, 
           source, 
           new String[] {
             "name", "fromType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#NCName"
           });		
        addAnnotation
          (hrefTypeEDataType, 
           source, 
           new String[] {
             "name", "hrefType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#anyURI"
           });		
        addAnnotation
          (labelTypeEDataType, 
           source, 
           new String[] {
             "name", "labelType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#NCName"
           });		
        addAnnotation
          (roleTypeEDataType, 
           source, 
           new String[] {
             "name", "roleType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#anyURI",
             "minLength", "1"
           });		
        addAnnotation
          (showTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "showType:Object",
             "baseType", "showType"
           });		
        addAnnotation
          (titleAttrTypeEDataType, 
           source, 
           new String[] {
             "name", "titleAttrType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
           });		
        addAnnotation
          (toTypeEDataType, 
           source, 
           new String[] {
             "name", "toType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#NCName"
           });		
        addAnnotation
          (typeTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "typeType:Object",
             "baseType", "typeType"
           });
    }

} //XlinkPackageImpl
