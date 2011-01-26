/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Axis Description Type1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.AxisDescriptionType1#getAxisDescription <em>Axis Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getAxisDescriptionType1()
 * @model extendedMetaData="name='axisDescription_._type' kind='elementOnly'"
 * @generated
 */
public interface AxisDescriptionType1 extends EObject {
    /**
	 * Returns the value of the '<em><b>Axis Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Axis Description</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Axis Description</em>' containment reference.
	 * @see #setAxisDescription(AxisDescriptionType)
	 * @see net.opengis.wcs10.Wcs10Package#getAxisDescriptionType1_AxisDescription()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='AxisDescription' namespace='##targetNamespace'"
	 * @generated
	 */
    AxisDescriptionType getAxisDescription();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AxisDescriptionType1#getAxisDescription <em>Axis Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Axis Description</em>' containment reference.
	 * @see #getAxisDescription()
	 * @generated
	 */
    void setAxisDescription(AxisDescriptionType value);

} // AxisDescriptionType1
