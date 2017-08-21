/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Absolute External Positional Accuracy Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Closeness of reported coordinate values to values accepted as or being true. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbsoluteExternalPositionalAccuracyType#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbsoluteExternalPositionalAccuracyType()
 * @model extendedMetaData="name='AbsoluteExternalPositionalAccuracyType' kind='elementOnly'"
 * @generated
 */
public interface AbsoluteExternalPositionalAccuracyType extends AbstractPositionalAccuracyType {
    /**
     * Returns the value of the '<em><b>Result</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A quantitative result defined by the evaluation procedure used, and identified by the measureDescription. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Result</em>' containment reference.
     * @see #setResult(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getAbsoluteExternalPositionalAccuracyType_Result()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='result' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getResult();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbsoluteExternalPositionalAccuracyType#getResult <em>Result</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result</em>' containment reference.
     * @see #getResult()
     * @generated
     */
    void setResult(MeasureType value);

} // AbsoluteExternalPositionalAccuracyType
