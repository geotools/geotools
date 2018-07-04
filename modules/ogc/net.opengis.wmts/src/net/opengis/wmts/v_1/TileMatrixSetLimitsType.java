/**
 */
package net.opengis.wmts.v_1;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tile Matrix Set Limits Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetLimitsType#getTileMatrixLimits <em>Tile Matrix Limits</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetLimitsType()
 * @model extendedMetaData="name='TileMatrixSetLimits_._type' kind='elementOnly'"
 * @generated
 */
public interface TileMatrixSetLimitsType extends EObject {
    /**
     * Returns the value of the '<em><b>Tile Matrix Limits</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.TileMatrixLimitsType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Metadata describing the limits of the TileMatrixSet indices. 
     * 							Multiplicity must be the multiplicity of TileMatrix in this 
     * 							TileMatrixSet.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Limits</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetLimitsType_TileMatrixLimits()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TileMatrixLimits' namespace='##targetNamespace'"
     * @generated
     */
    EList<TileMatrixLimitsType> getTileMatrixLimits();

} // TileMatrixSetLimitsType
