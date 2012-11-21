/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Parameter Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.AdditionalParameterType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.ows20.AdditionalParameterType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getAdditionalParameterType()
 * @model extendedMetaData="name='AdditionalParameter_._type' kind='elementOnly'"
 * @generated
 */
public interface AdditionalParameterType extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name or identifier of this AdditionalParameter,
     *             unique for this OGC Web Service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name</em>' containment reference.
     * @see #setName(CodeType)
     * @see net.opengis.ows20.Ows20Package#getAdditionalParameterType_Name()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Name' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getName();

    /**
     * Sets the value of the '{@link net.opengis.ows20.AdditionalParameterType#getName <em>Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' containment reference.
     * @see #getName()
     * @generated
     */
    void setName(CodeType value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of one or more values of this
     *             AdditionalParameter.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getAdditionalParameterType_Value()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
     * @generated
     */
    EList<EObject> getValue();

} // AdditionalParameterType
