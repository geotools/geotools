/**
 */
package net.opengis.wmts.v_1.util;

import java.util.Map;

import net.opengis.wmts.v_1.*;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wmts.v_1.wmtsv_1Package
 * @generated
 */
public class wmtsv_1Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final wmtsv_1Validator INSTANCE = new wmtsv_1Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.wmts.v_1";

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
    public wmtsv_1Validator() {
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
      return wmtsv_1Package.eINSTANCE;
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
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE:
                return validateBinaryPayloadType((BinaryPayloadType)value, diagnostics, context);
            case wmtsv_1Package.CAPABILITIES_TYPE:
                return validateCapabilitiesType((CapabilitiesType)value, diagnostics, context);
            case wmtsv_1Package.CONTENTS_TYPE:
                return validateContentsType((ContentsType)value, diagnostics, context);
            case wmtsv_1Package.DIMENSION_NAME_VALUE_TYPE:
                return validateDimensionNameValueType((DimensionNameValueType)value, diagnostics, context);
            case wmtsv_1Package.DIMENSION_TYPE:
                return validateDimensionType((DimensionType)value, diagnostics, context);
            case wmtsv_1Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE:
                return validateFeatureInfoResponseType((FeatureInfoResponseType)value, diagnostics, context);
            case wmtsv_1Package.GET_CAPABILITIES_TYPE:
                return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE:
                return validateGetFeatureInfoType((GetFeatureInfoType)value, diagnostics, context);
            case wmtsv_1Package.GET_TILE_TYPE:
                return validateGetTileType((GetTileType)value, diagnostics, context);
            case wmtsv_1Package.LAYER_TYPE:
                return validateLayerType((LayerType)value, diagnostics, context);
            case wmtsv_1Package.LEGEND_URL_TYPE:
                return validateLegendURLType((LegendURLType)value, diagnostics, context);
            case wmtsv_1Package.STYLE_TYPE:
                return validateStyleType((StyleType)value, diagnostics, context);
            case wmtsv_1Package.TEXT_PAYLOAD_TYPE:
                return validateTextPayloadType((TextPayloadType)value, diagnostics, context);
            case wmtsv_1Package.THEMES_TYPE:
                return validateThemesType((ThemesType)value, diagnostics, context);
            case wmtsv_1Package.THEME_TYPE:
                return validateThemeType((ThemeType)value, diagnostics, context);
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE:
                return validateTileMatrixLimitsType((TileMatrixLimitsType)value, diagnostics, context);
            case wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE:
                return validateTileMatrixSetLimitsType((TileMatrixSetLimitsType)value, diagnostics, context);
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE:
                return validateTileMatrixSetLinkType((TileMatrixSetLinkType)value, diagnostics, context);
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE:
                return validateTileMatrixSetType((TileMatrixSetType)value, diagnostics, context);
            case wmtsv_1Package.TILE_MATRIX_TYPE:
                return validateTileMatrixType((TileMatrixType)value, diagnostics, context);
            case wmtsv_1Package.URL_TEMPLATE_TYPE:
                return validateURLTemplateType((URLTemplateType)value, diagnostics, context);
            case wmtsv_1Package.GET_CAPABILITIES_VALUE_TYPE:
                return validateGetCapabilitiesValueType((GetCapabilitiesValueType)value, diagnostics, context);
            case wmtsv_1Package.GET_FEATURE_INFO_VALUE_TYPE:
                return validateGetFeatureInfoValueType((GetFeatureInfoValueType)value, diagnostics, context);
            case wmtsv_1Package.GET_TILE_VALUE_TYPE:
                return validateGetTileValueType((GetTileValueType)value, diagnostics, context);
            case wmtsv_1Package.REQUEST_SERVICE_TYPE:
                return validateRequestServiceType((RequestServiceType)value, diagnostics, context);
            case wmtsv_1Package.RESOURCE_TYPE_TYPE:
                return validateResourceTypeType((ResourceTypeType)value, diagnostics, context);
            case wmtsv_1Package.VERSION_TYPE:
                return validateVersionType((VersionType)value, diagnostics, context);
            case wmtsv_1Package.ACCEPTED_FORMATS_TYPE:
                return validateAcceptedFormatsType((String)value, diagnostics, context);
            case wmtsv_1Package.GET_CAPABILITIES_VALUE_TYPE_OBJECT:
                return validateGetCapabilitiesValueTypeObject((GetCapabilitiesValueType)value, diagnostics, context);
            case wmtsv_1Package.GET_FEATURE_INFO_VALUE_TYPE_OBJECT:
                return validateGetFeatureInfoValueTypeObject((GetFeatureInfoValueType)value, diagnostics, context);
            case wmtsv_1Package.GET_TILE_VALUE_TYPE_OBJECT:
                return validateGetTileValueTypeObject((GetTileValueType)value, diagnostics, context);
            case wmtsv_1Package.REQUEST_SERVICE_TYPE_OBJECT:
                return validateRequestServiceTypeObject((RequestServiceType)value, diagnostics, context);
            case wmtsv_1Package.RESOURCE_TYPE_TYPE_OBJECT:
                return validateResourceTypeTypeObject((ResourceTypeType)value, diagnostics, context);
            case wmtsv_1Package.SECTIONS_TYPE:
                return validateSectionsType((String)value, diagnostics, context);
            case wmtsv_1Package.TEMPLATE_TYPE:
                return validateTemplateType((String)value, diagnostics, context);
            case wmtsv_1Package.VERSION_TYPE_OBJECT:
                return validateVersionTypeObject((VersionType)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBinaryPayloadType(BinaryPayloadType binaryPayloadType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(binaryPayloadType, diagnostics, context);
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
    public boolean validateDimensionNameValueType(DimensionNameValueType dimensionNameValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dimensionNameValueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDimensionType(DimensionType dimensionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dimensionType, diagnostics, context);
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
    public boolean validateFeatureInfoResponseType(FeatureInfoResponseType featureInfoResponseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureInfoResponseType, diagnostics, context);
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
    public boolean validateGetFeatureInfoType(GetFeatureInfoType getFeatureInfoType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getFeatureInfoType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetTileType(GetTileType getTileType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getTileType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLayerType(LayerType layerType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(layerType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLegendURLType(LegendURLType legendURLType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(legendURLType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStyleType(StyleType styleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(styleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTextPayloadType(TextPayloadType textPayloadType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(textPayloadType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateThemesType(ThemesType themesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(themesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateThemeType(ThemeType themeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(themeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTileMatrixLimitsType(TileMatrixLimitsType tileMatrixLimitsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(tileMatrixLimitsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTileMatrixSetLimitsType(TileMatrixSetLimitsType tileMatrixSetLimitsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(tileMatrixSetLimitsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTileMatrixSetLinkType(TileMatrixSetLinkType tileMatrixSetLinkType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(tileMatrixSetLinkType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTileMatrixSetType(TileMatrixSetType tileMatrixSetType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(tileMatrixSetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTileMatrixType(TileMatrixType tileMatrixType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(tileMatrixType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateURLTemplateType(URLTemplateType urlTemplateType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(urlTemplateType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCapabilitiesValueType(GetCapabilitiesValueType getCapabilitiesValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetFeatureInfoValueType(GetFeatureInfoValueType getFeatureInfoValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetTileValueType(GetTileValueType getTileValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRequestServiceType(RequestServiceType requestServiceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResourceTypeType(ResourceTypeType resourceTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionType(VersionType versionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAcceptedFormatsType(String acceptedFormatsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateAcceptedFormatsType_Pattern(acceptedFormatsType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateAcceptedFormatsType_Pattern
     */
    public static final  PatternMatcher [][] ACCEPTED_FORMATS_TYPE__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("((application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*)(,(application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*)")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Accepted Formats Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAcceptedFormatsType_Pattern(String acceptedFormatsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(wmtsv_1Package.Literals.ACCEPTED_FORMATS_TYPE, acceptedFormatsType, ACCEPTED_FORMATS_TYPE__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCapabilitiesValueTypeObject(GetCapabilitiesValueType getCapabilitiesValueTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetFeatureInfoValueTypeObject(GetFeatureInfoValueType getFeatureInfoValueTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetTileValueTypeObject(GetTileValueType getTileValueTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRequestServiceTypeObject(RequestServiceType requestServiceTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResourceTypeTypeObject(ResourceTypeType resourceTypeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSectionsType(String sectionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateSectionsType_Pattern(sectionsType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateSectionsType_Pattern
     */
    public static final  PatternMatcher [][] SECTIONS_TYPE__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes)(,(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes))*")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Sections Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSectionsType_Pattern(String sectionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(wmtsv_1Package.Literals.SECTIONS_TYPE, sectionsType, SECTIONS_TYPE__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemplateType(String templateType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateTemplateType_Pattern(templateType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateTemplateType_Pattern
     */
    public static final  PatternMatcher [][] TEMPLATE_TYPE__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("([A-Za-z0-9\\-_\\.!~\\*\'\\(\\);/\\?:@\\+:$,#\\{\\}=&]|%[A-Fa-f0-9][A-Fa-f0-9])+")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Template Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTemplateType_Pattern(String templateType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(wmtsv_1Package.Literals.TEMPLATE_TYPE, templateType, TEMPLATE_TYPE__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionTypeObject(VersionType versionTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
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

} //wmtsv_1Validator
