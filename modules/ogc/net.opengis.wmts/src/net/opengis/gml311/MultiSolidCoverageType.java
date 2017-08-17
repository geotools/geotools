/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Solid Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A discrete coverage type whose domain is defined by a collection of Solids.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiSolidCoverageType#getMultiSolidDomain <em>Multi Solid Domain</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiSolidCoverageType()
 * @model extendedMetaData="name='MultiSolidCoverageType' kind='elementOnly'"
 * @generated
 */
public interface MultiSolidCoverageType extends AbstractDiscreteCoverageType {
    /**
     * Returns the value of the '<em><b>Multi Solid Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Solid Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Solid Domain</em>' containment reference.
     * @see #setMultiSolidDomain(MultiSolidDomainType)
     * @see net.opengis.gml311.Gml311Package#getMultiSolidCoverageType_MultiSolidDomain()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='multiSolidDomain' namespace='##targetNamespace'"
     * @generated
     */
    MultiSolidDomainType getMultiSolidDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiSolidCoverageType#getMultiSolidDomain <em>Multi Solid Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Solid Domain</em>' containment reference.
     * @see #getMultiSolidDomain()
     * @generated
     */
    void setMultiSolidDomain(MultiSolidDomainType value);

} // MultiSolidCoverageType
