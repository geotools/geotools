/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Topo Complex Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This type represents a TP_Complex capable of holding topological primitives.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TopoComplexType#getMaximalComplex <em>Maximal Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.TopoComplexType#getSuperComplex <em>Super Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.TopoComplexType#getSubComplex <em>Sub Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.TopoComplexType#getTopoPrimitiveMember <em>Topo Primitive Member</em>}</li>
 *   <li>{@link net.opengis.gml311.TopoComplexType#getTopoPrimitiveMembers <em>Topo Primitive Members</em>}</li>
 *   <li>{@link net.opengis.gml311.TopoComplexType#isIsMaximal <em>Is Maximal</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTopoComplexType()
 * @model extendedMetaData="name='TopoComplexType' kind='elementOnly'"
 * @generated
 */
public interface TopoComplexType extends AbstractTopologyType {
    /**
     * Returns the value of the '<em><b>Maximal Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Need schamatron test here that isMaximal attribute value is true
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximal Complex</em>' containment reference.
     * @see #setMaximalComplex(TopoComplexMemberType)
     * @see net.opengis.gml311.Gml311Package#getTopoComplexType_MaximalComplex()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='maximalComplex' namespace='##targetNamespace'"
     * @generated
     */
    TopoComplexMemberType getMaximalComplex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TopoComplexType#getMaximalComplex <em>Maximal Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximal Complex</em>' containment reference.
     * @see #getMaximalComplex()
     * @generated
     */
    void setMaximalComplex(TopoComplexMemberType value);

    /**
     * Returns the value of the '<em><b>Super Complex</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TopoComplexMemberType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Super Complex</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Super Complex</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTopoComplexType_SuperComplex()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='superComplex' namespace='##targetNamespace'"
     * @generated
     */
    EList<TopoComplexMemberType> getSuperComplex();

    /**
     * Returns the value of the '<em><b>Sub Complex</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TopoComplexMemberType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sub Complex</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sub Complex</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTopoComplexType_SubComplex()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='subComplex' namespace='##targetNamespace'"
     * @generated
     */
    EList<TopoComplexMemberType> getSubComplex();

    /**
     * Returns the value of the '<em><b>Topo Primitive Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TopoPrimitiveMemberType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Primitive Member</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Primitive Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTopoComplexType_TopoPrimitiveMember()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='topoPrimitiveMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<TopoPrimitiveMemberType> getTopoPrimitiveMember();

    /**
     * Returns the value of the '<em><b>Topo Primitive Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Primitive Members</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Primitive Members</em>' containment reference.
     * @see #setTopoPrimitiveMembers(TopoPrimitiveArrayAssociationType)
     * @see net.opengis.gml311.Gml311Package#getTopoComplexType_TopoPrimitiveMembers()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='topoPrimitiveMembers' namespace='##targetNamespace'"
     * @generated
     */
    TopoPrimitiveArrayAssociationType getTopoPrimitiveMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TopoComplexType#getTopoPrimitiveMembers <em>Topo Primitive Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Primitive Members</em>' containment reference.
     * @see #getTopoPrimitiveMembers()
     * @generated
     */
    void setTopoPrimitiveMembers(TopoPrimitiveArrayAssociationType value);

    /**
     * Returns the value of the '<em><b>Is Maximal</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Maximal</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Maximal</em>' attribute.
     * @see #isSetIsMaximal()
     * @see #unsetIsMaximal()
     * @see #setIsMaximal(boolean)
     * @see net.opengis.gml311.Gml311Package#getTopoComplexType_IsMaximal()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='isMaximal'"
     * @generated
     */
    boolean isIsMaximal();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TopoComplexType#isIsMaximal <em>Is Maximal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Maximal</em>' attribute.
     * @see #isSetIsMaximal()
     * @see #unsetIsMaximal()
     * @see #isIsMaximal()
     * @generated
     */
    void setIsMaximal(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.TopoComplexType#isIsMaximal <em>Is Maximal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetIsMaximal()
     * @see #isIsMaximal()
     * @see #setIsMaximal(boolean)
     * @generated
     */
    void unsetIsMaximal();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.TopoComplexType#isIsMaximal <em>Is Maximal</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Is Maximal</em>' attribute is set.
     * @see #unsetIsMaximal()
     * @see #isIsMaximal()
     * @see #setIsMaximal(boolean)
     * @generated
     */
    boolean isSetIsMaximal();

} // TopoComplexType
