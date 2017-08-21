/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Curve Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A discrete coverage type whose domain is defined by a collection of curves.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiCurveCoverageType#getMultiCurveDomain <em>Multi Curve Domain</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiCurveCoverageType()
 * @model extendedMetaData="name='MultiCurveCoverageType' kind='elementOnly'"
 * @generated
 */
public interface MultiCurveCoverageType extends AbstractDiscreteCoverageType {
    /**
     * Returns the value of the '<em><b>Multi Curve Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Curve Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Curve Domain</em>' containment reference.
     * @see #setMultiCurveDomain(MultiCurveDomainType)
     * @see net.opengis.gml311.Gml311Package#getMultiCurveCoverageType_MultiCurveDomain()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='multiCurveDomain' namespace='##targetNamespace'"
     * @generated
     */
    MultiCurveDomainType getMultiCurveDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiCurveCoverageType#getMultiCurveDomain <em>Multi Curve Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Curve Domain</em>' containment reference.
     * @see #getMultiCurveDomain()
     * @generated
     */
    void setMultiCurveDomain(MultiCurveDomainType value);

} // MultiCurveCoverageType
