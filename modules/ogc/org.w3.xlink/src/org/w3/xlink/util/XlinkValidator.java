/**
 */
package org.w3.xlink.util;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

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
import org.w3.xlink.XlinkPackage;
import org.w3.xlink.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.w3.xlink.XlinkPackage
 * @generated
 */
public class XlinkValidator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final XlinkValidator INSTANCE = new XlinkValidator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "org.w3.xlink";

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
    public XlinkValidator() {
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
      return XlinkPackage.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map context) {
        switch (classifierID) {
            case XlinkPackage.ARC_TYPE:
                return validateArcType((ArcType)value, diagnostics, context);
            case XlinkPackage.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case XlinkPackage.EXTENDED:
                return validateExtended((Extended)value, diagnostics, context);
            case XlinkPackage.LOCATOR_TYPE:
                return validateLocatorType((LocatorType)value, diagnostics, context);
            case XlinkPackage.RESOURCE_TYPE:
                return validateResourceType((ResourceType)value, diagnostics, context);
            case XlinkPackage.SIMPLE:
                return validateSimple((Simple)value, diagnostics, context);
            case XlinkPackage.TITLE_ELT_TYPE:
                return validateTitleEltType((TitleEltType)value, diagnostics, context);
            case XlinkPackage.OBJECT:
                return validateObject((Object)value, diagnostics, context);
            case XlinkPackage.ACTUATE_TYPE:
                return validateActuateType((ActuateType)value, diagnostics, context);
            case XlinkPackage.SHOW_TYPE:
                return validateShowType((ShowType)value, diagnostics, context);
            case XlinkPackage.TYPE_TYPE:
                return validateTypeType((TypeType)value, diagnostics, context);
            case XlinkPackage.ACTUATE_TYPE_OBJECT:
                return validateActuateTypeObject((ActuateType)value, diagnostics, context);
            case XlinkPackage.ARCROLE_TYPE:
                return validateArcroleType((String)value, diagnostics, context);
            case XlinkPackage.FROM_TYPE:
                return validateFromType((String)value, diagnostics, context);
            case XlinkPackage.HREF_TYPE:
                return validateHrefType((String)value, diagnostics, context);
            case XlinkPackage.LABEL_TYPE:
                return validateLabelType((String)value, diagnostics, context);
            case XlinkPackage.ROLE_TYPE:
                return validateRoleType((String)value, diagnostics, context);
            case XlinkPackage.SHOW_TYPE_OBJECT:
                return validateShowTypeObject((ShowType)value, diagnostics, context);
            case XlinkPackage.TITLE_ATTR_TYPE:
                return validateTitleAttrType((String)value, diagnostics, context);
            case XlinkPackage.TO_TYPE:
                return validateToType((String)value, diagnostics, context);
            case XlinkPackage.TYPE_TYPE_OBJECT:
                return validateTypeTypeObject((TypeType)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcType(ArcType arcType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(arcType, diagnostics, context);
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
    public boolean validateExtended(Extended extended, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(extended, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLocatorType(LocatorType locatorType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(locatorType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResourceType(ResourceType resourceType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(resourceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSimple(Simple simple, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(simple, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTitleEltType(TitleEltType titleEltType, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint(titleEltType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateObject(Object object, DiagnosticChain diagnostics, Map context) {
        return validate_EveryDefaultConstraint((EObject)object, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateActuateType(ActuateType actuateType, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateShowType(ShowType showType, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeType(TypeType typeType, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateActuateTypeObject(ActuateType actuateTypeObject, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcroleType(String arcroleType, DiagnosticChain diagnostics, Map context) {
        boolean result = validateArcroleType_MinLength(arcroleType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MinLength constraint of '<em>Arcrole Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcroleType_MinLength(String arcroleType, DiagnosticChain diagnostics, Map context) {
        int length = arcroleType.length();
        boolean result = length >= 1;
        if (!result && diagnostics != null)
            reportMinLengthViolation(XlinkPackage.Literals.ARCROLE_TYPE, arcroleType, length, 1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFromType(String fromType, DiagnosticChain diagnostics, Map context) {
        boolean result = xmlTypeValidator.validateNCName_Pattern(fromType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateHrefType(String hrefType, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLabelType(String labelType, DiagnosticChain diagnostics, Map context) {
        boolean result = xmlTypeValidator.validateNCName_Pattern(labelType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRoleType(String roleType, DiagnosticChain diagnostics, Map context) {
        boolean result = validateRoleType_MinLength(roleType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MinLength constraint of '<em>Role Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRoleType_MinLength(String roleType, DiagnosticChain diagnostics, Map context) {
        int length = roleType.length();
        boolean result = length >= 1;
        if (!result && diagnostics != null)
            reportMinLengthViolation(XlinkPackage.Literals.ROLE_TYPE, roleType, length, 1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateShowTypeObject(ShowType showTypeObject, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTitleAttrType(String titleAttrType, DiagnosticChain diagnostics, Map context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateToType(String toType, DiagnosticChain diagnostics, Map context) {
        boolean result = xmlTypeValidator.validateNCName_Pattern(toType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeTypeObject(TypeType typeTypeObject, DiagnosticChain diagnostics, Map context) {
        return true;
    }

} //XlinkValidator
