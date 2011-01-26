/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.gml.*;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;
import org.opengis.coverage.grid.GridEnvelope;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.gml.GmlPackage
 * @generated
 */
public class GmlValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final GmlValidator INSTANCE = new GmlValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "net.opengis.gml";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GmlValidator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EPackage getEPackage() {
	  return GmlPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map context) {
		switch (classifierID) {
			case GmlPackage.ABSTRACT_GEOMETRIC_PRIMITIVE_TYPE:
				return validateAbstractGeometricPrimitiveType((AbstractGeometricPrimitiveType)value, diagnostics, context);
			case GmlPackage.ABSTRACT_GEOMETRY_BASE_TYPE:
				return validateAbstractGeometryBaseType((AbstractGeometryBaseType)value, diagnostics, context);
			case GmlPackage.ABSTRACT_GEOMETRY_TYPE:
				return validateAbstractGeometryType((AbstractGeometryType)value, diagnostics, context);
			case GmlPackage.ABSTRACT_GML_TYPE:
				return validateAbstractGMLType((AbstractGMLType)value, diagnostics, context);
			case GmlPackage.ABSTRACT_META_DATA_TYPE:
				return validateAbstractMetaDataType((AbstractMetaDataType)value, diagnostics, context);
			case GmlPackage.ABSTRACT_RING_PROPERTY_TYPE:
				return validateAbstractRingPropertyType((AbstractRingPropertyType)value, diagnostics, context);
			case GmlPackage.ABSTRACT_RING_TYPE:
				return validateAbstractRingType((AbstractRingType)value, diagnostics, context);
			case GmlPackage.ABSTRACT_SURFACE_TYPE:
				return validateAbstractSurfaceType((AbstractSurfaceType)value, diagnostics, context);
			case GmlPackage.BOUNDING_SHAPE_TYPE:
				return validateBoundingShapeType((BoundingShapeType)value, diagnostics, context);
			case GmlPackage.CODE_LIST_TYPE:
				return validateCodeListType((CodeListType)value, diagnostics, context);
			case GmlPackage.CODE_TYPE:
				return validateCodeType((CodeType)value, diagnostics, context);
			case GmlPackage.DIRECT_POSITION_TYPE:
				return validateDirectPositionType((DirectPositionType)value, diagnostics, context);
			case GmlPackage.DOCUMENT_ROOT:
				return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
			case GmlPackage.ENVELOPE_TYPE:
				return validateEnvelopeType((EnvelopeType)value, diagnostics, context);
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE:
				return validateEnvelopeWithTimePeriodType((EnvelopeWithTimePeriodType)value, diagnostics, context);
			case GmlPackage.GRID_ENVELOPE_TYPE:
				return validateGridEnvelopeType((GridEnvelopeType)value, diagnostics, context);
			case GmlPackage.GRID_LIMITS_TYPE:
				return validateGridLimitsType((GridLimitsType)value, diagnostics, context);
			case GmlPackage.GRID_TYPE:
				return validateGridType((GridType)value, diagnostics, context);
			case GmlPackage.LINEAR_RING_TYPE:
				return validateLinearRingType((LinearRingType)value, diagnostics, context);
			case GmlPackage.META_DATA_PROPERTY_TYPE:
				return validateMetaDataPropertyType((MetaDataPropertyType)value, diagnostics, context);
			case GmlPackage.POINT_TYPE:
				return validatePointType((PointType)value, diagnostics, context);
			case GmlPackage.POLYGON_TYPE:
				return validatePolygonType((PolygonType)value, diagnostics, context);
			case GmlPackage.RECTIFIED_GRID_TYPE:
				return validateRectifiedGridType((RectifiedGridType)value, diagnostics, context);
			case GmlPackage.REFERENCE_TYPE:
				return validateReferenceType((ReferenceType)value, diagnostics, context);
			case GmlPackage.STRING_OR_REF_TYPE:
				return validateStringOrRefType((StringOrRefType)value, diagnostics, context);
			case GmlPackage.TIME_POSITION_TYPE:
				return validateTimePositionType((TimePositionType)value, diagnostics, context);
			case GmlPackage.VECTOR_TYPE:
				return validateVectorType((VectorType)value, diagnostics, context);
			case GmlPackage.TIME_INDETERMINATE_VALUE_TYPE:
				return validateTimeIndeterminateValueType((TimeIndeterminateValueType)value, diagnostics, context);
			case GmlPackage.DOUBLE_LIST:
				return validateDoubleList((List)value, diagnostics, context);
			case GmlPackage.INTEGER_LIST:
				return validateIntegerList((List)value, diagnostics, context);
			case GmlPackage.NAME_LIST:
				return validateNameList((List)value, diagnostics, context);
			case GmlPackage.TEMPORAL_POSITION_TYPE:
				return validateTemporalPositionType(value, diagnostics, context);
			case GmlPackage.TIME_DURATION_TYPE:
				return validateTimeDurationType(value, diagnostics, context);
			case GmlPackage.TIME_INDETERMINATE_VALUE_TYPE_OBJECT:
				return validateTimeIndeterminateValueTypeObject((TimeIndeterminateValueType)value, diagnostics, context);
			case GmlPackage.VECTOR_TYPE_BASE:
				return validateVectorTypeBase((List)value, diagnostics, context);
			case GmlPackage.GRID_ENVELOPE:
				return validateGridEnvelope((GridEnvelope)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractGeometricPrimitiveType(AbstractGeometricPrimitiveType abstractGeometricPrimitiveType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractGeometricPrimitiveType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractGeometryBaseType(AbstractGeometryBaseType abstractGeometryBaseType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractGeometryBaseType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractGeometryType(AbstractGeometryType abstractGeometryType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractGeometryType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractGMLType(AbstractGMLType abstractGMLType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractGMLType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractMetaDataType(AbstractMetaDataType abstractMetaDataType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractMetaDataType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractRingPropertyType(AbstractRingPropertyType abstractRingPropertyType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractRingPropertyType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractRingType(AbstractRingType abstractRingType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractRingType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAbstractSurfaceType(AbstractSurfaceType abstractSurfaceType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(abstractSurfaceType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBoundingShapeType(BoundingShapeType boundingShapeType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(boundingShapeType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCodeListType(CodeListType codeListType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(codeListType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCodeType(CodeType codeType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(codeType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDirectPositionType(DirectPositionType directPositionType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(directPositionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEnvelopeType(EnvelopeType envelopeType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(envelopeType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEnvelopeWithTimePeriodType(EnvelopeWithTimePeriodType envelopeWithTimePeriodType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(envelopeWithTimePeriodType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGridEnvelopeType(GridEnvelopeType gridEnvelopeType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(gridEnvelopeType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGridLimitsType(GridLimitsType gridLimitsType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(gridLimitsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGridType(GridType gridType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(gridType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLinearRingType(LinearRingType linearRingType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(linearRingType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMetaDataPropertyType(MetaDataPropertyType metaDataPropertyType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(metaDataPropertyType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePointType(PointType pointType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(pointType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePolygonType(PolygonType polygonType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(polygonType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRectifiedGridType(RectifiedGridType rectifiedGridType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(rectifiedGridType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateReferenceType(ReferenceType referenceType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(referenceType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStringOrRefType(StringOrRefType stringOrRefType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(stringOrRefType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimePositionType(TimePositionType timePositionType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(timePositionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVectorType(VectorType vectorType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(vectorType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimeIndeterminateValueType(TimeIndeterminateValueType timeIndeterminateValueType, DiagnosticChain diagnostics, Map context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDoubleList(List doubleList, DiagnosticChain diagnostics, Map context) {
		boolean result = validateDoubleList_ItemType(doubleList, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Double List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDoubleList_ItemType(List doubleList, DiagnosticChain diagnostics, Map context) {
		boolean result = true;
		for (Iterator i = doubleList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (XMLTypePackage.Literals.DOUBLE.isInstance(item)) {
				result &= xmlTypeValidator.validateDouble(((Double)item).doubleValue(), diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(XMLTypePackage.Literals.DOUBLE, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIntegerList(List integerList, DiagnosticChain diagnostics, Map context) {
		boolean result = validateIntegerList_ItemType(integerList, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Integer List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIntegerList_ItemType(List integerList, DiagnosticChain diagnostics, Map context) {
		boolean result = true;
		for (Iterator i = integerList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (XMLTypePackage.Literals.INTEGER.isInstance(item)) {
				result &= xmlTypeValidator.validateInteger((BigInteger)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(XMLTypePackage.Literals.INTEGER, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNameList(List nameList, DiagnosticChain diagnostics, Map context) {
		boolean result = validateNameList_ItemType(nameList, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Name List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNameList_ItemType(List nameList, DiagnosticChain diagnostics, Map context) {
		boolean result = true;
		for (Iterator i = nameList.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (XMLTypePackage.Literals.NAME.isInstance(item)) {
				result &= xmlTypeValidator.validateName((String)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(XMLTypePackage.Literals.NAME, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTemporalPositionType(Object temporalPositionType, DiagnosticChain diagnostics, Map context) {
		boolean result = validateTemporalPositionType_MemberTypes(temporalPositionType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Temporal Position Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTemporalPositionType_MemberTypes(Object temporalPositionType, DiagnosticChain diagnostics, Map context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (XMLTypePackage.Literals.DATE_TIME.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateDateTime(temporalPositionType, tempDiagnostics, context)) return true;
			}
			if (XMLTypePackage.Literals.DATE.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateDate(temporalPositionType, tempDiagnostics, context)) return true;
			}
			if (XMLTypePackage.Literals.GYEAR_MONTH.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateGYearMonth(temporalPositionType, tempDiagnostics, context)) return true;
			}
			if (XMLTypePackage.Literals.GYEAR.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateGYear(temporalPositionType, tempDiagnostics, context)) return true;
			}
			if (XMLTypePackage.Literals.ANY_URI.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateAnyURI((String)temporalPositionType, tempDiagnostics, context)) return true;
			}
			if (XMLTypePackage.Literals.DECIMAL.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateDecimal((BigDecimal)temporalPositionType, tempDiagnostics, context)) return true;
			}
			List children = tempDiagnostics.getChildren();
			for (int i = 0; i < children.size(); i++) {
				diagnostics.add((Diagnostic)children.get(i));
			}
		}
		else {
			if (XMLTypePackage.Literals.DATE_TIME.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateDateTime(temporalPositionType, null, context)) return true;
			}
			if (XMLTypePackage.Literals.DATE.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateDate(temporalPositionType, null, context)) return true;
			}
			if (XMLTypePackage.Literals.GYEAR_MONTH.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateGYearMonth(temporalPositionType, null, context)) return true;
			}
			if (XMLTypePackage.Literals.GYEAR.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateGYear(temporalPositionType, null, context)) return true;
			}
			if (XMLTypePackage.Literals.ANY_URI.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateAnyURI((String)temporalPositionType, null, context)) return true;
			}
			if (XMLTypePackage.Literals.DECIMAL.isInstance(temporalPositionType)) {
				if (xmlTypeValidator.validateDecimal((BigDecimal)temporalPositionType, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimeDurationType(Object timeDurationType, DiagnosticChain diagnostics, Map context) {
		boolean result = validateTimeDurationType_MemberTypes(timeDurationType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Time Duration Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimeDurationType_MemberTypes(Object timeDurationType, DiagnosticChain diagnostics, Map context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (XMLTypePackage.Literals.DURATION.isInstance(timeDurationType)) {
				if (xmlTypeValidator.validateDuration(timeDurationType, tempDiagnostics, context)) return true;
			}
			if (XMLTypePackage.Literals.DECIMAL.isInstance(timeDurationType)) {
				if (xmlTypeValidator.validateDecimal((BigDecimal)timeDurationType, tempDiagnostics, context)) return true;
			}
			List children = tempDiagnostics.getChildren();
			for (int i = 0; i < children.size(); i++) {
				diagnostics.add((Diagnostic)children.get(i));
			}
		}
		else {
			if (XMLTypePackage.Literals.DURATION.isInstance(timeDurationType)) {
				if (xmlTypeValidator.validateDuration(timeDurationType, null, context)) return true;
			}
			if (XMLTypePackage.Literals.DECIMAL.isInstance(timeDurationType)) {
				if (xmlTypeValidator.validateDecimal((BigDecimal)timeDurationType, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimeIndeterminateValueTypeObject(TimeIndeterminateValueType timeIndeterminateValueTypeObject, DiagnosticChain diagnostics, Map context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVectorTypeBase(List vectorTypeBase, DiagnosticChain diagnostics, Map context) {
		boolean result = validateDoubleList_ItemType(vectorTypeBase, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGridEnvelope(GridEnvelope gridEnvelope, DiagnosticChain diagnostics, Map context) {
		return true;
	}

} //GmlValidator
