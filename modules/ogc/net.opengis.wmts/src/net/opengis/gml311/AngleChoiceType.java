/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Angle Choice Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Value of an angle quantity provided in either degree-minute-second format or single value format.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AngleChoiceType#getAngle <em>Angle</em>}</li>
 *   <li>{@link net.opengis.gml311.AngleChoiceType#getDmsAngle <em>Dms Angle</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAngleChoiceType()
 * @model extendedMetaData="name='AngleChoiceType' kind='elementOnly'"
 * @generated
 */
public interface AngleChoiceType extends EObject {
    /**
     * Returns the value of the '<em><b>Angle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Angle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Angle</em>' containment reference.
     * @see #setAngle(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getAngleChoiceType_Angle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='angle' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getAngle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AngleChoiceType#getAngle <em>Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Angle</em>' containment reference.
     * @see #getAngle()
     * @generated
     */
    void setAngle(MeasureType value);

    /**
     * Returns the value of the '<em><b>Dms Angle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dms Angle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dms Angle</em>' containment reference.
     * @see #setDmsAngle(DMSAngleType)
     * @see net.opengis.gml311.Gml311Package#getAngleChoiceType_DmsAngle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='dmsAngle' namespace='##targetNamespace'"
     * @generated
     */
    DMSAngleType getDmsAngle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AngleChoiceType#getDmsAngle <em>Dms Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dms Angle</em>' containment reference.
     * @see #getDmsAngle()
     * @generated
     */
    void setDmsAngle(DMSAngleType value);

} // AngleChoiceType
