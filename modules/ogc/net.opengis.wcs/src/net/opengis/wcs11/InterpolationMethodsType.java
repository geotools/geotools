/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interpolation Methods Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.InterpolationMethodsType#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs11.InterpolationMethodsType#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodsType()
 * @model extendedMetaData="name='InterpolationMethods_._type' kind='elementOnly'"
 * @generated
 */
public interface InterpolationMethodsType extends EObject {
    /**
     * Returns the value of the '<em><b>Interpolation Method</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs11.InterpolationMethodType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of identifiers of spatial interpolation methods. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation Method</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodsType_InterpolationMethod()
     * @model type="net.opengis.wcs11.InterpolationMethodType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='InterpolationMethod' namespace='##targetNamespace'"
     * @generated
     */
    EList getInterpolationMethod();

    /**
     * Returns the value of the '<em><b>Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of interpolation method that will be used if the client does not specify one. Should be included when a default exists and is known. 
     * (Arliss) Can any string be used to identify an interpolation method in a KVP encoded Get Coverage operation request? 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default</em>' attribute.
     * @see #setDefault(String)
     * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodsType_Default()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='Default' namespace='##targetNamespace'"
     * @generated
     */
    String getDefault();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.InterpolationMethodsType#getDefault <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' attribute.
     * @see #getDefault()
     * @generated
     */
    void setDefault(String value);

} // InterpolationMethodsType
