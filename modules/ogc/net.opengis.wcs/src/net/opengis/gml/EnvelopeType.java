/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Envelope Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Envelope defines an extent using a pair of positions defining opposite corners in arbitrary dimensions.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.EnvelopeType#getPos <em>Pos</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getEnvelopeType()
 * @model extendedMetaData="name='EnvelopeType' kind='elementOnly'"
 * @generated
 */
public interface EnvelopeType extends AbstractGeometryType {
    /**
	 * Returns the value of the '<em><b>Pos</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.DirectPositionType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pos</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Pos</em>' containment reference list.
	 * @see net.opengis.gml.GmlPackage#getEnvelopeType_Pos()
	 * @model type="net.opengis.gml.DirectPositionType" containment="true" lower="2" upper="2"
	 *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getPos();

} // EnvelopeType
