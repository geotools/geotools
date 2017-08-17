/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bag Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A non-abstract generic collection type that can be used as a document element for a collection of any GML types - Geometries, Topologies, Features ...
 * 
 * FeatureCollections may only contain Features.  GeometryCollections may only contain Geometrys.  Bags are less constrained  they must contain objects that are substitutable for gml:_Object.  This may mix several levels, including Features, Definitions, Dictionaries, Geometries etc.  
 * 
 * The content model would ideally be 
 *    member 0..*
 *    members 0..1
 *    member 0..*
 * for maximum flexibility in building a collection from both homogeneous and distinct components: 
 * included "member" elements each contain a single Object
 * an included "members" element contains a set of Objects 
 * 
 * However, this is non-deterministic, thus prohibited by XSD.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.BagType#getMember <em>Member</em>}</li>
 *   <li>{@link net.opengis.gml311.BagType#getMembers <em>Members</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getBagType()
 * @model extendedMetaData="name='BagType' kind='elementOnly'"
 * @generated
 */
public interface BagType extends AbstractGMLType {
    /**
     * Returns the value of the '<em><b>Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AssociationType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Member</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getBagType_Member()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='member' namespace='##targetNamespace'"
     * @generated
     */
    EList<AssociationType> getMember();

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
     * @see net.opengis.gml311.Gml311Package#getBagType_Members()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='members' namespace='##targetNamespace'"
     * @generated
     */
    ArrayAssociationType getMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.BagType#getMembers <em>Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Members</em>' containment reference.
     * @see #getMembers()
     * @generated
     */
    void setMembers(ArrayAssociationType value);

} // BagType
