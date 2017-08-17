/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Point Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A discrete coverage type whose domain is defined by a collection of point
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiPointCoverageType#getMultiPointDomain <em>Multi Point Domain</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiPointCoverageType()
 * @model extendedMetaData="name='MultiPointCoverageType' kind='elementOnly'"
 * @generated
 */
public interface MultiPointCoverageType extends AbstractDiscreteCoverageType {
    /**
     * Returns the value of the '<em><b>Multi Point Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Point Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Point Domain</em>' containment reference.
     * @see #setMultiPointDomain(MultiPointDomainType)
     * @see net.opengis.gml311.Gml311Package#getMultiPointCoverageType_MultiPointDomain()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='multiPointDomain' namespace='##targetNamespace'"
     * @generated
     */
    MultiPointDomainType getMultiPointDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiPointCoverageType#getMultiPointDomain <em>Multi Point Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Point Domain</em>' containment reference.
     * @see #getMultiPointDomain()
     * @generated
     */
    void setMultiPointDomain(MultiPointDomainType value);

} // MultiPointCoverageType
