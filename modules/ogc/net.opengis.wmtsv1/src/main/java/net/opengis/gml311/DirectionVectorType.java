/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Direction Vector Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Direction expressed as a vector, either using components, or using angles.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DirectionVectorType#getVector <em>Vector</em>}</li>
 *   <li>{@link net.opengis.gml311.DirectionVectorType#getHorizontalAngle <em>Horizontal Angle</em>}</li>
 *   <li>{@link net.opengis.gml311.DirectionVectorType#getVerticalAngle <em>Vertical Angle</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDirectionVectorType()
 * @model extendedMetaData="name='DirectionVectorType' kind='elementOnly'"
 * @generated
 */
public interface DirectionVectorType extends EObject {
    /**
     * Returns the value of the '<em><b>Vector</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vector</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vector</em>' containment reference.
     * @see #setVector(VectorType)
     * @see net.opengis.gml311.Gml311Package#getDirectionVectorType_Vector()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='vector' namespace='##targetNamespace'"
     * @generated
     */
    VectorType getVector();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DirectionVectorType#getVector <em>Vector</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vector</em>' containment reference.
     * @see #getVector()
     * @generated
     */
    void setVector(VectorType value);

    /**
     * Returns the value of the '<em><b>Horizontal Angle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Horizontal Angle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Horizontal Angle</em>' containment reference.
     * @see #setHorizontalAngle(AngleType)
     * @see net.opengis.gml311.Gml311Package#getDirectionVectorType_HorizontalAngle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='horizontalAngle' namespace='##targetNamespace'"
     * @generated
     */
    AngleType getHorizontalAngle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DirectionVectorType#getHorizontalAngle <em>Horizontal Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Horizontal Angle</em>' containment reference.
     * @see #getHorizontalAngle()
     * @generated
     */
    void setHorizontalAngle(AngleType value);

    /**
     * Returns the value of the '<em><b>Vertical Angle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical Angle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical Angle</em>' containment reference.
     * @see #setVerticalAngle(AngleType)
     * @see net.opengis.gml311.Gml311Package#getDirectionVectorType_VerticalAngle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='verticalAngle' namespace='##targetNamespace'"
     * @generated
     */
    AngleType getVerticalAngle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DirectionVectorType#getVerticalAngle <em>Vertical Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical Angle</em>' containment reference.
     * @see #getVerticalAngle()
     * @generated
     */
    void setVerticalAngle(AngleType value);

} // DirectionVectorType
