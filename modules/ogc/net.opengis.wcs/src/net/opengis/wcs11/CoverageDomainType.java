/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of the spatial-temporal domain of a coverage. The Domain shall include a SpatialDomain (describing the spatial locations for which coverages can be requested), and should included a TemporalDomain (describing the time instants or intervals for which coverages can be requested). 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.CoverageDomainType#getSpatialDomain <em>Spatial Domain</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageDomainType#getTemporalDomain <em>Temporal Domain</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getCoverageDomainType()
 * @model extendedMetaData="name='CoverageDomainType' kind='elementOnly'"
 * @generated
 */
public interface CoverageDomainType extends EObject {
    /**
     * Returns the value of the '<em><b>Spatial Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Domain</em>' containment reference.
     * @see #setSpatialDomain(SpatialDomainType)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDomainType_SpatialDomain()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='SpatialDomain' namespace='##targetNamespace'"
     * @generated
     */
    SpatialDomainType getSpatialDomain();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageDomainType#getSpatialDomain <em>Spatial Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spatial Domain</em>' containment reference.
     * @see #getSpatialDomain()
     * @generated
     */
    void setSpatialDomain(SpatialDomainType value);

    /**
     * Returns the value of the '<em><b>Temporal Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Although optional, the TemporalDomain should be included whenever a value is known or a useful estimate is available. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Temporal Domain</em>' containment reference.
     * @see #setTemporalDomain(TimeSequenceType)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDomainType_TemporalDomain()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TemporalDomain' namespace='##targetNamespace'"
     * @generated
     */
    TimeSequenceType getTemporalDomain();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageDomainType#getTemporalDomain <em>Temporal Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Domain</em>' containment reference.
     * @see #getTemporalDomain()
     * @generated
     */
    void setTemporalDomain(TimeSequenceType value);

} // CoverageDomainType
