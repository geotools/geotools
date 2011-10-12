/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Filter Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.FilterCapabilitiesType#getConformance <em>Conformance</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterCapabilitiesType#getIdCapabilities <em>Id Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterCapabilitiesType#getScalarCapabilities <em>Scalar Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterCapabilitiesType#getSpatialCapabilities <em>Spatial Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterCapabilitiesType#getTemporalCapabilities <em>Temporal Capabilities</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterCapabilitiesType#getFunctions <em>Functions</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterCapabilitiesType#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType()
 * @model extendedMetaData="name='Filter_Capabilities_._type' kind='elementOnly'"
 * @generated
 */
public interface FilterCapabilitiesType extends EObject {
    /**
     * Returns the value of the '<em><b>Conformance</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conformance</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conformance</em>' containment reference.
     * @see #setConformance(ConformanceType)
     * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType_Conformance()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Conformance' namespace='##targetNamespace'"
     * @generated
     */
    ConformanceType getConformance();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterCapabilitiesType#getConformance <em>Conformance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conformance</em>' containment reference.
     * @see #getConformance()
     * @generated
     */
    void setConformance(ConformanceType value);

    /**
     * Returns the value of the '<em><b>Id Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id Capabilities</em>' containment reference.
     * @see #setIdCapabilities(IdCapabilitiesType)
     * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType_IdCapabilities()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Id_Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    IdCapabilitiesType getIdCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterCapabilitiesType#getIdCapabilities <em>Id Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id Capabilities</em>' containment reference.
     * @see #getIdCapabilities()
     * @generated
     */
    void setIdCapabilities(IdCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Scalar Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Scalar Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Scalar Capabilities</em>' containment reference.
     * @see #setScalarCapabilities(ScalarCapabilitiesType)
     * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType_ScalarCapabilities()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Scalar_Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    ScalarCapabilitiesType getScalarCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterCapabilitiesType#getScalarCapabilities <em>Scalar Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scalar Capabilities</em>' containment reference.
     * @see #getScalarCapabilities()
     * @generated
     */
    void setScalarCapabilities(ScalarCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Spatial Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Capabilities</em>' containment reference.
     * @see #setSpatialCapabilities(SpatialCapabilitiesType)
     * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType_SpatialCapabilities()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Spatial_Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    SpatialCapabilitiesType getSpatialCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterCapabilitiesType#getSpatialCapabilities <em>Spatial Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spatial Capabilities</em>' containment reference.
     * @see #getSpatialCapabilities()
     * @generated
     */
    void setSpatialCapabilities(SpatialCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Temporal Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Capabilities</em>' containment reference.
     * @see #setTemporalCapabilities(TemporalCapabilitiesType)
     * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType_TemporalCapabilities()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Temporal_Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    TemporalCapabilitiesType getTemporalCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterCapabilitiesType#getTemporalCapabilities <em>Temporal Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Capabilities</em>' containment reference.
     * @see #getTemporalCapabilities()
     * @generated
     */
    void setTemporalCapabilities(TemporalCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Functions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Functions</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Functions</em>' containment reference.
     * @see #setFunctions(AvailableFunctionsType)
     * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType_Functions()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Functions' namespace='##targetNamespace'"
     * @generated
     */
    AvailableFunctionsType getFunctions();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterCapabilitiesType#getFunctions <em>Functions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Functions</em>' containment reference.
     * @see #getFunctions()
     * @generated
     */
    void setFunctions(AvailableFunctionsType value);

    /**
     * Returns the value of the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extended Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extended Capabilities</em>' containment reference.
     * @see #setExtendedCapabilities(ExtendedCapabilitiesType)
     * @see net.opengis.fes20.Fes20Package#getFilterCapabilitiesType_ExtendedCapabilities()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Extended_Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    ExtendedCapabilitiesType getExtendedCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterCapabilitiesType#getExtendedCapabilities <em>Extended Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extended Capabilities</em>' containment reference.
     * @see #getExtendedCapabilities()
     * @generated
     */
    void setExtendedCapabilities(ExtendedCapabilitiesType value);

} // FilterCapabilitiesType
