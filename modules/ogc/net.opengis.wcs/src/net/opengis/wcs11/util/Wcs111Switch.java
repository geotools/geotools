/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.util;

import java.util.List;

import net.opengis.ows11.CapabilitiesBaseType;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DescriptionType;

import net.opengis.wcs11.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs11.Wcs111Package
 * @generated
 */
public class Wcs111Switch {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Wcs111Package modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wcs111Switch() {
        if (modelPackage == null) {
            modelPackage = Wcs111Package.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public Object doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else {
            List eSuperTypes = theEClass.getESuperTypes();
            return
                eSuperTypes.isEmpty() ?
                    defaultCase(theEObject) :
                    doSwitch((EClass)eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case Wcs111Package.AVAILABLE_KEYS_TYPE: {
                AvailableKeysType availableKeysType = (AvailableKeysType)theEObject;
                Object result = caseAvailableKeysType(availableKeysType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.AXIS_SUBSET_TYPE: {
                AxisSubsetType axisSubsetType = (AxisSubsetType)theEObject;
                Object result = caseAxisSubsetType(axisSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.AXIS_TYPE: {
                AxisType axisType = (AxisType)theEObject;
                Object result = caseAxisType(axisType);
                if (result == null) result = caseDescriptionType(axisType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.CAPABILITIES_TYPE: {
                CapabilitiesType capabilitiesType = (CapabilitiesType)theEObject;
                Object result = caseCapabilitiesType(capabilitiesType);
                if (result == null) result = caseCapabilitiesBaseType(capabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.CONTENTS_TYPE: {
                ContentsType contentsType = (ContentsType)theEObject;
                Object result = caseContentsType(contentsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.COVERAGE_DESCRIPTIONS_TYPE: {
                CoverageDescriptionsType coverageDescriptionsType = (CoverageDescriptionsType)theEObject;
                Object result = caseCoverageDescriptionsType(coverageDescriptionsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE: {
                CoverageDescriptionType coverageDescriptionType = (CoverageDescriptionType)theEObject;
                Object result = caseCoverageDescriptionType(coverageDescriptionType);
                if (result == null) result = caseDescriptionType(coverageDescriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.COVERAGE_DOMAIN_TYPE: {
                CoverageDomainType coverageDomainType = (CoverageDomainType)theEObject;
                Object result = caseCoverageDomainType(coverageDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.COVERAGES_TYPE: {
                CoveragesType coveragesType = (CoveragesType)theEObject;
                Object result = caseCoveragesType(coveragesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.COVERAGE_SUMMARY_TYPE: {
                CoverageSummaryType coverageSummaryType = (CoverageSummaryType)theEObject;
                Object result = caseCoverageSummaryType(coverageSummaryType);
                if (result == null) result = caseDescriptionType(coverageSummaryType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.DESCRIBE_COVERAGE_TYPE: {
                DescribeCoverageType describeCoverageType = (DescribeCoverageType)theEObject;
                Object result = caseDescribeCoverageType(describeCoverageType);
                if (result == null) result = caseRequestBaseType(describeCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                Object result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.DOMAIN_SUBSET_TYPE: {
                DomainSubsetType domainSubsetType = (DomainSubsetType)theEObject;
                Object result = caseDomainSubsetType(domainSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.FIELD_SUBSET_TYPE: {
                FieldSubsetType fieldSubsetType = (FieldSubsetType)theEObject;
                Object result = caseFieldSubsetType(fieldSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.FIELD_TYPE: {
                FieldType fieldType = (FieldType)theEObject;
                Object result = caseFieldType(fieldType);
                if (result == null) result = caseDescriptionType(fieldType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.GET_CAPABILITIES_TYPE: {
                GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
                Object result = caseGetCapabilitiesType(getCapabilitiesType);
                if (result == null) result = caseGetCapabilitiesType_1(getCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.GET_COVERAGE_TYPE: {
                GetCoverageType getCoverageType = (GetCoverageType)theEObject;
                Object result = caseGetCoverageType(getCoverageType);
                if (result == null) result = caseRequestBaseType(getCoverageType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.GRID_CRS_TYPE: {
                GridCrsType gridCrsType = (GridCrsType)theEObject;
                Object result = caseGridCrsType(gridCrsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.IMAGE_CRS_REF_TYPE: {
                ImageCRSRefType imageCRSRefType = (ImageCRSRefType)theEObject;
                Object result = caseImageCRSRefType(imageCRSRefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.INTERPOLATION_METHOD_BASE_TYPE: {
                InterpolationMethodBaseType interpolationMethodBaseType = (InterpolationMethodBaseType)theEObject;
                Object result = caseInterpolationMethodBaseType(interpolationMethodBaseType);
                if (result == null) result = caseCodeType(interpolationMethodBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.INTERPOLATION_METHODS_TYPE: {
                InterpolationMethodsType interpolationMethodsType = (InterpolationMethodsType)theEObject;
                Object result = caseInterpolationMethodsType(interpolationMethodsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.INTERPOLATION_METHOD_TYPE: {
                InterpolationMethodType interpolationMethodType = (InterpolationMethodType)theEObject;
                Object result = caseInterpolationMethodType(interpolationMethodType);
                if (result == null) result = caseInterpolationMethodBaseType(interpolationMethodType);
                if (result == null) result = caseCodeType(interpolationMethodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.OUTPUT_TYPE: {
                OutputType outputType = (OutputType)theEObject;
                Object result = caseOutputType(outputType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.RANGE_SUBSET_TYPE: {
                RangeSubsetType rangeSubsetType = (RangeSubsetType)theEObject;
                Object result = caseRangeSubsetType(rangeSubsetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.RANGE_TYPE: {
                RangeType rangeType = (RangeType)theEObject;
                Object result = caseRangeType(rangeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.REQUEST_BASE_TYPE: {
                RequestBaseType requestBaseType = (RequestBaseType)theEObject;
                Object result = caseRequestBaseType(requestBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.SPATIAL_DOMAIN_TYPE: {
                SpatialDomainType spatialDomainType = (SpatialDomainType)theEObject;
                Object result = caseSpatialDomainType(spatialDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.TIME_PERIOD_TYPE: {
                TimePeriodType timePeriodType = (TimePeriodType)theEObject;
                Object result = caseTimePeriodType(timePeriodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case Wcs111Package.TIME_SEQUENCE_TYPE: {
                TimeSequenceType timeSequenceType = (TimeSequenceType)theEObject;
                Object result = caseTimeSequenceType(timeSequenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Available Keys Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Available Keys Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAvailableKeysType(AvailableKeysType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Axis Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Axis Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAxisSubsetType(AxisSubsetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Axis Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Axis Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAxisType(AxisType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCapabilitiesType(CapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Contents Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Contents Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseContentsType(ContentsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Descriptions Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Descriptions Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoverageDescriptionsType(CoverageDescriptionsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoverageDescriptionType(CoverageDescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoverageDomainType(CoverageDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverages Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverages Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoveragesType(CoveragesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverage Summary Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverage Summary Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoverageSummaryType(CoverageSummaryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Describe Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Describe Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDescribeCoverageType(DescribeCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Domain Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDomainSubsetType(DomainSubsetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Field Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Field Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseFieldSubsetType(FieldSubsetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Field Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Field Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseFieldType(FieldType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Coverage Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Coverage Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetCoverageType(GetCoverageType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Grid Crs Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Grid Crs Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGridCrsType(GridCrsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Image CRS Ref Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Image CRS Ref Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseImageCRSRefType(ImageCRSRefType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interpolation Method Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interpolation Method Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInterpolationMethodBaseType(InterpolationMethodBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interpolation Methods Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interpolation Methods Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInterpolationMethodsType(InterpolationMethodsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interpolation Method Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interpolation Method Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInterpolationMethodType(InterpolationMethodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Output Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOutputType(OutputType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Subset Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Subset Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRangeSubsetType(RangeSubsetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRangeType(RangeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Request Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Request Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRequestBaseType(RequestBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Spatial Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Spatial Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSpatialDomainType(SpatialDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Period Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Period Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTimePeriodType(TimePeriodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Sequence Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Sequence Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseTimeSequenceType(TimeSequenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDescriptionType(DescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetCapabilitiesType_1(net.opengis.ows11.GetCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Code Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Code Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCodeType(CodeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public Object defaultCase(EObject object) {
        return null;
    }

} //Wcs111Switch
