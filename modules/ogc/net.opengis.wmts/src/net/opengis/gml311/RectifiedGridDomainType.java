/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rectified Grid Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.RectifiedGridDomainType#getRectifiedGrid <em>Rectified Grid</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getRectifiedGridDomainType()
 * @model extendedMetaData="name='RectifiedGridDomainType' kind='elementOnly'"
 * @generated
 */
public interface RectifiedGridDomainType extends DomainSetType {
    /**
     * Returns the value of the '<em><b>Rectified Grid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Should be substitutionGroup="gml:Grid" but changed in order to accomplish Xerces-J schema validation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Rectified Grid</em>' containment reference.
     * @see #setRectifiedGrid(RectifiedGridType)
     * @see net.opengis.gml311.Gml311Package#getRectifiedGridDomainType_RectifiedGrid()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='RectifiedGrid' namespace='##targetNamespace'"
     * @generated
     */
    RectifiedGridType getRectifiedGrid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RectifiedGridDomainType#getRectifiedGrid <em>Rectified Grid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rectified Grid</em>' containment reference.
     * @see #getRectifiedGrid()
     * @generated
     */
    void setRectifiedGrid(RectifiedGridType value);

} // RectifiedGridDomainType
