/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Topo Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractTopoPrimitiveType#getIsolated <em>Isolated</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractTopoPrimitiveType#getContainer <em>Container</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractTopoPrimitiveType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractTopoPrimitiveType' kind='elementOnly'"
 * @generated
 */
public interface AbstractTopoPrimitiveType extends AbstractTopologyType {
    /**
     * Returns the value of the '<em><b>Isolated</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IsolatedPropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Isolated</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Isolated</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractTopoPrimitiveType_Isolated()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='isolated' namespace='##targetNamespace'"
     * @generated
     */
    EList<IsolatedPropertyType> getIsolated();

    /**
     * Returns the value of the '<em><b>Container</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Container</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Container</em>' containment reference.
     * @see #setContainer(ContainerPropertyType)
     * @see net.opengis.gml311.Gml311Package#getAbstractTopoPrimitiveType_Container()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='container' namespace='##targetNamespace'"
     * @generated
     */
    ContainerPropertyType getContainer();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractTopoPrimitiveType#getContainer <em>Container</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Container</em>' containment reference.
     * @see #getContainer()
     * @generated
     */
    void setContainer(ContainerPropertyType value);

} // AbstractTopoPrimitiveType
