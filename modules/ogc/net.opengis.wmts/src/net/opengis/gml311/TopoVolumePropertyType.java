/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Topo Volume Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TopoVolumePropertyType#getTopoVolume <em>Topo Volume</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTopoVolumePropertyType()
 * @model extendedMetaData="name='TopoVolumePropertyType' kind='elementOnly'"
 * @generated
 */
public interface TopoVolumePropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Topo Volume</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Volume</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Volume</em>' containment reference.
     * @see #setTopoVolume(TopoVolumeType)
     * @see net.opengis.gml311.Gml311Package#getTopoVolumePropertyType_TopoVolume()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TopoVolume' namespace='##targetNamespace'"
     * @generated
     */
    TopoVolumeType getTopoVolume();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TopoVolumePropertyType#getTopoVolume <em>Topo Volume</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Volume</em>' containment reference.
     * @see #getTopoVolume()
     * @generated
     */
    void setTopoVolume(TopoVolumeType value);

} // TopoVolumePropertyType
