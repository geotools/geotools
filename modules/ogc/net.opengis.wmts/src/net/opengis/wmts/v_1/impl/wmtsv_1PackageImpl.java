/**
 */
package net.opengis.wmts.v_1.impl;

import net.opengis.gml311.Gml311Package;

import net.opengis.gml311.impl.Gml311PackageImpl;

import net.opengis.ows11.Ows11Package;

import net.opengis.wmts.v_1.BinaryPayloadType;
import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.DimensionNameValueType;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.DocumentRoot;
import net.opengis.wmts.v_1.FeatureInfoResponseType;
import net.opengis.wmts.v_1.GetCapabilitiesType;
import net.opengis.wmts.v_1.GetCapabilitiesValueType;
import net.opengis.wmts.v_1.GetFeatureInfoType;
import net.opengis.wmts.v_1.GetFeatureInfoValueType;
import net.opengis.wmts.v_1.GetTileType;
import net.opengis.wmts.v_1.GetTileValueType;
import net.opengis.wmts.v_1.LayerType;
import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.RequestServiceType;
import net.opengis.wmts.v_1.ResourceTypeType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.TextPayloadType;
import net.opengis.wmts.v_1.ThemeType;
import net.opengis.wmts.v_1.ThemesType;
import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLinkType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.URLTemplateType;
import net.opengis.wmts.v_1.VersionType;

import net.opengis.wmts.v_1.util.wmtsv_1Validator;

import net.opengis.wmts.v_1.wmtsv_1Factory;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.w3._2001.smil20.Smil20Package;

import org.w3._2001.smil20.impl.Smil20PackageImpl;

import org.w3._2001.smil20.language.LanguagePackage;

import org.w3._2001.smil20.language.impl.LanguagePackageImpl;

import org.w3.xlink.XlinkPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class wmtsv_1PackageImpl extends EPackageImpl implements wmtsv_1Package {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass binaryPayloadTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass capabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass contentsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dimensionNameValueTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dimensionTypeEClass = null;

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
    private EClass featureInfoResponseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getCapabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getFeatureInfoTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass getTileTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass layerTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass legendURLTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass styleTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass textPayloadTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass themesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass themeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tileMatrixLimitsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tileMatrixSetLimitsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tileMatrixSetLinkTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tileMatrixSetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass tileMatrixTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass urlTemplateTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum getCapabilitiesValueTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum getFeatureInfoValueTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum getTileValueTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum requestServiceTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum resourceTypeTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum versionTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType acceptedFormatsTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType getCapabilitiesValueTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType getFeatureInfoValueTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType getTileValueTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType requestServiceTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType resourceTypeTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType sectionsTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType templateTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType versionTypeObjectEDataType = null;

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
     * @see net.opengis.wmts.v_1.wmtsv_1Package#eNS_URI
     * @see #init()
     * @generated
     */
    private wmtsv_1PackageImpl() {
        super(eNS_URI, wmtsv_1Factory.eINSTANCE);
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
     * <p>This method is used to initialize {@link wmtsv_1Package#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static wmtsv_1Package init() {
        if (isInited) return (wmtsv_1Package)EPackage.Registry.INSTANCE.getEPackage(wmtsv_1Package.eNS_URI);

        // Obtain or create and register package
        wmtsv_1PackageImpl thewmtsv_1Package = (wmtsv_1PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof wmtsv_1PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new wmtsv_1PackageImpl());

        isInited = true;

        // Initialize simple dependencies
        Ows11Package.eINSTANCE.eClass();
        XlinkPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        Gml311PackageImpl theGml311Package = (Gml311PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI) instanceof Gml311PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI) : Gml311Package.eINSTANCE);
        Smil20PackageImpl theSmil20Package = (Smil20PackageImpl)(EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI) instanceof Smil20PackageImpl ? EPackage.Registry.INSTANCE.getEPackage(Smil20Package.eNS_URI) : Smil20Package.eINSTANCE);
        LanguagePackageImpl theLanguagePackage = (LanguagePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI) instanceof LanguagePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(LanguagePackage.eNS_URI) : LanguagePackage.eINSTANCE);

        // Load packages
        theGml311Package.loadPackage();

        // Create package meta-data objects
        thewmtsv_1Package.createPackageContents();
        theSmil20Package.createPackageContents();
        theLanguagePackage.createPackageContents();

        // Initialize created meta-data
        thewmtsv_1Package.initializePackageContents();
        theSmil20Package.initializePackageContents();
        theLanguagePackage.initializePackageContents();

        // Fix loaded packages
        theGml311Package.fixPackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (thewmtsv_1Package, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return wmtsv_1Validator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        thewmtsv_1Package.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(wmtsv_1Package.eNS_URI, thewmtsv_1Package);
        return thewmtsv_1Package;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBinaryPayloadType() {
        return binaryPayloadTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryPayloadType_Format() {
        return (EAttribute)binaryPayloadTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBinaryPayloadType_BinaryContent() {
        return (EAttribute)binaryPayloadTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCapabilitiesType() {
        return capabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_Contents() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_Themes() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_WSDL() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_ServiceMetadataURL() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getContentsType() {
        return contentsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getContentsType_TileMatrixSet() {
        return (EReference)contentsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDimensionNameValueType() {
        return dimensionNameValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionNameValueType_Value() {
        return (EAttribute)dimensionNameValueTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionNameValueType_Name() {
        return (EAttribute)dimensionNameValueTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDimensionType() {
        return dimensionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDimensionType_Identifier() {
        return (EReference)dimensionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDimensionType_UOM() {
        return (EReference)dimensionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionType_UnitSymbol() {
        return (EAttribute)dimensionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionType_Default() {
        return (EAttribute)dimensionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionType_Current() {
        return (EAttribute)dimensionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDimensionType_Value() {
        return (EAttribute)dimensionTypeEClass.getEStructuralFeatures().get(5);
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
    public EReference getDocumentRoot_BinaryPayload() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Capabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Dimension() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DimensionNameValue() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_FeatureInfoResponse() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetFeatureInfo() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetTile() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Layer() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_LegendURL() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Style() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TextPayload() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Theme() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Themes() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TileMatrix() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TileMatrixLimits() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TileMatrixSet() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TileMatrixSetLimits() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TileMatrixSetLink() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFeatureInfoResponseType() {
        return featureInfoResponseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFeatureInfoResponseType_FeatureCollectionGroup() {
        return (EAttribute)featureInfoResponseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureInfoResponseType_FeatureCollection() {
        return (EReference)featureInfoResponseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureInfoResponseType_TextPayload() {
        return (EReference)featureInfoResponseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureInfoResponseType_BinaryPayload() {
        return (EReference)featureInfoResponseTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFeatureInfoResponseType_AnyContent() {
        return (EReference)featureInfoResponseTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetCapabilitiesType() {
        return getCapabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCapabilitiesType_Service() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetFeatureInfoType() {
        return getFeatureInfoTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetFeatureInfoType_GetTile() {
        return (EReference)getFeatureInfoTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureInfoType_J() {
        return (EAttribute)getFeatureInfoTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureInfoType_I() {
        return (EAttribute)getFeatureInfoTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureInfoType_InfoFormat() {
        return (EAttribute)getFeatureInfoTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureInfoType_Service() {
        return (EAttribute)getFeatureInfoTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetFeatureInfoType_Version() {
        return (EAttribute)getFeatureInfoTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGetTileType() {
        return getTileTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_Layer() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_Style() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_Format() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetTileType_DimensionNameValue() {
        return (EReference)getTileTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_TileMatrixSet() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_TileMatrix() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_TileRow() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_TileCol() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_Service() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetTileType_Version() {
        return (EAttribute)getTileTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLayerType() {
        return layerTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLayerType_Style() {
        return (EReference)layerTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLayerType_Format() {
        return (EAttribute)layerTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLayerType_InfoFormat() {
        return (EAttribute)layerTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLayerType_Dimension() {
        return (EReference)layerTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLayerType_TileMatrixSetLink() {
        return (EReference)layerTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getLayerType_ResourceURL() {
        return (EReference)layerTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getLegendURLType() {
        return legendURLTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLegendURLType_Format() {
        return (EAttribute)legendURLTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLegendURLType_Height() {
        return (EAttribute)legendURLTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLegendURLType_MaxScaleDenominator() {
        return (EAttribute)legendURLTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLegendURLType_MinScaleDenominator() {
        return (EAttribute)legendURLTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getLegendURLType_Width() {
        return (EAttribute)legendURLTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStyleType() {
        return styleTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStyleType_Identifier() {
        return (EReference)styleTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getStyleType_LegendURL() {
        return (EReference)styleTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getStyleType_IsDefault() {
        return (EAttribute)styleTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTextPayloadType() {
        return textPayloadTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTextPayloadType_Format() {
        return (EAttribute)textPayloadTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTextPayloadType_TextContent() {
        return (EAttribute)textPayloadTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getThemesType() {
        return themesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getThemesType_Theme() {
        return (EReference)themesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getThemeType() {
        return themeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getThemeType_Identifier() {
        return (EReference)themeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getThemeType_Theme() {
        return (EReference)themeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getThemeType_LayerRef() {
        return (EAttribute)themeTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTileMatrixLimitsType() {
        return tileMatrixLimitsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixLimitsType_TileMatrix() {
        return (EAttribute)tileMatrixLimitsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixLimitsType_MinTileRow() {
        return (EAttribute)tileMatrixLimitsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixLimitsType_MaxTileRow() {
        return (EAttribute)tileMatrixLimitsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixLimitsType_MinTileCol() {
        return (EAttribute)tileMatrixLimitsTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixLimitsType_MaxTileCol() {
        return (EAttribute)tileMatrixLimitsTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTileMatrixSetLimitsType() {
        return tileMatrixSetLimitsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTileMatrixSetLimitsType_TileMatrixLimits() {
        return (EReference)tileMatrixSetLimitsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTileMatrixSetLinkType() {
        return tileMatrixSetLinkTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixSetLinkType_TileMatrixSet() {
        return (EAttribute)tileMatrixSetLinkTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTileMatrixSetLinkType_TileMatrixSetLimits() {
        return (EReference)tileMatrixSetLinkTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTileMatrixSetType() {
        return tileMatrixSetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTileMatrixSetType_Identifier() {
        return (EReference)tileMatrixSetTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixSetType_BoundingBoxGroup() {
        return (EAttribute)tileMatrixSetTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTileMatrixSetType_BoundingBox() {
        return (EReference)tileMatrixSetTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixSetType_SupportedCRS() {
        return (EAttribute)tileMatrixSetTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixSetType_WellKnownScaleSet() {
        return (EAttribute)tileMatrixSetTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTileMatrixSetType_TileMatrix() {
        return (EReference)tileMatrixSetTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTileMatrixType() {
        return tileMatrixTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTileMatrixType_Identifier() {
        return (EReference)tileMatrixTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixType_ScaleDenominator() {
        return (EAttribute)tileMatrixTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixType_TopLeftCorner() {
        return (EAttribute)tileMatrixTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixType_TileWidth() {
        return (EAttribute)tileMatrixTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixType_TileHeight() {
        return (EAttribute)tileMatrixTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixType_MatrixWidth() {
        return (EAttribute)tileMatrixTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTileMatrixType_MatrixHeight() {
        return (EAttribute)tileMatrixTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getURLTemplateType() {
        return urlTemplateTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getURLTemplateType_Format() {
        return (EAttribute)urlTemplateTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getURLTemplateType_ResourceType() {
        return (EAttribute)urlTemplateTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getURLTemplateType_Template() {
        return (EAttribute)urlTemplateTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getGetCapabilitiesValueType() {
        return getCapabilitiesValueTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getGetFeatureInfoValueType() {
        return getFeatureInfoValueTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getGetTileValueType() {
        return getTileValueTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getRequestServiceType() {
        return requestServiceTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getResourceTypeType() {
        return resourceTypeTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getVersionType() {
        return versionTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getAcceptedFormatsType() {
        return acceptedFormatsTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getGetCapabilitiesValueTypeObject() {
        return getCapabilitiesValueTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getGetFeatureInfoValueTypeObject() {
        return getFeatureInfoValueTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getGetTileValueTypeObject() {
        return getTileValueTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getRequestServiceTypeObject() {
        return requestServiceTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getResourceTypeTypeObject() {
        return resourceTypeTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getSectionsType() {
        return sectionsTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTemplateType() {
        return templateTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getVersionTypeObject() {
        return versionTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wmtsv_1Factory getwmtsv_1Factory() {
        return (wmtsv_1Factory)getEFactoryInstance();
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
        binaryPayloadTypeEClass = createEClass(BINARY_PAYLOAD_TYPE);
        createEAttribute(binaryPayloadTypeEClass, BINARY_PAYLOAD_TYPE__FORMAT);
        createEAttribute(binaryPayloadTypeEClass, BINARY_PAYLOAD_TYPE__BINARY_CONTENT);

        capabilitiesTypeEClass = createEClass(CAPABILITIES_TYPE);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__CONTENTS);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__THEMES);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__WSDL);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__SERVICE_METADATA_URL);

        contentsTypeEClass = createEClass(CONTENTS_TYPE);
        createEReference(contentsTypeEClass, CONTENTS_TYPE__TILE_MATRIX_SET);

        dimensionNameValueTypeEClass = createEClass(DIMENSION_NAME_VALUE_TYPE);
        createEAttribute(dimensionNameValueTypeEClass, DIMENSION_NAME_VALUE_TYPE__VALUE);
        createEAttribute(dimensionNameValueTypeEClass, DIMENSION_NAME_VALUE_TYPE__NAME);

        dimensionTypeEClass = createEClass(DIMENSION_TYPE);
        createEReference(dimensionTypeEClass, DIMENSION_TYPE__IDENTIFIER);
        createEReference(dimensionTypeEClass, DIMENSION_TYPE__UOM);
        createEAttribute(dimensionTypeEClass, DIMENSION_TYPE__UNIT_SYMBOL);
        createEAttribute(dimensionTypeEClass, DIMENSION_TYPE__DEFAULT);
        createEAttribute(dimensionTypeEClass, DIMENSION_TYPE__CURRENT);
        createEAttribute(dimensionTypeEClass, DIMENSION_TYPE__VALUE);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__BINARY_PAYLOAD);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DIMENSION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DIMENSION_NAME_VALUE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__FEATURE_INFO_RESPONSE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_FEATURE_INFO);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_TILE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LAYER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__LEGEND_URL);
        createEReference(documentRootEClass, DOCUMENT_ROOT__STYLE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TEXT_PAYLOAD);
        createEReference(documentRootEClass, DOCUMENT_ROOT__THEME);
        createEReference(documentRootEClass, DOCUMENT_ROOT__THEMES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TILE_MATRIX);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TILE_MATRIX_LIMITS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TILE_MATRIX_SET);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TILE_MATRIX_SET_LINK);

        featureInfoResponseTypeEClass = createEClass(FEATURE_INFO_RESPONSE_TYPE);
        createEAttribute(featureInfoResponseTypeEClass, FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION_GROUP);
        createEReference(featureInfoResponseTypeEClass, FEATURE_INFO_RESPONSE_TYPE__FEATURE_COLLECTION);
        createEReference(featureInfoResponseTypeEClass, FEATURE_INFO_RESPONSE_TYPE__TEXT_PAYLOAD);
        createEReference(featureInfoResponseTypeEClass, FEATURE_INFO_RESPONSE_TYPE__BINARY_PAYLOAD);
        createEReference(featureInfoResponseTypeEClass, FEATURE_INFO_RESPONSE_TYPE__ANY_CONTENT);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);

        getFeatureInfoTypeEClass = createEClass(GET_FEATURE_INFO_TYPE);
        createEReference(getFeatureInfoTypeEClass, GET_FEATURE_INFO_TYPE__GET_TILE);
        createEAttribute(getFeatureInfoTypeEClass, GET_FEATURE_INFO_TYPE__J);
        createEAttribute(getFeatureInfoTypeEClass, GET_FEATURE_INFO_TYPE__I);
        createEAttribute(getFeatureInfoTypeEClass, GET_FEATURE_INFO_TYPE__INFO_FORMAT);
        createEAttribute(getFeatureInfoTypeEClass, GET_FEATURE_INFO_TYPE__SERVICE);
        createEAttribute(getFeatureInfoTypeEClass, GET_FEATURE_INFO_TYPE__VERSION);

        getTileTypeEClass = createEClass(GET_TILE_TYPE);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__LAYER);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__STYLE);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__FORMAT);
        createEReference(getTileTypeEClass, GET_TILE_TYPE__DIMENSION_NAME_VALUE);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__TILE_MATRIX_SET);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__TILE_MATRIX);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__TILE_ROW);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__TILE_COL);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__SERVICE);
        createEAttribute(getTileTypeEClass, GET_TILE_TYPE__VERSION);

        layerTypeEClass = createEClass(LAYER_TYPE);
        createEReference(layerTypeEClass, LAYER_TYPE__STYLE);
        createEAttribute(layerTypeEClass, LAYER_TYPE__FORMAT);
        createEAttribute(layerTypeEClass, LAYER_TYPE__INFO_FORMAT);
        createEReference(layerTypeEClass, LAYER_TYPE__DIMENSION);
        createEReference(layerTypeEClass, LAYER_TYPE__TILE_MATRIX_SET_LINK);
        createEReference(layerTypeEClass, LAYER_TYPE__RESOURCE_URL);

        legendURLTypeEClass = createEClass(LEGEND_URL_TYPE);
        createEAttribute(legendURLTypeEClass, LEGEND_URL_TYPE__FORMAT);
        createEAttribute(legendURLTypeEClass, LEGEND_URL_TYPE__HEIGHT);
        createEAttribute(legendURLTypeEClass, LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR);
        createEAttribute(legendURLTypeEClass, LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR);
        createEAttribute(legendURLTypeEClass, LEGEND_URL_TYPE__WIDTH);

        styleTypeEClass = createEClass(STYLE_TYPE);
        createEReference(styleTypeEClass, STYLE_TYPE__IDENTIFIER);
        createEReference(styleTypeEClass, STYLE_TYPE__LEGEND_URL);
        createEAttribute(styleTypeEClass, STYLE_TYPE__IS_DEFAULT);

        textPayloadTypeEClass = createEClass(TEXT_PAYLOAD_TYPE);
        createEAttribute(textPayloadTypeEClass, TEXT_PAYLOAD_TYPE__FORMAT);
        createEAttribute(textPayloadTypeEClass, TEXT_PAYLOAD_TYPE__TEXT_CONTENT);

        themesTypeEClass = createEClass(THEMES_TYPE);
        createEReference(themesTypeEClass, THEMES_TYPE__THEME);

        themeTypeEClass = createEClass(THEME_TYPE);
        createEReference(themeTypeEClass, THEME_TYPE__IDENTIFIER);
        createEReference(themeTypeEClass, THEME_TYPE__THEME);
        createEAttribute(themeTypeEClass, THEME_TYPE__LAYER_REF);

        tileMatrixLimitsTypeEClass = createEClass(TILE_MATRIX_LIMITS_TYPE);
        createEAttribute(tileMatrixLimitsTypeEClass, TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX);
        createEAttribute(tileMatrixLimitsTypeEClass, TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW);
        createEAttribute(tileMatrixLimitsTypeEClass, TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW);
        createEAttribute(tileMatrixLimitsTypeEClass, TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL);
        createEAttribute(tileMatrixLimitsTypeEClass, TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL);

        tileMatrixSetLimitsTypeEClass = createEClass(TILE_MATRIX_SET_LIMITS_TYPE);
        createEReference(tileMatrixSetLimitsTypeEClass, TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS);

        tileMatrixSetLinkTypeEClass = createEClass(TILE_MATRIX_SET_LINK_TYPE);
        createEAttribute(tileMatrixSetLinkTypeEClass, TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET);
        createEReference(tileMatrixSetLinkTypeEClass, TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS);

        tileMatrixSetTypeEClass = createEClass(TILE_MATRIX_SET_TYPE);
        createEReference(tileMatrixSetTypeEClass, TILE_MATRIX_SET_TYPE__IDENTIFIER);
        createEAttribute(tileMatrixSetTypeEClass, TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP);
        createEReference(tileMatrixSetTypeEClass, TILE_MATRIX_SET_TYPE__BOUNDING_BOX);
        createEAttribute(tileMatrixSetTypeEClass, TILE_MATRIX_SET_TYPE__SUPPORTED_CRS);
        createEAttribute(tileMatrixSetTypeEClass, TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET);
        createEReference(tileMatrixSetTypeEClass, TILE_MATRIX_SET_TYPE__TILE_MATRIX);

        tileMatrixTypeEClass = createEClass(TILE_MATRIX_TYPE);
        createEReference(tileMatrixTypeEClass, TILE_MATRIX_TYPE__IDENTIFIER);
        createEAttribute(tileMatrixTypeEClass, TILE_MATRIX_TYPE__SCALE_DENOMINATOR);
        createEAttribute(tileMatrixTypeEClass, TILE_MATRIX_TYPE__TOP_LEFT_CORNER);
        createEAttribute(tileMatrixTypeEClass, TILE_MATRIX_TYPE__TILE_WIDTH);
        createEAttribute(tileMatrixTypeEClass, TILE_MATRIX_TYPE__TILE_HEIGHT);
        createEAttribute(tileMatrixTypeEClass, TILE_MATRIX_TYPE__MATRIX_WIDTH);
        createEAttribute(tileMatrixTypeEClass, TILE_MATRIX_TYPE__MATRIX_HEIGHT);

        urlTemplateTypeEClass = createEClass(URL_TEMPLATE_TYPE);
        createEAttribute(urlTemplateTypeEClass, URL_TEMPLATE_TYPE__FORMAT);
        createEAttribute(urlTemplateTypeEClass, URL_TEMPLATE_TYPE__RESOURCE_TYPE);
        createEAttribute(urlTemplateTypeEClass, URL_TEMPLATE_TYPE__TEMPLATE);

        // Create enums
        getCapabilitiesValueTypeEEnum = createEEnum(GET_CAPABILITIES_VALUE_TYPE);
        getFeatureInfoValueTypeEEnum = createEEnum(GET_FEATURE_INFO_VALUE_TYPE);
        getTileValueTypeEEnum = createEEnum(GET_TILE_VALUE_TYPE);
        requestServiceTypeEEnum = createEEnum(REQUEST_SERVICE_TYPE);
        resourceTypeTypeEEnum = createEEnum(RESOURCE_TYPE_TYPE);
        versionTypeEEnum = createEEnum(VERSION_TYPE);

        // Create data types
        acceptedFormatsTypeEDataType = createEDataType(ACCEPTED_FORMATS_TYPE);
        getCapabilitiesValueTypeObjectEDataType = createEDataType(GET_CAPABILITIES_VALUE_TYPE_OBJECT);
        getFeatureInfoValueTypeObjectEDataType = createEDataType(GET_FEATURE_INFO_VALUE_TYPE_OBJECT);
        getTileValueTypeObjectEDataType = createEDataType(GET_TILE_VALUE_TYPE_OBJECT);
        requestServiceTypeObjectEDataType = createEDataType(REQUEST_SERVICE_TYPE_OBJECT);
        resourceTypeTypeObjectEDataType = createEDataType(RESOURCE_TYPE_TYPE_OBJECT);
        sectionsTypeEDataType = createEDataType(SECTIONS_TYPE);
        templateTypeEDataType = createEDataType(TEMPLATE_TYPE);
        versionTypeObjectEDataType = createEDataType(VERSION_TYPE_OBJECT);
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
        Ows11Package theOws11Package = (Ows11Package)EPackage.Registry.INSTANCE.getEPackage(Ows11Package.eNS_URI);
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
        Gml311Package theGml311Package = (Gml311Package)EPackage.Registry.INSTANCE.getEPackage(Gml311Package.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        capabilitiesTypeEClass.getESuperTypes().add(theOws11Package.getCapabilitiesBaseType());
        contentsTypeEClass.getESuperTypes().add(theOws11Package.getContentsBaseType());
        dimensionTypeEClass.getESuperTypes().add(theOws11Package.getDescriptionType());
        getCapabilitiesTypeEClass.getESuperTypes().add(theOws11Package.getGetCapabilitiesType());
        layerTypeEClass.getESuperTypes().add(theOws11Package.getDatasetDescriptionSummaryBaseType());
        legendURLTypeEClass.getESuperTypes().add(theOws11Package.getOnlineResourceType());
        styleTypeEClass.getESuperTypes().add(theOws11Package.getDescriptionType());
        themeTypeEClass.getESuperTypes().add(theOws11Package.getDescriptionType());
        tileMatrixSetTypeEClass.getESuperTypes().add(theOws11Package.getDescriptionType());
        tileMatrixTypeEClass.getESuperTypes().add(theOws11Package.getDescriptionType());

        // Initialize classes, features, and operations; add parameters
        initEClass(binaryPayloadTypeEClass, BinaryPayloadType.class, "BinaryPayloadType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBinaryPayloadType_Format(), theOws11Package.getMimeType(), "format", null, 1, 1, BinaryPayloadType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBinaryPayloadType_BinaryContent(), theXMLTypePackage.getBase64Binary(), "binaryContent", null, 1, 1, BinaryPayloadType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(capabilitiesTypeEClass, CapabilitiesType.class, "CapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCapabilitiesType_Contents(), this.getContentsType(), null, "contents", null, 0, 1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCapabilitiesType_Themes(), this.getThemesType(), null, "themes", null, 0, -1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCapabilitiesType_WSDL(), theOws11Package.getOnlineResourceType(), null, "wSDL", null, 0, -1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCapabilitiesType_ServiceMetadataURL(), theOws11Package.getOnlineResourceType(), null, "serviceMetadataURL", null, 0, -1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(contentsTypeEClass, ContentsType.class, "ContentsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getContentsType_TileMatrixSet(), this.getTileMatrixSetType(), null, "tileMatrixSet", null, 0, -1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dimensionNameValueTypeEClass, DimensionNameValueType.class, "DimensionNameValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDimensionNameValueType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, DimensionNameValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDimensionNameValueType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, DimensionNameValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dimensionTypeEClass, DimensionType.class, "DimensionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDimensionType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, DimensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDimensionType_UOM(), theOws11Package.getDomainMetadataType(), null, "uOM", null, 0, 1, DimensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDimensionType_UnitSymbol(), theXMLTypePackage.getString(), "unitSymbol", null, 0, 1, DimensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDimensionType_Default(), theXMLTypePackage.getString(), "default", null, 0, 1, DimensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDimensionType_Current(), theXMLTypePackage.getBoolean(), "current", null, 0, 1, DimensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDimensionType_Value(), theXMLTypePackage.getString(), "value", null, 1, -1, DimensionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_BinaryPayload(), this.getBinaryPayloadType(), null, "binaryPayload", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Capabilities(), this.getCapabilitiesType(), null, "capabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Dimension(), this.getDimensionType(), null, "dimension", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DimensionNameValue(), this.getDimensionNameValueType(), null, "dimensionNameValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_FeatureInfoResponse(), this.getFeatureInfoResponseType(), null, "featureInfoResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetFeatureInfo(), this.getGetFeatureInfoType(), null, "getFeatureInfo", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetTile(), this.getGetTileType(), null, "getTile", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Layer(), this.getLayerType(), null, "layer", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_LegendURL(), this.getLegendURLType(), null, "legendURL", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Style(), this.getStyleType(), null, "style", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TextPayload(), this.getTextPayloadType(), null, "textPayload", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Theme(), this.getThemeType(), null, "theme", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Themes(), this.getThemesType(), null, "themes", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TileMatrix(), this.getTileMatrixType(), null, "tileMatrix", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TileMatrixLimits(), this.getTileMatrixLimitsType(), null, "tileMatrixLimits", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TileMatrixSet(), this.getTileMatrixSetType(), null, "tileMatrixSet", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TileMatrixSetLimits(), this.getTileMatrixSetLimitsType(), null, "tileMatrixSetLimits", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TileMatrixSetLink(), this.getTileMatrixSetLinkType(), null, "tileMatrixSetLink", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(featureInfoResponseTypeEClass, FeatureInfoResponseType.class, "FeatureInfoResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeatureInfoResponseType_FeatureCollectionGroup(), ecorePackage.getEFeatureMapEntry(), "featureCollectionGroup", null, 0, 1, FeatureInfoResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureInfoResponseType_FeatureCollection(), theGml311Package.getAbstractFeatureCollectionType(), null, "featureCollection", null, 0, 1, FeatureInfoResponseType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureInfoResponseType_TextPayload(), this.getTextPayloadType(), null, "textPayload", null, 0, 1, FeatureInfoResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureInfoResponseType_BinaryPayload(), this.getBinaryPayloadType(), null, "binaryPayload", null, 0, 1, FeatureInfoResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureInfoResponseType_AnyContent(), ecorePackage.getEObject(), null, "anyContent", null, 0, 1, FeatureInfoResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCapabilitiesType_Service(), theOws11Package.getServiceType(), "service", "WMTS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getFeatureInfoTypeEClass, GetFeatureInfoType.class, "GetFeatureInfoType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetFeatureInfoType_GetTile(), this.getGetTileType(), null, "getTile", null, 1, 1, GetFeatureInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureInfoType_J(), theXMLTypePackage.getNonNegativeInteger(), "j", null, 1, 1, GetFeatureInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureInfoType_I(), theXMLTypePackage.getNonNegativeInteger(), "i", null, 1, 1, GetFeatureInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureInfoType_InfoFormat(), theOws11Package.getMimeType(), "infoFormat", null, 1, 1, GetFeatureInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureInfoType_Service(), theXMLTypePackage.getString(), "service", "WMTS", 1, 1, GetFeatureInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetFeatureInfoType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, GetFeatureInfoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getTileTypeEClass, GetTileType.class, "GetTileType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetTileType_Layer(), theXMLTypePackage.getString(), "layer", null, 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_Style(), theXMLTypePackage.getString(), "style", null, 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_Format(), theOws11Package.getMimeType(), "format", null, 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetTileType_DimensionNameValue(), this.getDimensionNameValueType(), null, "dimensionNameValue", null, 0, -1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_TileMatrixSet(), theXMLTypePackage.getString(), "tileMatrixSet", null, 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_TileMatrix(), theXMLTypePackage.getString(), "tileMatrix", null, 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_TileRow(), theXMLTypePackage.getNonNegativeInteger(), "tileRow", null, 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_TileCol(), theXMLTypePackage.getNonNegativeInteger(), "tileCol", null, 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_Service(), theXMLTypePackage.getString(), "service", "WMTS", 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetTileType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, GetTileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(layerTypeEClass, LayerType.class, "LayerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLayerType_Style(), this.getStyleType(), null, "style", null, 1, -1, LayerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLayerType_Format(), theOws11Package.getMimeType(), "format", null, 1, -1, LayerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLayerType_InfoFormat(), theOws11Package.getMimeType(), "infoFormat", null, 0, -1, LayerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLayerType_Dimension(), this.getDimensionType(), null, "dimension", null, 0, -1, LayerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLayerType_TileMatrixSetLink(), this.getTileMatrixSetLinkType(), null, "tileMatrixSetLink", null, 1, -1, LayerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getLayerType_ResourceURL(), this.getURLTemplateType(), null, "resourceURL", null, 0, -1, LayerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(legendURLTypeEClass, LegendURLType.class, "LegendURLType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLegendURLType_Format(), theOws11Package.getMimeType(), "format", null, 0, 1, LegendURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLegendURLType_Height(), theXMLTypePackage.getPositiveInteger(), "height", null, 0, 1, LegendURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLegendURLType_MaxScaleDenominator(), theXMLTypePackage.getDouble(), "maxScaleDenominator", null, 0, 1, LegendURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLegendURLType_MinScaleDenominator(), theXMLTypePackage.getDouble(), "minScaleDenominator", null, 0, 1, LegendURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getLegendURLType_Width(), theXMLTypePackage.getPositiveInteger(), "width", null, 0, 1, LegendURLType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(styleTypeEClass, StyleType.class, "StyleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStyleType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, StyleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStyleType_LegendURL(), this.getLegendURLType(), null, "legendURL", null, 0, -1, StyleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStyleType_IsDefault(), theXMLTypePackage.getBoolean(), "isDefault", null, 0, 1, StyleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(textPayloadTypeEClass, TextPayloadType.class, "TextPayloadType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTextPayloadType_Format(), theOws11Package.getMimeType(), "format", null, 1, 1, TextPayloadType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTextPayloadType_TextContent(), theXMLTypePackage.getString(), "textContent", null, 1, 1, TextPayloadType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(themesTypeEClass, ThemesType.class, "ThemesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getThemesType_Theme(), this.getThemeType(), null, "theme", null, 0, -1, ThemesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(themeTypeEClass, ThemeType.class, "ThemeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getThemeType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, ThemeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getThemeType_Theme(), this.getThemeType(), null, "theme", null, 0, -1, ThemeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getThemeType_LayerRef(), theXMLTypePackage.getAnyURI(), "layerRef", null, 0, -1, ThemeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tileMatrixLimitsTypeEClass, TileMatrixLimitsType.class, "TileMatrixLimitsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTileMatrixLimitsType_TileMatrix(), theXMLTypePackage.getString(), "tileMatrix", null, 1, 1, TileMatrixLimitsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixLimitsType_MinTileRow(), theXMLTypePackage.getPositiveInteger(), "minTileRow", null, 1, 1, TileMatrixLimitsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixLimitsType_MaxTileRow(), theXMLTypePackage.getPositiveInteger(), "maxTileRow", null, 1, 1, TileMatrixLimitsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixLimitsType_MinTileCol(), theXMLTypePackage.getPositiveInteger(), "minTileCol", null, 1, 1, TileMatrixLimitsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixLimitsType_MaxTileCol(), theXMLTypePackage.getPositiveInteger(), "maxTileCol", null, 1, 1, TileMatrixLimitsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tileMatrixSetLimitsTypeEClass, TileMatrixSetLimitsType.class, "TileMatrixSetLimitsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTileMatrixSetLimitsType_TileMatrixLimits(), this.getTileMatrixLimitsType(), null, "tileMatrixLimits", null, 1, -1, TileMatrixSetLimitsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tileMatrixSetLinkTypeEClass, TileMatrixSetLinkType.class, "TileMatrixSetLinkType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTileMatrixSetLinkType_TileMatrixSet(), theXMLTypePackage.getString(), "tileMatrixSet", null, 1, 1, TileMatrixSetLinkType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTileMatrixSetLinkType_TileMatrixSetLimits(), this.getTileMatrixSetLimitsType(), null, "tileMatrixSetLimits", null, 0, 1, TileMatrixSetLinkType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tileMatrixSetTypeEClass, TileMatrixSetType.class, "TileMatrixSetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTileMatrixSetType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, TileMatrixSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixSetType_BoundingBoxGroup(), ecorePackage.getEFeatureMapEntry(), "boundingBoxGroup", null, 0, 1, TileMatrixSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTileMatrixSetType_BoundingBox(), theOws11Package.getBoundingBoxType(), null, "boundingBox", null, 0, 1, TileMatrixSetType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixSetType_SupportedCRS(), theXMLTypePackage.getAnyURI(), "supportedCRS", null, 1, 1, TileMatrixSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixSetType_WellKnownScaleSet(), theXMLTypePackage.getAnyURI(), "wellKnownScaleSet", null, 0, 1, TileMatrixSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTileMatrixSetType_TileMatrix(), this.getTileMatrixType(), null, "tileMatrix", null, 1, -1, TileMatrixSetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(tileMatrixTypeEClass, TileMatrixType.class, "TileMatrixType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTileMatrixType_Identifier(), theOws11Package.getCodeType(), null, "identifier", null, 1, 1, TileMatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixType_ScaleDenominator(), theXMLTypePackage.getDouble(), "scaleDenominator", null, 1, 1, TileMatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixType_TopLeftCorner(), theOws11Package.getPositionType(), "topLeftCorner", null, 1, 1, TileMatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixType_TileWidth(), theXMLTypePackage.getPositiveInteger(), "tileWidth", null, 1, 1, TileMatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixType_TileHeight(), theXMLTypePackage.getPositiveInteger(), "tileHeight", null, 1, 1, TileMatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixType_MatrixWidth(), theXMLTypePackage.getPositiveInteger(), "matrixWidth", null, 1, 1, TileMatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTileMatrixType_MatrixHeight(), theXMLTypePackage.getPositiveInteger(), "matrixHeight", null, 1, 1, TileMatrixType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(urlTemplateTypeEClass, URLTemplateType.class, "URLTemplateType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getURLTemplateType_Format(), theOws11Package.getMimeType(), "format", null, 1, 1, URLTemplateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getURLTemplateType_ResourceType(), this.getResourceTypeType(), "resourceType", null, 1, 1, URLTemplateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getURLTemplateType_Template(), this.getTemplateType(), "template", null, 1, 1, URLTemplateType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(getCapabilitiesValueTypeEEnum, GetCapabilitiesValueType.class, "GetCapabilitiesValueType");
        addEEnumLiteral(getCapabilitiesValueTypeEEnum, GetCapabilitiesValueType.GET_CAPABILITIES);

        initEEnum(getFeatureInfoValueTypeEEnum, GetFeatureInfoValueType.class, "GetFeatureInfoValueType");
        addEEnumLiteral(getFeatureInfoValueTypeEEnum, GetFeatureInfoValueType.GET_FEATURE_INFO);

        initEEnum(getTileValueTypeEEnum, GetTileValueType.class, "GetTileValueType");
        addEEnumLiteral(getTileValueTypeEEnum, GetTileValueType.GET_TILE);

        initEEnum(requestServiceTypeEEnum, RequestServiceType.class, "RequestServiceType");
        addEEnumLiteral(requestServiceTypeEEnum, RequestServiceType.WMTS);

        initEEnum(resourceTypeTypeEEnum, ResourceTypeType.class, "ResourceTypeType");
        addEEnumLiteral(resourceTypeTypeEEnum, ResourceTypeType.TILE);
        addEEnumLiteral(resourceTypeTypeEEnum, ResourceTypeType.FEATURE_INFO);

        initEEnum(versionTypeEEnum, VersionType.class, "VersionType");
        addEEnumLiteral(versionTypeEEnum, VersionType._100);

        // Initialize data types
        initEDataType(acceptedFormatsTypeEDataType, String.class, "AcceptedFormatsType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(getCapabilitiesValueTypeObjectEDataType, GetCapabilitiesValueType.class, "GetCapabilitiesValueTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(getFeatureInfoValueTypeObjectEDataType, GetFeatureInfoValueType.class, "GetFeatureInfoValueTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(getTileValueTypeObjectEDataType, GetTileValueType.class, "GetTileValueTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(requestServiceTypeObjectEDataType, RequestServiceType.class, "RequestServiceTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(resourceTypeTypeObjectEDataType, ResourceTypeType.class, "ResourceTypeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(sectionsTypeEDataType, String.class, "SectionsType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(templateTypeEDataType, String.class, "TemplateType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(versionTypeObjectEDataType, VersionType.class, "VersionTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // null
        createNullAnnotations();
        // http://www.w3.org/XML/1998/namespace
        createNamespaceAnnotations();
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>null</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createNullAnnotations() {
        String source = null;	
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "wmtsPayload_response\nwmts\nwmtsGetTile_request\nwmtsGetCapabilities_request\nwmtsGetCapabilities_response\nwmtsGetTile_request\nowsAll.xsd\nowsGetResourceByID.xsd\nowsExceptionReport.xsd\nowsDomainType.xsd\nowsContents.xsd\nowsInputOutputData.xsd\nowsManifest.xsd\nowsDataIdentification.xsd\nowsCommon.xsd\nowsGetCapabilities.xsd\nowsServiceIdentification.xsd\nowsServiceProvider.xsd\nowsOperationsMetadata.xsd\nows19115subset.xsd"
           });
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
          (acceptedFormatsTypeEDataType, 
           source, 
           new String[] {
             "name", "AcceptedFormatsType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "((application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*)(,(application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*)"
           });	
        addAnnotation
          (binaryPayloadTypeEClass, 
           source, 
           new String[] {
             "name", "BinaryPayload_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getBinaryPayloadType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getBinaryPayloadType_BinaryContent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BinaryContent",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (capabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Capabilities_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getCapabilitiesType_Contents(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Contents",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCapabilitiesType_Themes(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Themes",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCapabilitiesType_WSDL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WSDL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCapabilitiesType_ServiceMetadataURL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceMetadataURL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (contentsTypeEClass, 
           source, 
           new String[] {
             "name", "ContentsType",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getContentsType_TileMatrixSet(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSet",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (dimensionNameValueTypeEClass, 
           source, 
           new String[] {
             "name", "DimensionNameValue_._type",
             "kind", "simple"
           });	
        addAnnotation
          (getDimensionNameValueType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });	
        addAnnotation
          (getDimensionNameValueType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });	
        addAnnotation
          (dimensionTypeEClass, 
           source, 
           new String[] {
             "name", "Dimension_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getDimensionType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getDimensionType_UOM(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UOM",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getDimensionType_UnitSymbol(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UnitSymbol",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDimensionType_Default(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Default",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDimensionType_Current(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Current",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDimensionType_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace"
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
          (getDocumentRoot_BinaryPayload(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BinaryPayload",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_Capabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Capabilities",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_Dimension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Dimension",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_DimensionNameValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DimensionNameValue",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_FeatureInfoResponse(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FeatureInfoResponse",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_GetCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetCapabilities",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_GetFeatureInfo(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetFeatureInfo",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_GetTile(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetTile",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_Layer(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Layer",
             "namespace", "##targetNamespace",
             "affiliation", "http://www.opengis.net/ows/1.1#DatasetDescriptionSummary"
           });	
        addAnnotation
          (getDocumentRoot_LegendURL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LegendURL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_Style(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Style",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_TextPayload(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TextPayload",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_Theme(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Theme",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_Themes(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Themes",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_TileMatrix(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrix",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_TileMatrixLimits(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixLimits",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_TileMatrixSet(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSet",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_TileMatrixSetLimits(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSetLimits",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getDocumentRoot_TileMatrixSetLink(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSetLink",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (featureInfoResponseTypeEClass, 
           source, 
           new String[] {
             "name", "FeatureInfoResponse_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getFeatureInfoResponseType_FeatureCollectionGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "_FeatureCollection:group",
             "namespace", "http://www.opengis.net/gml"
           });	
        addAnnotation
          (getFeatureInfoResponseType_FeatureCollection(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "_FeatureCollection",
             "namespace", "http://www.opengis.net/gml",
             "group", "http://www.opengis.net/gml#_FeatureCollection:group"
           });	
        addAnnotation
          (getFeatureInfoResponseType_TextPayload(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TextPayload",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getFeatureInfoResponseType_BinaryPayload(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BinaryPayload",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getFeatureInfoResponseType_AnyContent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AnyContent",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "GetCapabilities_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getGetCapabilitiesType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });	
        addAnnotation
          (getCapabilitiesValueTypeEEnum, 
           source, 
           new String[] {
             "name", "GetCapabilitiesValueType"
           });	
        addAnnotation
          (getCapabilitiesValueTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "GetCapabilitiesValueType:Object",
             "baseType", "GetCapabilitiesValueType"
           });	
        addAnnotation
          (getFeatureInfoTypeEClass, 
           source, 
           new String[] {
             "name", "GetFeatureInfo_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getGetFeatureInfoType_GetTile(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetTile",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetFeatureInfoType_J(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "J",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetFeatureInfoType_I(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "I",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetFeatureInfoType_InfoFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "InfoFormat",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetFeatureInfoType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });	
        addAnnotation
          (getGetFeatureInfoType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });	
        addAnnotation
          (getFeatureInfoValueTypeEEnum, 
           source, 
           new String[] {
             "name", "GetFeatureInfoValueType"
           });	
        addAnnotation
          (getFeatureInfoValueTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "GetFeatureInfoValueType:Object",
             "baseType", "GetFeatureInfoValueType"
           });	
        addAnnotation
          (getTileTypeEClass, 
           source, 
           new String[] {
             "name", "GetTile_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getGetTileType_Layer(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Layer",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_Style(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Style",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_DimensionNameValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DimensionNameValue",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_TileMatrixSet(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSet",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_TileMatrix(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrix",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_TileRow(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileRow",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_TileCol(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileCol",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getGetTileType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });	
        addAnnotation
          (getGetTileType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });	
        addAnnotation
          (getTileValueTypeEEnum, 
           source, 
           new String[] {
             "name", "GetTileValueType"
           });	
        addAnnotation
          (getTileValueTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "GetTileValueType:Object",
             "baseType", "GetTileValueType"
           });	
        addAnnotation
          (layerTypeEClass, 
           source, 
           new String[] {
             "name", "LayerType",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getLayerType_Style(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Style",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getLayerType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getLayerType_InfoFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "InfoFormat",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getLayerType_Dimension(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Dimension",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getLayerType_TileMatrixSetLink(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSetLink",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getLayerType_ResourceURL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ResourceURL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (legendURLTypeEClass, 
           source, 
           new String[] {
             "name", "LegendURL_._type",
             "kind", "empty"
           });	
        addAnnotation
          (getLegendURLType_Format(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "format"
           });	
        addAnnotation
          (getLegendURLType_Height(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "height"
           });	
        addAnnotation
          (getLegendURLType_MaxScaleDenominator(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "maxScaleDenominator"
           });	
        addAnnotation
          (getLegendURLType_MinScaleDenominator(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "minScaleDenominator"
           });	
        addAnnotation
          (getLegendURLType_Width(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "width"
           });	
        addAnnotation
          (requestServiceTypeEEnum, 
           source, 
           new String[] {
             "name", "RequestServiceType"
           });	
        addAnnotation
          (requestServiceTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "RequestServiceType:Object",
             "baseType", "RequestServiceType"
           });	
        addAnnotation
          (resourceTypeTypeEEnum, 
           source, 
           new String[] {
             "name", "resourceType_._type"
           });	
        addAnnotation
          (resourceTypeTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "resourceType_._type:Object",
             "baseType", "resourceType_._type"
           });	
        addAnnotation
          (sectionsTypeEDataType, 
           source, 
           new String[] {
             "name", "SectionsType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes)(,(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes))*"
           });	
        addAnnotation
          (styleTypeEClass, 
           source, 
           new String[] {
             "name", "Style_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getStyleType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getStyleType_LegendURL(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LegendURL",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getStyleType_IsDefault(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "isDefault"
           });	
        addAnnotation
          (templateTypeEDataType, 
           source, 
           new String[] {
             "name", "template_._type",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", "([A-Za-z0-9\\-_\\.!~\\*\'\\(\\);/\\?:@\\+:$,#\\{\\}=&]|%25[A-Fa-f0-9][A-Fa-f0-9])+"
           });	
        addAnnotation
          (textPayloadTypeEClass, 
           source, 
           new String[] {
             "name", "TextPayload_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTextPayloadType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTextPayloadType_TextContent(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TextContent",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (themesTypeEClass, 
           source, 
           new String[] {
             "name", "Themes_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getThemesType_Theme(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Theme",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (themeTypeEClass, 
           source, 
           new String[] {
             "name", "Theme_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getThemeType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getThemeType_Theme(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Theme",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getThemeType_LayerRef(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "LayerRef",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (tileMatrixLimitsTypeEClass, 
           source, 
           new String[] {
             "name", "TileMatrixLimits_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTileMatrixLimitsType_TileMatrix(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrix",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixLimitsType_MinTileRow(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MinTileRow",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixLimitsType_MaxTileRow(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MaxTileRow",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixLimitsType_MinTileCol(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MinTileCol",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixLimitsType_MaxTileCol(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MaxTileCol",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (tileMatrixSetLimitsTypeEClass, 
           source, 
           new String[] {
             "name", "TileMatrixSetLimits_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTileMatrixSetLimitsType_TileMatrixLimits(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixLimits",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (tileMatrixSetLinkTypeEClass, 
           source, 
           new String[] {
             "name", "TileMatrixSetLink_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTileMatrixSetLinkType_TileMatrixSet(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSet",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixSetLinkType_TileMatrixSetLimits(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrixSetLimits",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (tileMatrixSetTypeEClass, 
           source, 
           new String[] {
             "name", "TileMatrixSet_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTileMatrixSetType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getTileMatrixSetType_BoundingBoxGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "BoundingBox:group",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getTileMatrixSetType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows/1.1",
             "group", "http://www.opengis.net/ows/1.1#BoundingBox:group"
           });	
        addAnnotation
          (getTileMatrixSetType_SupportedCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedCRS",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getTileMatrixSetType_WellKnownScaleSet(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WellKnownScaleSet",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixSetType_TileMatrix(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileMatrix",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (tileMatrixTypeEClass, 
           source, 
           new String[] {
             "name", "TileMatrix_._type",
             "kind", "elementOnly"
           });	
        addAnnotation
          (getTileMatrixType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/ows/1.1"
           });	
        addAnnotation
          (getTileMatrixType_ScaleDenominator(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ScaleDenominator",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixType_TopLeftCorner(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TopLeftCorner",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixType_TileWidth(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileWidth",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixType_TileHeight(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TileHeight",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixType_MatrixWidth(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MatrixWidth",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (getTileMatrixType_MatrixHeight(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MatrixHeight",
             "namespace", "##targetNamespace"
           });	
        addAnnotation
          (urlTemplateTypeEClass, 
           source, 
           new String[] {
             "name", "URLTemplateType",
             "kind", "empty"
           });	
        addAnnotation
          (getURLTemplateType_Format(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "format"
           });	
        addAnnotation
          (getURLTemplateType_ResourceType(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "resourceType"
           });	
        addAnnotation
          (getURLTemplateType_Template(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "template"
           });	
        addAnnotation
          (versionTypeEEnum, 
           source, 
           new String[] {
             "name", "VersionType"
           });	
        addAnnotation
          (versionTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "VersionType:Object",
             "baseType", "VersionType"
           });
    }

} //wmtsv_1PackageImpl
