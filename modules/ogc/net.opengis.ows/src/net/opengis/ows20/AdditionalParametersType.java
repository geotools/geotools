/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Parameters Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.AdditionalParametersType#getAdditionalParameter1 <em>Additional Parameter1</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getAdditionalParametersType()
 * @model extendedMetaData="name='AdditionalParametersType' kind='elementOnly'"
 * @generated
 */
public interface AdditionalParametersType extends AdditionalParametersBaseType {
    /**
     * Returns the value of the '<em><b>Additional Parameter1</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.AdditionalParameterType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * One additional metadata parameter.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Additional Parameter1</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getAdditionalParametersType_AdditionalParameter1()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AdditionalParameter' namespace='##targetNamespace'"
     * @generated
     */
    EList<AdditionalParameterType> getAdditionalParameter1();

} // AdditionalParametersType
