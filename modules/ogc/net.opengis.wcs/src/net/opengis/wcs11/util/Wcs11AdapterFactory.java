/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.util;

import net.opengis.ows11.CapabilitiesBaseType;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DescriptionType;

import net.opengis.wcs11.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs11.Wcs11Package
 * @generated
 */
public class Wcs11AdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Wcs11Package modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wcs11AdapterFactory() {
		if (modelPackage == null) {
			modelPackage = Wcs11Package.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
    public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Wcs11Switch modelSwitch =
		new Wcs11Switch() {
			@Override
            public Object caseAvailableKeysType(AvailableKeysType object) {
				return createAvailableKeysTypeAdapter();
			}
			@Override
            public Object caseAxisSubsetType(AxisSubsetType object) {
				return createAxisSubsetTypeAdapter();
			}
			@Override
            public Object caseAxisType(AxisType object) {
				return createAxisTypeAdapter();
			}
			@Override
            public Object caseCapabilitiesType(CapabilitiesType object) {
				return createCapabilitiesTypeAdapter();
			}
			@Override
            public Object caseContentsType(ContentsType object) {
				return createContentsTypeAdapter();
			}
			@Override
            public Object caseCoverageDescriptionsType(CoverageDescriptionsType object) {
				return createCoverageDescriptionsTypeAdapter();
			}
			@Override
            public Object caseCoverageDescriptionType(CoverageDescriptionType object) {
				return createCoverageDescriptionTypeAdapter();
			}
			@Override
            public Object caseCoverageDomainType(CoverageDomainType object) {
				return createCoverageDomainTypeAdapter();
			}
			@Override
            public Object caseCoveragesType(CoveragesType object) {
				return createCoveragesTypeAdapter();
			}
			@Override
            public Object caseCoverageSummaryType(CoverageSummaryType object) {
				return createCoverageSummaryTypeAdapter();
			}
			@Override
            public Object caseDescribeCoverageType(DescribeCoverageType object) {
				return createDescribeCoverageTypeAdapter();
			}
			@Override
            public Object caseDocumentRoot(DocumentRoot object) {
				return createDocumentRootAdapter();
			}
			@Override
            public Object caseDomainSubsetType(DomainSubsetType object) {
				return createDomainSubsetTypeAdapter();
			}
			@Override
            public Object caseFieldSubsetType(FieldSubsetType object) {
				return createFieldSubsetTypeAdapter();
			}
			@Override
            public Object caseFieldType(FieldType object) {
				return createFieldTypeAdapter();
			}
			@Override
            public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
				return createGetCapabilitiesTypeAdapter();
			}
			@Override
            public Object caseGetCoverageType(GetCoverageType object) {
				return createGetCoverageTypeAdapter();
			}
			@Override
            public Object caseGridCrsType(GridCrsType object) {
				return createGridCrsTypeAdapter();
			}
			@Override
            public Object caseImageCRSRefType(ImageCRSRefType object) {
				return createImageCRSRefTypeAdapter();
			}
			@Override
            public Object caseInterpolationMethodBaseType(InterpolationMethodBaseType object) {
				return createInterpolationMethodBaseTypeAdapter();
			}
			@Override
            public Object caseInterpolationMethodsType(InterpolationMethodsType object) {
				return createInterpolationMethodsTypeAdapter();
			}
			@Override
            public Object caseInterpolationMethodType(InterpolationMethodType object) {
				return createInterpolationMethodTypeAdapter();
			}
			@Override
            public Object caseOutputType(OutputType object) {
				return createOutputTypeAdapter();
			}
			@Override
            public Object caseRangeSubsetType(RangeSubsetType object) {
				return createRangeSubsetTypeAdapter();
			}
			@Override
            public Object caseRangeType(RangeType object) {
				return createRangeTypeAdapter();
			}
			@Override
            public Object caseRequestBaseType(RequestBaseType object) {
				return createRequestBaseTypeAdapter();
			}
			@Override
            public Object caseSpatialDomainType(SpatialDomainType object) {
				return createSpatialDomainTypeAdapter();
			}
			@Override
            public Object caseTimePeriodType(TimePeriodType object) {
				return createTimePeriodTypeAdapter();
			}
			@Override
            public Object caseTimeSequenceType(TimeSequenceType object) {
				return createTimeSequenceTypeAdapter();
			}
			@Override
            public Object caseDescriptionType(DescriptionType object) {
				return createDescriptionTypeAdapter();
			}
			@Override
            public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
				return createCapabilitiesBaseTypeAdapter();
			}
			@Override
            public Object caseOws11_GetCapabilitiesType(net.opengis.ows11.GetCapabilitiesType object) {
				return createOws11_GetCapabilitiesTypeAdapter();
			}
			@Override
            public Object caseCodeType(CodeType object) {
				return createCodeTypeAdapter();
			}
			@Override
            public Object defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
    public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.AvailableKeysType <em>Available Keys Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.AvailableKeysType
	 * @generated
	 */
	public Adapter createAvailableKeysTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.AxisSubsetType <em>Axis Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.AxisSubsetType
	 * @generated
	 */
	public Adapter createAxisSubsetTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.AxisType <em>Axis Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.AxisType
	 * @generated
	 */
	public Adapter createAxisTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.CapabilitiesType <em>Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.CapabilitiesType
	 * @generated
	 */
	public Adapter createCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.ContentsType <em>Contents Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.ContentsType
	 * @generated
	 */
	public Adapter createContentsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.CoverageDescriptionsType <em>Coverage Descriptions Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.CoverageDescriptionsType
	 * @generated
	 */
	public Adapter createCoverageDescriptionsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.CoverageDescriptionType <em>Coverage Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.CoverageDescriptionType
	 * @generated
	 */
	public Adapter createCoverageDescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.CoverageDomainType <em>Coverage Domain Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.CoverageDomainType
	 * @generated
	 */
	public Adapter createCoverageDomainTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.CoveragesType <em>Coverages Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.CoveragesType
	 * @generated
	 */
	public Adapter createCoveragesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.CoverageSummaryType <em>Coverage Summary Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.CoverageSummaryType
	 * @generated
	 */
	public Adapter createCoverageSummaryTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.DescribeCoverageType <em>Describe Coverage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.DescribeCoverageType
	 * @generated
	 */
	public Adapter createDescribeCoverageTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.DocumentRoot
	 * @generated
	 */
	public Adapter createDocumentRootAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.DomainSubsetType <em>Domain Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.DomainSubsetType
	 * @generated
	 */
	public Adapter createDomainSubsetTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.FieldSubsetType <em>Field Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.FieldSubsetType
	 * @generated
	 */
	public Adapter createFieldSubsetTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.FieldType <em>Field Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.FieldType
	 * @generated
	 */
	public Adapter createFieldTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.GetCapabilitiesType
	 * @generated
	 */
	public Adapter createGetCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.GetCoverageType <em>Get Coverage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.GetCoverageType
	 * @generated
	 */
	public Adapter createGetCoverageTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.GridCrsType <em>Grid Crs Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.GridCrsType
	 * @generated
	 */
	public Adapter createGridCrsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.ImageCRSRefType <em>Image CRS Ref Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.ImageCRSRefType
	 * @generated
	 */
	public Adapter createImageCRSRefTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.InterpolationMethodBaseType <em>Interpolation Method Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.InterpolationMethodBaseType
	 * @generated
	 */
	public Adapter createInterpolationMethodBaseTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.InterpolationMethodsType <em>Interpolation Methods Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.InterpolationMethodsType
	 * @generated
	 */
	public Adapter createInterpolationMethodsTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.InterpolationMethodType <em>Interpolation Method Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.InterpolationMethodType
	 * @generated
	 */
	public Adapter createInterpolationMethodTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.OutputType <em>Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.OutputType
	 * @generated
	 */
	public Adapter createOutputTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.RangeSubsetType <em>Range Subset Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.RangeSubsetType
	 * @generated
	 */
	public Adapter createRangeSubsetTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.RangeType <em>Range Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.RangeType
	 * @generated
	 */
	public Adapter createRangeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.RequestBaseType <em>Request Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.RequestBaseType
	 * @generated
	 */
	public Adapter createRequestBaseTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.SpatialDomainType <em>Spatial Domain Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.SpatialDomainType
	 * @generated
	 */
	public Adapter createSpatialDomainTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.TimePeriodType <em>Time Period Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.TimePeriodType
	 * @generated
	 */
	public Adapter createTimePeriodTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.wcs11.TimeSequenceType <em>Time Sequence Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.wcs11.TimeSequenceType
	 * @generated
	 */
	public Adapter createTimeSequenceTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows11.DescriptionType <em>Description Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows11.DescriptionType
	 * @generated
	 */
	public Adapter createDescriptionTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows11.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows11.CapabilitiesBaseType
	 * @generated
	 */
	public Adapter createCapabilitiesBaseTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows11.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows11.GetCapabilitiesType
	 * @generated
	 */
	public Adapter createOws11_GetCapabilitiesTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link net.opengis.ows11.CodeType <em>Code Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.opengis.ows11.CodeType
	 * @generated
	 */
	public Adapter createCodeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //Wcs11AdapterFactory
