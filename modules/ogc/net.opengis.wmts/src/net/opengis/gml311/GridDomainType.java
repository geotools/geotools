/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grid Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.GridDomainType#getGrid <em>Grid</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getGridDomainType()
 * @model extendedMetaData="name='GridDomainType' kind='elementOnly'"
 * @generated
 */
public interface GridDomainType extends DomainSetType {
    /**
     * Returns the value of the '<em><b>Grid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid</em>' containment reference.
     * @see #setGrid(GridType)
     * @see net.opengis.gml311.Gml311Package#getGridDomainType_Grid()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Grid' namespace='##targetNamespace'"
     * @generated
     */
    GridType getGrid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GridDomainType#getGrid <em>Grid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid</em>' containment reference.
     * @see #getGrid()
     * @generated
     */
    void setGrid(GridType value);

} // GridDomainType
