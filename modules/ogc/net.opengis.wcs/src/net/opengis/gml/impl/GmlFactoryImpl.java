/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.opengis.gml.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.util.Diagnostician;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.opengis.coverage.grid.GridEnvelope;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GmlFactoryImpl extends EFactoryImpl implements GmlFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static GmlFactory init() {
		try {
			GmlFactory theGmlFactory = (GmlFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/gml"); 
			if (theGmlFactory != null) {
				return theGmlFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new GmlFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GmlFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case GmlPackage.ABSTRACT_RING_PROPERTY_TYPE: return createAbstractRingPropertyType();
			case GmlPackage.ABSTRACT_SURFACE_TYPE: return createAbstractSurfaceType();
			case GmlPackage.BOUNDING_SHAPE_TYPE: return createBoundingShapeType();
			case GmlPackage.CODE_LIST_TYPE: return createCodeListType();
			case GmlPackage.CODE_TYPE: return createCodeType();
			case GmlPackage.DIRECT_POSITION_TYPE: return createDirectPositionType();
			case GmlPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case GmlPackage.ENVELOPE_TYPE: return createEnvelopeType();
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE: return createEnvelopeWithTimePeriodType();
			case GmlPackage.GRID_ENVELOPE_TYPE: return createGridEnvelopeType();
			case GmlPackage.GRID_LIMITS_TYPE: return createGridLimitsType();
			case GmlPackage.GRID_TYPE: return createGridType();
			case GmlPackage.LINEAR_RING_TYPE: return createLinearRingType();
			case GmlPackage.META_DATA_PROPERTY_TYPE: return createMetaDataPropertyType();
			case GmlPackage.POINT_TYPE: return createPointType();
			case GmlPackage.POLYGON_TYPE: return createPolygonType();
			case GmlPackage.RECTIFIED_GRID_TYPE: return createRectifiedGridType();
			case GmlPackage.REFERENCE_TYPE: return createReferenceType();
			case GmlPackage.STRING_OR_REF_TYPE: return createStringOrRefType();
			case GmlPackage.TIME_POSITION_TYPE: return createTimePositionType();
			case GmlPackage.VECTOR_TYPE: return createVectorType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case GmlPackage.TIME_INDETERMINATE_VALUE_TYPE:
				return createTimeIndeterminateValueTypeFromString(eDataType, initialValue);
			case GmlPackage.DOUBLE_LIST:
				return createDoubleListFromString(eDataType, initialValue);
			case GmlPackage.INTEGER_LIST:
				return createIntegerListFromString(eDataType, initialValue);
			case GmlPackage.NAME_LIST:
				return createNameListFromString(eDataType, initialValue);
			case GmlPackage.TEMPORAL_POSITION_TYPE:
				return createTemporalPositionTypeFromString(eDataType, initialValue);
			case GmlPackage.TIME_DURATION_TYPE:
				return createTimeDurationTypeFromString(eDataType, initialValue);
			case GmlPackage.TIME_INDETERMINATE_VALUE_TYPE_OBJECT:
				return createTimeIndeterminateValueTypeObjectFromString(eDataType, initialValue);
			case GmlPackage.VECTOR_TYPE_BASE:
				return createVectorTypeBaseFromString(eDataType, initialValue);
			case GmlPackage.GRID_ENVELOPE:
				return createGridEnvelopeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case GmlPackage.TIME_INDETERMINATE_VALUE_TYPE:
				return convertTimeIndeterminateValueTypeToString(eDataType, instanceValue);
			case GmlPackage.DOUBLE_LIST:
				return convertDoubleListToString(eDataType, instanceValue);
			case GmlPackage.INTEGER_LIST:
				return convertIntegerListToString(eDataType, instanceValue);
			case GmlPackage.NAME_LIST:
				return convertNameListToString(eDataType, instanceValue);
			case GmlPackage.TEMPORAL_POSITION_TYPE:
				return convertTemporalPositionTypeToString(eDataType, instanceValue);
			case GmlPackage.TIME_DURATION_TYPE:
				return convertTimeDurationTypeToString(eDataType, instanceValue);
			case GmlPackage.TIME_INDETERMINATE_VALUE_TYPE_OBJECT:
				return convertTimeIndeterminateValueTypeObjectToString(eDataType, instanceValue);
			case GmlPackage.VECTOR_TYPE_BASE:
				return convertVectorTypeBaseToString(eDataType, instanceValue);
			case GmlPackage.GRID_ENVELOPE:
				return convertGridEnvelopeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractRingPropertyType createAbstractRingPropertyType() {
		AbstractRingPropertyTypeImpl abstractRingPropertyType = new AbstractRingPropertyTypeImpl();
		return abstractRingPropertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractSurfaceType createAbstractSurfaceType() {
		AbstractSurfaceTypeImpl abstractSurfaceType = new AbstractSurfaceTypeImpl();
		return abstractSurfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BoundingShapeType createBoundingShapeType() {
		BoundingShapeTypeImpl boundingShapeType = new BoundingShapeTypeImpl();
		return boundingShapeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeListType createCodeListType() {
		CodeListTypeImpl codeListType = new CodeListTypeImpl();
		return codeListType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeType createCodeType() {
		CodeTypeImpl codeType = new CodeTypeImpl();
		return codeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DirectPositionType createDirectPositionType() {
		DirectPositionTypeImpl directPositionType = new DirectPositionTypeImpl();
		return directPositionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvelopeType createEnvelopeType() {
		EnvelopeTypeImpl envelopeType = new EnvelopeTypeImpl();
		return envelopeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnvelopeWithTimePeriodType createEnvelopeWithTimePeriodType() {
		EnvelopeWithTimePeriodTypeImpl envelopeWithTimePeriodType = new EnvelopeWithTimePeriodTypeImpl();
		return envelopeWithTimePeriodType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GridEnvelopeType createGridEnvelopeType() {
		GridEnvelopeTypeImpl gridEnvelopeType = new GridEnvelopeTypeImpl();
		return gridEnvelopeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GridLimitsType createGridLimitsType() {
		GridLimitsTypeImpl gridLimitsType = new GridLimitsTypeImpl();
		return gridLimitsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GridType createGridType() {
		GridTypeImpl gridType = new GridTypeImpl();
		return gridType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LinearRingType createLinearRingType() {
		LinearRingTypeImpl linearRingType = new LinearRingTypeImpl();
		return linearRingType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetaDataPropertyType createMetaDataPropertyType() {
		MetaDataPropertyTypeImpl metaDataPropertyType = new MetaDataPropertyTypeImpl();
		return metaDataPropertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PointType createPointType() {
		PointTypeImpl pointType = new PointTypeImpl();
		return pointType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PolygonType createPolygonType() {
		PolygonTypeImpl polygonType = new PolygonTypeImpl();
		return polygonType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RectifiedGridType createRectifiedGridType() {
		RectifiedGridTypeImpl rectifiedGridType = new RectifiedGridTypeImpl();
		return rectifiedGridType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceType createReferenceType() {
		ReferenceTypeImpl referenceType = new ReferenceTypeImpl();
		return referenceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringOrRefType createStringOrRefType() {
		StringOrRefTypeImpl stringOrRefType = new StringOrRefTypeImpl();
		return stringOrRefType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimePositionType createTimePositionType() {
		TimePositionTypeImpl timePositionType = new TimePositionTypeImpl();
		return timePositionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VectorType createVectorType() {
		VectorTypeImpl vectorType = new VectorTypeImpl();
		return vectorType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimeIndeterminateValueType createTimeIndeterminateValueTypeFromString(EDataType eDataType, String initialValue) {
		TimeIndeterminateValueType result = TimeIndeterminateValueType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTimeIndeterminateValueTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createDoubleListFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List result = new ArrayList();
		for (StringTokenizer stringTokenizer = new StringTokenizer(initialValue); stringTokenizer.hasMoreTokens(); ) {
			String item = stringTokenizer.nextToken();
			result.add((Double)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DOUBLE, item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDoubleListToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List list = (List)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DOUBLE, i.next()));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createIntegerListFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List result = new ArrayList();
		for (StringTokenizer stringTokenizer = new StringTokenizer(initialValue); stringTokenizer.hasMoreTokens(); ) {
			String item = stringTokenizer.nextToken();
			result.add((BigInteger)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INTEGER, item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIntegerListToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List list = (List)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INTEGER, i.next()));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createNameListFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List result = new ArrayList();
		for (StringTokenizer stringTokenizer = new StringTokenizer(initialValue); stringTokenizer.hasMoreTokens(); ) {
			String item = stringTokenizer.nextToken();
			result.add((String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.NAME, item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertNameListToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List list = (List)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.NAME, i.next()));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createTemporalPositionTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		Object result = null;
		RuntimeException exception = null;
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DATE_TIME, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DATE, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.GYEAR_MONTH, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.GYEAR, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_URI, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DECIMAL, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		if (result != null || exception == null) return result;
    
		throw exception;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTemporalPositionTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		if (XMLTypePackage.Literals.DATE_TIME.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DATE_TIME, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (XMLTypePackage.Literals.DATE.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DATE, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (XMLTypePackage.Literals.GYEAR_MONTH.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.GYEAR_MONTH, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (XMLTypePackage.Literals.GYEAR.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.GYEAR, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (XMLTypePackage.Literals.ANY_URI.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_URI, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (XMLTypePackage.Literals.DECIMAL.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DECIMAL, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createTimeDurationTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		Object result = null;
		RuntimeException exception = null;
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DURATION, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DECIMAL, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		if (result != null || exception == null) return result;
    
		throw exception;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTimeDurationTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		if (XMLTypePackage.Literals.DURATION.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DURATION, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (XMLTypePackage.Literals.DECIMAL.isInstance(instanceValue)) {
			try {
				String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DECIMAL, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimeIndeterminateValueType createTimeIndeterminateValueTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createTimeIndeterminateValueTypeFromString(GmlPackage.Literals.TIME_INDETERMINATE_VALUE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTimeIndeterminateValueTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertTimeIndeterminateValueTypeToString(GmlPackage.Literals.TIME_INDETERMINATE_VALUE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createVectorTypeBaseFromString(EDataType eDataType, String initialValue) {
		return createDoubleListFromString(GmlPackage.Literals.DOUBLE_LIST, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVectorTypeBaseToString(EDataType eDataType, Object instanceValue) {
		return convertDoubleListToString(GmlPackage.Literals.DOUBLE_LIST, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GridEnvelope createGridEnvelopeFromString(EDataType eDataType, String initialValue) {
		return (GridEnvelope)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertGridEnvelopeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GmlPackage getGmlPackage() {
		return (GmlPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static GmlPackage getPackage() {
		return GmlPackage.eINSTANCE;
	}

} //GmlFactoryImpl
