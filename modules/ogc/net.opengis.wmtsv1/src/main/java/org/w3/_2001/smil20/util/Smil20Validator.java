/**
 */
package org.w3._2001.smil20.util;

import java.math.BigDecimal;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

import org.w3._2001.smil20.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.Smil20Package
 * @generated
 */
public class Smil20Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final Smil20Validator INSTANCE = new Smil20Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "org.w3._2001.smil20";

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
    public Smil20Validator() {
        super();
        xmlTypeValidator = XMLTypeValidator.INSTANCE;
    }

    /**
     * Returns the package of this validator switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EPackage getEPackage() {
      return Smil20Package.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
        switch (classifierID) {
            case Smil20Package.ANIMATE_COLOR_PROTOTYPE:
                return validateAnimateColorPrototype((AnimateColorPrototype)value, diagnostics, context);
            case Smil20Package.ANIMATE_MOTION_PROTOTYPE:
                return validateAnimateMotionPrototype((AnimateMotionPrototype)value, diagnostics, context);
            case Smil20Package.ANIMATE_PROTOTYPE:
                return validateAnimatePrototype((AnimatePrototype)value, diagnostics, context);
            case Smil20Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case Smil20Package.SET_PROTOTYPE:
                return validateSetPrototype((SetPrototype)value, diagnostics, context);
            case Smil20Package.ACCUMULATE_TYPE:
                return validateAccumulateType((AccumulateType)value, diagnostics, context);
            case Smil20Package.ADDITIVE_TYPE:
                return validateAdditiveType((AdditiveType)value, diagnostics, context);
            case Smil20Package.ATTRIBUTE_TYPE_TYPE:
                return validateAttributeTypeType((AttributeTypeType)value, diagnostics, context);
            case Smil20Package.CALC_MODE_TYPE:
                return validateCalcModeType((CalcModeType)value, diagnostics, context);
            case Smil20Package.FILL_DEFAULT_TYPE:
                return validateFillDefaultType((FillDefaultType)value, diagnostics, context);
            case Smil20Package.FILL_TIMING_ATTRS_TYPE:
                return validateFillTimingAttrsType((FillTimingAttrsType)value, diagnostics, context);
            case Smil20Package.RESTART_DEFAULT_TYPE:
                return validateRestartDefaultType((RestartDefaultType)value, diagnostics, context);
            case Smil20Package.RESTART_TIMING_TYPE:
                return validateRestartTimingType((RestartTimingType)value, diagnostics, context);
            case Smil20Package.SYNC_BEHAVIOR_DEFAULT_TYPE:
                return validateSyncBehaviorDefaultType((SyncBehaviorDefaultType)value, diagnostics, context);
            case Smil20Package.SYNC_BEHAVIOR_TYPE:
                return validateSyncBehaviorType((SyncBehaviorType)value, diagnostics, context);
            case Smil20Package.ACCUMULATE_TYPE_OBJECT:
                return validateAccumulateTypeObject((AccumulateType)value, diagnostics, context);
            case Smil20Package.ADDITIVE_TYPE_OBJECT:
                return validateAdditiveTypeObject((AdditiveType)value, diagnostics, context);
            case Smil20Package.ATTRIBUTE_TYPE_TYPE_OBJECT:
                return validateAttributeTypeTypeObject((AttributeTypeType)value, diagnostics, context);
            case Smil20Package.CALC_MODE_TYPE_OBJECT:
                return validateCalcModeTypeObject((CalcModeType)value, diagnostics, context);
            case Smil20Package.FILL_DEFAULT_TYPE_OBJECT:
                return validateFillDefaultTypeObject((FillDefaultType)value, diagnostics, context);
            case Smil20Package.FILL_TIMING_ATTRS_TYPE_OBJECT:
                return validateFillTimingAttrsTypeObject((FillTimingAttrsType)value, diagnostics, context);
            case Smil20Package.NON_NEGATIVE_DECIMAL_TYPE:
                return validateNonNegativeDecimalType((BigDecimal)value, diagnostics, context);
            case Smil20Package.RESTART_DEFAULT_TYPE_OBJECT:
                return validateRestartDefaultTypeObject((RestartDefaultType)value, diagnostics, context);
            case Smil20Package.RESTART_TIMING_TYPE_OBJECT:
                return validateRestartTimingTypeObject((RestartTimingType)value, diagnostics, context);
            case Smil20Package.SYNC_BEHAVIOR_DEFAULT_TYPE_OBJECT:
                return validateSyncBehaviorDefaultTypeObject((SyncBehaviorDefaultType)value, diagnostics, context);
            case Smil20Package.SYNC_BEHAVIOR_TYPE_OBJECT:
                return validateSyncBehaviorTypeObject((SyncBehaviorType)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAnimateColorPrototype(AnimateColorPrototype animateColorPrototype, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(animateColorPrototype, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAnimateMotionPrototype(AnimateMotionPrototype animateMotionPrototype, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(animateMotionPrototype, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAnimatePrototype(AnimatePrototype animatePrototype, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(animatePrototype, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSetPrototype(SetPrototype setPrototype, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(setPrototype, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAccumulateType(AccumulateType accumulateType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditiveType(AdditiveType additiveType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAttributeTypeType(AttributeTypeType attributeTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCalcModeType(CalcModeType calcModeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFillDefaultType(FillDefaultType fillDefaultType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFillTimingAttrsType(FillTimingAttrsType fillTimingAttrsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRestartDefaultType(RestartDefaultType restartDefaultType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRestartTimingType(RestartTimingType restartTimingType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSyncBehaviorDefaultType(SyncBehaviorDefaultType syncBehaviorDefaultType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSyncBehaviorType(SyncBehaviorType syncBehaviorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAccumulateTypeObject(AccumulateType accumulateTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditiveTypeObject(AdditiveType additiveTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAttributeTypeTypeObject(AttributeTypeType attributeTypeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCalcModeTypeObject(CalcModeType calcModeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFillDefaultTypeObject(FillDefaultType fillDefaultTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFillTimingAttrsTypeObject(FillTimingAttrsType fillTimingAttrsTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNonNegativeDecimalType(BigDecimal nonNegativeDecimalType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNonNegativeDecimalType_Min(nonNegativeDecimalType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateNonNegativeDecimalType_Min
     */
    public static final BigDecimal NON_NEGATIVE_DECIMAL_TYPE__MIN__VALUE = new BigDecimal("0.0");

    /**
     * Validates the Min constraint of '<em>Non Negative Decimal Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNonNegativeDecimalType_Min(BigDecimal nonNegativeDecimalType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = nonNegativeDecimalType.compareTo(NON_NEGATIVE_DECIMAL_TYPE__MIN__VALUE) >= 0;
        if (!result && diagnostics != null)
            reportMinViolation(Smil20Package.Literals.NON_NEGATIVE_DECIMAL_TYPE, nonNegativeDecimalType, NON_NEGATIVE_DECIMAL_TYPE__MIN__VALUE, true, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRestartDefaultTypeObject(RestartDefaultType restartDefaultTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRestartTimingTypeObject(RestartTimingType restartTimingTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSyncBehaviorDefaultTypeObject(SyncBehaviorDefaultType syncBehaviorDefaultTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSyncBehaviorTypeObject(SyncBehaviorType syncBehaviorTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        // TODO
        // Specialize this to return a resource locator for messages specific to this validator.
        // Ensure that you remove @generated or mark it @generated NOT
        return super.getResourceLocator();
    }

} //Smil20Validator
