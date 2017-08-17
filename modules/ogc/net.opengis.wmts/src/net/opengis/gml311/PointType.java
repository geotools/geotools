/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Point Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Point is defined by a single coordinate tuple.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.PointType#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.PointType#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.PointType#getCoord <em>Coord</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getPointType()
 * @model extendedMetaData="name='PointType' kind='elementOnly'"
 * @generated
 */
public interface PointType extends AbstractGeometricPrimitiveType {
    /**
     * Returns the value of the '<em><b>Pos</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pos</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pos</em>' containment reference.
     * @see #setPos(DirectPositionType)
     * @see net.opengis.gml311.Gml311Package#getPointType_Pos()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionType getPos();

    /**
     * Sets the value of the '{@link net.opengis.gml311.PointType#getPos <em>Pos</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pos</em>' containment reference.
     * @see #getPos()
     * @generated
     */
    void setPos(DirectPositionType value);

    /**
     * Returns the value of the '<em><b>Coordinates</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.1.0 for coordinates with ordinate values that are numbers. Use "pos" 
     * 								instead. The "coordinates" element shall only be used for coordinates with ordinates that require a string 
     * 								representation, e.g. DMS representations.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinates</em>' containment reference.
     * @see #setCoordinates(CoordinatesType)
     * @see net.opengis.gml311.Gml311Package#getPointType_Coordinates()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coordinates' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getCoordinates();

    /**
     * Sets the value of the '{@link net.opengis.gml311.PointType#getCoordinates <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinates</em>' containment reference.
     * @see #getCoordinates()
     * @generated
     */
    void setCoordinates(CoordinatesType value);

    /**
     * Returns the value of the '<em><b>Coord</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.0. Use "pos" instead. The "coord" element is included for 
     * 								backwards compatibility with GML 2.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coord</em>' containment reference.
     * @see #setCoord(CoordType)
     * @see net.opengis.gml311.Gml311Package#getPointType_Coord()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coord' namespace='##targetNamespace'"
     * @generated
     */
    CoordType getCoord();

    /**
     * Sets the value of the '{@link net.opengis.gml311.PointType#getCoord <em>Coord</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coord</em>' containment reference.
     * @see #getCoord()
     * @generated
     */
    void setCoord(CoordType value);

} // PointType
