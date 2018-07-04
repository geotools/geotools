/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rectified Grid Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.RectifiedGridCoverageType#getRectifiedGridDomain <em>Rectified Grid Domain</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getRectifiedGridCoverageType()
 * @model extendedMetaData="name='RectifiedGridCoverageType' kind='elementOnly'"
 * @generated
 */
public interface RectifiedGridCoverageType extends AbstractDiscreteCoverageType {
    /**
     * Returns the value of the '<em><b>Rectified Grid Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Rectified Grid Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Rectified Grid Domain</em>' containment reference.
     * @see #setRectifiedGridDomain(RectifiedGridDomainType)
     * @see net.opengis.gml311.Gml311Package#getRectifiedGridCoverageType_RectifiedGridDomain()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='rectifiedGridDomain' namespace='##targetNamespace'"
     * @generated
     */
    RectifiedGridDomainType getRectifiedGridDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RectifiedGridCoverageType#getRectifiedGridDomain <em>Rectified Grid Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rectified Grid Domain</em>' containment reference.
     * @see #getRectifiedGridDomain()
     * @generated
     */
    void setRectifiedGridDomain(RectifiedGridDomainType value);

} // RectifiedGridCoverageType
