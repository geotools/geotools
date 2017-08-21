/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.ContentsBaseType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contents Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.ContentsType#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getContentsType()
 * @model extendedMetaData="name='ContentsType' kind='elementOnly'"
 * @generated
 */
public interface ContentsType extends ContentsBaseType {
    /**
     * Returns the value of the '<em><b>Tile Matrix Set</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.TileMatrixSetType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A description of the geometry of a tile fragmentation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getContentsType_TileMatrixSet()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TileMatrixSet' namespace='##targetNamespace'"
     * @generated
     */
    EList<TileMatrixSetType> getTileMatrixSet();

} // ContentsType
