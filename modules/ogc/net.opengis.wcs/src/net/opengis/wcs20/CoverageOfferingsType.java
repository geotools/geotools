/**
 */
package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Offerings Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.CoverageOfferingsType#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageOfferingsType#getOfferedCoverage <em>Offered Coverage</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getCoverageOfferingsType()
 * @model extendedMetaData="name='CoverageOfferingsType' kind='elementOnly'"
 * @generated
 */
public interface CoverageOfferingsType extends EObject {
    /**
     * Returns the value of the '<em><b>Service Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * ServiceMetadata contains information describing the WCS service on hand. Extension elements allow WCS extension standards to define their individual extra service metadata.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Metadata</em>' containment reference.
     * @see #setServiceMetadata(ServiceMetadataType)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageOfferingsType_ServiceMetadata()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ServiceMetadata' namespace='##targetNamespace'"
     * @generated
     */
    ServiceMetadataType getServiceMetadata();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageOfferingsType#getServiceMetadata <em>Service Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Metadata</em>' containment reference.
     * @see #getServiceMetadata()
     * @generated
     */
    void setServiceMetadata(ServiceMetadataType value);

    /**
     * Returns the value of the '<em><b>Offered Coverage</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs20.OfferedCoverageType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An OfferedCoverage is the information set about a specific coverage offered by the WCS service on hand. It consists of a coverage, as defined in the GML Application Schema for Coverages [OGC 09-146r2] and coverage specific service parameters. Like CoverageOfferings, an OfferedCoverage element is never delivered to the client, but defines the response of coverage access requests.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Offered Coverage</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getCoverageOfferingsType_OfferedCoverage()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='OfferedCoverage' namespace='##targetNamespace'"
     * @generated
     */
    EList<OfferedCoverageType> getOfferedCoverage();

} // CoverageOfferingsType
