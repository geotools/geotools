/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Point Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiPointDomainType#getMultiPoint <em>Multi Point</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiPointDomainType()
 * @model extendedMetaData="name='MultiPointDomainType' kind='elementOnly'"
 * @generated
 */
public interface MultiPointDomainType extends DomainSetType {
    /**
     * Returns the value of the '<em><b>Multi Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Point</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Point</em>' containment reference.
     * @see #setMultiPoint(MultiPointType)
     * @see net.opengis.gml311.Gml311Package#getMultiPointDomainType_MultiPoint()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MultiPoint' namespace='##targetNamespace'"
     * @generated
     */
    MultiPointType getMultiPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiPointDomainType#getMultiPoint <em>Multi Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Point</em>' containment reference.
     * @see #getMultiPoint()
     * @generated
     */
    void setMultiPoint(MultiPointType value);

} // MultiPointDomainType
