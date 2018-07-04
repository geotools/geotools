/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Observation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ObservationType#getValidTime <em>Valid Time</em>}</li>
 *   <li>{@link net.opengis.gml311.ObservationType#getUsing <em>Using</em>}</li>
 *   <li>{@link net.opengis.gml311.ObservationType#getTargetGroup <em>Target Group</em>}</li>
 *   <li>{@link net.opengis.gml311.ObservationType#getTarget <em>Target</em>}</li>
 *   <li>{@link net.opengis.gml311.ObservationType#getResultOf <em>Result Of</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getObservationType()
 * @model extendedMetaData="name='ObservationType' kind='elementOnly'"
 * @generated
 */
public interface ObservationType extends AbstractFeatureType {
    /**
     * Returns the value of the '<em><b>Valid Time</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Time</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Time</em>' containment reference.
     * @see #setValidTime(TimePrimitivePropertyType)
     * @see net.opengis.gml311.Gml311Package#getObservationType_ValidTime()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='validTime' namespace='##targetNamespace'"
     * @generated
     */
    TimePrimitivePropertyType getValidTime();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ObservationType#getValidTime <em>Valid Time</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Time</em>' containment reference.
     * @see #getValidTime()
     * @generated
     */
    void setValidTime(TimePrimitivePropertyType value);

    /**
     * Returns the value of the '<em><b>Using</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element contains or points to a description of a sensor, instrument or procedure used for the observation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Using</em>' containment reference.
     * @see #setUsing(FeaturePropertyType)
     * @see net.opengis.gml311.Gml311Package#getObservationType_Using()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='using' namespace='##targetNamespace'"
     * @generated
     */
    FeaturePropertyType getUsing();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ObservationType#getUsing <em>Using</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Using</em>' containment reference.
     * @see #getUsing()
     * @generated
     */
    void setUsing(FeaturePropertyType value);

    /**
     * Returns the value of the '<em><b>Target Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element contains or points to the specimen, region or station which is the object of the observation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Target Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getObservationType_TargetGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='target:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getTargetGroup();

    /**
     * Returns the value of the '<em><b>Target</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element contains or points to the specimen, region or station which is the object of the observation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Target</em>' containment reference.
     * @see #setTarget(TargetPropertyType)
     * @see net.opengis.gml311.Gml311Package#getObservationType_Target()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='target' namespace='##targetNamespace' group='target:group'"
     * @generated
     */
    TargetPropertyType getTarget();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ObservationType#getTarget <em>Target</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' containment reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(TargetPropertyType value);

    /**
     * Returns the value of the '<em><b>Result Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The result of the observation: an image, external object, etc
     * <!-- end-model-doc -->
     * @return the value of the '<em>Result Of</em>' containment reference.
     * @see #setResultOf(AssociationType)
     * @see net.opengis.gml311.Gml311Package#getObservationType_ResultOf()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='resultOf' namespace='##targetNamespace'"
     * @generated
     */
    AssociationType getResultOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ObservationType#getResultOf <em>Result Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result Of</em>' containment reference.
     * @see #getResultOf()
     * @generated
     */
    void setResultOf(AssociationType value);

} // ObservationType
