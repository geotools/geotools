/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Domain Set Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines the spatial-temporal domain set of a coverage offering. The domainSet shall include a SpatialDomain (describing the spatial locations for which coverages can be requested), a TemporalDomain (describing the time instants or inter-vals for which coverages can be requested), or both.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.DomainSetType#getSpatialDomain <em>Spatial Domain</em>}</li>
 *   <li>{@link net.opengis.wcs10.DomainSetType#getTemporalDomain <em>Temporal Domain</em>}</li>
 *   <li>{@link net.opengis.wcs10.DomainSetType#getTemporalDomain1 <em>Temporal Domain1</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getDomainSetType()
 * @model extendedMetaData="name='DomainSetType' kind='elementOnly'"
 * @generated
 */
public interface DomainSetType extends EObject {
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
	 * @see net.opengis.wcs10.Wcs10Package#getDomainSetType_SpatialDomain()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='spatialDomain' namespace='##targetNamespace'"
	 * @generated
	 */
    SpatialDomainType getSpatialDomain();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DomainSetType#getSpatialDomain <em>Spatial Domain</em>}' containment reference.
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
	 * Defines the temporal domain of a coverage offering, that is, the times for which valid data are available. The times shall to be ordered from the oldest to the newest.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Temporal Domain</em>' containment reference.
	 * @see #setTemporalDomain(TimeSequenceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDomainSetType_TemporalDomain()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='temporalDomain' namespace='##targetNamespace'"
	 * @generated
	 */
    TimeSequenceType getTemporalDomain();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DomainSetType#getTemporalDomain <em>Temporal Domain</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Domain</em>' containment reference.
	 * @see #getTemporalDomain()
	 * @generated
	 */
    void setTemporalDomain(TimeSequenceType value);

    /**
	 * Returns the value of the '<em><b>Temporal Domain1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Defines the temporal domain of a coverage offering, that is, the times for which valid data are available. The times shall to be ordered from the oldest to the newest.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Temporal Domain1</em>' containment reference.
	 * @see #setTemporalDomain1(TimeSequenceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDomainSetType_TemporalDomain1()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='temporalDomain' namespace='##targetNamespace'"
	 * @generated
	 */
    TimeSequenceType getTemporalDomain1();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DomainSetType#getTemporalDomain1 <em>Temporal Domain1</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Domain1</em>' containment reference.
	 * @see #getTemporalDomain1()
	 * @generated
	 */
    void setTemporalDomain1(TimeSequenceType value);

} // DomainSetType
