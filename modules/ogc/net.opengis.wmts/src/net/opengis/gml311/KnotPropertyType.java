/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knot Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Encapsulates a knot to use it in a geometric type.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.KnotPropertyType#getKnot <em>Knot</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getKnotPropertyType()
 * @model extendedMetaData="name='KnotPropertyType' kind='elementOnly'"
 * @generated
 */
public interface KnotPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Knot</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Knot</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Knot</em>' containment reference.
     * @see #setKnot(KnotType)
     * @see net.opengis.gml311.Gml311Package#getKnotPropertyType_Knot()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Knot' namespace='##targetNamespace'"
     * @generated
     */
    KnotType getKnot();

    /**
     * Sets the value of the '{@link net.opengis.gml311.KnotPropertyType#getKnot <em>Knot</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Knot</em>' containment reference.
     * @see #getKnot()
     * @generated
     */
    void setKnot(KnotType value);

} // KnotPropertyType
