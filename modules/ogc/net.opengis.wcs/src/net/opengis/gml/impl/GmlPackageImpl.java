/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import java.util.List;

import net.opengis.gml.AbstractGMLType;
import net.opengis.gml.AbstractGeometricPrimitiveType;
import net.opengis.gml.AbstractGeometryBaseType;
import net.opengis.gml.AbstractGeometryType;
import net.opengis.gml.AbstractMetaDataType;
import net.opengis.gml.AbstractRingPropertyType;
import net.opengis.gml.AbstractRingType;
import net.opengis.gml.AbstractSurfaceType;
import net.opengis.gml.BoundingShapeType;
import net.opengis.gml.CodeListType;
import net.opengis.gml.CodeType;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.DocumentRoot;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.EnvelopeWithTimePeriodType;
import net.opengis.gml.GmlFactory;
import net.opengis.gml.GmlPackage;
import net.opengis.gml.GridEnvelopeType;
import net.opengis.gml.GridLimitsType;
import net.opengis.gml.GridType;
import net.opengis.gml.LinearRingType;
import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.PointType;
import net.opengis.gml.PolygonType;
import net.opengis.gml.RectifiedGridType;
import net.opengis.gml.ReferenceType;
import net.opengis.gml.StringOrRefType;
import net.opengis.gml.TimeIndeterminateValueType;
import net.opengis.gml.TimePositionType;
import net.opengis.gml.VectorType;

import net.opengis.gml.util.GmlValidator;

import net.opengis.ows11.Ows11Package;
import net.opengis.wcs10.Wcs10Package;
import net.opengis.wcs10.impl.Wcs10PackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.opengis.coverage.grid.GridEnvelope;
import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GmlPackageImpl extends EPackageImpl implements GmlPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractGeometricPrimitiveTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractGeometryBaseTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractGeometryTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractGMLTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractMetaDataTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractRingPropertyTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractRingTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractSurfaceTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass boundingShapeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass codeListTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass codeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass directPositionTypeEClass = null;

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
	private EClass envelopeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass envelopeWithTimePeriodTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass gridEnvelopeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass gridLimitsTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass gridTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass linearRingTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metaDataPropertyTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pointTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass polygonTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rectifiedGridTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stringOrRefTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass timePositionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vectorTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum timeIndeterminateValueTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType doubleListEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType integerListEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType nameListEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType temporalPositionTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType timeDurationTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType timeIndeterminateValueTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType vectorTypeBaseEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType gridEnvelopeEDataType = null;

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
	 * @see net.opengis.gml.GmlPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private GmlPackageImpl() {
		super(eNS_URI, GmlFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link GmlPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static GmlPackage init() {
		if (isInited) return (GmlPackage)EPackage.Registry.INSTANCE.getEPackage(GmlPackage.eNS_URI);

		// Obtain or create and register package
		GmlPackageImpl theGmlPackage = (GmlPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof GmlPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new GmlPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XlinkPackage.eINSTANCE.eClass();
		Ows11Package.eINSTANCE.eClass();

		// Create package meta-data objects
		theGmlPackage.createPackageContents();

		// Initialize created meta-data
		theGmlPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theGmlPackage, 
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return GmlValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theGmlPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(GmlPackage.eNS_URI, theGmlPackage);
		return theGmlPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractGeometricPrimitiveType() {
		return abstractGeometricPrimitiveTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractGeometryBaseType() {
		return abstractGeometryBaseTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractGeometryType() {
		return abstractGeometryTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAbstractGeometryType_SrsName() {
		return (EAttribute)abstractGeometryTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractGMLType() {
		return abstractGMLTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractGMLType_MetaDataProperty() {
		return (EReference)abstractGMLTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractGMLType_Description() {
		return (EReference)abstractGMLTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractGMLType_Name() {
		return (EReference)abstractGMLTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractMetaDataType() {
		return abstractMetaDataTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAbstractMetaDataType_Mixed() {
		return (EAttribute)abstractMetaDataTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAbstractMetaDataType_Id() {
		return (EAttribute)abstractMetaDataTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractRingPropertyType() {
		return abstractRingPropertyTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractRingPropertyType_LinearRing() {
		return (EReference)abstractRingPropertyTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractRingType() {
		return abstractRingTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractSurfaceType() {
		return abstractSurfaceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBoundingShapeType() {
		return boundingShapeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBoundingShapeType_EnvelopeGroup() {
		return (EAttribute)boundingShapeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBoundingShapeType_Envelope() {
		return (EReference)boundingShapeTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCodeListType() {
		return codeListTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeListType_Value() {
		return (EAttribute)codeListTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeListType_CodeSpace() {
		return (EAttribute)codeListTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCodeType() {
		return codeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeType_Value() {
		return (EAttribute)codeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCodeType_CodeSpace() {
		return (EAttribute)codeTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDirectPositionType() {
		return directPositionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDirectPositionType_Value() {
		return (EAttribute)directPositionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDirectPositionType_Dimension() {
		return (EAttribute)directPositionTypeEClass.getEStructuralFeatures().get(1);
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
	public EReference getDocumentRoot_GeometricPrimitive() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Geometry() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GML() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Object() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_MetaData() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Ring() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Surface() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_BoundedBy() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Description() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Envelope() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_EnvelopeWithTimePeriod() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Exterior() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Grid() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Interior() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_LinearRing() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_MetaDataProperty() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Name() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Polygon() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Pos() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_RectifiedGrid() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_TimePosition() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Id() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_RemoteSchema() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnvelopeType() {
		return envelopeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnvelopeType_Pos() {
		return (EReference)envelopeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnvelopeWithTimePeriodType() {
		return envelopeWithTimePeriodTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnvelopeWithTimePeriodType_TimePosition() {
		return (EReference)envelopeWithTimePeriodTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnvelopeWithTimePeriodType_Frame() {
		return (EAttribute)envelopeWithTimePeriodTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGridEnvelopeType() {
		return gridEnvelopeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGridEnvelopeType_Low() {
		return (EAttribute)gridEnvelopeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGridEnvelopeType_High() {
		return (EAttribute)gridEnvelopeTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGridLimitsType() {
		return gridLimitsTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGridLimitsType_GridEnvelope() {
		return (EReference)gridLimitsTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGridType() {
		return gridTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGridType_Limits() {
		return (EAttribute)gridTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGridType_AxisName() {
		return (EAttribute)gridTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGridType_Dimension() {
		return (EAttribute)gridTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLinearRingType() {
		return linearRingTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLinearRingType_Pos() {
		return (EReference)linearRingTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetaDataPropertyType() {
		return metaDataPropertyTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_MetaDataGroup() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMetaDataPropertyType_MetaData() {
		return (EReference)metaDataPropertyTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_About() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_Actuate() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_Arcrole() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_Href() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_RemoteSchema() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_Role() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_Show() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_Title() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaDataPropertyType_Type() {
		return (EAttribute)metaDataPropertyTypeEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPointType() {
		return pointTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPointType_Pos() {
		return (EReference)pointTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPolygonType() {
		return polygonTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPolygonType_Exterior() {
		return (EReference)polygonTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPolygonType_Interior() {
		return (EReference)polygonTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRectifiedGridType() {
		return rectifiedGridTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRectifiedGridType_Origin() {
		return (EReference)rectifiedGridTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRectifiedGridType_OffsetVector() {
		return (EReference)rectifiedGridTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferenceType() {
		return referenceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Actuate() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Arcrole() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Href() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_RemoteSchema() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Role() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Show() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Title() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferenceType_Type() {
		return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStringOrRefType() {
		return stringOrRefTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Value() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Actuate() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Arcrole() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Href() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_RemoteSchema() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Role() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Show() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Title() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringOrRefType_Type() {
		return (EAttribute)stringOrRefTypeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTimePositionType() {
		return timePositionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimePositionType_Value() {
		return (EAttribute)timePositionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimePositionType_CalendarEraName() {
		return (EAttribute)timePositionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimePositionType_Frame() {
		return (EAttribute)timePositionTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimePositionType_IndeterminatePosition() {
		return (EAttribute)timePositionTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVectorType() {
		return vectorTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTimeIndeterminateValueType() {
		return timeIndeterminateValueTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDoubleList() {
		return doubleListEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getIntegerList() {
		return integerListEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getNameList() {
		return nameListEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTemporalPositionType() {
		return temporalPositionTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTimeDurationType() {
		return timeDurationTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTimeIndeterminateValueTypeObject() {
		return timeIndeterminateValueTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getVectorTypeBase() {
		return vectorTypeBaseEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getGridEnvelope() {
		return gridEnvelopeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GmlFactory getGmlFactory() {
		return (GmlFactory)getEFactoryInstance();
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
		abstractGeometricPrimitiveTypeEClass = createEClass(ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE);

		abstractGeometryBaseTypeEClass = createEClass(ABSTRACT_GEOMETRY_BASE_TYPE);

		abstractGeometryTypeEClass = createEClass(ABSTRACT_GEOMETRY_TYPE);
		createEAttribute(abstractGeometryTypeEClass, ABSTRACT_GEOMETRY_TYPE__SRS_NAME);

		abstractGMLTypeEClass = createEClass(ABSTRACT_GML_TYPE);
		createEReference(abstractGMLTypeEClass, ABSTRACT_GML_TYPE__META_DATA_PROPERTY);
		createEReference(abstractGMLTypeEClass, ABSTRACT_GML_TYPE__DESCRIPTION);
		createEReference(abstractGMLTypeEClass, ABSTRACT_GML_TYPE__NAME);

		abstractMetaDataTypeEClass = createEClass(ABSTRACT_META_DATA_TYPE);
		createEAttribute(abstractMetaDataTypeEClass, ABSTRACT_META_DATA_TYPE__MIXED);
		createEAttribute(abstractMetaDataTypeEClass, ABSTRACT_META_DATA_TYPE__ID);

		abstractRingPropertyTypeEClass = createEClass(ABSTRACT_RING_PROPERTY_TYPE);
		createEReference(abstractRingPropertyTypeEClass, ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING);

		abstractRingTypeEClass = createEClass(ABSTRACT_RING_TYPE);

		abstractSurfaceTypeEClass = createEClass(ABSTRACT_SURFACE_TYPE);

		boundingShapeTypeEClass = createEClass(BOUNDING_SHAPE_TYPE);
		createEAttribute(boundingShapeTypeEClass, BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP);
		createEReference(boundingShapeTypeEClass, BOUNDING_SHAPE_TYPE__ENVELOPE);

		codeListTypeEClass = createEClass(CODE_LIST_TYPE);
		createEAttribute(codeListTypeEClass, CODE_LIST_TYPE__VALUE);
		createEAttribute(codeListTypeEClass, CODE_LIST_TYPE__CODE_SPACE);

		codeTypeEClass = createEClass(CODE_TYPE);
		createEAttribute(codeTypeEClass, CODE_TYPE__VALUE);
		createEAttribute(codeTypeEClass, CODE_TYPE__CODE_SPACE);

		directPositionTypeEClass = createEClass(DIRECT_POSITION_TYPE);
		createEAttribute(directPositionTypeEClass, DIRECT_POSITION_TYPE__VALUE);
		createEAttribute(directPositionTypeEClass, DIRECT_POSITION_TYPE__DIMENSION);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GEOMETRIC_PRIMITIVE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GEOMETRY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GML);
		createEReference(documentRootEClass, DOCUMENT_ROOT__OBJECT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__META_DATA);
		createEReference(documentRootEClass, DOCUMENT_ROOT__RING);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SURFACE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__BOUNDED_BY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIPTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__ENVELOPE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__ENVELOPE_WITH_TIME_PERIOD);
		createEReference(documentRootEClass, DOCUMENT_ROOT__EXTERIOR);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GRID);
		createEReference(documentRootEClass, DOCUMENT_ROOT__INTERIOR);
		createEReference(documentRootEClass, DOCUMENT_ROOT__LINEAR_RING);
		createEReference(documentRootEClass, DOCUMENT_ROOT__META_DATA_PROPERTY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__NAME);
		createEReference(documentRootEClass, DOCUMENT_ROOT__POLYGON);
		createEReference(documentRootEClass, DOCUMENT_ROOT__POS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__RECTIFIED_GRID);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TIME_POSITION);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ID);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__REMOTE_SCHEMA);

		envelopeTypeEClass = createEClass(ENVELOPE_TYPE);
		createEReference(envelopeTypeEClass, ENVELOPE_TYPE__POS);

		envelopeWithTimePeriodTypeEClass = createEClass(ENVELOPE_WITH_TIME_PERIOD_TYPE);
		createEReference(envelopeWithTimePeriodTypeEClass, ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION);
		createEAttribute(envelopeWithTimePeriodTypeEClass, ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME);

		gridEnvelopeTypeEClass = createEClass(GRID_ENVELOPE_TYPE);
		createEAttribute(gridEnvelopeTypeEClass, GRID_ENVELOPE_TYPE__LOW);
		createEAttribute(gridEnvelopeTypeEClass, GRID_ENVELOPE_TYPE__HIGH);

		gridLimitsTypeEClass = createEClass(GRID_LIMITS_TYPE);
		createEReference(gridLimitsTypeEClass, GRID_LIMITS_TYPE__GRID_ENVELOPE);

		gridTypeEClass = createEClass(GRID_TYPE);
		createEAttribute(gridTypeEClass, GRID_TYPE__LIMITS);
		createEAttribute(gridTypeEClass, GRID_TYPE__AXIS_NAME);
		createEAttribute(gridTypeEClass, GRID_TYPE__DIMENSION);

		linearRingTypeEClass = createEClass(LINEAR_RING_TYPE);
		createEReference(linearRingTypeEClass, LINEAR_RING_TYPE__POS);

		metaDataPropertyTypeEClass = createEClass(META_DATA_PROPERTY_TYPE);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__META_DATA_GROUP);
		createEReference(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__META_DATA);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__ABOUT);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__ACTUATE);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__ARCROLE);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__HREF);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__REMOTE_SCHEMA);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__ROLE);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__SHOW);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__TITLE);
		createEAttribute(metaDataPropertyTypeEClass, META_DATA_PROPERTY_TYPE__TYPE);

		pointTypeEClass = createEClass(POINT_TYPE);
		createEReference(pointTypeEClass, POINT_TYPE__POS);

		polygonTypeEClass = createEClass(POLYGON_TYPE);
		createEReference(polygonTypeEClass, POLYGON_TYPE__EXTERIOR);
		createEReference(polygonTypeEClass, POLYGON_TYPE__INTERIOR);

		rectifiedGridTypeEClass = createEClass(RECTIFIED_GRID_TYPE);
		createEReference(rectifiedGridTypeEClass, RECTIFIED_GRID_TYPE__ORIGIN);
		createEReference(rectifiedGridTypeEClass, RECTIFIED_GRID_TYPE__OFFSET_VECTOR);

		referenceTypeEClass = createEClass(REFERENCE_TYPE);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__ACTUATE);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__ARCROLE);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__HREF);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__REMOTE_SCHEMA);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__ROLE);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__SHOW);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__TITLE);
		createEAttribute(referenceTypeEClass, REFERENCE_TYPE__TYPE);

		stringOrRefTypeEClass = createEClass(STRING_OR_REF_TYPE);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__VALUE);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__ACTUATE);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__ARCROLE);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__HREF);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__REMOTE_SCHEMA);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__ROLE);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__SHOW);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__TITLE);
		createEAttribute(stringOrRefTypeEClass, STRING_OR_REF_TYPE__TYPE);

		timePositionTypeEClass = createEClass(TIME_POSITION_TYPE);
		createEAttribute(timePositionTypeEClass, TIME_POSITION_TYPE__VALUE);
		createEAttribute(timePositionTypeEClass, TIME_POSITION_TYPE__CALENDAR_ERA_NAME);
		createEAttribute(timePositionTypeEClass, TIME_POSITION_TYPE__FRAME);
		createEAttribute(timePositionTypeEClass, TIME_POSITION_TYPE__INDETERMINATE_POSITION);

		vectorTypeEClass = createEClass(VECTOR_TYPE);

		// Create enums
		timeIndeterminateValueTypeEEnum = createEEnum(TIME_INDETERMINATE_VALUE_TYPE);

		// Create data types
		doubleListEDataType = createEDataType(DOUBLE_LIST);
		integerListEDataType = createEDataType(INTEGER_LIST);
		nameListEDataType = createEDataType(NAME_LIST);
		temporalPositionTypeEDataType = createEDataType(TEMPORAL_POSITION_TYPE);
		timeDurationTypeEDataType = createEDataType(TIME_DURATION_TYPE);
		timeIndeterminateValueTypeObjectEDataType = createEDataType(TIME_INDETERMINATE_VALUE_TYPE_OBJECT);
		vectorTypeBaseEDataType = createEDataType(VECTOR_TYPE_BASE);
		gridEnvelopeEDataType = createEDataType(GRID_ENVELOPE);
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
		XlinkPackage theXlinkPackage = (XlinkPackage)EPackage.Registry.INSTANCE.getEPackage(XlinkPackage.eNS_URI);

		// Add supertypes to classes
		abstractGeometricPrimitiveTypeEClass.getESuperTypes().add(this.getAbstractGeometryType());
		abstractGeometryBaseTypeEClass.getESuperTypes().add(this.getAbstractGMLType());
		abstractGeometryTypeEClass.getESuperTypes().add(this.getAbstractGeometryBaseType());
		abstractRingTypeEClass.getESuperTypes().add(this.getAbstractGeometryType());
		abstractSurfaceTypeEClass.getESuperTypes().add(this.getAbstractGeometricPrimitiveType());
		envelopeTypeEClass.getESuperTypes().add(this.getAbstractGeometryType());
		envelopeWithTimePeriodTypeEClass.getESuperTypes().add(this.getEnvelopeType());
		gridTypeEClass.getESuperTypes().add(this.getAbstractGeometryType());
		linearRingTypeEClass.getESuperTypes().add(this.getAbstractRingType());
		pointTypeEClass.getESuperTypes().add(this.getAbstractGeometryType());
		polygonTypeEClass.getESuperTypes().add(this.getAbstractSurfaceType());
		rectifiedGridTypeEClass.getESuperTypes().add(this.getGridType());
		vectorTypeEClass.getESuperTypes().add(this.getDirectPositionType());

		// Initialize classes and features; add operations and parameters
		initEClass(abstractGeometricPrimitiveTypeEClass, AbstractGeometricPrimitiveType.class, "AbstractGeometricPrimitiveType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(abstractGeometryBaseTypeEClass, AbstractGeometryBaseType.class, "AbstractGeometryBaseType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(abstractGeometryTypeEClass, AbstractGeometryType.class, "AbstractGeometryType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractGeometryType_SrsName(), theXMLTypePackage.getAnyURI(), "srsName", null, 0, 1, AbstractGeometryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractGMLTypeEClass, AbstractGMLType.class, "AbstractGMLType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAbstractGMLType_MetaDataProperty(), this.getMetaDataPropertyType(), null, "metaDataProperty", null, 0, -1, AbstractGMLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractGMLType_Description(), this.getStringOrRefType(), null, "description", null, 0, 1, AbstractGMLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractGMLType_Name(), this.getCodeType(), null, "name", null, 0, -1, AbstractGMLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractMetaDataTypeEClass, AbstractMetaDataType.class, "AbstractMetaDataType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractMetaDataType_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, AbstractMetaDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAbstractMetaDataType_Id(), theXMLTypePackage.getID(), "id", null, 0, 1, AbstractMetaDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractRingPropertyTypeEClass, AbstractRingPropertyType.class, "AbstractRingPropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAbstractRingPropertyType_LinearRing(), this.getLinearRingType(), null, "linearRing", null, 1, 1, AbstractRingPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractRingTypeEClass, AbstractRingType.class, "AbstractRingType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(abstractSurfaceTypeEClass, AbstractSurfaceType.class, "AbstractSurfaceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(boundingShapeTypeEClass, BoundingShapeType.class, "BoundingShapeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBoundingShapeType_EnvelopeGroup(), theEcorePackage.getEFeatureMapEntry(), "envelopeGroup", null, 1, 1, BoundingShapeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBoundingShapeType_Envelope(), this.getEnvelopeType(), null, "envelope", null, 1, 1, BoundingShapeType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(codeListTypeEClass, CodeListType.class, "CodeListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCodeListType_Value(), this.getNameList(), "value", null, 0, 1, CodeListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeListType_CodeSpace(), theXMLTypePackage.getAnyURI(), "codeSpace", null, 0, 1, CodeListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(codeTypeEClass, CodeType.class, "CodeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCodeType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, CodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCodeType_CodeSpace(), theXMLTypePackage.getAnyURI(), "codeSpace", null, 0, 1, CodeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(directPositionTypeEClass, DirectPositionType.class, "DirectPositionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDirectPositionType_Value(), this.getDoubleList(), "value", null, 0, 1, DirectPositionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDirectPositionType_Dimension(), theXMLTypePackage.getPositiveInteger(), "dimension", null, 0, 1, DirectPositionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GeometricPrimitive(), this.getAbstractGeometricPrimitiveType(), null, "geometricPrimitive", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Geometry(), this.getAbstractGeometryType(), null, "geometry", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GML(), this.getAbstractGMLType(), null, "gML", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Object(), theEcorePackage.getEObject(), null, "object", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_MetaData(), this.getAbstractMetaDataType(), null, "metaData", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Ring(), this.getAbstractRingType(), null, "ring", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Surface(), this.getAbstractSurfaceType(), null, "surface", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_BoundedBy(), this.getBoundingShapeType(), null, "boundedBy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Description(), this.getStringOrRefType(), null, "description", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Envelope(), this.getEnvelopeType(), null, "envelope", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_EnvelopeWithTimePeriod(), this.getEnvelopeWithTimePeriodType(), null, "envelopeWithTimePeriod", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Exterior(), this.getAbstractRingPropertyType(), null, "exterior", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Grid(), this.getGridType(), null, "grid", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Interior(), this.getAbstractRingPropertyType(), null, "interior", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_LinearRing(), this.getLinearRingType(), null, "linearRing", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_MetaDataProperty(), this.getMetaDataPropertyType(), null, "metaDataProperty", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Name(), this.getCodeType(), null, "name", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Polygon(), this.getPolygonType(), null, "polygon", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Pos(), this.getDirectPositionType(), null, "pos", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_RectifiedGrid(), this.getRectifiedGridType(), null, "rectifiedGrid", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_TimePosition(), this.getTimePositionType(), null, "timePosition", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Id(), theXMLTypePackage.getID(), "id", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_RemoteSchema(), theXMLTypePackage.getAnyURI(), "remoteSchema", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(envelopeTypeEClass, EnvelopeType.class, "EnvelopeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnvelopeType_Pos(), this.getDirectPositionType(), null, "pos", null, 2, 2, EnvelopeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(envelopeWithTimePeriodTypeEClass, EnvelopeWithTimePeriodType.class, "EnvelopeWithTimePeriodType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnvelopeWithTimePeriodType_TimePosition(), this.getTimePositionType(), null, "timePosition", null, 2, 2, EnvelopeWithTimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEnvelopeWithTimePeriodType_Frame(), theXMLTypePackage.getAnyURI(), "frame", "#ISO-8601", 0, 1, EnvelopeWithTimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(gridEnvelopeTypeEClass, GridEnvelopeType.class, "GridEnvelopeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGridEnvelopeType_Low(), this.getIntegerList(), "low", null, 1, 1, GridEnvelopeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGridEnvelopeType_High(), this.getIntegerList(), "high", null, 1, 1, GridEnvelopeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(gridLimitsTypeEClass, GridLimitsType.class, "GridLimitsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGridLimitsType_GridEnvelope(), this.getGridEnvelopeType(), null, "gridEnvelope", null, 1, 1, GridLimitsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(gridTypeEClass, GridType.class, "GridType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGridType_Limits(), this.getGridEnvelope(), "limits", null, 0, 1, GridType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGridType_AxisName(), ecorePackage.getEString(), "axisName", null, 0, -1, GridType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGridType_Dimension(), theXMLTypePackage.getPositiveInteger(), "dimension", null, 1, 1, GridType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linearRingTypeEClass, LinearRingType.class, "LinearRingType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLinearRingType_Pos(), this.getDirectPositionType(), null, "pos", null, 4, -1, LinearRingType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metaDataPropertyTypeEClass, MetaDataPropertyType.class, "MetaDataPropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMetaDataPropertyType_MetaDataGroup(), theEcorePackage.getEFeatureMapEntry(), "metaDataGroup", null, 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMetaDataPropertyType_MetaData(), this.getAbstractMetaDataType(), null, "metaData", null, 0, 1, MetaDataPropertyType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_About(), theXMLTypePackage.getAnyURI(), "about", null, 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_Actuate(), theXlinkPackage.getActuateType(), "actuate", "onLoad", 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_RemoteSchema(), theXMLTypePackage.getAnyURI(), "remoteSchema", null, 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_Show(), theXlinkPackage.getShowType(), "show", "new", 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaDataPropertyType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, MetaDataPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pointTypeEClass, PointType.class, "PointType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPointType_Pos(), this.getDirectPositionType(), null, "pos", null, 1, 1, PointType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(polygonTypeEClass, PolygonType.class, "PolygonType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPolygonType_Exterior(), this.getAbstractRingPropertyType(), null, "exterior", null, 0, 1, PolygonType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPolygonType_Interior(), this.getAbstractRingPropertyType(), null, "interior", null, 0, -1, PolygonType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rectifiedGridTypeEClass, RectifiedGridType.class, "RectifiedGridType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRectifiedGridType_Origin(), this.getPointType(), null, "origin", null, 1, 1, RectifiedGridType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRectifiedGridType_OffsetVector(), this.getVectorType(), null, "offsetVector", null, 0, -1, RectifiedGridType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referenceTypeEClass, ReferenceType.class, "ReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getReferenceType_Actuate(), theXlinkPackage.getActuateType(), "actuate", "onLoad", 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_RemoteSchema(), theXMLTypePackage.getAnyURI(), "remoteSchema", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Show(), theXlinkPackage.getShowType(), "show", "new", 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferenceType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stringOrRefTypeEClass, StringOrRefType.class, "StringOrRefType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStringOrRefType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_Actuate(), theXlinkPackage.getActuateType(), "actuate", "onLoad", 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_Arcrole(), theXMLTypePackage.getAnyURI(), "arcrole", null, 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_Href(), theXMLTypePackage.getAnyURI(), "href", null, 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_RemoteSchema(), theXMLTypePackage.getAnyURI(), "remoteSchema", null, 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_Role(), theXMLTypePackage.getAnyURI(), "role", null, 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_Show(), theXlinkPackage.getShowType(), "show", "new", 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_Title(), theXMLTypePackage.getString(), "title", null, 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringOrRefType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, StringOrRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(timePositionTypeEClass, TimePositionType.class, "TimePositionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTimePositionType_Value(), this.getTemporalPositionType(), "value", null, 0, 1, TimePositionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimePositionType_CalendarEraName(), theXMLTypePackage.getString(), "calendarEraName", null, 0, 1, TimePositionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimePositionType_Frame(), theXMLTypePackage.getAnyURI(), "frame", "#ISO-8601", 0, 1, TimePositionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimePositionType_IndeterminatePosition(), this.getTimeIndeterminateValueType(), "indeterminatePosition", "after", 0, 1, TimePositionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vectorTypeEClass, VectorType.class, "VectorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(timeIndeterminateValueTypeEEnum, TimeIndeterminateValueType.class, "TimeIndeterminateValueType");
		addEEnumLiteral(timeIndeterminateValueTypeEEnum, TimeIndeterminateValueType.AFTER_LITERAL);
		addEEnumLiteral(timeIndeterminateValueTypeEEnum, TimeIndeterminateValueType.BEFORE_LITERAL);
		addEEnumLiteral(timeIndeterminateValueTypeEEnum, TimeIndeterminateValueType.NOW_LITERAL);
		addEEnumLiteral(timeIndeterminateValueTypeEEnum, TimeIndeterminateValueType.UNKNOWN_LITERAL);

		// Initialize data types
		initEDataType(doubleListEDataType, List.class, "DoubleList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(integerListEDataType, List.class, "IntegerList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(nameListEDataType, List.class, "NameList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(temporalPositionTypeEDataType, Object.class, "TemporalPositionType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(timeDurationTypeEDataType, Object.class, "TimeDurationType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(timeIndeterminateValueTypeObjectEDataType, TimeIndeterminateValueType.class, "TimeIndeterminateValueTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(vectorTypeBaseEDataType, List.class, "VectorTypeBase", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(gridEnvelopeEDataType, GridEnvelope.class, "GridEnvelope", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.w3.org/XML/1998/namespace
		createNamespaceAnnotations();
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.w3.org/XML/1998/namespace</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNamespaceAnnotations() {
		String source = "http://www.w3.org/XML/1998/namespace";		
		addAnnotation
		  (this, 
		   source, 
		   new String[] {
			 "lang", "en"
		   });																																																																																																																																																																																						
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
		  (abstractGeometricPrimitiveTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractGeometricPrimitiveType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (abstractGeometryBaseTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractGeometryBaseType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (abstractGeometryTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractGeometryType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getAbstractGeometryType_SrsName(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "srsName"
		   });			
		addAnnotation
		  (abstractGMLTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractGMLType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getAbstractGMLType_MetaDataProperty(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "metaDataProperty",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAbstractGMLType_Description(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "description",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getAbstractGMLType_Name(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "name",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (abstractMetaDataTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractMetaDataType",
			 "kind", "mixed"
		   });			
		addAnnotation
		  (getAbstractMetaDataType_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getAbstractMetaDataType_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (abstractRingPropertyTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractRingPropertyType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getAbstractRingPropertyType_LinearRing(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "LinearRing",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (abstractRingTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractRingType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (abstractSurfaceTypeEClass, 
		   source, 
		   new String[] {
			 "name", "AbstractSurfaceType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (boundingShapeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "BoundingShapeType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getBoundingShapeType_EnvelopeGroup(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "Envelope:group",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getBoundingShapeType_Envelope(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Envelope",
			 "namespace", "##targetNamespace",
			 "group", "Envelope:group"
		   });		
		addAnnotation
		  (codeListTypeEClass, 
		   source, 
		   new String[] {
			 "name", "CodeListType",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getCodeListType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getCodeListType_CodeSpace(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "codeSpace"
		   });		
		addAnnotation
		  (codeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "CodeType",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getCodeType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getCodeType_CodeSpace(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "codeSpace"
		   });		
		addAnnotation
		  (directPositionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DirectPositionType",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getDirectPositionType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getDirectPositionType_Dimension(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dimension"
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
		  (getDocumentRoot_GeometricPrimitive(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_GeometricPrimitive",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Geometry"
		   });			
		addAnnotation
		  (getDocumentRoot_Geometry(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_Geometry",
			 "namespace", "##targetNamespace",
			 "affiliation", "_GML"
		   });			
		addAnnotation
		  (getDocumentRoot_GML(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_GML",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Object"
		   });			
		addAnnotation
		  (getDocumentRoot_Object(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_Object",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_MetaData(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_MetaData",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Object"
		   });			
		addAnnotation
		  (getDocumentRoot_Ring(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_Ring",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Geometry"
		   });			
		addAnnotation
		  (getDocumentRoot_Surface(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_Surface",
			 "namespace", "##targetNamespace",
			 "affiliation", "_GeometricPrimitive"
		   });			
		addAnnotation
		  (getDocumentRoot_BoundedBy(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "boundedBy",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Description(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "description",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Envelope(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Envelope",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Geometry"
		   });		
		addAnnotation
		  (getDocumentRoot_EnvelopeWithTimePeriod(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "EnvelopeWithTimePeriod",
			 "namespace", "##targetNamespace",
			 "affiliation", "Envelope"
		   });		
		addAnnotation
		  (getDocumentRoot_Exterior(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "exterior",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Grid(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Grid",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Geometry"
		   });		
		addAnnotation
		  (getDocumentRoot_Interior(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "interior",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_LinearRing(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "LinearRing",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Ring"
		   });		
		addAnnotation
		  (getDocumentRoot_MetaDataProperty(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "metaDataProperty",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Name(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "name",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Polygon(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Polygon",
			 "namespace", "##targetNamespace",
			 "affiliation", "_Surface"
		   });		
		addAnnotation
		  (getDocumentRoot_Pos(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "pos",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_RectifiedGrid(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "RectifiedGrid",
			 "namespace", "##targetNamespace",
			 "affiliation", "Grid"
		   });		
		addAnnotation
		  (getDocumentRoot_TimePosition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "timePosition",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_RemoteSchema(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "remoteSchema",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (envelopeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "EnvelopeType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getEnvelopeType_Pos(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "pos",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (envelopeWithTimePeriodTypeEClass, 
		   source, 
		   new String[] {
			 "name", "EnvelopeWithTimePeriodType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getEnvelopeWithTimePeriodType_TimePosition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "timePosition",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getEnvelopeWithTimePeriodType_Frame(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "frame"
		   });		
		addAnnotation
		  (gridEnvelopeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GridEnvelopeType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getGridEnvelopeType_Low(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "low",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGridEnvelopeType_High(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "high",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (gridLimitsTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GridLimitsType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getGridLimitsType_GridEnvelope(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "GridEnvelope",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (gridTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GridType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getGridType_Dimension(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dimension"
		   });		
		addAnnotation
		  (linearRingTypeEClass, 
		   source, 
		   new String[] {
			 "name", "LinearRingType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getLinearRingType_Pos(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "pos",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (metaDataPropertyTypeEClass, 
		   source, 
		   new String[] {
			 "name", "MetaDataPropertyType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getMetaDataPropertyType_MetaDataGroup(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "_MetaData:group",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getMetaDataPropertyType_MetaData(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "_MetaData",
			 "namespace", "##targetNamespace",
			 "group", "_MetaData:group"
		   });			
		addAnnotation
		  (getMetaDataPropertyType_About(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "about"
		   });		
		addAnnotation
		  (getMetaDataPropertyType_Actuate(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "actuate",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getMetaDataPropertyType_Arcrole(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "arcrole",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getMetaDataPropertyType_Href(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "href",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getMetaDataPropertyType_RemoteSchema(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "remoteSchema",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getMetaDataPropertyType_Role(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "role",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getMetaDataPropertyType_Show(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "show",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getMetaDataPropertyType_Title(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "title",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getMetaDataPropertyType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (pointTypeEClass, 
		   source, 
		   new String[] {
			 "name", "PointType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getPointType_Pos(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "pos",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (polygonTypeEClass, 
		   source, 
		   new String[] {
			 "name", "PolygonType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getPolygonType_Exterior(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "exterior",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getPolygonType_Interior(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "interior",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (rectifiedGridTypeEClass, 
		   source, 
		   new String[] {
			 "name", "RectifiedGridType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getRectifiedGridType_Origin(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "origin",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (referenceTypeEClass, 
		   source, 
		   new String[] {
			 "name", "ReferenceType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getReferenceType_Actuate(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "actuate",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getReferenceType_Arcrole(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "arcrole",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getReferenceType_Href(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "href",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getReferenceType_RemoteSchema(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "remoteSchema",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getReferenceType_Role(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "role",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getReferenceType_Show(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "show",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getReferenceType_Title(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "title",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getReferenceType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (stringOrRefTypeEClass, 
		   source, 
		   new String[] {
			 "name", "StringOrRefType",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getStringOrRefType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getStringOrRefType_Actuate(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "actuate",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getStringOrRefType_Arcrole(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "arcrole",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getStringOrRefType_Href(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "href",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getStringOrRefType_RemoteSchema(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "remoteSchema",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getStringOrRefType_Role(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "role",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getStringOrRefType_Show(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "show",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });			
		addAnnotation
		  (getStringOrRefType_Title(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "title",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (getStringOrRefType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "http://www.w3.org/1999/xlink"
		   });		
		addAnnotation
		  (timePositionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "TimePositionType",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getTimePositionType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getTimePositionType_CalendarEraName(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "calendarEraName"
		   });		
		addAnnotation
		  (getTimePositionType_Frame(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "frame"
		   });		
		addAnnotation
		  (getTimePositionType_IndeterminatePosition(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "indeterminatePosition"
		   });		
		addAnnotation
		  (vectorTypeEClass, 
		   source, 
		   new String[] {
			 "name", "VectorType",
			 "kind", "simple"
		   });			
		addAnnotation
		  (timeIndeterminateValueTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "TimeIndeterminateValueType"
		   });			
		addAnnotation
		  (doubleListEDataType, 
		   source, 
		   new String[] {
			 "name", "doubleList",
			 "itemType", "http://www.eclipse.org/emf/2003/XMLType#double"
		   });		
		addAnnotation
		  (integerListEDataType, 
		   source, 
		   new String[] {
			 "name", "integerList",
			 "itemType", "http://www.eclipse.org/emf/2003/XMLType#integer"
		   });		
		addAnnotation
		  (nameListEDataType, 
		   source, 
		   new String[] {
			 "name", "NameList",
			 "itemType", "http://www.eclipse.org/emf/2003/XMLType#Name"
		   });		
		addAnnotation
		  (temporalPositionTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "TemporalPositionType",
			 "memberTypes", "http://www.eclipse.org/emf/2003/XMLType#dateTime http://www.eclipse.org/emf/2003/XMLType#date http://www.eclipse.org/emf/2003/XMLType#gYearMonth http://www.eclipse.org/emf/2003/XMLType#gYear http://www.eclipse.org/emf/2003/XMLType#anyURI http://www.eclipse.org/emf/2003/XMLType#decimal"
		   });		
		addAnnotation
		  (timeDurationTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "TimeDurationType",
			 "memberTypes", "http://www.eclipse.org/emf/2003/XMLType#duration http://www.eclipse.org/emf/2003/XMLType#decimal"
		   });		
		addAnnotation
		  (timeIndeterminateValueTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "TimeIndeterminateValueType:Object",
			 "baseType", "TimeIndeterminateValueType"
		   });		
		addAnnotation
		  (vectorTypeBaseEDataType, 
		   source, 
		   new String[] {
			 "name", "VectorType_._base",
			 "baseType", "doubleList"
		   });
	}

} //GmlPackageImpl
