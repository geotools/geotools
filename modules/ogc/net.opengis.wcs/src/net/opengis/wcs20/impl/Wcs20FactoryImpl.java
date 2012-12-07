/**
 */
package net.opengis.wcs20.impl;

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
import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.DocumentRoot;
import net.opengis.wcs20.ExtensionItemType;
import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.GetCapabilitiesType;
import net.opengis.wcs20.GetCoverageType;
import net.opengis.wcs20.OfferedCoverageType;
import net.opengis.wcs20.ServiceMetadataType;
import net.opengis.wcs20.ServiceParametersType;
import net.opengis.wcs20.Wcs20Factory;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wcs20FactoryImpl extends EFactoryImpl implements Wcs20Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Wcs20Factory init() {
        try {
            Wcs20Factory theWcs20Factory = (Wcs20Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wcs/2.0"); 
            if (theWcs20Factory != null) {
                return theWcs20Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Wcs20FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs20FactoryImpl() {
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
            case Wcs20Package.CAPABILITIES_TYPE: return createCapabilitiesType();
            case Wcs20Package.CONTENTS_TYPE: return createContentsType();
            case Wcs20Package.COVERAGE_DESCRIPTIONS_TYPE: return createCoverageDescriptionsType();
            case Wcs20Package.COVERAGE_DESCRIPTION_TYPE: return createCoverageDescriptionType();
            case Wcs20Package.COVERAGE_OFFERINGS_TYPE: return createCoverageOfferingsType();
            case Wcs20Package.COVERAGE_SUBTYPE_PARENT_TYPE: return createCoverageSubtypeParentType();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE: return createCoverageSummaryType();
            case Wcs20Package.DESCRIBE_COVERAGE_TYPE: return createDescribeCoverageType();
            case Wcs20Package.DIMENSION_SLICE_TYPE: return createDimensionSliceType();
            case Wcs20Package.DIMENSION_TRIM_TYPE: return createDimensionTrimType();
            case Wcs20Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Wcs20Package.EXTENSION_TYPE: return createExtensionType();
            case Wcs20Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Wcs20Package.GET_COVERAGE_TYPE: return createGetCoverageType();
            case Wcs20Package.OFFERED_COVERAGE_TYPE: return createOfferedCoverageType();
            case Wcs20Package.SERVICE_METADATA_TYPE: return createServiceMetadataType();
            case Wcs20Package.SERVICE_PARAMETERS_TYPE: return createServiceParametersType();
            case Wcs20Package.EXTENSION_ITEM_TYPE: return createExtensionItemType();
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
            case Wcs20Package.VERSION_STRING_TYPE:
                return createVersionStringTypeFromString(eDataType, initialValue);
            case Wcs20Package.VERSION_STRING_TYPE_1:
                return createVersionStringType_1FromString(eDataType, initialValue);
            case Wcs20Package.QNAME:
                return createQNameFromString(eDataType, initialValue);
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
            case Wcs20Package.VERSION_STRING_TYPE:
                return convertVersionStringTypeToString(eDataType, instanceValue);
            case Wcs20Package.VERSION_STRING_TYPE_1:
                return convertVersionStringType_1ToString(eDataType, instanceValue);
            case Wcs20Package.QNAME:
                return convertQNameToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesType createCapabilitiesType() {
        CapabilitiesTypeImpl capabilitiesType = new CapabilitiesTypeImpl();
        return capabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContentsType createContentsType() {
        ContentsTypeImpl contentsType = new ContentsTypeImpl();
        return contentsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDescriptionsType createCoverageDescriptionsType() {
        CoverageDescriptionsTypeImpl coverageDescriptionsType = new CoverageDescriptionsTypeImpl();
        return coverageDescriptionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDescriptionType createCoverageDescriptionType() {
        CoverageDescriptionTypeImpl coverageDescriptionType = new CoverageDescriptionTypeImpl();
        return coverageDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageOfferingsType createCoverageOfferingsType() {
        CoverageOfferingsTypeImpl coverageOfferingsType = new CoverageOfferingsTypeImpl();
        return coverageOfferingsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSubtypeParentType createCoverageSubtypeParentType() {
        CoverageSubtypeParentTypeImpl coverageSubtypeParentType = new CoverageSubtypeParentTypeImpl();
        return coverageSubtypeParentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSummaryType createCoverageSummaryType() {
        CoverageSummaryTypeImpl coverageSummaryType = new CoverageSummaryTypeImpl();
        return coverageSummaryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeCoverageType createDescribeCoverageType() {
        DescribeCoverageTypeImpl describeCoverageType = new DescribeCoverageTypeImpl();
        return describeCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DimensionSliceType createDimensionSliceType() {
        DimensionSliceTypeImpl dimensionSliceType = new DimensionSliceTypeImpl();
        return dimensionSliceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DimensionTrimType createDimensionTrimType() {
        DimensionTrimTypeImpl dimensionTrimType = new DimensionTrimTypeImpl();
        return dimensionTrimType;
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
    public ExtensionType createExtensionType() {
        ExtensionTypeImpl extensionType = new ExtensionTypeImpl();
        return extensionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType createGetCapabilitiesType() {
        GetCapabilitiesTypeImpl getCapabilitiesType = new GetCapabilitiesTypeImpl();
        return getCapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCoverageType createGetCoverageType() {
        GetCoverageTypeImpl getCoverageType = new GetCoverageTypeImpl();
        return getCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OfferedCoverageType createOfferedCoverageType() {
        OfferedCoverageTypeImpl offeredCoverageType = new OfferedCoverageTypeImpl();
        return offeredCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceMetadataType createServiceMetadataType() {
        ServiceMetadataTypeImpl serviceMetadataType = new ServiceMetadataTypeImpl();
        return serviceMetadataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceParametersType createServiceParametersType() {
        ServiceParametersTypeImpl serviceParametersType = new ServiceParametersTypeImpl();
        return serviceParametersType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtensionItemType createExtensionItemType() {
        ExtensionItemTypeImpl extensionItemType = new ExtensionItemTypeImpl();
        return extensionItemType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createVersionStringTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertVersionStringTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createVersionStringType_1FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertVersionStringType_1ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName createQNameFromString(EDataType eDataType, String initialValue) {
        return (QName)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertQNameToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs20Package getWcs20Package() {
        return (Wcs20Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static Wcs20Package getPackage() {
        return Wcs20Package.eINSTANCE;
    }

} //Wcs20FactoryImpl
