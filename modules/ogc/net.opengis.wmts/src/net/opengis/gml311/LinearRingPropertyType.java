/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Linear Ring Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Encapsulates a ring to represent properties in features or geometry collections.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.LinearRingPropertyType#getLinearRing <em>Linear Ring</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getLinearRingPropertyType()
 * @model extendedMetaData="name='LinearRingPropertyType' kind='elementOnly'"
 * @generated
 */
public interface LinearRingPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Linear Ring</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Linear Ring</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Linear Ring</em>' containment reference.
     * @see #setLinearRing(LinearRingType)
     * @see net.opengis.gml311.Gml311Package#getLinearRingPropertyType_LinearRing()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='LinearRing' namespace='##targetNamespace'"
     * @generated
     */
    LinearRingType getLinearRing();

    /**
     * Sets the value of the '{@link net.opengis.gml311.LinearRingPropertyType#getLinearRing <em>Linear Ring</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Linear Ring</em>' containment reference.
     * @see #getLinearRing()
     * @generated
     */
    void setLinearRing(LinearRingType value);

} // LinearRingPropertyType
