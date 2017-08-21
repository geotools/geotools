/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Solid Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiSolidDomainType#getMultiSolid <em>Multi Solid</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiSolidDomainType()
 * @model extendedMetaData="name='MultiSolidDomainType' kind='elementOnly'"
 * @generated
 */
public interface MultiSolidDomainType extends DomainSetType {
    /**
     * Returns the value of the '<em><b>Multi Solid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Solid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Solid</em>' containment reference.
     * @see #setMultiSolid(MultiSolidType)
     * @see net.opengis.gml311.Gml311Package#getMultiSolidDomainType_MultiSolid()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MultiSolid' namespace='##targetNamespace'"
     * @generated
     */
    MultiSolidType getMultiSolid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiSolidDomainType#getMultiSolid <em>Multi Solid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Solid</em>' containment reference.
     * @see #getMultiSolid()
     * @generated
     */
    void setMultiSolid(MultiSolidType value);

} // MultiSolidDomainType
