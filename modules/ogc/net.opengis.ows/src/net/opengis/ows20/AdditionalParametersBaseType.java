/**
 */
package net.opengis.ows20;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Parameters Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.AdditionalParametersBaseType#getAdditionalParameter <em>Additional Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getAdditionalParametersBaseType()
 * @model extendedMetaData="name='AdditionalParametersBaseType' kind='elementOnly'"
 * @generated
 */
public interface AdditionalParametersBaseType extends MetadataType {
    /**
     * Returns the value of the '<em><b>Additional Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * One additional metadata parameter.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Additional Parameter</em>' containment reference.
     * @see #setAdditionalParameter(AdditionalParameterType)
     * @see net.opengis.ows20.Ows20Package#getAdditionalParametersBaseType_AdditionalParameter()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='AdditionalParameter' namespace='##targetNamespace'"
     * @generated
     */
    AdditionalParameterType getAdditionalParameter();

    /**
     * Sets the value of the '{@link net.opengis.ows20.AdditionalParametersBaseType#getAdditionalParameter <em>Additional Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Additional Parameter</em>' containment reference.
     * @see #getAdditionalParameter()
     * @generated
     */
    void setAdditionalParameter(AdditionalParameterType value);

} // AdditionalParametersBaseType
