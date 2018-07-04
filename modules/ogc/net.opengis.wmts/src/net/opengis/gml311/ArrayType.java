/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A non-abstract generic collection type that can be used as a document element for a homogeneous collection of any GML types - Geometries, Topologies, Features ...
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ArrayType#getMembers <em>Members</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getArrayType()
 * @model extendedMetaData="name='ArrayType' kind='elementOnly'"
 * @generated
 */
public interface ArrayType extends AbstractGMLType {
    /**
     * Returns the value of the '<em><b>Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Members</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Members</em>' containment reference.
     * @see #setMembers(ArrayAssociationType)
     * @see net.opengis.gml311.Gml311Package#getArrayType_Members()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='members' namespace='##targetNamespace'"
     * @generated
     */
    ArrayAssociationType getMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ArrayType#getMembers <em>Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Members</em>' containment reference.
     * @see #getMembers()
     * @generated
     */
    void setMembers(ArrayAssociationType value);

} // ArrayType
