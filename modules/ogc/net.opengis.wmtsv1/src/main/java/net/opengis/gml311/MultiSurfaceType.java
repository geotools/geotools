/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Surface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A MultiSurface is defined by one or more Surfaces, referenced through surfaceMember elements.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiSurfaceType#getSurfaceMember <em>Surface Member</em>}</li>
 *   <li>{@link net.opengis.gml311.MultiSurfaceType#getSurfaceMembers <em>Surface Members</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiSurfaceType()
 * @model extendedMetaData="name='MultiSurfaceType' kind='elementOnly'"
 * @generated
 */
public interface MultiSurfaceType extends AbstractGeometricAggregateType {
    /**
     * Returns the value of the '<em><b>Surface Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.SurfacePropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a surface via the XLink-attributes or contains the surface element. A surface element is any element which is substitutable for "_Surface".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getMultiSurfaceType_SurfaceMember()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='surfaceMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<SurfacePropertyType> getSurfaceMember();

    /**
     * Returns the value of the '<em><b>Surface Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of surfaces. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Members</em>' containment reference.
     * @see #setSurfaceMembers(SurfaceArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getMultiSurfaceType_SurfaceMembers()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='surfaceMembers' namespace='##targetNamespace'"
     * @generated
     */
    SurfaceArrayPropertyType getSurfaceMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiSurfaceType#getSurfaceMembers <em>Surface Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Surface Members</em>' containment reference.
     * @see #getSurfaceMembers()
     * @generated
     */
    void setSurfaceMembers(SurfaceArrayPropertyType value);

} // MultiSurfaceType
