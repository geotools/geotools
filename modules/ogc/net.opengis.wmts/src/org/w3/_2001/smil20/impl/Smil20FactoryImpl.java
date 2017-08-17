/**
 */
package org.w3._2001.smil20.impl;

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.w3._2001.smil20.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Smil20FactoryImpl extends EFactoryImpl implements Smil20Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Smil20Factory init() {
        try {
            Smil20Factory theSmil20Factory = (Smil20Factory)EPackage.Registry.INSTANCE.getEFactory(Smil20Package.eNS_URI);
            if (theSmil20Factory != null) {
                return theSmil20Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Smil20FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Smil20FactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case Smil20Package.ANIMATE_COLOR_PROTOTYPE: return createAnimateColorPrototype();
            case Smil20Package.ANIMATE_MOTION_PROTOTYPE: return createAnimateMotionPrototype();
            case Smil20Package.ANIMATE_PROTOTYPE: return createAnimatePrototype();
            case Smil20Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Smil20Package.SET_PROTOTYPE: return createSetPrototype();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case Smil20Package.ACCUMULATE_TYPE:
                return createAccumulateTypeFromString(eDataType, initialValue);
            case Smil20Package.ADDITIVE_TYPE:
                return createAdditiveTypeFromString(eDataType, initialValue);
            case Smil20Package.ATTRIBUTE_TYPE_TYPE:
                return createAttributeTypeTypeFromString(eDataType, initialValue);
            case Smil20Package.CALC_MODE_TYPE:
                return createCalcModeTypeFromString(eDataType, initialValue);
            case Smil20Package.FILL_DEFAULT_TYPE:
                return createFillDefaultTypeFromString(eDataType, initialValue);
            case Smil20Package.FILL_TIMING_ATTRS_TYPE:
                return createFillTimingAttrsTypeFromString(eDataType, initialValue);
            case Smil20Package.RESTART_DEFAULT_TYPE:
                return createRestartDefaultTypeFromString(eDataType, initialValue);
            case Smil20Package.RESTART_TIMING_TYPE:
                return createRestartTimingTypeFromString(eDataType, initialValue);
            case Smil20Package.SYNC_BEHAVIOR_DEFAULT_TYPE:
                return createSyncBehaviorDefaultTypeFromString(eDataType, initialValue);
            case Smil20Package.SYNC_BEHAVIOR_TYPE:
                return createSyncBehaviorTypeFromString(eDataType, initialValue);
            case Smil20Package.ACCUMULATE_TYPE_OBJECT:
                return createAccumulateTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.ADDITIVE_TYPE_OBJECT:
                return createAdditiveTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.ATTRIBUTE_TYPE_TYPE_OBJECT:
                return createAttributeTypeTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.CALC_MODE_TYPE_OBJECT:
                return createCalcModeTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.FILL_DEFAULT_TYPE_OBJECT:
                return createFillDefaultTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.FILL_TIMING_ATTRS_TYPE_OBJECT:
                return createFillTimingAttrsTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.NON_NEGATIVE_DECIMAL_TYPE:
                return createNonNegativeDecimalTypeFromString(eDataType, initialValue);
            case Smil20Package.RESTART_DEFAULT_TYPE_OBJECT:
                return createRestartDefaultTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.RESTART_TIMING_TYPE_OBJECT:
                return createRestartTimingTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.SYNC_BEHAVIOR_DEFAULT_TYPE_OBJECT:
                return createSyncBehaviorDefaultTypeObjectFromString(eDataType, initialValue);
            case Smil20Package.SYNC_BEHAVIOR_TYPE_OBJECT:
                return createSyncBehaviorTypeObjectFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case Smil20Package.ACCUMULATE_TYPE:
                return convertAccumulateTypeToString(eDataType, instanceValue);
            case Smil20Package.ADDITIVE_TYPE:
                return convertAdditiveTypeToString(eDataType, instanceValue);
            case Smil20Package.ATTRIBUTE_TYPE_TYPE:
                return convertAttributeTypeTypeToString(eDataType, instanceValue);
            case Smil20Package.CALC_MODE_TYPE:
                return convertCalcModeTypeToString(eDataType, instanceValue);
            case Smil20Package.FILL_DEFAULT_TYPE:
                return convertFillDefaultTypeToString(eDataType, instanceValue);
            case Smil20Package.FILL_TIMING_ATTRS_TYPE:
                return convertFillTimingAttrsTypeToString(eDataType, instanceValue);
            case Smil20Package.RESTART_DEFAULT_TYPE:
                return convertRestartDefaultTypeToString(eDataType, instanceValue);
            case Smil20Package.RESTART_TIMING_TYPE:
                return convertRestartTimingTypeToString(eDataType, instanceValue);
            case Smil20Package.SYNC_BEHAVIOR_DEFAULT_TYPE:
                return convertSyncBehaviorDefaultTypeToString(eDataType, instanceValue);
            case Smil20Package.SYNC_BEHAVIOR_TYPE:
                return convertSyncBehaviorTypeToString(eDataType, instanceValue);
            case Smil20Package.ACCUMULATE_TYPE_OBJECT:
                return convertAccumulateTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.ADDITIVE_TYPE_OBJECT:
                return convertAdditiveTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.ATTRIBUTE_TYPE_TYPE_OBJECT:
                return convertAttributeTypeTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.CALC_MODE_TYPE_OBJECT:
                return convertCalcModeTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.FILL_DEFAULT_TYPE_OBJECT:
                return convertFillDefaultTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.FILL_TIMING_ATTRS_TYPE_OBJECT:
                return convertFillTimingAttrsTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.NON_NEGATIVE_DECIMAL_TYPE:
                return convertNonNegativeDecimalTypeToString(eDataType, instanceValue);
            case Smil20Package.RESTART_DEFAULT_TYPE_OBJECT:
                return convertRestartDefaultTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.RESTART_TIMING_TYPE_OBJECT:
                return convertRestartTimingTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.SYNC_BEHAVIOR_DEFAULT_TYPE_OBJECT:
                return convertSyncBehaviorDefaultTypeObjectToString(eDataType, instanceValue);
            case Smil20Package.SYNC_BEHAVIOR_TYPE_OBJECT:
                return convertSyncBehaviorTypeObjectToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnimateColorPrototype createAnimateColorPrototype() {
        AnimateColorPrototypeImpl animateColorPrototype = new AnimateColorPrototypeImpl();
        return animateColorPrototype;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnimateMotionPrototype createAnimateMotionPrototype() {
        AnimateMotionPrototypeImpl animateMotionPrototype = new AnimateMotionPrototypeImpl();
        return animateMotionPrototype;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnimatePrototype createAnimatePrototype() {
        AnimatePrototypeImpl animatePrototype = new AnimatePrototypeImpl();
        return animatePrototype;
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
    public SetPrototype createSetPrototype() {
        SetPrototypeImpl setPrototype = new SetPrototypeImpl();
        return setPrototype;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AccumulateType createAccumulateTypeFromString(EDataType eDataType, String initialValue) {
        AccumulateType result = AccumulateType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAccumulateTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditiveType createAdditiveTypeFromString(EDataType eDataType, String initialValue) {
        AdditiveType result = AdditiveType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAdditiveTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AttributeTypeType createAttributeTypeTypeFromString(EDataType eDataType, String initialValue) {
        AttributeTypeType result = AttributeTypeType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAttributeTypeTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CalcModeType createCalcModeTypeFromString(EDataType eDataType, String initialValue) {
        CalcModeType result = CalcModeType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCalcModeTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FillDefaultType createFillDefaultTypeFromString(EDataType eDataType, String initialValue) {
        FillDefaultType result = FillDefaultType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFillDefaultTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FillTimingAttrsType createFillTimingAttrsTypeFromString(EDataType eDataType, String initialValue) {
        FillTimingAttrsType result = FillTimingAttrsType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFillTimingAttrsTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RestartDefaultType createRestartDefaultTypeFromString(EDataType eDataType, String initialValue) {
        RestartDefaultType result = RestartDefaultType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRestartDefaultTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RestartTimingType createRestartTimingTypeFromString(EDataType eDataType, String initialValue) {
        RestartTimingType result = RestartTimingType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRestartTimingTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SyncBehaviorDefaultType createSyncBehaviorDefaultTypeFromString(EDataType eDataType, String initialValue) {
        SyncBehaviorDefaultType result = SyncBehaviorDefaultType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSyncBehaviorDefaultTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SyncBehaviorType createSyncBehaviorTypeFromString(EDataType eDataType, String initialValue) {
        SyncBehaviorType result = SyncBehaviorType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSyncBehaviorTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AccumulateType createAccumulateTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createAccumulateTypeFromString(Smil20Package.Literals.ACCUMULATE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAccumulateTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertAccumulateTypeToString(Smil20Package.Literals.ACCUMULATE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditiveType createAdditiveTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createAdditiveTypeFromString(Smil20Package.Literals.ADDITIVE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAdditiveTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertAdditiveTypeToString(Smil20Package.Literals.ADDITIVE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AttributeTypeType createAttributeTypeTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createAttributeTypeTypeFromString(Smil20Package.Literals.ATTRIBUTE_TYPE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAttributeTypeTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertAttributeTypeTypeToString(Smil20Package.Literals.ATTRIBUTE_TYPE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CalcModeType createCalcModeTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createCalcModeTypeFromString(Smil20Package.Literals.CALC_MODE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCalcModeTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertCalcModeTypeToString(Smil20Package.Literals.CALC_MODE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FillDefaultType createFillDefaultTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createFillDefaultTypeFromString(Smil20Package.Literals.FILL_DEFAULT_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFillDefaultTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertFillDefaultTypeToString(Smil20Package.Literals.FILL_DEFAULT_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FillTimingAttrsType createFillTimingAttrsTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createFillTimingAttrsTypeFromString(Smil20Package.Literals.FILL_TIMING_ATTRS_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFillTimingAttrsTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertFillTimingAttrsTypeToString(Smil20Package.Literals.FILL_TIMING_ATTRS_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigDecimal createNonNegativeDecimalTypeFromString(EDataType eDataType, String initialValue) {
        return (BigDecimal)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DECIMAL, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNonNegativeDecimalTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DECIMAL, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RestartDefaultType createRestartDefaultTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createRestartDefaultTypeFromString(Smil20Package.Literals.RESTART_DEFAULT_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRestartDefaultTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertRestartDefaultTypeToString(Smil20Package.Literals.RESTART_DEFAULT_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RestartTimingType createRestartTimingTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createRestartTimingTypeFromString(Smil20Package.Literals.RESTART_TIMING_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRestartTimingTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertRestartTimingTypeToString(Smil20Package.Literals.RESTART_TIMING_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SyncBehaviorDefaultType createSyncBehaviorDefaultTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createSyncBehaviorDefaultTypeFromString(Smil20Package.Literals.SYNC_BEHAVIOR_DEFAULT_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSyncBehaviorDefaultTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertSyncBehaviorDefaultTypeToString(Smil20Package.Literals.SYNC_BEHAVIOR_DEFAULT_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SyncBehaviorType createSyncBehaviorTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createSyncBehaviorTypeFromString(Smil20Package.Literals.SYNC_BEHAVIOR_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSyncBehaviorTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertSyncBehaviorTypeToString(Smil20Package.Literals.SYNC_BEHAVIOR_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Smil20Package getSmil20Package() {
        return (Smil20Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static Smil20Package getPackage() {
        return Smil20Package.eINSTANCE;
    }

} //Smil20FactoryImpl
