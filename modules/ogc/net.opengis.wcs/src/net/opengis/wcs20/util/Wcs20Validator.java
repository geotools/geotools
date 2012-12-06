/**
 */
package net.opengis.wcs20.util;

import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.wcs20.CapabilitiesType;
import net.opengis.wcs20.ContentsType;
import net.opengis.wcs20.CoverageDescriptionType;
import net.opengis.wcs20.CoverageDescriptionsType;
import net.opengis.wcs20.CoverageOfferingsType;
import net.opengis.wcs20.CoverageSubtypeParentType;
import net.opengis.wcs20.CoverageSummaryType;
import net.opengis.wcs20.DescribeCoverageType;
import net.opengis.wcs20.DimensionSliceType;
import net.opengis.wcs20.DimensionSubsetType;
import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.DocumentRoot;
import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.GetCapabilitiesType;
import net.opengis.wcs20.GetCoverageType;
import net.opengis.wcs20.OfferedCoverageType;
import net.opengis.wcs20.RequestBaseType;
import net.opengis.wcs20.ServiceMetadataType;
import net.opengis.wcs20.ServiceParametersType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs20.Wcs20Package
 * @generated
 */
public class Wcs20Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final Wcs20Validator INSTANCE = new Wcs20Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.wcs20";

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
    public Wcs20Validator() {
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
      return Wcs20Package.eINSTANCE;
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
            case Wcs20Package.CAPABILITIES_TYPE:
                return validateCapabilitiesType((CapabilitiesType)value, diagnostics, context);
            case Wcs20Package.CONTENTS_TYPE:
                return validateContentsType((ContentsType)value, diagnostics, context);
            case Wcs20Package.COVERAGE_DESCRIPTIONS_TYPE:
                return validateCoverageDescriptionsType((CoverageDescriptionsType)value, diagnostics, context);
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE:
                return validateCoverageDescriptionType((CoverageDescriptionType)value, diagnostics, context);
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE:
                return validateCoverageOfferingsType((CoverageOfferingsType)value, diagnostics, context);
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE:
                return validateCoverageSubtypeParentType((CoverageSubtypeParentType)value, diagnostics, context);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE:
                return validateCoverageSummaryType((CoverageSummaryType)value, diagnostics, context);
            case Wcs20Package.DESCRIBE_COVERAGE_TYPE:
                return validateDescribeCoverageType((DescribeCoverageType)value, diagnostics, context);
            case Wcs20Package.DIMENSION_SLICE_TYPE:
                return validateDimensionSliceType((DimensionSliceType)value, diagnostics, context);
            case Wcs20Package.DIMENSION_SUBSET_TYPE:
                return validateDimensionSubsetType((DimensionSubsetType)value, diagnostics, context);
            case Wcs20Package.DIMENSION_TRIM_TYPE:
                return validateDimensionTrimType((DimensionTrimType)value, diagnostics, context);
            case Wcs20Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case Wcs20Package.EXTENSION_TYPE:
                return validateExtensionType((ExtensionType)value, diagnostics, context);
            case Wcs20Package.GET_CAPABILITIES_TYPE:
                return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
            case Wcs20Package.GET_COVERAGE_TYPE:
                return validateGetCoverageType((GetCoverageType)value, diagnostics, context);
            case Wcs20Package.OFFERED_COVERAGE_TYPE:
                return validateOfferedCoverageType((OfferedCoverageType)value, diagnostics, context);
            case Wcs20Package.REQUEST_BASE_TYPE:
                return validateRequestBaseType((RequestBaseType)value, diagnostics, context);
            case Wcs20Package.SERVICE_METADATA_TYPE:
                return validateServiceMetadataType((ServiceMetadataType)value, diagnostics, context);
            case Wcs20Package.SERVICE_PARAMETERS_TYPE:
                return validateServiceParametersType((ServiceParametersType)value, diagnostics, context);
            case Wcs20Package.OBJECT:
                return validateObject((Object)value, diagnostics, context);
            case Wcs20Package.VERSION_STRING_TYPE:
                return validateVersionStringType((String)value, diagnostics, context);
            case Wcs20Package.VERSION_STRING_TYPE_1:
                return validateVersionStringType_1((String)value, diagnostics, context);
            case Wcs20Package.QNAME:
                return validateQName((QName)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCapabilitiesType(CapabilitiesType capabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(capabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateContentsType(ContentsType contentsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(contentsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageDescriptionsType(CoverageDescriptionsType coverageDescriptionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coverageDescriptionsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageDescriptionType(CoverageDescriptionType coverageDescriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coverageDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageOfferingsType(CoverageOfferingsType coverageOfferingsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coverageOfferingsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageSubtypeParentType(CoverageSubtypeParentType coverageSubtypeParentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coverageSubtypeParentType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCoverageSummaryType(CoverageSummaryType coverageSummaryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(coverageSummaryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescribeCoverageType(DescribeCoverageType describeCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(describeCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDimensionSliceType(DimensionSliceType dimensionSliceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dimensionSliceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDimensionSubsetType(DimensionSubsetType dimensionSubsetType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dimensionSubsetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDimensionTrimType(DimensionTrimType dimensionTrimType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dimensionTrimType, diagnostics, context);
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
    public boolean validateExtensionType(ExtensionType extensionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(extensionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCapabilitiesType(GetCapabilitiesType getCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCoverageType(GetCoverageType getCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOfferedCoverageType(OfferedCoverageType offeredCoverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(offeredCoverageType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRequestBaseType(RequestBaseType requestBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(requestBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateServiceMetadataType(ServiceMetadataType serviceMetadataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(serviceMetadataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateServiceParametersType(ServiceParametersType serviceParametersType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(serviceParametersType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateObject(Object object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint((EObject)object, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionStringType(String versionStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateVersionStringType_Pattern(versionStringType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateVersionStringType_Pattern
     */
    public static final  PatternMatcher [][] VERSION_STRING_TYPE__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("2\\.0\\.\\d+")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Version String Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionStringType_Pattern(String versionStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Wcs20Package.Literals.VERSION_STRING_TYPE, versionStringType, VERSION_STRING_TYPE__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionStringType_1(String versionStringType_1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQName(QName qName, DiagnosticChain diagnostics, Map<Object, Object> context) {
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

} //Wcs20Validator
