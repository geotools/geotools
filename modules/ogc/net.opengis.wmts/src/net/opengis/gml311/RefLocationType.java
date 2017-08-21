/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ref Location Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.RefLocationType#getAffinePlacement <em>Affine Placement</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getRefLocationType()
 * @model extendedMetaData="name='refLocation_._type' kind='elementOnly'"
 * @generated
 */
public interface RefLocationType extends EObject {
    /**
     * Returns the value of the '<em><b>Affine Placement</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "refLocation" is an affine mapping 
     *           that places  the curve defined by the Fresnel Integrals  
     *           into the co-ordinate reference system of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Affine Placement</em>' containment reference.
     * @see #setAffinePlacement(AffinePlacementType)
     * @see net.opengis.gml311.Gml311Package#getRefLocationType_AffinePlacement()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='AffinePlacement' namespace='##targetNamespace'"
     * @generated
     */
    AffinePlacementType getAffinePlacement();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RefLocationType#getAffinePlacement <em>Affine Placement</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Affine Placement</em>' containment reference.
     * @see #getAffinePlacement()
     * @generated
     */
    void setAffinePlacement(AffinePlacementType value);

} // RefLocationType
