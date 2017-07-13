/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Node Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Type declaration of the element "TimeNode".
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeNodeType#getPreviousEdge <em>Previous Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeNodeType#getNextEdge <em>Next Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeNodeType#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeNodeType()
 * @model extendedMetaData="name='TimeNodeType' kind='elementOnly'"
 * @generated
 */
public interface TimeNodeType extends AbstractTimeTopologyPrimitiveType {
    /**
     * Returns the value of the '<em><b>Previous Edge</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TimeEdgePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Previous Edge</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Previous Edge</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTimeNodeType_PreviousEdge()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='previousEdge' namespace='##targetNamespace'"
     * @generated
     */
    EList<TimeEdgePropertyType> getPreviousEdge();

    /**
     * Returns the value of the '<em><b>Next Edge</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TimeEdgePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Next Edge</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Next Edge</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTimeNodeType_NextEdge()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='nextEdge' namespace='##targetNamespace'"
     * @generated
     */
    EList<TimeEdgePropertyType> getNextEdge();

    /**
     * Returns the value of the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Position</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Position</em>' containment reference.
     * @see #setPosition(TimeInstantPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimeNodeType_Position()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='position' namespace='##targetNamespace'"
     * @generated
     */
    TimeInstantPropertyType getPosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeNodeType#getPosition <em>Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Position</em>' containment reference.
     * @see #getPosition()
     * @generated
     */
    void setPosition(TimeInstantPropertyType value);

} // TimeNodeType
