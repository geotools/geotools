/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Track Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The track of a moving object is a sequence of specialized timeslices        that indicate the status of the object.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TrackType#getMovingObjectStatus <em>Moving Object Status</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTrackType()
 * @model extendedMetaData="name='TrackType' kind='elementOnly'"
 * @generated
 */
public interface TrackType extends HistoryPropertyType {
    /**
     * Returns the value of the '<em><b>Moving Object Status</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.MovingObjectStatusType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Moving Object Status</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Moving Object Status</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTrackType_MovingObjectStatus()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='MovingObjectStatus' namespace='##targetNamespace'"
     * @generated
     */
    EList<MovingObjectStatusType> getMovingObjectStatus();

} // TrackType
