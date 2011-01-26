/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Map;

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
public class Wcs11FactoryImpl extends EFactoryImpl implements Wcs11Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Wcs11Factory init() {
		try {
			Wcs11Factory theWcs11Factory = (Wcs11Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wcs/1.1.1"); 
			if (theWcs11Factory != null) {
				return theWcs11Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Wcs11FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wcs11FactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Wcs11Package.AVAILABLE_KEYS_TYPE: return createAvailableKeysType();
			case Wcs11Package.AXIS_SUBSET_TYPE: return createAxisSubsetType();
			case Wcs11Package.AXIS_TYPE: return createAxisType();
			case Wcs11Package.CAPABILITIES_TYPE: return createCapabilitiesType();
			case Wcs11Package.CONTENTS_TYPE: return createContentsType();
			case Wcs11Package.COVERAGE_DESCRIPTIONS_TYPE: return createCoverageDescriptionsType();
			case Wcs11Package.COVERAGE_DESCRIPTION_TYPE: return createCoverageDescriptionType();
			case Wcs11Package.COVERAGE_DOMAIN_TYPE: return createCoverageDomainType();
			case Wcs11Package.COVERAGES_TYPE: return createCoveragesType();
			case Wcs11Package.COVERAGE_SUMMARY_TYPE: return createCoverageSummaryType();
			case Wcs11Package.DESCRIBE_COVERAGE_TYPE: return createDescribeCoverageType();
			case Wcs11Package.DOCUMENT_ROOT: return createDocumentRoot();
			case Wcs11Package.DOMAIN_SUBSET_TYPE: return createDomainSubsetType();
			case Wcs11Package.FIELD_SUBSET_TYPE: return createFieldSubsetType();
			case Wcs11Package.FIELD_TYPE: return createFieldType();
			case Wcs11Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
			case Wcs11Package.GET_COVERAGE_TYPE: return createGetCoverageType();
			case Wcs11Package.GRID_CRS_TYPE: return createGridCrsType();
			case Wcs11Package.IMAGE_CRS_REF_TYPE: return createImageCRSRefType();
			case Wcs11Package.INTERPOLATION_METHOD_BASE_TYPE: return createInterpolationMethodBaseType();
			case Wcs11Package.INTERPOLATION_METHODS_TYPE: return createInterpolationMethodsType();
			case Wcs11Package.INTERPOLATION_METHOD_TYPE: return createInterpolationMethodType();
			case Wcs11Package.OUTPUT_TYPE: return createOutputType();
			case Wcs11Package.RANGE_SUBSET_TYPE: return createRangeSubsetType();
			case Wcs11Package.RANGE_TYPE: return createRangeType();
			case Wcs11Package.REQUEST_BASE_TYPE: return createRequestBaseType();
			case Wcs11Package.SPATIAL_DOMAIN_TYPE: return createSpatialDomainType();
			case Wcs11Package.TIME_PERIOD_TYPE: return createTimePeriodType();
			case Wcs11Package.TIME_SEQUENCE_TYPE: return createTimeSequenceType();
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
			case Wcs11Package.IDENTIFIER_TYPE:
				return createIdentifierTypeFromString(eDataType, initialValue);
			case Wcs11Package.INTERPOLATION_METHOD_BASE_TYPE_BASE:
				return createInterpolationMethodBaseTypeBaseFromString(eDataType, initialValue);
			case Wcs11Package.TIME_DURATION_TYPE:
				return createTimeDurationTypeFromString(eDataType, initialValue);
			case Wcs11Package.MAP:
				return createMapFromString(eDataType, initialValue);
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
			case Wcs11Package.IDENTIFIER_TYPE:
				return convertIdentifierTypeToString(eDataType, instanceValue);
			case Wcs11Package.INTERPOLATION_METHOD_BASE_TYPE_BASE:
				return convertInterpolationMethodBaseTypeBaseToString(eDataType, instanceValue);
			case Wcs11Package.TIME_DURATION_TYPE:
				return convertTimeDurationTypeToString(eDataType, instanceValue);
			case Wcs11Package.MAP:
				return convertMapToString(eDataType, instanceValue);
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
	public Map createMapFromString(EDataType eDataType, String initialValue) {
		return (Map)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMapToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wcs11Package getWcs11Package() {
		return (Wcs11Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static Wcs11Package getPackage() {
		return Wcs11Package.eINSTANCE;
	}

} //Wcs11FactoryImpl
