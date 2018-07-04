/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Curve Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiCurveDomainType#getMultiCurve <em>Multi Curve</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiCurveDomainType()
 * @model extendedMetaData="name='MultiCurveDomainType' kind='elementOnly'"
 * @generated
 */
public interface MultiCurveDomainType extends DomainSetType {
    /**
     * Returns the value of the '<em><b>Multi Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Curve</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Curve</em>' containment reference.
     * @see #setMultiCurve(MultiCurveType)
     * @see net.opengis.gml311.Gml311Package#getMultiCurveDomainType_MultiCurve()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MultiCurve' namespace='##targetNamespace'"
     * @generated
     */
    MultiCurveType getMultiCurve();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiCurveDomainType#getMultiCurve <em>Multi Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Curve</em>' containment reference.
     * @see #getMultiCurve()
     * @generated
     */
    void setMultiCurve(MultiCurveType value);

} // MultiCurveDomainType
