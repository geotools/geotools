/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.impl;

import com.vividsolutions.jts.geom.Geometry;
import java.util.List;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureFactory;
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureGeometryInfo;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EStructureInfo;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class EFeaturePackageImpl extends EPackageImpl implements EFeaturePackage {
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType transactionEDataType = null;

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
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static EFeaturePackage init() {
        if (isInited) return (EFeaturePackage)EPackage.Registry.INSTANCE.getEPackage(EFeaturePackage.eNS_URI);

        // Obtain or create and register package
        EFeaturePackageImpl theEFeaturePackage = (EFeaturePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EFeaturePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EFeaturePackageImpl());

        isInited = true;

        // Create package meta-data objects
        theEFeaturePackage.createPackageContents();

        // Initialize created meta-data
        theEFeaturePackage.initializePackageContents();

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
    @Override
    public EClass getEFeature() {
        return eFeatureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeature_ID() {
        return (EAttribute)eFeatureEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeature_Data() {
        return (EAttribute)eFeatureEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeature_SRID() {
        return (EAttribute)eFeatureEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeature_Default() {
        return (EAttribute)eFeatureEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getEFeature_Structure() {
        return (EAttribute)eFeatureEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEFeatureProperty() {
        return eFeaturePropertyEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEFeatureAttribute() {
        return eFeatureAttributeEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEFeatureGeometry() {
        return eFeatureGeometryEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEStructuralFeature() {
        return eStructuralFeatureEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getFeature() {
        return featureEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getProperty() {
        return propertyEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getAttribute() {
        return attributeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getTransaction() {
        return transactionEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getGeometryAttribute() {
        return geometryAttributeEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getGeometry() {
        return geometryEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEStructureInfo() {
        return eStructureInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEFeatureInfo() {
        return eFeatureInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEFeatureAttributeInfo() {
        return eFeatureAttributeInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getEFeatureGeometryInfo() {
        return eFeatureGeometryInfoEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EDataType getList() {
        return listEDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EFeatureFactory getEFeatureFactory() {
        return (EFeatureFactory)getEFactoryInstance();
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
        eFeatureEClass = createEClass(EFEATURE);
        createEAttribute(eFeatureEClass, EFEATURE__ID);
        createEAttribute(eFeatureEClass, EFEATURE__DATA);
        createEAttribute(eFeatureEClass, EFEATURE__SRID);
        createEAttribute(eFeatureEClass, EFEATURE__DEFAULT);
        createEAttribute(eFeatureEClass, EFEATURE__STRUCTURE);

        // Create data types
        featureEDataType = createEDataType(FEATURE);
        propertyEDataType = createEDataType(PROPERTY);
        attributeEDataType = createEDataType(ATTRIBUTE);
        transactionEDataType = createEDataType(TRANSACTION);
        geometryAttributeEDataType = createEDataType(GEOMETRY_ATTRIBUTE);
        geometryEDataType = createEDataType(GEOMETRY);
        eStructureInfoEDataType = createEDataType(ESTRUCTURE_INFO);
        eFeatureInfoEDataType = createEDataType(EFEATURE_INFO);
        eFeatureAttributeInfoEDataType = createEDataType(EFEATURE_ATTRIBUTE_INFO);
        eFeatureGeometryInfoEDataType = createEDataType(EFEATURE_GEOMETRY_INFO);
        eStructuralFeatureEDataType = createEDataType(ESTRUCTURAL_FEATURE);
        listEDataType = createEDataType(LIST);
        eFeaturePropertyEDataType = createEDataType(EFEATURE_PROPERTY);
        eFeatureAttributeEDataType = createEDataType(EFEATURE_ATTRIBUTE);
        eFeatureGeometryEDataType = createEDataType(EFEATURE_GEOMETRY);
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

        // Create type parameters
        addETypeParameter(listEDataType, "T");
        addETypeParameter(eFeaturePropertyEDataType, "V");
        ETypeParameter eFeaturePropertyEDataType_T = addETypeParameter(eFeaturePropertyEDataType, "T");
        addETypeParameter(eFeatureAttributeEDataType, "V");
        ETypeParameter eFeatureGeometryEDataType_V = addETypeParameter(eFeatureGeometryEDataType, "V");

        // Set bounds for type parameters
        EGenericType g1 = createEGenericType(this.getProperty());
        eFeaturePropertyEDataType_T.getEBounds().add(g1);
        g1 = createEGenericType(this.getGeometry());
        eFeatureGeometryEDataType_V.getEBounds().add(g1);

        // Add supertypes to classes

        // Initialize classes and features; add operations and parameters
        initEClass(eFeatureEClass, EFeature.class, "EFeature", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEFeature_ID(), ecorePackage.getEString(), "ID", "", 1, 1, EFeature.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEFeature_Data(), this.getFeature(), "data", null, 1, 1, EFeature.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getEFeature_SRID(), ecorePackage.getEString(), "SRID", "EPSG:4326", 1, 1, EFeature.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEFeature_Default(), ecorePackage.getEString(), "default", "geom", 1, 1, EFeature.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEFeature_Structure(), this.getEFeatureInfo(), "structure", null, 1, 1, EFeature.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        EOperation op = addEOperation(eFeatureEClass, null, "getAttributeList", 1, 1, IS_UNIQUE, IS_ORDERED);
        ETypeParameter t1 = addETypeParameter(op, "V");
        g1 = createEGenericType(ecorePackage.getEJavaClass());
        EGenericType g2 = createEGenericType(t1);
        g1.getETypeArguments().add(g2);
        addEParameter(op, g1, "valueType", 0, 1, IS_UNIQUE, IS_ORDERED);
        g1 = createEGenericType(this.getList());
        g2 = createEGenericType(this.getEFeatureAttribute());
        g1.getETypeArguments().add(g2);
        EGenericType g3 = createEGenericType(t1);
        g2.getETypeArguments().add(g3);
        initEOperation(op, g1);

        op = addEOperation(eFeatureEClass, null, "getGeometryList", 1, 1, IS_UNIQUE, IS_ORDERED);
        t1 = addETypeParameter(op, "V");
        g1 = createEGenericType(this.getGeometry());
        t1.getEBounds().add(g1);
        g1 = createEGenericType(ecorePackage.getEJavaClass());
        g2 = createEGenericType(t1);
        g1.getETypeArguments().add(g2);
        addEParameter(op, g1, "valueType", 0, 1, IS_UNIQUE, IS_ORDERED);
        g1 = createEGenericType(this.getList());
        g2 = createEGenericType(this.getEFeatureGeometry());
        g1.getETypeArguments().add(g2);
        g3 = createEGenericType(t1);
        g2.getETypeArguments().add(g3);
        initEOperation(op, g1);

        op = addEOperation(eFeatureEClass, this.getFeature(), "getData", 1, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getTransaction(), "transaction", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = addEOperation(eFeatureEClass, this.getFeature(), "setData", 1, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getFeature(), "newData", 1, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getTransaction(), "transaction", 1, 1, IS_UNIQUE, IS_ORDERED);

        // Initialize data types
        initEDataType(featureEDataType, Feature.class, "Feature", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(propertyEDataType, Property.class, "Property", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(attributeEDataType, Attribute.class, "Attribute", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(transactionEDataType, Transaction.class, "Transaction", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(geometryAttributeEDataType, GeometryAttribute.class, "GeometryAttribute", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(geometryEDataType, Geometry.class, "Geometry", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eStructureInfoEDataType, EStructureInfo.class, "EStructureInfo", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eFeatureInfoEDataType, EFeatureInfo.class, "EFeatureInfo", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eFeatureAttributeInfoEDataType, EFeatureAttributeInfo.class, "EFeatureAttributeInfo", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eFeatureGeometryInfoEDataType, EFeatureGeometryInfo.class, "EFeatureGeometryInfo", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eStructuralFeatureEDataType, EStructuralFeature.class, "EStructuralFeature", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(listEDataType, List.class, "List", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eFeaturePropertyEDataType, EFeatureProperty.class, "EFeatureProperty", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eFeatureAttributeEDataType, EFeatureAttribute.class, "EFeatureAttribute", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(eFeatureGeometryEDataType, EFeatureGeometry.class, "EFeatureGeometry", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // http://www.eclipse.org/emf/2002/GenModel
        createGenModelAnnotations();
    }

    /**
     * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/GenModel</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createGenModelAnnotations() {
        String source = "http://www.eclipse.org/emf/2002/GenModel";		
        addAnnotation
          (getEFeature_Default(), 
           source, 
           new String[] {
             "Doumentation", "Name of default EFeatureGeometry"
           });
    }

} // EFeaturePackageImpl
