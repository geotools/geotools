/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3.xlink.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.w3.xlink.ActuateType;
import org.w3.xlink.DocumentRoot;
import org.w3.xlink.ShowType;
import org.w3.xlink.XlinkFactory;
import org.w3.xlink.XlinkPackage;

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
    private EClass documentRootEClass = null;

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
    private EDataType actuateTypeObjectEDataType = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EDataType showTypeObjectEDataType = null;

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
    public static XlinkPackage init() {
		if (isInited) return (XlinkPackage)EPackage.Registry.INSTANCE.getEPackage(XlinkPackage.eNS_URI);

		// Obtain or create and register package
		XlinkPackageImpl theXlinkPackage = (XlinkPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof XlinkPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new XlinkPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theXlinkPackage.createPackageContents();

		// Initialize created meta-data
		theXlinkPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theXlinkPackage.freeze();

		return theXlinkPackage;
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
    public EAttribute getDocumentRoot_Actuate() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(3);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Arcrole() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(4);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_From() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(5);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Href() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(6);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Label() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(7);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Role() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(8);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Show() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(9);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_Title() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(10);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDocumentRoot_To() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(11);
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
    public EDataType getActuateTypeObject() {
		return actuateTypeObjectEDataType;
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
		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ACTUATE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ARCROLE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__FROM);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__HREF);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__LABEL);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ROLE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__SHOW);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__TITLE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__TO);

		// Create enums
		actuateTypeEEnum = createEEnum(ACTUATE_TYPE);
		showTypeEEnum = createEEnum(SHOW_TYPE);

		// Create data types
		actuateTypeObjectEDataType = createEDataType(ACTUATE_TYPE_OBJECT);
		showTypeObjectEDataType = createEDataType(SHOW_TYPE_OBJECT);
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
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Actuate(), this.getActuateType(), "actuate", "onLoad", 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_From(), theXMLTypePackage.getString(), "from", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Label(), theXMLTypePackage.getString(), "label", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Show(), this.getShowType(), "show", "new", 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_To(), theXMLTypePackage.getString(), "to", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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

		// Initialize data types
		initEDataType(actuateTypeObjectEDataType, ActuateType.class, "ActuateTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(showTypeObjectEDataType, ShowType.class, "ShowTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

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
		  (getDocumentRoot_Title(), 
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
		  (actuateTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "actuate_._type"
		   });		
		addAnnotation
		  (showTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "show_._type"
		   });		
		addAnnotation
		  (actuateTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "actuate_._type:Object",
			 "baseType", "actuate_._type"
		   });		
		addAnnotation
		  (showTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "show_._type:Object",
			 "baseType", "show_._type"
		   });
	}

} //XlinkPackageImpl
