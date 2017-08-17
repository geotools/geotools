/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Moving Object Status Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This type encapsulates various dynamic properties of moving objects        
 *              (points, lines, regions). It is useful for dealing with features whose        
 *              geometry or topology changes over time.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MovingObjectStatusType#getLocationGroup <em>Location Group</em>}</li>
 *   <li>{@link net.opengis.gml311.MovingObjectStatusType#getLocation <em>Location</em>}</li>
 *   <li>{@link net.opengis.gml311.MovingObjectStatusType#getSpeed <em>Speed</em>}</li>
 *   <li>{@link net.opengis.gml311.MovingObjectStatusType#getBearing <em>Bearing</em>}</li>
 *   <li>{@link net.opengis.gml311.MovingObjectStatusType#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link net.opengis.gml311.MovingObjectStatusType#getElevation <em>Elevation</em>}</li>
 *   <li>{@link net.opengis.gml311.MovingObjectStatusType#getStatus <em>Status</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType()
 * @model extendedMetaData="name='MovingObjectStatusType' kind='elementOnly'"
 * @generated
 */
public interface MovingObjectStatusType extends AbstractTimeSliceType {
    /**
     * Returns the value of the '<em><b>Location Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated in GML 3.1.0
     * <!-- end-model-doc -->
     * @return the value of the '<em>Location Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType_LocationGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='group' name='location:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getLocationGroup();

    /**
     * Returns the value of the '<em><b>Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated in GML 3.1.0
     * <!-- end-model-doc -->
     * @return the value of the '<em>Location</em>' containment reference.
     * @see #setLocation(LocationPropertyType)
     * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType_Location()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='location' namespace='##targetNamespace' group='location:group'"
     * @generated
     */
    LocationPropertyType getLocation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MovingObjectStatusType#getLocation <em>Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location</em>' containment reference.
     * @see #getLocation()
     * @generated
     */
    void setLocation(LocationPropertyType value);

    /**
     * Returns the value of the '<em><b>Speed</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Speed</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Speed</em>' containment reference.
     * @see #setSpeed(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType_Speed()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='speed' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getSpeed();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MovingObjectStatusType#getSpeed <em>Speed</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Speed</em>' containment reference.
     * @see #getSpeed()
     * @generated
     */
    void setSpeed(MeasureType value);

    /**
     * Returns the value of the '<em><b>Bearing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bearing</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bearing</em>' containment reference.
     * @see #setBearing(DirectionPropertyType)
     * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType_Bearing()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='bearing' namespace='##targetNamespace'"
     * @generated
     */
    DirectionPropertyType getBearing();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MovingObjectStatusType#getBearing <em>Bearing</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bearing</em>' containment reference.
     * @see #getBearing()
     * @generated
     */
    void setBearing(DirectionPropertyType value);

    /**
     * Returns the value of the '<em><b>Acceleration</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Acceleration</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Acceleration</em>' containment reference.
     * @see #setAcceleration(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType_Acceleration()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='acceleration' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getAcceleration();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MovingObjectStatusType#getAcceleration <em>Acceleration</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Acceleration</em>' containment reference.
     * @see #getAcceleration()
     * @generated
     */
    void setAcceleration(MeasureType value);

    /**
     * Returns the value of the '<em><b>Elevation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Elevation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Elevation</em>' containment reference.
     * @see #setElevation(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType_Elevation()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='elevation' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getElevation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MovingObjectStatusType#getElevation <em>Elevation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Elevation</em>' containment reference.
     * @see #getElevation()
     * @generated
     */
    void setElevation(MeasureType value);

    /**
     * Returns the value of the '<em><b>Status</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Status</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Status</em>' containment reference.
     * @see #setStatus(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getMovingObjectStatusType_Status()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='status' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getStatus();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MovingObjectStatusType#getStatus <em>Status</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Status</em>' containment reference.
     * @see #getStatus()
     * @generated
     */
    void setStatus(StringOrRefType value);

} // MovingObjectStatusType
