/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lon Lat Envelope Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines spatial extent by extending LonLatEnvelope with an optional time position pair.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.LonLatEnvelopeType#getTimePosition <em>Time Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getLonLatEnvelopeType()
 * @model extendedMetaData="name='LonLatEnvelopeType' kind='elementOnly'"
 * @generated
 */
public interface LonLatEnvelopeType extends LonLatEnvelopeBaseType {
    /**
	 * Returns the value of the '<em><b>Time Position</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.TimePositionType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Direct representation of a temporal position.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time Position</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getLonLatEnvelopeType_TimePosition()
	 * @model type="net.opengis.gml.TimePositionType" containment="true" upper="2"
	 *        extendedMetaData="kind='element' name='timePosition' namespace='http://www.opengis.net/gml'"
	 * @generated
	 */
    EList getTimePosition();

} // LonLatEnvelopeType
