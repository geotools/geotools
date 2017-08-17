/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Geometry Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A geometry collection must include one or more geometries, referenced through geometryMember elements.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiGeometryType#getGeometryMember <em>Geometry Member</em>}</li>
 *   <li>{@link net.opengis.gml311.MultiGeometryType#getGeometryMembers <em>Geometry Members</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiGeometryType()
 * @model extendedMetaData="name='MultiGeometryType' kind='elementOnly'"
 * @generated
 */
public interface MultiGeometryType extends AbstractGeometricAggregateType {
    /**
     * Returns the value of the '<em><b>Geometry Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.GeometryPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a geometry element via the XLink-attributes or contains the geometry element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getMultiGeometryType_GeometryMember()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='geometryMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<GeometryPropertyType> getGeometryMember();

    /**
     * Returns the value of the '<em><b>Geometry Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of geometry elements. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Members</em>' containment reference.
     * @see #setGeometryMembers(GeometryArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getMultiGeometryType_GeometryMembers()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='geometryMembers' namespace='##targetNamespace'"
     * @generated
     */
    GeometryArrayPropertyType getGeometryMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiGeometryType#getGeometryMembers <em>Geometry Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Members</em>' containment reference.
     * @see #getGeometryMembers()
     * @generated
     */
    void setGeometryMembers(GeometryArrayPropertyType value);

} // MultiGeometryType
