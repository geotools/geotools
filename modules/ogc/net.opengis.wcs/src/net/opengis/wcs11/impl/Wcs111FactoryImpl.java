/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.wcs11.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.util.Diagnostician;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wcs111FactoryImpl extends EFactoryImpl implements Wcs111Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Wcs111Factory init() {
        try {
            Wcs111Factory theWcs111Factory = (Wcs111Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wcs/1.1.1"); 
            if (theWcs111Factory != null) {
                return theWcs111Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Wcs111FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs111FactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case Wcs111Package.AVAILABLE_KEYS_TYPE: return createAvailableKeysType();
            case Wcs111Package.AXIS_SUBSET_TYPE: return createAxisSubsetType();
            case Wcs111Package.AXIS_TYPE: return createAxisType();
            case Wcs111Package.CAPABILITIES_TYPE: return createCapabilitiesType();
            case Wcs111Package.CONTENTS_TYPE: return createContentsType();
            case Wcs111Package.COVERAGE_DESCRIPTIONS_TYPE: return createCoverageDescriptionsType();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE: return createCoverageDescriptionType();
            case Wcs111Package.COVERAGE_DOMAIN_TYPE: return createCoverageDomainType();
            case Wcs111Package.COVERAGES_TYPE: return createCoveragesType();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE: return createCoverageSummaryType();
            case Wcs111Package.DESCRIBE_COVERAGE_TYPE: return createDescribeCoverageType();
            case Wcs111Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Wcs111Package.DOMAIN_SUBSET_TYPE: return createDomainSubsetType();
            case Wcs111Package.FIELD_SUBSET_TYPE: return createFieldSubsetType();
            case Wcs111Package.FIELD_TYPE: return createFieldType();
            case Wcs111Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Wcs111Package.GET_COVERAGE_TYPE: return createGetCoverageType();
            case Wcs111Package.GRID_CRS_TYPE: return createGridCrsType();
            case Wcs111Package.IMAGE_CRS_REF_TYPE: return createImageCRSRefType();
            case Wcs111Package.INTERPOLATION_METHOD_BASE_TYPE: return createInterpolationMethodBaseType();
            case Wcs111Package.INTERPOLATION_METHODS_TYPE: return createInterpolationMethodsType();
            case Wcs111Package.INTERPOLATION_METHOD_TYPE: return createInterpolationMethodType();
            case Wcs111Package.OUTPUT_TYPE: return createOutputType();
            case Wcs111Package.RANGE_SUBSET_TYPE: return createRangeSubsetType();
            case Wcs111Package.RANGE_TYPE: return createRangeType();
            case Wcs111Package.REQUEST_BASE_TYPE: return createRequestBaseType();
            case Wcs111Package.SPATIAL_DOMAIN_TYPE: return createSpatialDomainType();
            case Wcs111Package.TIME_PERIOD_TYPE: return createTimePeriodType();
            case Wcs111Package.TIME_SEQUENCE_TYPE: return createTimeSequenceType();
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
            case Wcs111Package.IDENTIFIER_TYPE:
                return createIdentifierTypeFromString(eDataType, initialValue);
            case Wcs111Package.INTERPOLATION_METHOD_BASE_TYPE_BASE:
                return createInterpolationMethodBaseTypeBaseFromString(eDataType, initialValue);
            case Wcs111Package.TIME_DURATION_TYPE:
                return createTimeDurationTypeFromString(eDataType, initialValue);
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
            case Wcs111Package.IDENTIFIER_TYPE:
                return convertIdentifierTypeToString(eDataType, instanceValue);
            case Wcs111Package.INTERPOLATION_METHOD_BASE_TYPE_BASE:
                return convertInterpolationMethodBaseTypeBaseToString(eDataType, instanceValue);
            case Wcs111Package.TIME_DURATION_TYPE:
                return convertTimeDurationTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvailableKeysType createAvailableKeysType() {
        AvailableKeysTypeImpl availableKeysType = new AvailableKeysTypeImpl();
        return availableKeysType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AxisSubsetType createAxisSubsetType() {
        AxisSubsetTypeImpl axisSubsetType = new AxisSubsetTypeImpl();
        return axisSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AxisType createAxisType() {
        AxisTypeImpl axisType = new AxisTypeImpl();
        return axisType;
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
    public CoverageDomainType createCoverageDomainType() {
        CoverageDomainTypeImpl coverageDomainType = new CoverageDomainTypeImpl();
        return coverageDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoveragesType createCoveragesType() {
        CoveragesTypeImpl coveragesType = new CoveragesTypeImpl();
        return coveragesType;
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
    public DocumentRoot createDocumentRoot() {
        DocumentRootImpl documentRoot = new DocumentRootImpl();
        return documentRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainSubsetType createDomainSubsetType() {
        DomainSubsetTypeImpl domainSubsetType = new DomainSubsetTypeImpl();
        return domainSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FieldSubsetType createFieldSubsetType() {
        FieldSubsetTypeImpl fieldSubsetType = new FieldSubsetTypeImpl();
        return fieldSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FieldType createFieldType() {
        FieldTypeImpl fieldType = new FieldTypeImpl();
        return fieldType;
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
    public GridCrsType createGridCrsType() {
        GridCrsTypeImpl gridCrsType = new GridCrsTypeImpl();
        return gridCrsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImageCRSRefType createImageCRSRefType() {
        ImageCRSRefTypeImpl imageCRSRefType = new ImageCRSRefTypeImpl();
        return imageCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodBaseType createInterpolationMethodBaseType() {
        InterpolationMethodBaseTypeImpl interpolationMethodBaseType = new InterpolationMethodBaseTypeImpl();
        return interpolationMethodBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodsType createInterpolationMethodsType() {
        InterpolationMethodsTypeImpl interpolationMethodsType = new InterpolationMethodsTypeImpl();
        return interpolationMethodsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodType createInterpolationMethodType() {
        InterpolationMethodTypeImpl interpolationMethodType = new InterpolationMethodTypeImpl();
        return interpolationMethodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputType createOutputType() {
        OutputTypeImpl outputType = new OutputTypeImpl();
        return outputType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeSubsetType createRangeSubsetType() {
        RangeSubsetTypeImpl rangeSubsetType = new RangeSubsetTypeImpl();
        return rangeSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeType createRangeType() {
        RangeTypeImpl rangeType = new RangeTypeImpl();
        return rangeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequestBaseType createRequestBaseType() {
        RequestBaseTypeImpl requestBaseType = new RequestBaseTypeImpl();
        return requestBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpatialDomainType createSpatialDomainType() {
        SpatialDomainTypeImpl spatialDomainType = new SpatialDomainTypeImpl();
        return spatialDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePeriodType createTimePeriodType() {
        TimePeriodTypeImpl timePeriodType = new TimePeriodTypeImpl();
        return timePeriodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeSequenceType createTimeSequenceType() {
        TimeSequenceTypeImpl timeSequenceType = new TimeSequenceTypeImpl();
        return timeSequenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createIdentifierTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIdentifierTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createInterpolationMethodBaseTypeBaseFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertInterpolationMethodBaseTypeBaseToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
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
    public Wcs111Package getWcs111Package() {
        return (Wcs111Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static Wcs111Package getPackage() {
        return Wcs111Package.eINSTANCE;
    }

} //Wcs111FactoryImpl
